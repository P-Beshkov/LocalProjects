package com.atlassian.tutorial.myPlugin.impl;

import com.atlassian.event.api.EventListener;
import com.atlassian.event.api.EventPublisher;
import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.event.issue.IssueEvent;
import com.atlassian.jira.event.type.EventType;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueInputParameters;
import com.atlassian.jira.issue.link.IssueLinkManager;
import com.atlassian.jira.issue.link.LinkCollection;
import com.atlassian.jira.issue.worklog.WorklogManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsDevService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Named;
import java.util.List;

/**
 * Created by pbeshkov on 11.9.2017 г..
 */
@ExportAsDevService
@Component
@Named("eventListener")
public class IssueListener implements InitializingBean, DisposableBean {

	private static final Logger log = LoggerFactory.getLogger(IssueListener.class);
	private EventPublisher eventPublisher;
	@ComponentImport
	private ApplicationProperties applicationProperties;

	@Autowired
	public IssueListener(@ComponentImport EventPublisher eventPublisher) {
		log.info("Innit issue listener eventPublisher: {}", eventPublisher);
		this.eventPublisher = eventPublisher;
	}

	@EventListener
	public void onIssueEvent(IssueEvent issueEvent) {
		ApplicationUser user = issueEvent.getUser();
		Long eventTypeId = issueEvent.getEventTypeId();
		//		if (eventTypeId.equals(EventType.ISSUE_CREATED_ID)) {
		//			log.info("Issue {} has been created at {}.", issue.getKey(), issue.getCreated());
		//		} else if (eventTypeId.equals(EventType.ISSUE_RESOLVED_ID)) {
		//			log.info("Issue {} has been resolved at {}.", issue.getKey(), issue.getResolutionDate());
		//		} else if (eventTypeId.equals(EventType.ISSUE_CLOSED_ID)) {
		//			log.info("Issue {} has been closed at {}.", issue.getKey(), issue.getUpdated());
		//		} else
		Issue issue = issueEvent.getIssue();
		String eventId = applicationProperties.getPropertyValue("event.id.issueInProgress");
		if (eventTypeId == 10000) {
			Issue parentObject = issue.getParentObject();
			if (parentObject != null) {
				boolean result;
				IssueService issueService = ComponentAccessor.getIssueService();
				IssueService.IssueResult transResult = null;
				int actionId = 11;
				IssueInputParameters issueInputParameters = issueService.newIssueInputParameters();

				IssueService.TransitionValidationResult validationResult = issueService.validateTransition(
						issueEvent.getUser(), parentObject.getId(), actionId, issueInputParameters);
				result = validationResult.isValid();
				if (result) {
					transResult = issueService.transition(issueEvent.getUser(), validationResult);
				}
				log.info("Transition valid:{}, result: {}", result, transResult);
			}
		} else if (eventTypeId == EventType.ISSUE_WORKLOGGED_ID) {
			IssueLinkManager issueLinkManager = ComponentAccessor.getIssueLinkManager();
			LinkCollection linkCollection = issueLinkManager.getLinkCollection(issue, issueEvent.getUser());
			List<Issue> mainStories = linkCollection.getOutwardIssues("Sub story");

			if (mainStories != null) {
				for (Issue story : mainStories) {
					updateMainStory(story, issue, issueEvent.getUser(), issueEvent.getWorklog().getTimeSpent());
				}
				log.info("Links: {}", linkCollection);
			}
		} else if (eventTypeId == EventType.ISSUE_UPDATED_ID){
			IssueLinkManager issueLinkManager = ComponentAccessor.getIssueLinkManager();
			LinkCollection linkCollection = issueLinkManager.getLinkCollection(issue, issueEvent.getUser());
			List<Issue> mainStories = linkCollection.getOutwardIssues("Sub story");

			if (mainStories != null) {
				for (Issue story : mainStories) {
					IssueService issueService = ComponentAccessor.getIssueService();
					IssueInputParameters updateInputParameters = issueService.newIssueInputParameters();
					log.info("Main story orig remaining: {}, subStory remaining: {}", story.getEstimate(), issue
							.getEstimate());
					updateInputParameters.setRemainingEstimate(story.getEstimate() - issue.getEstimate());
					IssueService.UpdateValidationResult updateValidResult = issueService.validateUpdate(user, story.getId(),
							updateInputParameters);
					boolean updateResult;
					if (updateValidResult.isValid()) {
						IssueService.IssueResult issueResult = issueService.update(user, updateValidResult);
						log.info("issueResult: {}", issueResult);
					}
				}
				log.info("Links: {}", linkCollection);
			}
		}

	}

	private void updateMainStory(Issue story, Issue subStory, ApplicationUser user, Long timeSpent){
		IssueService issueService = ComponentAccessor.getIssueService();
		WorklogManager worklogManager = ComponentAccessor.getWorklogManager();
//		Worklog worklog = new WorklogImpl();
//		worklogManager.create(user, worklogManager.)
		IssueInputParameters updateInputParameters = issueService.newIssueInputParameters();
		log.info("Main story orig remaining: {}, subStory remaining: {}", story.getEstimate(), subStory.getEstimate());
		updateInputParameters.setRemainingEstimate(story.getEstimate() - subStory.getEstimate());
		IssueService.UpdateValidationResult updateValidResult = issueService.validateUpdate(user, story.getId(),
				updateInputParameters);
		boolean updateResult;
		if (updateValidResult.isValid()) {
			IssueService.IssueResult issueResult = issueService.update(user, updateValidResult);
			log.info("issueResult: {}", issueResult);
		}
	}

	public void destroy() throws Exception {
		eventPublisher.unregister(this);
	}

	public void afterPropertiesSet() throws Exception {
		eventPublisher.register(this);
	}
}

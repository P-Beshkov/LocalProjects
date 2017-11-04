package com.webint.jira.impl;

import com.atlassian.event.api.EventListener;
import com.atlassian.event.api.EventPublisher;
import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.config.util.JiraHome;
import com.atlassian.jira.event.issue.IssueEvent;
import com.atlassian.jira.event.type.EventType;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueInputParameters;
import com.atlassian.jira.issue.link.IssueLinkManager;
import com.atlassian.jira.issue.link.LinkCollection;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsDevService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

/**
 * Created by pbeshkov on 11.9.2017 г..
 */
@ExportAsDevService
@Component
@Named("eventListener")
public class IssueListener implements InitializingBean, DisposableBean {

	private static final Logger log = LoggerFactory.getLogger(IssueListener.class);
	private Properties p;
	private EventPublisher eventPublisher;
	private long issueInProgressEventId;
	private int startProgressTransitionId;
	private long issueInTestingEventId;
	private static int inTestingTransitionId;

	@Autowired
	@Inject
	public IssueListener(@ComponentImport EventPublisher eventPublisher) {
		log.info("Innit issue listener eventPublisher: {}", eventPublisher);
		this.eventPublisher = eventPublisher;

		File jiraHome = ComponentAccessor.getComponent(JiraHome.class).getHome();
		File propertyFile = new File(jiraHome, "webint-rules.properties");
		try {
			FileInputStream fileInputStream = new FileInputStream(propertyFile);
			p = new Properties();
			p.load(fileInputStream);
			String eventId = p.getProperty("event.id.issueInProgress");
			issueInProgressEventId = Long.parseLong(eventId);
			String startProgress = p.getProperty("transition.id.start-progress");
			startProgressTransitionId =  Integer.parseInt(startProgress);
			String issueInTestingEvent = p.getProperty("event.id.issueInTesting");
			issueInTestingEventId = Long.parseLong(issueInTestingEvent);
			String inTestingTransition = p.getProperty("transition.id.start-testing");
			inTestingTransitionId  = Integer.parseInt(inTestingTransition);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@EventListener
	public void onIssueEvent(IssueEvent issueEvent) {
		ApplicationUser user = issueEvent.getUser();
		Long eventTypeId = issueEvent.getEventTypeId();
		Issue issue = issueEvent.getIssue();

		if (eventTypeId == issueInProgressEventId) {
			Issue parentObject = issue.getParentObject();
			if (parentObject != null) {
				transitionIssue(user, parentObject.getId(), startProgressTransitionId);
			}
		} else if (eventTypeId == issueInTestingEventId) {
			Issue parentObject = issue.getParentObject();
			if (parentObject != null) {
				transitionIssue(user, parentObject.getId(), inTestingTransitionId);
			}
		} else if (eventTypeId.equals(EventType.ISSUE_WORKLOGGED_ID) || eventTypeId.equals(EventType.ISSUE_UPDATED_ID)) {
			// Time spent is in seconds
			if(!checkIssue(issue, issueEvent.getUser())) {
				checkIssue(issue.getParentObject(), issueEvent.getUser());
			}
		}
	}

	private boolean checkIssue(Issue issue, ApplicationUser user) {
		if (issue == null || user == null) {
			return false;
		}
		IssueLinkManager issueLinkManager = ComponentAccessor.getIssueLinkManager();
		LinkCollection linkCollection = issueLinkManager.getLinkCollection(issue, user);
		List<Issue> mainStories = linkCollection.getOutwardIssues("Sub story");
		// Time spent is in seconds
		boolean hasMainStories = false;
		if (mainStories != null) {
			hasMainStories = true;
			for (Issue story : mainStories) {
				updateMainStory(story, user);
			}
			log.info("Links: {}", linkCollection);
		}
		return hasMainStories;
	}

	private void transitionIssue(ApplicationUser user, Long parentIssueId, int transitionId) {
		boolean result;
		IssueService issueService = ComponentAccessor.getIssueService();
		IssueService.IssueResult transResult = null;
		IssueInputParameters issueInputParameters = issueService.newIssueInputParameters();

		IssueService.TransitionValidationResult validationResult = issueService.validateTransition(
				user, parentIssueId, transitionId, issueInputParameters);
		result = validationResult.isValid();
		if (result) {
			transResult = issueService.transition(user, validationResult);
		}
		log.info("Transition valid:{}, result: {}", result, transResult);
	}

	private void updateMainStory(Issue mainStory, ApplicationUser user) {
		IssueLinkManager issueLinkManager = ComponentAccessor.getIssueLinkManager();
		LinkCollection linkCollection = issueLinkManager.getLinkCollection(mainStory, user);
		List<Issue> subStories = linkCollection.getInwardIssues("Sub story");
		long totalEstimateSeconds = 0;
		if (subStories != null) {
			for (Issue subStory : subStories) {
				long subTasksRemaining = getSubTasksRemaining(subStory);
				totalEstimateSeconds += subTasksRemaining + subStory.getEstimate();
			}
		}

		long totalEstimateMin = totalEstimateSeconds / 60;
		IssueService issueService = ComponentAccessor.getIssueService();
		IssueInputParameters updateInputParameters = issueService.newIssueInputParameters();

		//		log.info("Main mainStory orig remaining: {}, subStory remaining: {}", mainStoryEstimate, subStory.getEstimate());
		updateInputParameters.setRemainingEstimate(totalEstimateMin);
		IssueService.UpdateValidationResult updateValidResult = issueService.validateUpdate(user, mainStory.getId(),
				updateInputParameters);

		if (updateValidResult.isValid()) {
			IssueService.IssueResult issueResult = issueService.update(user, updateValidResult);
			log.info("issueResult: {}", issueResult);
		}
	}

	private long getSubTasksRemaining(Issue subStory) {
		Collection<Issue> subTasks = subStory.getSubTaskObjects();
		long result = 0;
		for (Issue subTask : subTasks) {
			result += subTask.getEstimate();
		}
		return result;
	}

	public void destroy() throws Exception {
		eventPublisher.unregister(this);
	}

	public void afterPropertiesSet() throws Exception {
		eventPublisher.register(this);
	}
}

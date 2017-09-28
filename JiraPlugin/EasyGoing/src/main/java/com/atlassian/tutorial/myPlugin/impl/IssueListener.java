package com.atlassian.tutorial.myPlugin.impl;

import com.atlassian.event.api.EventListener;
import com.atlassian.event.api.EventPublisher;
import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.event.issue.IssueEvent;
import com.atlassian.jira.event.type.EventType;
import com.atlassian.jira.issue.Issue;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsDevService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Named;

/**
 * Created by pbeshkov on 11.9.2017 г..
 */
@ExportAsDevService
@Component
@Named("eventListener")
public class IssueListener implements InitializingBean, DisposableBean {

	private static final Logger log = LoggerFactory.getLogger(IssueListener.class);
	private EventPublisher eventPublisher;

	@Autowired
	public IssueListener(@ComponentImport EventPublisher eventPublisher) {
		log.info("Innit issue listener eventPublisher: {}", eventPublisher);
		this.eventPublisher = eventPublisher;
	}

	@EventListener
	public void onIssueEvent(IssueEvent issueEvent) {
		Long eventTypeId = issueEvent.getEventTypeId();
		Issue issue = issueEvent.getIssue();
		Issue parentObject = issue.getParentObject();
		if(parentObject!=null) {
			boolean result = false;
			IssueService issueService = ComponentAccessor.getIssueService();
			IssueService.IssueResult transResult;
			int actionId = 0;

			//			IssueInputParameters issueInputParameters = new IssueInputParametersImpl();

			//			IssueService.TransitionValidationResult validationResult = issueService.validateTransition(null,
			//					issue.getId(), actionId, issueInputParameters);
			//			result = validationResult.isValid();
			//			if (result) {
			////				transResult = issueService.transition(user, validationResult);
			//			}
		}
		if (eventTypeId.equals(EventType.ISSUE_CREATED_ID)) {
			log.info("Issue {} has been created at {}.", issue.getKey(), issue.getCreated());
		} else if (eventTypeId.equals(EventType.ISSUE_RESOLVED_ID)) {
			log.info("Issue {} has been resolved at {}.", issue.getKey(), issue.getResolutionDate());
		} else if (eventTypeId.equals(EventType.ISSUE_CLOSED_ID)) {
			log.info("Issue {} has been closed at {}.", issue.getKey(), issue.getUpdated());
		}
	}

	public void destroy() throws Exception {
		eventPublisher.unregister(this);
	}

	public void afterPropertiesSet() throws Exception {
		eventPublisher.register(this);
	}
}

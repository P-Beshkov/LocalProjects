package com.atlassian.tutorial.myPlugin.impl;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.Issue;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.Validator;
import com.opensymphony.workflow.WorkflowException;

import java.util.Collection;
import java.util.Map;

public class TransitionValidator implements Validator {
	private final CustomFieldManager customFieldManager;

	private static final String FIELD_NAME = "field";

	public TransitionValidator(CustomFieldManager customFieldManager) {
		this.customFieldManager = customFieldManager;
	}

	public void validate(Map transientVars, Map args, PropertySet ps) throws WorkflowException {
		Issue issue = (Issue) transientVars.get("issue");
		String field = (String) args.get(FIELD_NAME);
		Collection<Issue> subTasks = issue.getSubTaskObjects();
		for (Issue subTask :subTasks) {
			if(subTask.getEstimate()!=0){
				throw new InvalidInputException("SubTask: " + subTask + " has remaining estimate");
			}
		}
//		CustomField customField = customFieldManager.getCustomFieldObjectByName(field);
//
//		if (customField!=null){
//			//Check if the custom field value is NULL
//			if (issue.getCustomFieldValue(customField) == null){
//				throw new InvalidInputException("The field:"+field+" is required!");
//			}
//		}
	}
}

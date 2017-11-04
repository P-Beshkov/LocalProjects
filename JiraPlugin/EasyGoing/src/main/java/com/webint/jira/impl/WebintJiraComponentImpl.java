package com.webint.jira.impl;

import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.ApplicationProperties;
import com.webint.jira.api.JiraPluginComponent;

import javax.inject.Inject;
import javax.inject.Named;

@ExportAsService({ JiraPluginComponent.class })
@Named("jiraPluginComponent")
public class WebintJiraComponentImpl implements JiraPluginComponent {
	@ComponentImport
	private final ApplicationProperties applicationProperties;

	@Inject
	public WebintJiraComponentImpl(final ApplicationProperties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}

	public String getName() {
		if (null != applicationProperties) {
			return "myComponent:" + applicationProperties.getDisplayName();
		}

		return "myComponent";
	}
}
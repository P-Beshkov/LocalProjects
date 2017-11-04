package it.com.atlassian.tutorial.myPlugin;

import org.junit.Test;
import org.junit.runner.RunWith;
import com.atlassian.plugins.osgi.test.AtlassianPluginsTestRunner;
import com.webint.jira.api.JiraPluginComponent;
import com.atlassian.sal.api.ApplicationProperties;

import static org.junit.Assert.assertEquals;

@RunWith(AtlassianPluginsTestRunner.class)
public class MyComponentWiredTest
{
    private final ApplicationProperties applicationProperties;
    private final JiraPluginComponent jiraPluginComponent;

    public MyComponentWiredTest(ApplicationProperties applicationProperties,JiraPluginComponent jiraPluginComponent)
    {
        this.applicationProperties = applicationProperties;
        this.jiraPluginComponent = jiraPluginComponent;
    }

    @Test
    public void testMyName()
    {
        assertEquals("names do not match!", "myComponent:" + applicationProperties.getDisplayName(),
				jiraPluginComponent.getName());
    }
}
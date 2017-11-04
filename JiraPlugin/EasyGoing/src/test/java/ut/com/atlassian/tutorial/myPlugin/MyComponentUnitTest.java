package ut.com.atlassian.tutorial.myPlugin;

import org.junit.Test;
import com.webint.jira.api.JiraPluginComponent;
import com.webint.jira.impl.WebintJiraComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        JiraPluginComponent component = new WebintJiraComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }
}
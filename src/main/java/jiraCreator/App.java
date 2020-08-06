package jiraCreator;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.atlassian.jira.rest.client.api.AuthenticationHandler;
import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.atlassian.jira.rest.client.auth.BasicHttpAuthenticationHandler;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTime;


public class App {
    private static final String JIRA_URL = "http://localhost:8080/";
    private static final String JIRA_USER = "barten74";
    private static final String JIRA_PASSWORD = "83513551084";
    private static final String JIRA_PROJECT_KEY = "TES";
    private static final String JIRA_ASSIGNEE = "barten74";

    public static void main(String[] args) throws JSONException {
        URI jiraServerUri = URI.create(JIRA_URL);
        AsynchronousJiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();

        AuthenticationHandler auth = new BasicHttpAuthenticationHandler(JIRA_USER,
                JIRA_PASSWORD);
        JiraRestClient restClient = factory.create(jiraServerUri, auth);
        IssueRestClient issueClient = restClient.getIssueClient();

        try {
            JSONObject obj = new JSONObject(new String(Files.readAllBytes(Paths.get("jirafile.json"))));
            IssueInputBuilder iib = new IssueInputBuilder();
            iib.setProjectKey(JIRA_PROJECT_KEY);
            iib.setSummary(obj.getString("Summary"));
            iib.setIssueTypeId(10003l);
            iib.setDescription(obj.getString("Description"));

            iib.setAssigneeName(JIRA_ASSIGNEE);
            iib.setDueDate(new DateTime());

            IssueInput issue = iib.build();
            BasicIssue issueObj = issueClient.createIssue(issue).claim();

            System.out.println("Issue " + issueObj.getKey() + " created successfully");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                restClient.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }



    }
}

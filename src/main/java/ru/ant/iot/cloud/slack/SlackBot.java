package ru.ant.iot.cloud.slack;

import org.apache.log4j.Logger;
import ru.ant.common.properties.PropertiesManager;
import ru.ant.common.properties.WebPropertiesManager;
import ru.ant.iot.cloud.queue.TaskQueueManager;

import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by ant on 26.05.2016.
 */
@WebServlet(name = "SlackBot", urlPatterns = {"/slack-bot"})
public class SlackBot extends HttpServlet {
    private static String TOKEN;
    private Logger log = Logger.getLogger(getClass());
    private TaskQueueManager taskQueueManager;

    @Override
    public void init() throws ServletException {
        super.init();

        PropertiesManager propertiesManager = new WebPropertiesManager(getServletContext());
        TOKEN = propertiesManager.getProperty("SlackBot.key");
        taskQueueManager = TaskQueueManager.getInstance();
        taskQueueManager.setPropertyManager(propertiesManager);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("token");
        if (token == null || !token.equals(TOKEN)) return;
        SlackRequest slackRequest = new SlackRequest(request);
        SlackResponse slackResponse;
        try {
            response.setContentType("application/json; charset=UTF-8");
            JsonObject json = slackRequest.getCommand().parse();
            if (taskQueueManager.offerTask(json)) {
                slackResponse = getResponse(slackRequest, "Command " + json.toString() + " added to queue");
            } else {
                slackResponse = getResponse(slackRequest, "FullQueue. Repeat later.");
            }
        } catch (Exception e) {
            slackResponse = getResponse(slackRequest, e.getMessage());
        }
        response.getWriter().write(slackResponse.getJson());
    }

    private SlackResponse getResponse(SlackRequest slackRequest, String message) {
        return new SlackResponse("@" + slackRequest.getUserName() + ": " + message);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

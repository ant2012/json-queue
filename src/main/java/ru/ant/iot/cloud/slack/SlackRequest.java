package ru.ant.iot.cloud.slack;

import ru.ant.iot.cloud.queue.command.RaspiQueueCommand;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by ant on 26.05.2016.
 */
public class SlackRequest {
    private final String token;
    private final String teamId;
    private final String teamDomain;
    private final String channelId;
    private final String channelName;
    private final String timestamp;
    private final String userId;
    private final String userName;
    private final String text;
    private final String triggerWord;

    public SlackRequest(HttpServletRequest request) {
        token = request.getParameter("token");
        teamId = request.getParameter("team_id");
        teamDomain = request.getParameter("team_domain");
        channelId = request.getParameter("channel_id");
        channelName = request.getParameter("channel_name");
        timestamp = request.getParameter("timestamp");
        userId = request.getParameter("user_id");
        userName = request.getParameter("user_name");
        text = request.getParameter("text");
        triggerWord = request.getParameter("trigger_word");
    }

    @Override
    public String toString() {
        return String.format("token=%1$s\n" +
                "team_id=%2$s\n" +
                "team_domain=%3$s\n" +
                "channel_id=%4$s\n" +
                "channel_name=%5$s\n" +
                "timestamp=%6$s\n" +
                "user_id=%7$s\n" +
                "user_name=%8$s\n" +
                "text=%9$s\n" +
                "trigger_word=%10$s"
                , token, teamId, teamDomain, channelId
                , channelName, timestamp, userId
                , userName, text, triggerWord);
    }

    public RaspiQueueCommand getCommand() {
        return new RaspiQueueCommand(triggerWord, text);
    }

    public String getUserName() {
        return userName;
    }
}

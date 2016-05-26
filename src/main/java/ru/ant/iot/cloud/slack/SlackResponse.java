package ru.ant.iot.cloud.slack;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * Created by ant on 26.05.2016.
 */
public class SlackResponse {
    private final String text;

    public SlackResponse(String text) {
        this.text = text;
    }


    public String getJson() {
        return Json.createObjectBuilder()
                .add("text", text)
                .build().toString();
    }
}

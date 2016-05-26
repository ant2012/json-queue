package ru.ant.iot.cloud.queue.command;

import org.apache.commons.lang3.NotImplementedException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * Created by ant on 26.05.2016.
 */
public class RaspiQueueCommand {
    private final String triggerWord;
    private final String text;
    private String commandClass;

    public RaspiQueueCommand(String triggerWord, String text) {
        this.triggerWord = triggerWord;
        this.text = text;
    }

    public JsonObject parse() {
        String[] words = text.substring(triggerWord.length()).trim().split(" ");
        JsonObjectBuilder json = Json.createObjectBuilder();
        commandClass = words[0];
        json.add("class", commandClass);
        switch (commandClass){
            case "snap": break;
            case "reboot": break;
            case "wol" :
                json.add("mac", words[1]);
                if(words.length>2) json.add("ip", words[2]);
                break;
            default: throw new NotImplementedException("Class " + commandClass + " not supported");
        }
        return json.build();
    }
}

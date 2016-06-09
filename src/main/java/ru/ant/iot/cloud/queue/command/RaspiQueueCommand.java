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
    private final String helpText = "Raspi bot help\n"
            + "Type: \"raspi: <command>\"\n"
            + "Commands:\n"
            + "reboot - Reboots device\n"
            + "wol <mac> [ip] - Sends magic packet\n"
            + "snap - makes photo for this chat via flickr\n"
            + "stream [resolution [framerate]] - starts mjpg-streamer\n"
            + "help - shows this help"
            ;

    public RaspiQueueCommand(String triggerWord, String text) {
        this.triggerWord = triggerWord;
        this.text = text;
    }

    public JsonObject parse() throws Exception {
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
            case "stream":
                if(words.length>1)
                    json.add("resolution", words[1]);
                if(words.length>2)
                    json.add("framerate", words[2]);
                break;
            case "help" :
            default:
                throw new Exception(helpText);
        }
        return json.build();
    }
}

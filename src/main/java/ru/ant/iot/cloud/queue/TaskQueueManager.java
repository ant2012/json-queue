package ru.ant.iot.cloud.queue;

import ru.ant.common.properties.PropertiesManager;

import javax.json.JsonObject;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Ant on 16.05.2016.
 */
public class TaskQueueManager {
    private final ArrayBlockingQueue<JsonObject> queue;

    public TaskQueueManager(PropertiesManager webPropertiesManager) {
        int queueMaxSize = Integer.parseInt(webPropertiesManager.getProperty("TaskQueueManager.queue.maxSize"));
        queue = new ArrayBlockingQueue<>(queueMaxSize, true);

    }

    public boolean offerTask(JsonObject task){
        return queue.offer(task);
    }

    public JsonObject getNextTask(){
        return queue.poll();
    }
}

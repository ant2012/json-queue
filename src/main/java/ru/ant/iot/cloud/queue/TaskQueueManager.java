package ru.ant.iot.cloud.queue;

import ru.ant.common.properties.PropertiesManager;

import javax.json.JsonObject;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Ant on 16.05.2016.
 */
public class TaskQueueManager {
    private static TaskQueueManager ourInstance = new TaskQueueManager();
    private PropertiesManager propertyManager;

    public static TaskQueueManager getInstance() {
        return ourInstance;
    }

    private ArrayBlockingQueue<JsonObject> queue;

    private TaskQueueManager() {
    }

    public boolean offerTask(JsonObject task){
        return queue.offer(task);
    }

    public JsonObject getNextTask(){
        return queue.poll();
    }

    public void setPropertyManager(PropertiesManager propertyManager) {
        this.propertyManager = propertyManager;
        int queueMaxSize = Integer.parseInt(propertyManager.getProperty("TaskQueueManager.queue.maxSize"));
        queue = new ArrayBlockingQueue<>(queueMaxSize, true);
    }

    public PropertiesManager getPropertyManager() {
        return propertyManager;
    }
}

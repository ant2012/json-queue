package ru.ant.iot.cloud.queue;

import org.apache.log4j.Logger;
import ru.ant.common.properties.PropertiesManager;
import ru.ant.common.properties.WebPropertiesManager;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParsingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Ant on 16.05.2016.
 */
@javax.servlet.annotation.WebServlet(name = "TaskQueueServlet", urlPatterns = {"/task-queue"})
public class TaskQueueServlet extends javax.servlet.http.HttpServlet {

    private Logger log = Logger.getLogger(getClass());
    private String secretKey;
    private TaskQueueManager taskQueueManager;

    @Override
    public void init() throws ServletException {
        super.init();

        PropertiesManager propertiesManager = new WebPropertiesManager(getServletContext());
        secretKey = propertiesManager.getProperty("secret.key");
        taskQueueManager = new TaskQueueManager(propertiesManager);
    }

    @Override
    public void destroy() {
        super.destroy();
        taskQueueManager = null;
    }

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) {
        String key = request.getParameter("key");
        if(!secretKey.equals(key)) return;

        String direction = request.getParameter("direction");
        direction = direction != null ? direction : "";
        try {
            switch (direction){
                case "add":
                    JsonObject json = readJsonData(request);
                    if(json == null) {
                        response.getWriter().write(getStatusJson("Can not parse JSON data"));
                        return;
                    }
                    if(taskQueueManager.offerTask(json))
                        response.getWriter().write(getStatusJson("ok"));
                    else
                        response.getWriter().write(getStatusJson("FullQueue"));
                    break;
                case "get":
                    json = taskQueueManager.getNextTask();
                    if(json == null) {
                        response.getWriter().write(getStatusJson("EmptyQueue"));
                        return;
                    }
                    response.setContentType("application/json; charset=UTF-8");
                    response.getWriter().write(packData(json));
                    break;
                default:
                    response.getWriter().write(getStatusJson("Wrong direction. Use [add] or [get]."));
            }
        } catch (IOException e) {
            logError(e);
        }
    }

    private String packData(JsonObject json) {
        return Json.createObjectBuilder()
                .add("status", "data")
                .add("data", json)
                .build().toString();
    }

    private String getStatusJson(String status) {
        return Json.createObjectBuilder().add("status", status).build().toString();
    }

    private void logError(Exception e) {
        log("Error processing TaskQueueServlet request", e);
    }

    private JsonObject readJsonData(HttpServletRequest request) throws IOException {
        JsonReader jsonReader = Json.createReader(request.getReader());
        try{
            JsonObject jsonObject = jsonReader.readObject();
            jsonReader.close();
            return jsonObject;
        }catch(JsonParsingException e){
            log.error("Cannot parse JSON data", e);
            return null;
        }
    }

}

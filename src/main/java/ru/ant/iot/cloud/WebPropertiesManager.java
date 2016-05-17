package ru.ant.iot.cloud;

import ru.ant.iot.common.Loggable;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by ant on 16.05.2016.
 */
public class WebPropertiesManager extends Loggable {
    private final String APP_PROPERTIES_FILENAME = "/WEB-INF/app.properties";
    private final Properties properties;

    public String getProperty(String key){
        return (String) properties.get(key);
    }

    public WebPropertiesManager(ServletContext servletContext) {

        InputStream appPropsStream = servletContext.getResourceAsStream(APP_PROPERTIES_FILENAME);
        if (appPropsStream == null) log.error("Error loading properties from " + APP_PROPERTIES_FILENAME);
        properties = new Properties();
        try {
            properties.load(appPropsStream);
        } catch (IOException e) {
            log.error("Error loading properties from " + APP_PROPERTIES_FILENAME);
        }
    }
}

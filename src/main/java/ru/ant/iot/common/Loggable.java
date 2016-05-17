package ru.ant.iot.common;

import org.apache.log4j.Logger;

/**
 * Created by ant on 16.05.2016.
 */
public abstract class Loggable {
    protected Logger log = Logger.getLogger(getClass());
}

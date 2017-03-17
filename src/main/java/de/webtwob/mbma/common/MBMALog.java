package de.webtwob.mbma.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by BB20101997 on 16. Mär. 2017.
 */
public class MBMALog {

    private static final Logger LOGGER = LogManager.getLogger("MBMA");

    private MBMALog() {}

    public static void info(String message,Object ... data){
        LOGGER.info(message,data);
    }

    public static void debug(String message,Object ... data){
        LOGGER.debug(message,data);
    }

    public static void warn(String message,Object ... data){
        LOGGER.warn(message,data);
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}

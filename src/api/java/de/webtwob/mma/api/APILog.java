package de.webtwob.mma.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by BB20101997 on 18. Mär. 2017.
 */
@SuppressWarnings("JavaDoc")
public class APILog {

    private static final Logger LOGGER = LogManager.getLogger(MMAAPI.MODID);

    private APILog() {
    }

    public static void info(String message, Object... data) {
        LOGGER.info(message, data);
    }

    public static void debug(String message, Object... data) {
        LOGGER.debug(message, data);
    }

    public static void warn(String message, Object... data) {
        LOGGER.warn(message, data);
    }

    public static void error(String message, Object... data) {
        LOGGER.error(message, data);
    }

}

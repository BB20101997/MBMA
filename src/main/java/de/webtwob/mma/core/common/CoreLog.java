package de.webtwob.mma.core.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.webtwob.mma.core.MMACore;

/**
 * Created by BB20101997 on 16. Mär. 2017.
 */
@SuppressWarnings("JavaDoc")
public class CoreLog {

    public static final Logger LOGGER = LogManager.getLogger(MMACore.MODID);

    private CoreLog() {
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

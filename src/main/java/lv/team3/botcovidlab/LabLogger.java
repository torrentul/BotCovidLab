package lv.team3.botcovidlab;

import org.apache.log4j.Logger;

public class LabLogger {
    private static final Logger logger = Logger.getLogger(LabLogger.class);

    public static void info(String msg) {
        logger.info(msg);
    }
}

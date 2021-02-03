package lv.team3.botcovidlab;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LabLogger {
    private static final Logger logger = LoggerFactory.getLogger(LabLogger.class);

    public static void info(String msg) {
        logger.info(msg);
    }
}

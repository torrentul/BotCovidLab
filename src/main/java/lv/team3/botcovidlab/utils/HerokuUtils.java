package lv.team3.botcovidlab.utils;

import lv.team3.botcovidlab.LabLogger;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class HerokuUtils {
    public static InputStream getFirebaseSettings() {
        LabLogger.info(System.getenv("firebaseSettings"));
        return new ByteArrayInputStream(System.getenv("firebaseSettings").getBytes());
    }
}

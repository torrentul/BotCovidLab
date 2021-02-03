package lv.team3.botcovidlab.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class HerokuUtils {
    public static InputStream getFirebaseSettings() {
        return new ByteArrayInputStream(System.getenv("firebaseSettings").getBytes());
    }
}

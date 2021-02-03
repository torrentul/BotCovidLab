package lv.team3.botcovidlab.utils;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;

public class HerokuUtils {
    public static InputStream getFirebaseSettings() {
        return new ByteArrayInputStream(System.getenv("firebaseSettings").getBytes());
    }

    public static JsonObject getFacebookSettings() {
        try {
            String settings = System.getenv("facebookSettings");
            return Json.createReader(new StringReader(settings)).readObject();
        } catch (Exception ignore) {
            return null;
        }
    }

    public static JsonObject getTelegramSettings() {
        return null;
    }
}

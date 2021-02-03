package lv.team3.botcovidlab.entityManager;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lv.team3.botcovidlab.BotCovidLab;
import lv.team3.botcovidlab.LabLogger;
import lv.team3.botcovidlab.utils.HerokuUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.InputStream;


/**
 * Creates connection with Firebase database
 * For FileInputStream path valid access .json document needs to be provided
 * For FirebaseOptions URL with the name of the Firebase database needs to be provided
 */
@Service
public class FirebaseInitializer {
    @PostConstruct
    public void initialize() {
        try {
            LabLogger.info("Trying to load ./botcovidlabAdminAccess.json");
            FileInputStream serviceAccount =
                    new FileInputStream("./botcovidlabAdminAccess.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://testbase-70cdb.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);
            LabLogger.info("Loaded ./botcovidlabAdminAccess.json");
        } catch (Exception ignore) {
            LabLogger.info("Failed to load ./botcovidlabAdminAccess.json");
            try {
                LabLogger.info("Trying to load firebase settings from system variables");
                InputStream serviceAccount = HerokuUtils.getFirebaseSettings();
                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setDatabaseUrl("https://testbase-70cdb.firebaseio.com")
                        .build();

                FirebaseApp.initializeApp(options);
                LabLogger.info("Firebase settings loaded from system variables");
            } catch (Exception e) {
                e.printStackTrace();
                LabLogger.info("Failed to load firebase settings from system variables");
            }
        }

    }
}

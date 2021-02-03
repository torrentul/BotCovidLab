package lv.team3.botcovidlab.entityManager;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lv.team3.botcovidlab.utils.HerokuUtils;
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
            FileInputStream serviceAccount =
                    new FileInputStream("./botcovidlabAdminAccess.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://testbase-70cdb.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (Exception ignore) {
            try {
                InputStream serviceAccount = HerokuUtils.getFirebaseSettings();
                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setDatabaseUrl("https://testbase-70cdb.firebaseio.com")
                        .build();

                FirebaseApp.initializeApp(options);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}

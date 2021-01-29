package lv.team3.botcovidlab;

import lv.team3.botcovidlab.adapter.telegram.launcher.TGBotLauncher;
import com.github.messenger4j.Messenger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.context.annotation.Bean;

import java.util.Map;


/*
 * Entry point of SpringBoot application
 * Janis Valentinovics
 */
@SpringBootApplication
@EnableScheduling
public class BotCovidLab {

    @Bean
    public Messenger messenger(@Value("${messenger4j.pageAccessToken}") String pageAccessToken,
                               @Value("${messenger4j.appSecret}") final String appSecret,
                               @Value("${messenger4j.verifyToken}") final String verifyToken) {
        return Messenger.create(pageAccessToken, appSecret, verifyToken);
    }

    private static final Map<String, String> getenv = System.getenv();
    public static void main(String[] args) {
    SpringApplication.run(BotCovidLab.class, args);
    TGBotLauncher.init();



}}

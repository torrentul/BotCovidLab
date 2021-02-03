package lv.team3.botcovidlab;

import com.github.messenger4j.Messenger;
import lv.team3.botcovidlab.adapter.telegram.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


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
//        try {
//            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
//            TelegramBot telegramBot = new TelegramBot();
//            botsApi.registerBot((LongPollingBot) telegramBot);
//            System.out.println("start");
//        } catch (TelegramApiException e) {
//            System.out.println("Destoyed");
//            e.printStackTrace();
//        }


}}

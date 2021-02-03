package lv.team3.botcovidlab;

import com.github.messenger4j.Messenger;
import lv.team3.botcovidlab.adapter.telegram.TelegramBot;
import lv.team3.botcovidlab.entityManager.FirebaseController;
import lv.team3.botcovidlab.entityManager.FirebaseService;
import lv.team3.botcovidlab.entityManager.Patient;
import lv.team3.botcovidlab.utils.HerokuUtils;
import org.apache.log4j.Logger;
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


import javax.json.JsonObject;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;


/*
 * Entry point of SpringBoot application
 * Janis Valentinovics
 */

@SpringBootApplication
@EnableScheduling
public class BotCovidLab {

    @Bean
    public Messenger messenger() {
        LabLogger.info("Loading messenger settings");
        JsonObject settings = HerokuUtils.getFacebookSettings();
        LabLogger.info("Messenger settings loaded");
        LabLogger.info("Creating messenger");
        return Messenger.create(settings.getString("access"), settings.getString("secret"), settings.getString("token"));
    }

    private static final Map<String, String> getenv = System.getenv();

    public static void main(String[] args) {
        SpringApplication.run(BotCovidLab.class, args);
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            TelegramBot telegramBot = new TelegramBot();
            botsApi.registerBot((LongPollingBot) telegramBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        Random r = new Random();
        for(int i = 0; i < 3; i++) {
            generateRandomOuhBoy(r);
        }
    }

    public static void generateRandomOuhBoy(Random random) {
        String[] sil = {
                "as", "kol", "si", "moa", "dom", "bois", "kuls", "nus", "jan", "vlad", "val", "ter", "is", "lav", "ni",
                "ma", "dar", "ke", "kon", "koj", "veme", "lada", "nag", "jup", "sina", "red", "ger", "za", "jat", "be"
        };
        String pid = String.format("%06d-%05d", random.nextInt(1000000), random.nextInt(100000));
        String tem = String.format("%.1f", 35.6F + (random.nextFloat() * 6.0F));
        String mob = String.format("%d%07d", random.nextFloat() > 0.2 ? 2 : 6, random.nextInt(10000000));
        Patient patient = new Patient();
        patient.setPersonalCode(pid);
        patient.setHasCough(random.nextBoolean());
        patient.setHasHeadache(random.nextBoolean());
        patient.setContactPerson(random.nextBoolean());
        patient.setTemperature(tem);
        patient.setHasTroubleBreathing(random.nextBoolean());
        patient.setPhoneNumber(mob);
        String[] fna = new String[2];
        String gen = random.nextFloat() > 0.5 ? "a" : "s";
        for(int j = 0; j < 2; j++) {
            int len = 2 + random.nextInt(3);
            StringBuilder nam = new StringBuilder();
            for(int i = 0; i < len; i++) {
                nam.append(sil[random.nextInt(sil.length)]);
            }
            nam.append(gen);
            fna[j] = nam.substring(0, 1).toUpperCase() + nam.substring(1);
        }
        patient.setName(fna[0]);
        patient.setLastName(fna[1]);
        try {
            FirebaseService.savePatientDetails(patient);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

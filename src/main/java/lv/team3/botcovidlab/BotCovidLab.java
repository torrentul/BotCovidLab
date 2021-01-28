package lv.team3.botcovidlab;

import lv.team3.botcovidlab.adapter.telegram.TGBotLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.Map;


/*
 * Entry point of SpringBoot application
 * Janis Valentinovics
 */
@SpringBootApplication
@EnableScheduling
public class BotCovidLab {
    public static void main(String[] args) {
    SpringApplication.run(BotCovidLab.class, args);
    TGBotLauncher.init();



}}

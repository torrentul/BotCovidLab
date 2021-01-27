package lv.team3.botcovidlab;

import lv.team3.botcovidlab.adapter.telegram.TGBotLauncher;
import lv.team3.botcovidlab.adapter.telegram.TelegramBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;


/*
 * Entry point of SpringBoot application
 * Janis Valentinovics
 */
@SpringBootApplication
public class BotCovidLab {
    private static final Map<String, String> getenv = System.getenv();
    public static void main(String[] args) {
    SpringApplication.run(BotCovidLab.class, args);
    TGBotLauncher.init();



}}

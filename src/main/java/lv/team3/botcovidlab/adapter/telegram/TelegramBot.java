package lv.team3.botcovidlab.adapter.telegram;

import lv.team3.botcovidlab.CovidStats;
import lv.team3.botcovidlab.processors.CovidStatsProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static java.lang.StrictMath.toIntExact;
@Component
@PropertySource("classpath:application.properties")
public  class TelegramBot extends TelegramLongPollingCommandBot {

    @Value("${telegram.name}")
    private String name;
    @Value("${telegram.token}")
    private String token;

    public TelegramBot() {}

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void processNonCommandUpdate(Update update) {

    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        // We check if the update has a message and the message has text
        for (Update update: updates){
            if (update.hasMessage() && update.getMessage().hasText()) {
                if (update.getMessage().getText().equals("/start")) {
                    SendMessage message = MainMenu.showMainMenu(update);
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            } else if (update.hasCallbackQuery()) {
                // Set variables
                EditMessageText new_message = MainMenu.getEditMessageText(update);
                try {
                    execute(new_message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            }
    }
}}

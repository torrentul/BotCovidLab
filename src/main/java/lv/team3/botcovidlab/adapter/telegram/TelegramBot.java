package lv.team3.botcovidlab.adapter.telegram;

import lv.team3.botcovidlab.adapter.telegram.handlers.menu.MainMenu;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

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
    for (Update u : updates){

    }


}}

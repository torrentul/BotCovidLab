package lv.team3.botcovidlab.adapter.telegram;


import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public  class TelegramBot extends TelegramLongPollingCommandBot {



    public TelegramBot() {
    }

    @Override
    public String getBotUsername() {
        return "LvKoronaTrc_bot";
    }

    @Override
    public void processNonCommandUpdate(Update update) {

    }

    @Override
    public String getBotToken() {
        return "1461376238:AAGLfBnjyZtk1MnB7WjZxSseeWYCqwRAzKc";
    }

}

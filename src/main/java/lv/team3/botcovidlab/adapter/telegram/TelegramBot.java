package lv.team3.botcovidlab.adapter.telegram;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.logging.Logger;


/**
 * Main Telegram bot class with Telegram Long Polling Bot overriden methods.
 * Extends TelegramLongPollingCommandBot
 * @author Vladislavs Kraslavskis
 */
@Component
public  class TelegramBot extends TelegramLongPollingCommandBot {
//    private String name="LvKoronaTrc_bot";
//    private String token="1461376238:AAGLfBnjyZtk1MnB7WjZxSseeWYCqwRAzKc";
    private String name="LettersInSpringBot";
    private String token="998565460:AAFCpgrGSRBh_EDeRZoD33zZxcrd7XHfmzw";
    private static final Logger log = Logger.getLogger(String.valueOf(TelegramBot.class));

    public TelegramBot() {}

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public void processNonCommandUpdate(Update update) {}


    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        System.out.println("1");
        for (Update update: updates){
            SendMessage sendMessage = UpdatesProcessor.handleUpdate(update);
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}

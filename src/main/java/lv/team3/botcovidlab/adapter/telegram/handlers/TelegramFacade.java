package lv.team3.botcovidlab.adapter.telegram.handlers;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lv.team3.botcovidlab.adapter.telegram.TelegramBot;
import lv.team3.botcovidlab.adapter.telegram.cache.PatientDataCache;
import lv.team3.botcovidlab.adapter.telegram.service.MainMenuService;
import lv.team3.botcovidlab.adapter.telegram.service.ReplyMessagesService;
import lv.team3.botcovidlab.entityManager.Patient;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * @author Sergei Viacheslaev
 */
@Component
@Slf4j
public class TelegramFacade {
    private BotStateContext botStateContext;
    private PatientDataCache patientDataCache;
    private MainMenuService mainMenuService;
    private TelegramBot telegramBot;
    private ReplyMessagesService messagesService;

    public TelegramFacade(BotStateContext botStateContext, PatientDataCache patientDataCache, MainMenuService mainMenuService,
                          @Lazy TelegramBot telegramBot, ReplyMessagesService messagesService) {
        this.botStateContext = botStateContext;
        this.patientDataCache = patientDataCache;
        this.mainMenuService = mainMenuService;
        this.telegramBot = telegramBot;
        this.messagesService = messagesService;
    }

    public BotApiMethod<?> handleUpdate(Update update) {
        SendMessage replyMessage = null;

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.info("New callbackQuery from User: {}, userId: {}, with data: {}", update.getCallbackQuery().getFrom().getUserName(),
                    callbackQuery.getFrom().getId(), update.getCallbackQuery().getData());
            return processCallbackQuery(callbackQuery);
        }


        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            log.info("New message from User:{}, userId: {}, chatId: {},  with text: {}",
                    message.getFrom().getUserName(), message.getFrom().getId(), message.getChatId(), message.getText());
            replyMessage = handleInputMessage(message);
        }

        return replyMessage;
    }


    private SendMessage handleInputMessage(Message message) {
        String inputMsg = message.getText();
        int userId = message.getFrom().getId();
        long chatId = message.getChatId();
        BotStates botState;
        SendMessage replyMessage;

        switch (inputMsg) {
            case "/start":
                botState = BotStates.DEFAULT;
                break;
            case "Get Covid-19 Stats of specific country":
                botState = BotStates.GET_STATS_COUNTRY;
                break;
            case "Get Worldwide Covid-19 statistics":
                botState = BotStates.GET_STATS_WORLDWIDE;
                break;
            case "Apply For Covid-19 Test in Latvia":

                botState = BotStates.QUESTION1;
                break;
            case "Covid-19 Sypmtoms":
                botState = BotStates.GET_SYMPTOMS;
                break;
            default:
                botState = patientDataCache.getPatientsCurrentBotState(userId);
                break;
        }

        patientDataCache.setPatiensCurrentBotState(userId, botState);

        replyMessage = botStateContext.processInputMessage(botState, message);

        return replyMessage;
    }


    private BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) {
        final long chatId = buttonQuery.getMessage().getChatId();
        final int userId = buttonQuery.getFrom().getId();
        BotApiMethod<?> callBackAnswer = mainMenuService.getMainMenuMessage(chatId, "Воспользуйтесь главным меню");


        //From Destiny choose buttons
        if (buttonQuery.getData().equals("buttonYes")) {
            callBackAnswer = new SendMessage(String.valueOf(chatId), "Как тебя зовут ?");
            patientDataCache.setPatiensCurrentBotState(userId, BotStates.QUESTION1);
        } else if (buttonQuery.getData().equals("buttonNo")) {
            callBackAnswer = sendAnswerCallbackQuery("Возвращайся, когда будешь готов", false, buttonQuery);
        } else if (buttonQuery.getData().equals("buttonIwillThink")) {
            callBackAnswer = sendAnswerCallbackQuery("Данная кнопка не поддерживается", true, buttonQuery);
        }

        //From Gender choose buttons
        else if (buttonQuery.getData().equals("buttonMan")) {
            Patient patient = patientDataCache.getPatientData(userId);
            patient.setIsContactPerson(true);
            patientDataCache.savePatientsProfileData(userId, patient);
            patientDataCache.setPatiensCurrentBotState(userId, BotStates.QUESTION2);
            callBackAnswer = new SendMessage(String.valueOf(chatId), "Твоя любимая цифра");
        } else if (buttonQuery.getData().equals("buttonWoman")) {
            Patient patient = patientDataCache.getPatientData(userId);
//            patient.setGender("Ж");
            patientDataCache.savePatientsProfileData(userId, patient);
            patientDataCache.setPatiensCurrentBotState(userId, BotStates.QUESTION2);
            callBackAnswer = new SendMessage(String.valueOf(chatId), "Твоя любимая цифра");

        } else {
            patientDataCache.setPatiensCurrentBotState(userId, BotStates.SHOW_MAIN_MENU);
        }


        return callBackAnswer;


    }


    private AnswerCallbackQuery sendAnswerCallbackQuery(String text, boolean alert, CallbackQuery callbackquery) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackquery.getId());
        answerCallbackQuery.setShowAlert(alert);
        answerCallbackQuery.setText(text);
        return answerCallbackQuery;
    }




}

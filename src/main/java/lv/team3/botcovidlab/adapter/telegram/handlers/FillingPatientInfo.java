package lv.team3.botcovidlab.adapter.telegram.handlers;


import lombok.extern.slf4j.Slf4j;
import lv.team3.botcovidlab.adapter.telegram.Emojis;
import lv.team3.botcovidlab.adapter.telegram.cache.PatientDataCache;
import lv.team3.botcovidlab.adapter.telegram.service.PredictionService;
import lv.team3.botcovidlab.adapter.telegram.service.ReplyMessagesService;
import lv.team3.botcovidlab.entityManager.FirebaseService;
import lv.team3.botcovidlab.entityManager.Patient;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;



@Slf4j
@Component
public class FillingPatientInfo implements InputMessageHandler {
    private PatientDataCache patientDataCache;
    private ReplyMessagesService messagesService;
    private PredictionService predictionService;
    private FirebaseService firebaseService;

    public FillingPatientInfo(PatientDataCache patientDataCache, ReplyMessagesService messagesService, PredictionService predictionService, FirebaseService firebaseService) {
        this.patientDataCache = patientDataCache;
        this.messagesService = messagesService;
        this.predictionService = predictionService;
        this.firebaseService = firebaseService;
    }

    @Override
    public SendMessage handle(Message message) {
        if (patientDataCache.getPatientsCurrentBotState(message.getFrom().getId()).equals(BotStates.IN_PROGRESS)) {
            patientDataCache.setPatiensCurrentBotState(message.getFrom().getId(), BotStates.QUESTION1);
        }
        return processUsersInput(message);
    }

    @Override
    public BotStates getHandlerName() {
        return BotStates.IN_PROGRESS;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        String usersAnswer = inputMsg.getText();
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

        Patient patient = patientDataCache.getPatientData(userId);
        BotStates botState = patientDataCache.getPatientsCurrentBotState(userId);

        SendMessage replyToUser = null;


//        DEFAULT(""),
//                IN_PROGRESS("Profile is filling"),
//                QUESTION1("Please, enter your firstname."),
//                QUESTION2("Please, enter your lastname."),
//                QUESTION3("What is your ID?"),
//                QUESTION4("What is your body temperature?"),
//                QUESTION5("Do you have a cough?"),
//                QUESTION6("Do you have troubles in breathing?"),
//                QUESTION7("Do you have a headache?"),
//                QUESTION8("Have you been In contact with covid positive person?"),
//                QUESTION9("Please, enter your telephone number:"),
//                SHOW_MAIN_MENU("Main Menu"),
//                PROFILE_FILLED("Your Application Recieved. We will contact with you as soon as possible.");

//                QUESTION1("Please, enter your firstname."),

        if (botState.equals(BotStates.QUESTION1)) {
            replyToUser.setText(BotStates.QUESTION1.name());
            patientDataCache.setPatiensCurrentBotState(userId, BotStates.QUESTION2);
        }
//                QUESTION2("Please, enter your lastname."),

        if (botState.equals(BotStates.QUESTION2)) {
            replyToUser.setText(BotStates.QUESTION2.name());
            patient.setName(usersAnswer);
            patientDataCache.setPatiensCurrentBotState(userId, BotStates.QUESTION3);
        }
//                QUESTION3("What is your ID?"),

        if (botState.equals(BotStates.QUESTION3)) {
            patient.setLastName(usersAnswer);
            replyToUser.setText(BotStates.QUESTION3.name());
            replyToUser.setReplyMarkup(getGenderButtonsMarkup());
        }
//                QUESTION4("What is your body temperature?"),

        if (botState.equals(BotStates.QUESTION4)) {
            replyToUser.setText(BotStates.QUESTION4.name());
            patient.setPersonalCode(usersAnswer);
            patientDataCache.setPatiensCurrentBotState(userId, BotStates.QUESTION5);
        }
//                QUESTION5("Do you have a cough?"),

        if (botState.equals(BotStates.QUESTION5)) {
            replyToUser.setText(BotStates.QUESTION5.name());
            //Implement Keyboard
            patient.setTemperature(Double.parseDouble(usersAnswer));
            patientDataCache.setPatiensCurrentBotState(userId, BotStates.QUESTION6);
        }
//                QUESTION6("Do you have troubles in breathing?"),

        if (botState.equals(BotStates.QUESTION6)) {
            replyToUser.setText(BotStates.QUESTION6.name());

            //Implement Keyboard
            patientDataCache.setPatiensCurrentBotState(userId, BotStates.QUESTION7);
        }
//                QUESTION7("Do you have a headache?"),


        if (botState.equals(BotStates.QUESTION7)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askSong");

            //Implement Keyboard

            patientDataCache.setPatiensCurrentBotState(userId, BotStates.PROFILE_FILLED);
        }
//                QUESTION8("Have you been In contact with covid positive person?"),

        if (botState.equals(BotStates.QUESTION7)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askSong");

            //Implement Keyboard

            patientDataCache.setPatiensCurrentBotState(userId, BotStates.PROFILE_FILLED);
        }
        //                QUESTION9("Please, enter your telephone number:"),

        if (botState.equals(BotStates.QUESTION9)) {
            replyToUser.setText(BotStates.QUESTION9.name());
            patient.setIsContactPerson(Boolean.parseBoolean(usersAnswer));
            patientDataCache.setPatiensCurrentBotState(userId, BotStates.PROFILE_FILLED);
        }

        if (botState.equals(BotStates.PROFILE_FILLED)) {
            patient.setPhoneNumber(usersAnswer);
            patient.setChatid(chatId);

//            profileDataService.saveUserProfileData(profileData);

            patientDataCache.setPatiensCurrentBotState(userId, BotStates.SHOW_MAIN_MENU);

            String profileFilledMessage = messagesService.getReplyText("reply.profileFilled",
                    patient.getName(), Emojis.SPARKLES);
            String predictionMessage = predictionService.getPrediction();

            replyToUser = new SendMessage(String.valueOf(chatId), String.format("%s%n%n%s %s", profileFilledMessage, Emojis.SCROLL, predictionMessage));
            replyToUser.setParseMode("HTML");
        }

        patientDataCache.savePatientsProfileData(userId,patient);

        return replyToUser;
    }

    private InlineKeyboardMarkup getGenderButtonsMarkup() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton buttonGenderMan = new InlineKeyboardButton();
        buttonGenderMan.setText("Yes");
        InlineKeyboardButton buttonGenderWoman = new InlineKeyboardButton();
        buttonGenderWoman.setText("No");

        //Every button must have callBackData, or else not work !
        buttonGenderMan.setCallbackData(String.valueOf(true));
        buttonGenderWoman.setCallbackData(String.valueOf(false));

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonGenderMan);
        keyboardButtonsRow1.add(buttonGenderWoman);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }


}
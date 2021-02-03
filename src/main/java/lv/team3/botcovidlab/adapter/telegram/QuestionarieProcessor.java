package lv.team3.botcovidlab.adapter.telegram;


import lv.team3.botcovidlab.adapter.telegram.cache.PatientDataCache;
import lv.team3.botcovidlab.adapter.telegram.state.BotStates;
import lv.team3.botcovidlab.entityManager.FirebaseService;
import lv.team3.botcovidlab.entityManager.Patient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.ExecutionException;

public class QuestionarieProcessor {
    public static void start(Update update) {
        Long id = update.getMessage().getChatId();
        if (PatientDataCache.getPatientsCurrentBotState(id).equals(BotStates.DEFAULT)  ){
            PatientDataCache.setPatiensCurrentBotState(id,BotStates.QUESTION1);
        }
    }


    public static SendMessage getMessageByStatus(Update update) {
        System.out.println("getMessageByStatus");
        SendMessage replyToUser = new SendMessage();
        long chatId = update.getMessage().getChatId();
        replyToUser.setChatId(String.valueOf(chatId));
        Patient patient = PatientDataCache.getPatientData(chatId);
        BotStates botState = PatientDataCache.getPatientsCurrentBotState(chatId);
        String usersAnswer = update.getMessage().getText();


        if (botState.equals(BotStates.QUESTION1)) {
            replyToUser.setText("Please, enter your firstname.");
            PatientDataCache.setPatiensCurrentBotState(chatId, BotStates.QUESTION2);
        }

        if (botState.equals(BotStates.QUESTION2)) {
            replyToUser.setText("Please, enter your lastname:");
            patient.setName(usersAnswer);
            System.out.println(usersAnswer);
            PatientDataCache.setPatiensCurrentBotState(chatId, BotStates.QUESTION3);
        }

        if (botState.equals(BotStates.QUESTION3)) {
            replyToUser.setText("What is your ID?");
            patient.setLastName(usersAnswer);
            System.out.println(usersAnswer);
            PatientDataCache.setPatiensCurrentBotState(chatId, BotStates.QUESTION4);
        }

        if (botState.equals(BotStates.QUESTION4)) {
            replyToUser.setText("What is your body temperature?");
            patient.setPersonalCode(usersAnswer);
            System.out.println(usersAnswer);
            PatientDataCache.setPatiensCurrentBotState(chatId, BotStates.QUESTION5);
        }

        if (botState.equals(BotStates.QUESTION5)) {
            replyToUser.setText("Do you have a cough?(Yes/No)");
            patient.setPersonalCode(usersAnswer);
            System.out.println(usersAnswer);
//            replyToUser.setReplyMarkup(getInLineKeyboard());
            PatientDataCache.setPatiensCurrentBotState(chatId, BotStates.QUESTION6);
        }
        if (botState.equals(BotStates.QUESTION6)) {
            replyToUser.setText("Do you have troubles in breathing?(Yes/No)");
            System.out.println(getBooleanFromAnswer(usersAnswer));
            System.out.println(usersAnswer);

            patient.setHasCough(getBooleanFromAnswer(usersAnswer));

            System.out.println(usersAnswer);
//            replyToUser.setReplyMarkup(getInLineKeyboard());
            PatientDataCache.setPatiensCurrentBotState(chatId, BotStates.QUESTION7);
        }
        if (botState.equals(BotStates.QUESTION7)) {
            replyToUser.setText("Do you have a headache?(Yes/No)");
            patient.setHasTroubleBreathing(getBooleanFromAnswer(usersAnswer));
            System.out.println(usersAnswer);
//            replyToUser.setReplyMarkup(getInLineKeyboard());
            PatientDataCache.setPatiensCurrentBotState(chatId, BotStates.QUESTION8);
        }
        if (botState.equals(BotStates.QUESTION8)) {
            replyToUser.setText("Have you been In contact with covid positive person?(Yes/No)");
            patient.setHasHeadache(getBooleanFromAnswer(usersAnswer));
            System.out.println(usersAnswer);
//            replyToUser.setReplyMarkup(getInLineKeyboard());
            PatientDataCache.setPatiensCurrentBotState(chatId, BotStates.QUESTION9);
        }
        if (botState.equals(BotStates.QUESTION9)) {
            replyToUser.setText("Please, enter your telephone number:");
            patient.setContactPerson(getBooleanFromAnswer(usersAnswer));
            System.out.println(usersAnswer);
//            replyToUser.setReplyMarkup(getInLineKeyboard());
            PatientDataCache.setPatiensCurrentBotState(chatId, BotStates.IN_PROGRESS);
        }
        if (botState.equals(BotStates.IN_PROGRESS)) {
            patient.setPhoneNumber(usersAnswer);
            System.out.println(usersAnswer);
            try {
                FirebaseService.savePatientDetails(patient);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            PatientDataCache.setPatiensCurrentBotState(chatId, BotStates.PROFILE_FILLED);
            replyToUser.setText("Thank you! We will call you back to confirm reservation!");
            System.out.println(patient.toString());
            System.out.println("Patient Saved!");
        }




        return replyToUser;

    }

//    private static InlineKeyboardMarkup getInLineKeyboard(){
//
//        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//        InlineKeyboardButton buttonYes = new InlineKeyboardButton();
//        buttonYes.setText("YES");
//        InlineKeyboardButton buttonNo = new InlineKeyboardButton();
//        buttonNo.setText("NO");
//
//        //Every button must have callBackData, or else not work !
//        buttonYes.setCallbackData(String.valueOf(true));
//        buttonNo.setCallbackData(String.valueOf(false));
//
//        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
//        keyboardButtonsRow1.add(buttonYes);
//        keyboardButtonsRow1.add(buttonNo);
//
//        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
//        rowList.add(keyboardButtonsRow1);
//
//        inlineKeyboardMarkup.setKeyboard(rowList);
//        System.out.println("Markup Created");
//        return inlineKeyboardMarkup;
//    }
    private static boolean getBooleanFromAnswer (String answer) {
        return answer.equals("Yes");
    }

}

package lv.team3.botcovidlab.adapter.telegram;

import lv.team3.botcovidlab.CovidStats;
import lv.team3.botcovidlab.adapter.telegram.cache.PatientDataCache;
import lv.team3.botcovidlab.adapter.telegram.state.BotStates;
import lv.team3.botcovidlab.entityManager.FirebaseService;
import lv.team3.botcovidlab.entityManager.Patient;
import lv.team3.botcovidlab.processors.CovidStatsProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * Processes Covid Application Questionnaire
 * @author Vladislavs Kraslavskis
 */

    public class QuestionnarieProcessor {


    /**
     * Change users Bot State, to  understand, that user is in application filling mode.
     * @param update recieved from Telegram.
     */
        public static void start(Update update) {
            Long id = update.getMessage().getChatId();
        if (PatientDataCache.getPatientsCurrentBotState(id).equals(BotStates.DEFAULT)  ){
            PatientDataCache.setPatiensCurrentBotState(id,BotStates.QUESTION1);
        }
    }

    /**
     * Generates SendMessage depending on which question user answered.
     * Sets answers to Patients.
     * @param update recieved from Telegram.
     */
    public static SendMessage getMessageByStatus(Update update) {
        System.out.println("getMessageByStatus");
        SendMessage replyToUser = new SendMessage();
        long chatId = update.getMessage().getChatId();
        replyToUser.setChatId(String.valueOf(chatId));
        Patient patient = PatientDataCache.getPatientData(chatId);
        BotStates botState = PatientDataCache.getPatientsCurrentBotState(chatId);
        String usersAnswer = update.getMessage().getText();
        PeriodMenuService periodMenuService = new PeriodMenuService();



        if (botState.equals(BotStates.FILLING_COUNTRY)) {

            List<CovidStats> covidStats = CovidStatsProcessor.getStatsForLastDay(usersAnswer);
            CovidStats covid = covidStats.get(0);

//            replyToUser.setText("Statistics for "+covid.getCountry()+" on:" + covid.getDate() + " Infected:" + covid.getInfected() + " Recovered:" + covid.getRecovered() + " Deaths:" + covid.getDeaths());
            PatientDataCache.setPatiensCountry(chatId,usersAnswer);
            replyToUser.setText("Please, select data period:");
            replyToUser.setReplyMarkup(periodMenuService.getCountryPeriodKeyboard());
            PatientDataCache.setPatiensCurrentBotState(chatId,BotStates.FILLING_PERIOD);
        }

        if (botState.equals(BotStates.FILLING_PERIOD)) {

            System.out.println(usersAnswer);
            List<CovidStats> covidStats = CovidStatsProcessor.getStatsForLastDay("Latvia");
            CovidStats covid = covidStats.get(0);

            replyToUser.setText("Statistics for "+covid.getCountry()+" on:" + covid.getDate() + " Infected:" + covid.getInfected() + " Recovered:" + covid.getRecovered() + " Deaths:" + covid.getDeaths());


            PatientDataCache.setPatiensCurrentBotState(chatId,PatientDataCache.getPreviousBotState(chatId));
        }

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
            patient.setTemperature(usersAnswer);
            System.out.println(usersAnswer);
            PatientDataCache.setPatiensCurrentBotState(chatId, BotStates.QUESTION5);
        }

        if (botState.equals(BotStates.QUESTION5)) {
            replyToUser.setText("Do you have a cough?(Yes/No)");
            patient.setPersonalCode(usersAnswer);
            System.out.println(usersAnswer);
            PatientDataCache.setPatiensCurrentBotState(chatId, BotStates.QUESTION6);
        }

        if (botState.equals(BotStates.QUESTION6)) {
            replyToUser.setText("Do you have troubles in breathing?(Yes/No)");
            System.out.println(getBooleanFromAnswer(usersAnswer));
            System.out.println(usersAnswer);
            patient.setHasCough(getBooleanFromAnswer(usersAnswer));
            System.out.println(usersAnswer);
            PatientDataCache.setPatiensCurrentBotState(chatId, BotStates.QUESTION7);
        }

        if (botState.equals(BotStates.QUESTION7)) {
            replyToUser.setText("Do you have a headache?(Yes/No)");
            patient.setHasTroubleBreathing(getBooleanFromAnswer(usersAnswer));
            System.out.println(usersAnswer);
            PatientDataCache.setPatiensCurrentBotState(chatId, BotStates.QUESTION8);
        }

        if (botState.equals(BotStates.QUESTION8)) {
            replyToUser.setText("Have you been In contact with covid positive person?(Yes/No)");
            patient.setHasHeadache(getBooleanFromAnswer(usersAnswer));
            System.out.println(usersAnswer);
            PatientDataCache.setPatiensCurrentBotState(chatId, BotStates.QUESTION9);
        }

        if (botState.equals(BotStates.QUESTION9)) {
            replyToUser.setText("Please, enter your telephone number:");
            patient.setContactPerson(getBooleanFromAnswer(usersAnswer));
            System.out.println(usersAnswer);
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
    /**
     * Convert user answer from String (Yes/No) to true/false
     * @param answer String with users answer(Yes/No)
     * @return true if answer is Yes and false if answer is No.
     */
    private static boolean getBooleanFromAnswer (String answer) {
        return answer.equals("Yes");
    }}

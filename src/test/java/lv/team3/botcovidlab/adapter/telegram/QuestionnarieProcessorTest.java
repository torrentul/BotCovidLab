package lv.team3.botcovidlab.adapter.telegram;

import lv.team3.botcovidlab.adapter.telegram.cache.PatientDataCache;
import lv.team3.botcovidlab.adapter.telegram.state.BotStates;
import lv.team3.botcovidlab.entityManager.Patient;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class QuestionnarieProcessorTest {

    @Test
    void start() {
        Update update = new Update();
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(112L);
        message.setChat(chat);
        update.setMessage(message);
        Long chat_id = update.getMessage().getChatId();

        QuestionnarieProcessor.start(update);
        BotStates states = PatientDataCache.getPatientsCurrentBotState(chat_id);
        assertEquals(BotStates.QUESTION1.name(), states.name());
        assertNotEquals(BotStates.PROFILE_FILLED.name(), states.name());
        assertNotEquals(BotStates.DEFAULT.name(), states.name());
    }

    @Test
    void getMessageByStatus() {
        Update update = new Update();
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(112L);
        message.setChat(chat);
        message.setText("Yes");
        update.setMessage(message);
        Long chat_id = update.getMessage().getChatId();
        Patient patient = new Patient();
        PatientDataCache.getPatientsCurrentBotState(chat_id);
        ArrayList<BotStates> botStates = new ArrayList<>();

        for (int i =0;i<getArr().size();i++) {
            PatientDataCache.setPatiensCurrentBotState(chat_id, getArr().get(i));
            String res =QuestionnarieProcessor.getMessageByStatus(update).getText();
            assertEquals(res,getArrStrings().get(i));

        }
    }
    static ArrayList<BotStates> getArr(){
        ArrayList<BotStates> arrayList = new ArrayList<>();
        arrayList.add(BotStates.QUESTION1);
        arrayList.add(BotStates.QUESTION2);
        arrayList.add(BotStates.QUESTION3);
        arrayList.add(BotStates.QUESTION4);
        arrayList.add(BotStates.QUESTION5);
        arrayList.add(BotStates.QUESTION6);
        arrayList.add(BotStates.QUESTION7);
        arrayList.add(BotStates.QUESTION8);
        arrayList.add(BotStates.QUESTION9);

        return arrayList;
    }
    static ArrayList<String> getArrStrings() {
    ArrayList<String> arrayList = new ArrayList<>();
    arrayList.add("Please, enter your firstname.");
        arrayList.add("Please, enter your lastname:");
        arrayList.add("What is your ID?");
        arrayList.add("What is your body temperature?");
        arrayList.add("Do you have a cough?(Yes/No)");
        arrayList.add("Do you have troubles in breathing?(Yes/No)");
        arrayList.add("Do you have a headache?(Yes/No)");
        arrayList.add("Have you been In contact with covid positive person?(Yes/No)");
        arrayList.add("Please, enter your telephone number:");
        arrayList.add("Thank you! We will call you back to confirm reservation!");
        return arrayList;
    }
    }


package lv.team3.botcovidlab.adapter.telegram.state;

import lv.team3.botcovidlab.adapter.telegram.cache.PatientDataCache;
import lv.team3.botcovidlab.entityManager.Patient;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BotStateContextTest {

    @Test
    void isFillingProfileState() {
    ArrayList<BotStates> arrayList = getArr();
        Update update = new Update();
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(112L);
        message.setChat(chat);
        update.setMessage(message);
        Long chat_id = update.getMessage().getChatId();
        BotStates currentState = PatientDataCache.getPatientsCurrentBotState(chat_id);
        Patient patient = new Patient();
        PatientDataCache.savePatientsProfileData(chat_id,patient);

        for (int i=0;i<arrayList.size();i++) {
            PatientDataCache.setPatiensCurrentBotState(chat_id, arrayList.get(i));
            assertTrue(BotStateContext.isFillingProfileState(update));
        }
        PatientDataCache.savePatientsProfileData(chat_id,patient);
        PatientDataCache.setPatiensCurrentBotState(chat_id,BotStates.PROFILE_FILLED);
        assertFalse(BotStateContext.isFillingProfileState(update));
        PatientDataCache.setPatiensCurrentBotState(chat_id,BotStates.DEFAULT);
        assertFalse(BotStateContext.isFillingProfileState(update));

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
        arrayList.add(BotStates.IN_PROGRESS);
        return arrayList;
    }
}
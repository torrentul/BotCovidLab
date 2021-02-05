package lv.team3.botcovidlab.adapter.telegram;

import lv.team3.botcovidlab.adapter.telegram.cache.PatientDataCache;
import lv.team3.botcovidlab.entityManager.Patient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

class UpdatesProcessorTest {

    @Test
    void handleUpdate() {
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


        Assertions.assertEquals(UpdatesProcessor.handleUpdate(getUpd("/start")).getText(),"Welcome to Latvian Korona Tracker!");
        Assertions.assertTrue(UpdatesProcessor.handleUpdate(getUpd("Get Worldwide Covid-19 statistics")).getText().contains("Statistics for World on:"));
        Assertions.assertTrue(UpdatesProcessor.handleUpdate(getUpd("Get Covid Stats For Latvia")).getText().contains("Statistics for"));
        Assertions.assertTrue(UpdatesProcessor.handleUpdate(getUpd("Apply For Covid-19 Test in Latvia")).getText().contains("Please, enter your firstname."));




    }

    static Update getUpd(String str){
        Update update = new Update();
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(112L);
        message.setChat(chat);
        message.setText(str);
        update.setMessage(message);
        return update;
    }


}
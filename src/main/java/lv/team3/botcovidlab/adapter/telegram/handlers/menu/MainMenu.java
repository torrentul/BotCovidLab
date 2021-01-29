package lv.team3.botcovidlab.adapter.telegram.handlers.menu;

import lv.team3.botcovidlab.CovidStats;
import lv.team3.botcovidlab.processors.CovidStatsProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.StrictMath.toIntExact;

public class MainMenu {

//    protected  static SendMessage showMainMenu(Update update) {
//        String message_text = update.getMessage().getText();
//        long chat_id = update.getMessage().getChatId();
//        SendMessage message = new SendMessage(); // Create a message object object
//        message.setChatId(String.valueOf(chat_id));
//        message.setText("You send /start");
//        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
//        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
//        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
//        List<InlineKeyboardButton> rowInline3 = new ArrayList<>();
//        List<InlineKeyboardButton> rowInline4 = new ArrayList<>();
//
//        InlineKeyboardButton kBsomeCountry = new InlineKeyboardButton();
//        kBsomeCountry.setText("Get Covid Stats of specific country");
//        kBsomeCountry.setCallbackData("get_stats_country");
//        rowInline1.add(kBsomeCountry);
//
//        InlineKeyboardButton World = new InlineKeyboardButton();
//        World.setText("Get Worldwide Covid-19 statistics");
//        World.setCallbackData("get_stats_world");
//        rowInline2.add(World);
//
//        InlineKeyboardButton ApplyTest = new InlineKeyboardButton();
//        ApplyTest.setText("Apply For Covid-19 Test in Latvia");
//        ApplyTest.setCallbackData("apply_test");
//        rowInline3.add(ApplyTest);
//
//        InlineKeyboardButton Symptoms = new InlineKeyboardButton();
//        Symptoms.setText("Covid-19 Symptoms");
//        Symptoms.setUrl("https://covid19.gov.lv/index.php/covid-19/par-covid-19/simptomi");
//        rowInline4.add(Symptoms);
//        // Set the keyboard to the markup
//        rowsInline.add(rowInline1);
//        rowsInline.add(rowInline2);
//        rowsInline.add(rowInline3);
//        rowsInline.add(rowInline4);
//        // Add it to the message
//        markupInline.setKeyboard(rowsInline);
//        message.setReplyMarkup(markupInline);
//
//
//
//        return message;
//    }


    public static EditMessageText getEditMessageText(Update update) {

        EditMessageText new_message = new EditMessageText();
        String call_data = update.getCallbackQuery().getData();
        long message_id = update.getCallbackQuery().getMessage().getMessageId();
        long chat_id = update.getCallbackQuery().getMessage().getChatId();

        if (call_data.equals("get_stats_country")) {
            SendMessage message = new SendMessage();
            message.setText("Please enter country name: ");
            message.setChatId(String.valueOf(chat_id));
//           List<CovidStats> covidStats = CovidStatsProcessor.getStats("Latvia",new Date());
            String answer="";
//            for (CovidStats covid : covidStats){
//            answer+=covid.getCountry()+", "+covid.getDate()+": "+"Infected: "+covid.getInfected()+" Recovered: "+covid.getRecovered()+" Deaths: "+covid.getDeaths();
//            new_message = new EditMessageText();
//            new_message.setChatId(String.valueOf(chat_id));
//            new_message.setMessageId(toIntExact(message_id));
//            new_message.setText(answer);}

        }
//        else if (call_data.equals("get_stats_world")){
//
//            CovidStats covidStats = CovidStatsProcessor.getStats("World","01-02-06");
//            String answer = covidStats.getCountry()+", "+covidStats.getDate()+": "+"Infected: "+covidStats.getInfected()+" Recovered: "+covidStats.getRecovered()+" Deaths: "+covidStats.getDeaths();
//            new_message = new EditMessageText();
//            new_message.setChatId(String.valueOf(chat_id));
//            new_message.setMessageId(toIntExact(message_id));
//            new_message.setText(answer);
//
//        }else if (call_data.equals("apply_test")){
//
//        }
        return new_message;

    }
}

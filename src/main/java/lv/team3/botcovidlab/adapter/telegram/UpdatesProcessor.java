package lv.team3.botcovidlab.adapter.telegram;

import lv.team3.botcovidlab.CovidStats;
import lv.team3.botcovidlab.adapter.telegram.cache.PatientDataCache;
import lv.team3.botcovidlab.adapter.telegram.state.BotStateContext;
import lv.team3.botcovidlab.adapter.telegram.state.BotStates;
import lv.team3.botcovidlab.processors.CovidStatsProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public class UpdatesProcessor {
    private static MainMenuService mainMenuService = new MainMenuService();


    public UpdatesProcessor() {
        this.mainMenuService = new MainMenuService();
    }





    public static SendMessage handleUpdate(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
        String res = "";
        Long chatid = update.getMessage().getChatId();
        sendMessage.setText("Your choice:");
        if (!BotStateContext.isFillingProfileState(update)){
        if (update.getMessage().getText().equals("/start")){
        res="Welcome to Latvian Korona Tracker!";
        sendMessage.setText(res);
        }else if (update.getMessage().getText().equals("Get Worldwide Covid-19 statistics")){
            System.out.println("Test");
            CovidStats covid = CovidStatsProcessor.getStatsForLastDay("World").get(0);
            if (CovidStatsProcessor.getStatsForLastDay("world").size()>0) {
                res = "Statistics for World on:" + covid.getDate() + " Infected:" + covid.getInfected() + " Recovered:" + covid.getRecovered() + " Deaths:" + covid.getDeaths();
            }else {
                res="No Data";
                System.out.println(covid.toString());
            }

            sendMessage.setText(res);
        }else if (update.getMessage().getText().equals("Get Covid Stats For Latvia")){
            CovidStats covid = CovidStatsProcessor.getStatsForLastDay("Latvia").get(0);
            res= "Statistics for Latvia on:"+covid.getDate()+" Infected:"+covid.getInfected()+" Recovered:"+covid.getRecovered()+" Deaths:"+covid.getDeaths();
            sendMessage.setText(res);
        }else if (update.getMessage().getText().equals("Apply For Covid-19 Test in Latvia")){
            if(PatientDataCache.getPatientsCurrentBotState(chatid).equals(BotStates.PROFILE_FILLED)){
                sendMessage.setText("Your application submitted! Please, wait for our call and learn Java!");
            }
            else {
            PatientDataCache.setPatiensCurrentBotState(chatid, BotStates.QUESTION2);
            sendMessage.setText("Please, enter your firstname.");
            QuestionarieProcessor.start(update);}
        }
            ReplyKeyboardMarkup replyKeyboardMarkup = mainMenuService.getMainMenuKeyboard();
            sendMessage.setReplyMarkup(replyKeyboardMarkup);

        }else {

            sendMessage = QuestionarieProcessor.getMessageByStatus(update);
            System.out.println("Success");



        }


        return sendMessage;
    }
}

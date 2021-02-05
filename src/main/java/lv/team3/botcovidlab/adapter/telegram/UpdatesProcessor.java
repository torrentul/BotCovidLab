package lv.team3.botcovidlab.adapter.telegram;

import lv.team3.botcovidlab.adapter.facebook.TotalStatUtil;
import lv.team3.botcovidlab.adapter.telegram.cache.PatientDataCache;
import lv.team3.botcovidlab.adapter.telegram.state.BotStateContext;
import lv.team3.botcovidlab.adapter.telegram.state.BotStates;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

/**
 * Generates reply message for user
 * @author Vladislavs Kraslavskis
 */
public class UpdatesProcessor {

    private static MainMenuService mainMenuService = new MainMenuService();
    /**
     * UpdatesProcessor constructor.
     */
    public UpdatesProcessor() {
        this.mainMenuService = new MainMenuService();
    }

    /**
     * @param update - Current update, recieved from Telegram.
     * @return true, if users application for Covid test is in progress.
     */
    public static SendMessage handleUpdate(Update update) {
        System.out.println("handleUpdate");
        System.out.println(update.toString());
        SendMessage sendMessage = new SendMessage();
        String call_data ="";
        String res = "";
        Long chatid = 0l;
        String textMessage="";

        if (update.hasCallbackQuery()){
            chatid=update.getCallbackQuery().getMessage().getChatId();
            textMessage=update.getCallbackQuery().getMessage().getText();
            call_data=update.getCallbackQuery().getData();
            System.out.println("Call Back");
        }else {
            System.out.println("Simple Msg");
            try {
                chatid = update.getMessage().getChatId();
                textMessage=update.getMessage().getText();
            } catch (NullPointerException e) {
                System.out.println("Can`t Parse chat ID");
            }

            try {
                sendMessage.setChatId(String.valueOf(chatid));
            } catch (Exception e) {
                System.out.println("No Data Chat ID");
            }
        }
        sendMessage.setChatId(String.valueOf(chatid));
        sendMessage.setText("Your choice:");
        if (BotStateContext.isFillingProfileState(update)){
            sendMessage = QuestionnarieProcessor.getMessageByStatus(update);
            System.out.println("Success");
        }else if(call_data.equals("today") || call_data.equals("7_Days") || call_data.equals("30_Days")){
            String result="";
            String country = PatientDataCache.getPatientsCountry(chatid);
            if (call_data.equals("today") ){
                result = TotalStatUtil.countTotalYesterday(country);
            }
            else if (call_data.equals("7_Days")){
                result = TotalStatUtil.countTotalSevenDays(country);
            }else if (call_data.equals("30_Days")){
                result=TotalStatUtil.countTotalThirtyDays(country);
            }
            sendMessage.setText(result);
            PatientDataCache.setPatiensCurrentBotState(chatid,PatientDataCache.getPreviousBotState(chatid));

        }else if(BotStateContext.isFillingCountry(update)){
            sendMessage =QuestionnarieProcessor.getMessageByStatus(update);
            PatientDataCache.setPatiensCurrentBotState(chatid,PatientDataCache.getPreviousBotState(chatid));

        }else {
            if (textMessage.equals("/start")){
                res="Welcome to Latvian Korona Tracker!";
                sendMessage.setText(res);
            }else if (textMessage.equals("Get Worldwide Covid-19 statistics")){
                sendMessage.setText("Please, select data period:");
                PatientDataCache.setPatiensCountry(chatid,"world");
                PatientDataCache.setPatiensPreviousBotState(chatid,PatientDataCache.getPatientsCurrentBotState(chatid));
                PatientDataCache.setPatiensCurrentBotState(chatid,BotStates.FILLING_PERIOD);


            }else if (textMessage.equals("Get Covid Stats For Specific Country")){


                sendMessage.setText("Please, enter country:");
                PatientDataCache.setPatiensPreviousBotState(chatid,PatientDataCache.getPatientsCurrentBotState(chatid));
                PatientDataCache.setPatiensCurrentBotState(chatid,BotStates.FILLING_COUNTRY);


            }else if (textMessage.equals("Apply For Covid-19 Test in Latvia")){
                if(PatientDataCache.getPatientsCurrentBotState(chatid).equals(BotStates.PROFILE_FILLED)){
                    sendMessage.setText("Your application submitted! Please, wait for our call and learn Java!");
                }
                else {
                    PatientDataCache.setPatiensCurrentBotState(chatid, BotStates.QUESTION2);
                    sendMessage.setText("Please, enter your firstname.");
                    QuestionnarieProcessor.start(update);}
            }

            ReplyKeyboardMarkup replyKeyboardMarkup = mainMenuService.getMainMenuKeyboard();
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
            if (textMessage.equals("Get Worldwide Covid-19 statistics")){
                PeriodMenuService periodMenuService = new PeriodMenuService();
                sendMessage.setReplyMarkup(periodMenuService.getCountryPeriodKeyboard());

            }
        }
        return sendMessage;
    }
}

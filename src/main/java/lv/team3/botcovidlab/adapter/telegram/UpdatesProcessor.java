package lv.team3.botcovidlab.adapter.telegram;

import lv.team3.botcovidlab.adapter.telegram.state.BotStateContext;
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

        sendMessage.setText("Your choice:");
        if (!BotStateContext.isFillingProfileState(update)){
        if (update.getMessage().getText().equals("/start")){
        res="Welcome to Latvian Korona Tracker!";
        sendMessage.setText(res);
        }else if (update.getMessage().getText().equals("Get Worldwide Covid-19 statistics")){
            res = "World info";


            sendMessage.setText(res);
        }else if (update.getMessage().getText().equals("Get Covid Stats of specific country")){
            res = "Latvian info";
            sendMessage.setText(res);
        }else if (update.getMessage().getText().equals("Apply For Covid-19 Test in Latvia")){
            QuestionarieProcessor.start(update);
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

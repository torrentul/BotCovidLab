package lv.team3.botcovidlab.adapter.telegram.handlers;

import lombok.extern.slf4j.Slf4j;
import lv.team3.botcovidlab.adapter.telegram.service.ReplyMessagesService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class StartMenuHandler implements InputMessageHandler{
    private ReplyMessagesService messagesService;


    private SendMessage processUsersInput(Message inputMsg) {
        long chatId = inputMsg.getChatId();

        SendMessage replyToUser = messagesService.getReplyMessage(chatId, "reply.askDestiny");
        replyToUser.setReplyMarkup(getInlineMessageButtons());

        return replyToUser;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotStates getHandlerName() {
        return BotStates.DEFAULT;
    }

    private InlineKeyboardMarkup getInlineMessageButtons() {

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline3 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline4 = new ArrayList<>();

        InlineKeyboardButton kBsomeCountry = new InlineKeyboardButton();
        kBsomeCountry.setText("Get Covid Stats of specific country");
        kBsomeCountry.setCallbackData("get_stats_country");
        rowInline1.add(kBsomeCountry);

        InlineKeyboardButton World = new InlineKeyboardButton();
        World.setText("Get Worldwide Covid-19 statistics");
        World.setCallbackData("get_stats_world");
        rowInline2.add(World);

        InlineKeyboardButton ApplyTest = new InlineKeyboardButton();
        ApplyTest.setText("Apply For Covid-19 Test in Latvia");
        ApplyTest.setCallbackData("apply_test");
        rowInline3.add(ApplyTest);

        InlineKeyboardButton Symptoms = new InlineKeyboardButton();
        Symptoms.setText("Covid-19 Symptoms");
        Symptoms.setUrl("https://covid19.gov.lv/index.php/covid-19/par-covid-19/simptomi");
        rowInline4.add(Symptoms);
        // Set the keyboard to the markup
        rowsInline.add(rowInline1);
        rowsInline.add(rowInline2);
        rowsInline.add(rowInline3);
        rowsInline.add(rowInline4);
        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        return markupInline;
//        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//
//        InlineKeyboardButton buttonYes = new InlineKeyboardButton();
//        buttonYes.setText("Да");
//        InlineKeyboardButton buttonNo = new InlineKeyboardButton();
//        buttonNo.setText("Нет, спасибо");
//        InlineKeyboardButton buttonIwillThink = new InlineKeyboardButton();
//        buttonIwillThink.setText("Я подумаю");
//        InlineKeyboardButton buttonIdontKnow = new InlineKeyboardButton();
//        buttonIdontKnow.setText("Еще не определился");
//
//        //Every button must have callBackData, or else not work !
//        buttonYes.setCallbackData("buttonYes");
//        buttonNo.setCallbackData("buttonNo");
//        buttonIwillThink.setCallbackData("buttonIwillThink");
//        buttonIdontKnow.setCallbackData("-");
//
//        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
//        keyboardButtonsRow1.add(buttonYes);
//        keyboardButtonsRow1.add(buttonNo);
//
//        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
//        keyboardButtonsRow2.add(buttonIwillThink);
//        keyboardButtonsRow2.add(buttonIdontKnow);
//
//
//        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
//        rowList.add(keyboardButtonsRow1);
//        rowList.add(keyboardButtonsRow2);
//
//        inlineKeyboardMarkup.setKeyboard(rowList);
//
//        return inlineKeyboardMarkup;
    }

}

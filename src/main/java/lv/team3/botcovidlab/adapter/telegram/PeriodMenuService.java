package lv.team3.botcovidlab.adapter.telegram;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Sets Up Main menu buttons and Keyboard Markup
 * @author Vladislavs Kraslavskis
 */
@Service
public class PeriodMenuService {
    /**
     * @return Keyboard Markup for Inline period selection menu
     */
    public InlineKeyboardMarkup getCountryPeriodKeyboard() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();

        InlineKeyboardButton btnT = new InlineKeyboardButton();
        btnT.setText("Today");
        btnT.setCallbackData("today");
        InlineKeyboardButton btn7 = new InlineKeyboardButton();
        btn7.setText("7 Days");
        btn7.setCallbackData("7_Days");
        InlineKeyboardButton btn30 = new InlineKeyboardButton();
        btn30.setText("30 Days");
        btn30.setCallbackData("30_Days");

        row1.add(btnT);
        row2.add(btn7);
        row3.add(btn30);

        rowsInline.add(row1);
        rowsInline.add(row2);
        rowsInline.add(row3);

        keyboardMarkup.setKeyboard(rowsInline);

        return keyboardMarkup;
    }


}

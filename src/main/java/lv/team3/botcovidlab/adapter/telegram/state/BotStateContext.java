package lv.team3.botcovidlab.adapter.telegram.state;


import lv.team3.botcovidlab.adapter.telegram.cache.PatientDataCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Defines if users application form for Covid-19 test is filling.
 * @author Vladislavs Kraslavskis
 */
@Component
public class BotStateContext {

    /**
     * @param update - Current update, recieved from Telegram.
     * @return true, if users application for Covid test is in progress.
     */
    public static boolean isFillingProfileState(Update update) {

        Long chat_id = update.getMessage().getChatId();

        BotStates currentState = PatientDataCache.getPatientsCurrentBotState(chat_id);
        System.out.println(PatientDataCache.getPatientsCurrentBotState(chat_id));

        switch (currentState) {
            case QUESTION1:
            case QUESTION2:
            case QUESTION3:
            case QUESTION4:
            case QUESTION5:
            case QUESTION6:
            case QUESTION7:
            case QUESTION8:
            case QUESTION9:
            case IN_PROGRESS:
                System.out.println(true);
                return true;
            default:
                System.out.println(false);
                return false;
        }
    }


}






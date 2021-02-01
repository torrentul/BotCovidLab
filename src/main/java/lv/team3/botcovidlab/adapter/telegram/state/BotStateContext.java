package lv.team3.botcovidlab.adapter.telegram.state;


import lv.team3.botcovidlab.adapter.telegram.cache.PatientDataCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Defines message handlers for each state.
 */
@Component
public class BotStateContext {


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






package lv.team3.botcovidlab.adapter.telegram.handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Defines message handlers for each state.
 */
@Component
public class BotStateContext {
    private Map<BotStates, InputMessageHandler> messageHandlers = new HashMap<>();

    public BotStateContext(List<InputMessageHandler> messageHandlers) {
        messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getHandlerName(), handler));
    }

    public SendMessage processInputMessage(BotStates currentState, Message message) {
        InputMessageHandler currentMessageHandler = findMessageHandler(currentState);
        return currentMessageHandler.handle(message);
    }

    private InputMessageHandler findMessageHandler(BotStates currentState) {
        if (isFillingProfileState(currentState)) {
            return messageHandlers.get(BotStates.IN_PROGRESS);
        }

        return messageHandlers.get(currentState);
    }

    private boolean isFillingProfileState(BotStates currentState) {
        switch (currentState) {
            case DEFAULT:
            case QUESTION1:
            case QUESTION2:
            case QUESTION3:
            case QUESTION4:
            case QUESTION5:
            case QUESTION6:
            case QUESTION7:
            case PROFILE_FILLED:
                return true;
            default:
                return false;
        }
    }


}






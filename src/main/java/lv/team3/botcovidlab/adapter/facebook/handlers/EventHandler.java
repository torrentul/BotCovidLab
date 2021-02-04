package lv.team3.botcovidlab.adapter.facebook.handlers;

import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.github.messenger4j.webhook.Event;
import com.github.messenger4j.webhook.event.PostbackEvent;
import com.github.messenger4j.webhook.event.QuickReplyMessageEvent;
import com.github.messenger4j.webhook.event.TextMessageEvent;
import lv.team3.botcovidlab.adapter.facebook.MessengerPlatformCallbackHandler;
import lv.team3.botcovidlab.adapter.facebook.ValidCountryList;
import lv.team3.botcovidlab.adapter.facebook.cache.FacebookPatientDataCache;
import lv.team3.botcovidlab.adapter.facebook.senders.Sender;
import org.springframework.stereotype.Service;
import java.net.MalformedURLException;
import java.time.Instant;

import static lv.team3.botcovidlab.adapter.facebook.TotalStatUtil.*;

/**
 * Class that handles all the incoming events
 *
 * @author Vladislavs Visnevskis
 */
@Service
public class EventHandler {

    private final Sender sender;
    private final FacebookPatientDataCache facebookPatientDataCache;

    /**
     * Event handler's constructor
     * @param sender object that sending responses
     * @param facebookPatientDataCache object that safe states
     */
    public EventHandler(Sender sender, FacebookPatientDataCache facebookPatientDataCache) {
        this.sender = sender;
        this.facebookPatientDataCache = facebookPatientDataCache;
    }

    /**
     * Handles the "TextMessageEvent" from Facebook messenger sender
     * If "Country" button pressed method checks for input validity
     * Else for any input shows main selection buttons
     *
     * @param event Facebook messenger text message event
     * @author Vladislavs Visnevskis
     */
    public void handleTextMessageEvent(TextMessageEvent event) {
        MessengerPlatformCallbackHandler.logger.debug("Received TextMessageEvent: {}", event);

        final String messageId = event.messageId();
        final String messageText = event.text();
        final String senderId = event.senderId();
        final Instant timestamp = event.timestamp();

        MessengerPlatformCallbackHandler.logger.info("Received message '{}' with text '{}' from user '{}' at '{}'", messageId, messageText, senderId, timestamp);

        if (facebookPatientDataCache.getUserStates(Long.parseLong(event.senderId())).isPressedButton()) {
            boolean isValidInput = false;
            for(String list : ValidCountryList.countries){
                if (list.toLowerCase().equals(event.text().toLowerCase())){
                    facebookPatientDataCache.getUserStates(Long.parseLong(event.senderId())).setInput(event.text().toLowerCase());
                    isValidInput = true;
                    break;
                }
            }
            if (isValidInput) {
                try {
                    facebookPatientDataCache.getUserStates(Long.parseLong(event.senderId())).setPressedButton(false);
                    sender.sendQuickReplyCountryButtons(senderId);
                } catch (MessengerApiException | MessengerIOException e) {
                    handleSendException(e);
                }
            }
            else{
                sender.sendTextMessage(senderId, "Incorrect input");
                facebookPatientDataCache.getUserStates(Long.parseLong(event.senderId())).setPressedButton(false);
            }
        }

        else {
            try {
                switch (messageText.toLowerCase()) {
                    case "covid Latvia":

                    default:
                        sender.sendButtonMessage(senderId);
                        sender.sendSecondButtonMessage(senderId);
                }
            } catch (MessengerApiException | MessengerIOException | MalformedURLException e) {
                handleSendException(e);
            }
        }
    }

    /**
     * Method that handles user tap of buttons
     *
     * @param event Facebook messenger event
     * Handles the "PostBackEvent" from Facebook messenger sender, after pressing the button
     * @author Vladislavs Visnevskis
     */
    public void handlePostbackEvent(PostbackEvent event) {
        MessengerPlatformCallbackHandler.logger.debug("Handling PostbackEvent");
        final String payload = event.payload().orElse("empty payload");
        MessengerPlatformCallbackHandler.logger.debug("payload: {}", payload);
        final String senderId = event.senderId();
        MessengerPlatformCallbackHandler.logger.debug("senderId: {}", senderId);
        final Instant timestamp = event.timestamp();
        MessengerPlatformCallbackHandler.logger.debug("timestamp: {}", timestamp);
        MessengerPlatformCallbackHandler.logger.info("Received postback for user '{}' and page '{}' with payload '{}' at '{}'", senderId, senderId, payload, timestamp);
        try {
            if (payload.equals("Latvia")) {
                facebookPatientDataCache.getUserStates(Long.parseLong(event.senderId())).setApplyButton(false);
                sender.sendQuickReplyLvButtons(senderId);
            }
            if (payload.equals("Worldwide")) {
                facebookPatientDataCache.getUserStates(Long.parseLong(event.senderId())).setApplyButton(false);
                sender.sendQuickReplyWwButtons(senderId);
            }
            if (payload.equals("Country")) {
                facebookPatientDataCache.getUserStates(Long.parseLong(event.senderId())).setApplyButton(false);
                sender.sendTextMessage(senderId,"Type the country");
                facebookPatientDataCache.getUserStates(Long.parseLong(event.senderId())).setPressedButton(true);
            }
            if (payload.equals("Apply")) {
                if (facebookPatientDataCache.getPatientData(Long.parseLong(event.senderId())).getTemperature() == null) {
                    sender.sendTextMessage(senderId, "Enter your first name");
                    facebookPatientDataCache.getUserStates(Long.parseLong(event.senderId())).setApplyButton(true);
                }
                else {
                    sender.sendTextMessage(senderId, "We have received your request - wait for confirmation");
                }
            }

        }
        catch (MessengerApiException | MessengerIOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Methods that handles users tap of quick reply button of period choice
     *
     * @param event Facebook messenger event
     * Handles the "QuickReplyMessageEvent" from Facebook messenger sender, after pressing the
     *             quick reply button
     * @author Vladislavs Visnevskis
     */
    public void handleQuickReplyMessageEvent(QuickReplyMessageEvent event) {
        MessengerPlatformCallbackHandler.logger.debug("Handling QuickReplyMessageEvent");
        final String payload = event.payload();
        MessengerPlatformCallbackHandler.logger.debug("payload: {}", payload);
        final String senderId = event.senderId();
        MessengerPlatformCallbackHandler.logger.debug("senderId: {}", senderId);
        final String messageId = event.messageId();
        MessengerPlatformCallbackHandler.logger.debug("messageId: {}", messageId);
        MessengerPlatformCallbackHandler.logger.info("Received quick reply for message '{}' with payload '{}'", messageId, payload);

        if (payload.equals("lvYesterday")) {
            sender.sendTextMessage(senderId, "\uD83C\uDDF1\uD83C\uDDFB " + "\n" + countTotalYesterday("latvia"));
        }
        if (payload.equals("lvSevenDays")) {
            sender.sendTextMessage(senderId,"\uD83C\uDDF1\uD83C\uDDFB " + "\n" + countTotalSevenDays("latvia"));
        }
        if (payload.equals("lvThirtyDays")) {
            sender.sendTextMessage(senderId,"\uD83C\uDDF1\uD83C\uDDFB " + "\n" + countTotalThirtyDays("latvia"));
        }
        if (payload.equals("wwYesterday")) {
            sender.sendTextMessage(senderId,"\uD83C\uDF0E" + "\n" + countTotalYesterday("world"));

        }
        if (payload.equals("wwSevenDays")) {
            sender.sendTextMessage(senderId,"\uD83C\uDF0E" + "\n" +  countTotalSevenDays("world"));
        }
        if (payload.equals("wwThirtyDays")) {
            sender.sendTextMessage(senderId,"\uD83C\uDF0E" + "\n" +  countTotalThirtyDays("world"));
        }

        if (payload.equals("countryYesterday")) {
            sender.sendTextMessage(senderId, countTotalYesterday(facebookPatientDataCache.getUserStates(Long.parseLong(event.senderId())).getInput()));
        }
        if (payload.equals("countrySevenDays")) {
            sender.sendTextMessage(senderId, countTotalSevenDays(facebookPatientDataCache.getUserStates(Long.parseLong(event.senderId())).getInput()));
        }
        if (payload.equals("countryThirtyDays")) {
            sender.sendTextMessage(senderId, countTotalThirtyDays(facebookPatientDataCache.getUserStates(Long.parseLong(event.senderId())).getInput()));
        }
    }

    /**
     * Handles the "handleFallbackEvent", unsupported messages from Facebook messenger sender
     *
     * @param event Facebook messenger event
     * @author Vladislavs Visnevskis
     */
    public void handleFallbackEvent(Event event) {
        MessengerPlatformCallbackHandler.logger.debug("Handling FallbackEvent");
        final String senderId = event.senderId();
        MessengerPlatformCallbackHandler.logger.debug("senderId: {}", senderId);

        MessengerPlatformCallbackHandler.logger.info("Received unsupported message from user '{}'", senderId);
    }

    /**
     * Handles and logging the send exception from Facebook messenger sender
     * @param exception Facebook messenger event
     * @author Vladislavs Visnevskis
     */
    public static void handleSendException(Exception exception) {
        MessengerPlatformCallbackHandler.logger.error("Message could not be sent. An unexpected error occurred.", exception);
    }


}

package lv.team3.botcovidlab.adapter.facebook;

import com.github.messenger4j.Messenger;
import com.github.messenger4j.common.WebviewHeightRatio;
import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.github.messenger4j.exception.MessengerVerificationException;
import com.github.messenger4j.send.MessagePayload;
import com.github.messenger4j.send.MessagingType;
import com.github.messenger4j.send.NotificationType;
import com.github.messenger4j.send.message.RichMediaMessage;
import com.github.messenger4j.send.message.TemplateMessage;
import com.github.messenger4j.send.message.TextMessage;
import com.github.messenger4j.send.message.quickreply.QuickReply;
import com.github.messenger4j.send.message.quickreply.TextQuickReply;
import com.github.messenger4j.send.message.richmedia.UrlRichMediaAsset;
import com.github.messenger4j.send.message.template.ButtonTemplate;
import com.github.messenger4j.send.message.template.button.*;
import com.github.messenger4j.send.recipient.IdRecipient;
import com.github.messenger4j.webhook.Event;
import com.github.messenger4j.webhook.event.*;
import lombok.Getter;
import lombok.Setter;
import lv.team3.botcovidlab.adapter.facebook.handlers.PatientApplicationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.messenger4j.Messenger.*;
import static java.util.Optional.*;
import static lv.team3.botcovidlab.adapter.facebook.TotalStatUtil.*;


@RestController
@RequestMapping("/callback")
public class MessengerPlatformCallbackHandler {

    public static final Logger logger = LoggerFactory.getLogger(MessengerPlatformCallbackHandler.class);

    private final Messenger messenger;
    private final PatientApplicationUtil patientApplicationUtil;

    @Getter
    @Setter
    public String country;
    @Getter
    @Setter
    public boolean countryButton;


    @Autowired
    public MessengerPlatformCallbackHandler(final Messenger messenger, final PatientApplicationUtil patientApplicationUtil) {
        this.messenger = messenger;
        this.patientApplicationUtil = patientApplicationUtil;
    }


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> verifyWebhook(@RequestParam(MODE_REQUEST_PARAM_NAME) final String mode,
                                                @RequestParam(VERIFY_TOKEN_REQUEST_PARAM_NAME) final String verifyToken, @RequestParam(CHALLENGE_REQUEST_PARAM_NAME) final String challenge) {
        logger.debug("Received Webhook verification request - mode: {} | verifyToken: {} | challenge: {}", mode, verifyToken, challenge);
        try {
            this.messenger.verifyWebhook(mode, verifyToken);
            return ResponseEntity.ok(challenge);
        } catch (MessengerVerificationException e) {
            logger.warn("Webhook verification failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    /**
     * Callback endpoint responsible for processing the inbound messages and events.
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> handleCallback(@RequestBody final String payload, @RequestHeader(SIGNATURE_HEADER_NAME) final String signature) throws MessengerApiException, MessengerIOException, MalformedURLException {
        logger.debug("Received Messenger Platform callback - payload: {} | signature: {}", payload, signature);

        try {
            this.messenger.onReceiveEvents(payload, of(signature), event -> {
                if (event.isTextMessageEvent() && ! patientApplicationUtil.isApplicationPressed) {
                    handleTextMessageEvent(event.asTextMessageEvent());
                } else if (event.isTextMessageEvent() && patientApplicationUtil.isApplicationPressed) {
                    patientApplicationUtil.handleTestApplicationEvent(event.asTextMessageEvent());
                } else if (event.isPostbackEvent()) {
                    handlePostbackEvent(event.asPostbackEvent());
                } else if (event.isQuickReplyMessageEvent()  && !patientApplicationUtil.isApplicationPressed) {
                    handleQuickReplyMessageEvent(event.asQuickReplyMessageEvent());
                } else if (event.isQuickReplyMessageEvent()  && patientApplicationUtil.isApplicationPressed) {
                    patientApplicationUtil.handleQuickReplyMessageApplyEvent(event.asQuickReplyMessageEvent());
                } else {
                    handleFallbackEvent(event);
                }
            });
            logger.debug("Processed callback payload successfully");
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (MessengerVerificationException e) {
            logger.warn("Processing of callback payload failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    public void handleTextMessageEvent(TextMessageEvent event) {
        MessengerPlatformCallbackHandler.logger.debug("Received TextMessageEvent: {}", event);

        final String messageId = event.messageId();
        final String messageText = event.text();
        final String senderId = event.senderId();
        final Instant timestamp = event.timestamp();

        MessengerPlatformCallbackHandler.logger.info("Received message '{}' with text '{}' from user '{}' at '{}'", messageId, messageText, senderId, timestamp);

        if (countryButton) {
            boolean isValidInput = false;
            for(String list : ValidCountryList.countries){
                if (list.toLowerCase().equals(event.text().toLowerCase())){
                    country = event.text().toLowerCase();
                    isValidInput = true;
                    break;
                }
            }
            if (isValidInput) {
                try {
                    sendQuickReplyCountryButtons(senderId);
                } catch (MessengerApiException | MessengerIOException e) {
                    MessengerPlatformCallbackHandler.handleSendException(e);
                }
            }
            else{
                sendTextMessage(senderId, "Incorrect input");
                countryButton = false;
            }
        }

        else {
            try {
                switch (messageText.toLowerCase()) {
                    case "covid Latvia":

                    default:
                        sendButtonMessage(senderId);
                        sendSecondButtonMessage(senderId);
                }
            } catch (MessengerApiException | MessengerIOException | MalformedURLException e) {
                handleSendException(e);
            }
        }
    }




    private void sendRichMediaMessage(String recipientId, UrlRichMediaAsset richMediaAsset) throws MessengerApiException, MessengerIOException {
        final RichMediaMessage richMediaMessage = RichMediaMessage.create(richMediaAsset);
        final MessagePayload messagePayload = MessagePayload.create(recipientId, MessagingType.RESPONSE, richMediaMessage);
        this.messenger.send(messagePayload);
    }


    private void sendButtonMessage(String recipientId) throws MessengerApiException, MessengerIOException, MalformedURLException {
        final List<Button> buttons = Arrays.asList(
                PostbackButton.create("Covid Latvia", "Latvia"),
                PostbackButton.create("Covid worldwide", "Worldwide"),
                PostbackButton.create("Covid by country", "Country")
        );

        final ButtonTemplate buttonTemplate = ButtonTemplate.create("Choose the stats", buttons);
        final TemplateMessage templateMessage = TemplateMessage.create(buttonTemplate);
        final MessagePayload messagePayload = MessagePayload.create(recipientId, MessagingType.RESPONSE, templateMessage);
        this.messenger.send(messagePayload);
    }

    private void sendSecondButtonMessage(String recipientId) throws MessengerApiException, MessengerIOException, MalformedURLException {
        final List<Button> buttons = Arrays.asList(
                UrlButton.create("Covid Symptoms", new URL("https://covid19.gov.lv/en/covid-19/about-covid-19/symptoms"), of(WebviewHeightRatio.COMPACT), of(false), empty(), empty()),
                PostbackButton.create("Apply for a test", "Apply")
        );

        final ButtonTemplate buttonTemplate = ButtonTemplate.create("Choose the option", buttons);
        final TemplateMessage templateMessage = TemplateMessage.create(buttonTemplate);
        final MessagePayload messagePayload = MessagePayload.create(recipientId, MessagingType.RESPONSE, templateMessage);
        this.messenger.send(messagePayload);
    }

    private void sendQuickReplyLvButtons(String recipientId) throws MessengerApiException, MessengerIOException {
        List<QuickReply> quickReplies = new ArrayList<>();

        quickReplies.add(TextQuickReply.create("Yesterday", "lvYesterday"));
        quickReplies.add(TextQuickReply.create("Last 7 days", "lvSevenDays"));
        quickReplies.add(TextQuickReply.create("Last 30 days", "lvThirtyDays"));

        TextMessage message = TextMessage.create("Choose the period", of(quickReplies), empty());
        messenger.send(MessagePayload.create(recipientId, MessagingType.RESPONSE, message));
    }

    private void sendQuickReplyWwButtons(String recipientId) throws MessengerApiException, MessengerIOException {
        List<QuickReply> quickReplies = new ArrayList<>();

        quickReplies.add(TextQuickReply.create("Yesterday", "wwYesterday"));
        quickReplies.add(TextQuickReply.create("Last 7 days", "wwSevenDays"));
        quickReplies.add(TextQuickReply.create("Last 30 days", "wwThirtyDays"));

        TextMessage message = TextMessage.create("Choose the period", of(quickReplies), empty());
        messenger.send(MessagePayload.create(recipientId, MessagingType.RESPONSE, message));
    }

    private void sendQuickReplyCountryButtons(String recipientId) throws MessengerApiException, MessengerIOException {
        countryButton = false;
        List<QuickReply> quickReplies = new ArrayList<>();

        quickReplies.add(TextQuickReply.create("Yesterday", "countryYesterday"));
        quickReplies.add(TextQuickReply.create("Last 7 days", "countrySevenDays"));
        quickReplies.add(TextQuickReply.create("Last 30 days", "countryThirtyDays"));

        TextMessage message = TextMessage.create("Choose the period", of(quickReplies), empty());
        messenger.send(MessagePayload.create(recipientId, MessagingType.RESPONSE, message));
    }

    private void handlePostbackEvent(PostbackEvent event) {
        logger.debug("Handling PostbackEvent");
        final String payload = event.payload().orElse("empty payload");
        logger.debug("payload: {}", payload);
        final String senderId = event.senderId();
        logger.debug("senderId: {}", senderId);
        final Instant timestamp = event.timestamp();
        logger.debug("timestamp: {}", timestamp);
        logger.info("Received postback for user '{}' and page '{}' with payload '{}' at '{}'", senderId, senderId, payload, timestamp);
        try {
            if (payload.equals("Latvia")) {
                sendQuickReplyLvButtons(senderId);
            }
            if (payload.equals("Worldwide")) {
                sendQuickReplyWwButtons(senderId);
            }
            if (payload.equals("Country")) {
                sendTextMessage(senderId,"Type the country");
                countryButton = true;
            }
            if (payload.equals("Apply")) {
                sendTextMessage(senderId,"Enter your first name");
                patientApplicationUtil.isApplicationPressed = true;
            }

        }
        catch (MessengerApiException | MessengerIOException e) {
            e.printStackTrace();
        }
    }

    private void handleQuickReplyMessageEvent(QuickReplyMessageEvent event) {
        logger.debug("Handling QuickReplyMessageEvent");
        final String payload = event.payload();
        logger.debug("payload: {}", payload);
        final String senderId = event.senderId();
        logger.debug("senderId: {}", senderId);
        final String messageId = event.messageId();
        logger.debug("messageId: {}", messageId);
        logger.info("Received quick reply for message '{}' with payload '{}'", messageId, payload);

        if (payload.equals("lvYesterday")) {
            sendTextMessage(senderId, "\uD83C\uDDF1\uD83C\uDDFB " + "\n" +countTotalYesterday("latvia"));
        }
        if (payload.equals("lvSevenDays")) {
            sendTextMessage(senderId,"\uD83C\uDDF1\uD83C\uDDFB " + "\n" + countTotalSevenDays("latvia"));
        }
        if (payload.equals("lvThirtyDays")) {
            sendTextMessage(senderId,"\uD83C\uDDF1\uD83C\uDDFB " + "\n" + countTotalThirtyDays("latvia"));
        }
        if (payload.equals("wwYesterday")) {
            sendTextMessage(senderId,"\uD83C\uDF0E" + "\n" + countTotalYesterday("world"));

        }
        if (payload.equals("wwSevenDays")) {
            sendTextMessage(senderId,"\uD83C\uDF0E" + "\n" +  countTotalSevenDays("world"));
        }
        if (payload.equals("wwThirtyDays")) {
            sendTextMessage(senderId,"\uD83C\uDF0E" + "\n" +  countTotalThirtyDays("world"));
        }

        if (payload.equals("countryYesterday")) {
            sendTextMessage(senderId, countTotalYesterday(country));
        }
        if (payload.equals("countrySevenDays")) {
            sendTextMessage(senderId, countTotalSevenDays(country));
        }
        if (payload.equals("countryThirtyDays")) {
            sendTextMessage(senderId, countTotalThirtyDays(country));
        }
    }

    private void handleFallbackEvent(Event event) {
        logger.debug("Handling FallbackEvent");
        final String senderId = event.senderId();
        logger.debug("senderId: {}", senderId);

        logger.info("Received unsupported message from user '{}'", senderId);
    }

    public void sendTextMessage(String recipientId, String text) {
        try {
            final IdRecipient recipient = IdRecipient.create(recipientId);
            final NotificationType notificationType = NotificationType.REGULAR;
            final String metadata = "DEVELOPER_DEFINED_METADATA";

            final TextMessage textMessage = TextMessage.create(text, empty(), of(metadata));
            final MessagePayload messagePayload = MessagePayload.create(recipient, MessagingType.RESPONSE, textMessage,
                    of(notificationType), empty());
            this.messenger.send(messagePayload);
        } catch (MessengerApiException | MessengerIOException e) {
            handleSendException(e);
        }
    }

    public static void handleSendException(Exception e) {
        logger.error("Message could not be sent. An unexpected error occurred.", e);
    }
}

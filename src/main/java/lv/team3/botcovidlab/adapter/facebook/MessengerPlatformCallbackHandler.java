package lv.team3.botcovidlab.adapter.facebook;

import com.github.messenger4j.Messenger;
import com.github.messenger4j.common.SupportedLocale;
import com.github.messenger4j.common.WebviewHeightRatio;
import com.github.messenger4j.common.WebviewShareButtonState;
import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.github.messenger4j.exception.MessengerVerificationException;
import com.github.messenger4j.messengerprofile.MessengerSettings;
import com.github.messenger4j.messengerprofile.SetupResponse;
import com.github.messenger4j.messengerprofile.SetupResponseFactory;
import com.github.messenger4j.messengerprofile.getstarted.StartButton;
import com.github.messenger4j.messengerprofile.persistentmenu.LocalizedPersistentMenu;
import com.github.messenger4j.messengerprofile.persistentmenu.PersistentMenu;
import com.github.messenger4j.messengerprofile.persistentmenu.action.NestedCallToAction;
import com.github.messenger4j.messengerprofile.persistentmenu.action.PostbackCallToAction;
import com.github.messenger4j.messengerprofile.persistentmenu.action.UrlCallToAction;
import com.github.messenger4j.send.MessagePayload;
import com.github.messenger4j.send.MessagingType;
import com.github.messenger4j.send.NotificationType;
import com.github.messenger4j.send.SenderActionPayload;
import com.github.messenger4j.send.message.RichMediaMessage;
import com.github.messenger4j.send.message.TemplateMessage;
import com.github.messenger4j.send.message.TextMessage;
import com.github.messenger4j.send.message.quickreply.LocationQuickReply;
import com.github.messenger4j.send.message.quickreply.QuickReply;
import com.github.messenger4j.send.message.quickreply.TextQuickReply;
import com.github.messenger4j.send.message.richmedia.UrlRichMediaAsset;
import com.github.messenger4j.send.message.template.ButtonTemplate;
import com.github.messenger4j.send.message.template.GenericTemplate;
import com.github.messenger4j.send.message.template.ListTemplate;
import com.github.messenger4j.send.message.template.ReceiptTemplate;
import com.github.messenger4j.send.message.template.button.*;
import com.github.messenger4j.send.message.template.common.Element;
import com.github.messenger4j.send.message.template.receipt.Address;
import com.github.messenger4j.send.message.template.receipt.Adjustment;
import com.github.messenger4j.send.message.template.receipt.Item;
import com.github.messenger4j.send.message.template.receipt.Summary;
import com.github.messenger4j.send.recipient.IdRecipient;
import com.github.messenger4j.send.senderaction.SenderAction;
import com.github.messenger4j.userprofile.UserProfile;
import com.github.messenger4j.webhook.Event;
import com.github.messenger4j.webhook.event.*;
import com.github.messenger4j.webhook.event.attachment.Attachment;
import com.github.messenger4j.webhook.event.attachment.LocationAttachment;
import com.github.messenger4j.webhook.event.attachment.RichMediaAttachment;
import lv.team3.botcovidlab.CovidStats;
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
import java.util.Collections;
import java.util.List;

import static com.github.messenger4j.Messenger.*;
import static com.github.messenger4j.send.message.richmedia.RichMediaAsset.Type.*;
import static java.util.Optional.*;
import static lv.team3.botcovidlab.adapter.facebook.DateUtility.*;
import static lv.team3.botcovidlab.adapter.facebook.TotalStatUtil.*;
import static lv.team3.botcovidlab.processors.CovidStatsProcessor.*;


@RestController
@RequestMapping("/callback")
public class MessengerPlatformCallbackHandler {

    public static final Logger logger = LoggerFactory.getLogger(MessengerPlatformCallbackHandler.class);

    private final Messenger messenger;

    private String country;
    private boolean isCountry;
    private String dateFrom;
    private String dateTo = getYesterdayDate();

    @Autowired
    public MessengerPlatformCallbackHandler(final Messenger messenger) {
        this.messenger = messenger;
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
                if (event.isTextMessageEvent()) {
                    handleTextMessageEvent(event.asTextMessageEvent());
                } else if (event.isPostbackEvent()) {
                    handlePostbackEvent(event.asPostbackEvent());
                } else if (event.isQuickReplyMessageEvent()) {
                    handleQuickReplyMessageEvent(event.asQuickReplyMessageEvent());
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
        logger.debug("Received TextMessageEvent: {}", event);

        final String messageId = event.messageId();
        final String messageText = event.text();
        final String senderId = event.senderId();
        final Instant timestamp = event.timestamp();

        logger.info("Received message '{}' with text '{}' from user '{}' at '{}'", messageId, messageText, senderId, timestamp);

        if (isCountry) {
            country = event.text().toLowerCase();
            try {
                sendQuickReplyCountryButtons(senderId);
            } catch (MessengerApiException | MessengerIOException e) {
                handleSendException(e);
            }
        }

        else {
            try {
                switch (messageText.toLowerCase()) {
                    case "covid Latvia":

                        break;

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
        isCountry = false;
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
                isCountry = true;

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

    private void sendTextMessage(String recipientId, String text) {
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

    private void handleSendException(Exception e) {
        logger.error("Message could not be sent. An unexpected error occurred.", e);
    }
}

package lv.team3.botcovidlab.adapter.facebook;

import com.github.messenger4j.Messenger;
import com.github.messenger4j.common.WebviewHeightRatio;
import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.github.messenger4j.exception.MessengerVerificationException;
import com.github.messenger4j.messengerprofile.MessengerSettings;
import com.github.messenger4j.messengerprofile.SetupResponse;
import com.github.messenger4j.messengerprofile.SetupResponseFactory;
import com.github.messenger4j.messengerprofile.getstarted.StartButton;
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
import static lv.team3.botcovidlab.processors.CovidStatsProcessor.getStats;


@RestController
@RequestMapping("/callback")
public class MessengerPlatformCallbackHandler {

    private static final String RESOURCE_URL = "https://raw.githubusercontent.com/fbsamples/messenger-platform-samples/master/node/public";

    private static final Logger logger = LoggerFactory.getLogger(MessengerPlatformCallbackHandler.class);

    private final Messenger messenger;

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
    public ResponseEntity<Void> handleCallback(@RequestBody final String payload, @RequestHeader(SIGNATURE_HEADER_NAME) final String signature) throws MessengerApiException, MessengerIOException {
        logger.debug("Received Messenger Platform callback - payload: {} | signature: {}", payload, signature);

        try {
            this.messenger.onReceiveEvents(payload, of(signature), event -> {
                if (event.isTextMessageEvent()) {
                    handleTextMessageEvent(event.asTextMessageEvent());
                } else if (event.isAttachmentMessageEvent()) {
                    handleAttachmentMessageEvent(event.asAttachmentMessageEvent());
                } else if (event.isQuickReplyMessageEvent()) {
                    handleQuickReplyMessageEvent(event.asQuickReplyMessageEvent());
                } else if (event.isPostbackEvent()) {
                    handlePostbackEvent(event.asPostbackEvent());
                } else if (event.isAccountLinkingEvent()) {
                    handleAccountLinkingEvent(event.asAccountLinkingEvent());
                } else if (event.isOptInEvent()) {
                    handleOptInEvent(event.asOptInEvent());
                } else if (event.isMessageEchoEvent()) {
                    handleMessageEchoEvent(event.asMessageEchoEvent());
                } else if (event.isMessageDeliveredEvent()) {
                    handleMessageDeliveredEvent(event.asMessageDeliveredEvent());
                } else if (event.isMessageReadEvent()) {
                    handleMessageReadEvent(event.asMessageReadEvent());
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



    private void handleTextMessageEvent(TextMessageEvent event) {
        logger.debug("Received TextMessageEvent: {}", event);

        final String messageId = event.messageId();
        final String messageText = event.text();
        final String senderId = event.senderId();
        final Instant timestamp = event.timestamp();

        logger.info("Received message '{}' with text '{}' from user '{}' at '{}'", messageId, messageText, senderId, timestamp);

        try {
            switch (messageText.toLowerCase()) {
                case "covid Latvia":

                    break;
                case "covid latvia":
                    List<CovidStats> stats = getStats("latvia", "2021-01-16", "2021-01-20");
                    stats.forEach(entry -> {
                        sendTextMessage(senderId, "Date: " + entry.getDate().toString() +
                                "Died: " + entry.getDeathsTotal() + " Active: " + entry.getActiveTotal() + " Recovered: " + entry.getRecoveredTotal());
                    });
                    break;

                default:
                    sendButtonMessage(senderId);
                    sendSecondButtonMessage(senderId);
            }
        } catch (MessengerApiException | MessengerIOException | MalformedURLException e) {
            handleSendException(e);
        }
    }

    private void sendImageMessage(String recipientId) throws MessengerApiException, MessengerIOException, MalformedURLException {
        final UrlRichMediaAsset richMediaAsset = UrlRichMediaAsset.create(IMAGE, new URL(RESOURCE_URL + "/assets/rift.png"));
        sendRichMediaMessage(recipientId, richMediaAsset);
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

    private void sendLatviaButtonMessage(String recipientId) throws MessengerApiException, MessengerIOException, MalformedURLException {
        final List<Button> buttons = Arrays.asList(
                PostbackButton.create("Last 30 days", "LvLastThirty"),
                PostbackButton.create("Last 7 days", "LvLastSeven"),
                PostbackButton.create("Yesterday", "LvYesterday")
        );

        final ButtonTemplate buttonTemplate = ButtonTemplate.create("Choose the option", buttons);
        final TemplateMessage templateMessage = TemplateMessage.create(buttonTemplate);
        final MessagePayload messagePayload = MessagePayload.create(recipientId, MessagingType.RESPONSE, templateMessage);
        this.messenger.send(messagePayload);
    }

    private void sendWorldwideButtonMessage(String recipientId) throws MessengerApiException, MessengerIOException, MalformedURLException {
        final List<Button> buttons = Arrays.asList(
                PostbackButton.create("Last 30 days", "WwLastThirty"),
                PostbackButton.create("Last 7 days", "WwLastSeven"),
                PostbackButton.create("Yesterday", "WwYesterday")
        );

        final ButtonTemplate buttonTemplate = ButtonTemplate.create("Choose the option", buttons);
        final TemplateMessage templateMessage = TemplateMessage.create(buttonTemplate);
        final MessagePayload messagePayload = MessagePayload.create(recipientId, MessagingType.RESPONSE, templateMessage);
        this.messenger.send(messagePayload);
    }



    private void handleAttachmentMessageEvent(AttachmentMessageEvent event) {
        logger.debug("Handling QuickReplyMessageEvent");
        final String senderId = event.senderId();
        logger.debug("senderId: {}", senderId);
        for (Attachment attachment : event.attachments()) {
            if (attachment.isRichMediaAttachment()) {
                final RichMediaAttachment richMediaAttachment = attachment.asRichMediaAttachment();
                final RichMediaAttachment.Type type = richMediaAttachment.type();
                final URL url = richMediaAttachment.url();
                logger.debug("Received rich media attachment of type '{}' with url: {}", type, url);
                final String text = String.format("Media %s received (url: %s)", type.name(), url);
                sendTextMessage(senderId, text);
            } else if (attachment.isLocationAttachment()) {
                final LocationAttachment locationAttachment = attachment.asLocationAttachment();
                final double longitude = locationAttachment.longitude();
                final double latitude = locationAttachment.latitude();
                logger.debug("Received location information (long: {}, lat: {})", longitude, latitude);
                final String text = String.format("Location received (long: %s, lat: %s)", String.valueOf(longitude), String.valueOf(latitude));
                sendTextMessage(senderId, text);
            }
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
        sendTextMessage(senderId, "Quick reply tapped");
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
                sendLatviaButtonMessage(senderId);
            }
            if (payload.equals("LvToday")) {
                List<CovidStats> stats = getStats("latvia", "2021-01-27", "2021-01-27");
                stats.forEach(entry -> {
                    sendTextMessage(senderId, "Date: " + entry.getDate().toString() +
                            "Died: " + entry.getDeathsTotal() + " Active: " + entry.getActiveTotal() + " Recovered: " + entry.getRecoveredTotal());
                });
            }
        }
        catch (MessengerApiException | MessengerIOException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void handleAccountLinkingEvent(AccountLinkingEvent event) {
        logger.debug("Handling AccountLinkingEvent");
        final String senderId = event.senderId();
        logger.debug("senderId: {}", senderId);
        final AccountLinkingEvent.Status accountLinkingStatus = event.status();
        logger.debug("accountLinkingStatus: {}", accountLinkingStatus);
        final String authorizationCode = event.authorizationCode().orElse("Empty authorization code!!!"); //You can throw an Exception
        logger.debug("authorizationCode: {}", authorizationCode);
        logger.info("Received account linking event for user '{}' with status '{}' and auth code '{}'", senderId, accountLinkingStatus, authorizationCode);
        sendTextMessage(senderId, "AccountLinking event tapped");
    }

    private void handleOptInEvent(OptInEvent event) {
        logger.debug("Handling OptInEvent");
        final String senderId = event.senderId();
        logger.debug("senderId: {}", senderId);
        final String recipientId = event.recipientId();
        logger.debug("recipientId: {}", recipientId);
        final String passThroughParam = event.refPayload().orElse("empty payload");
        logger.debug("passThroughParam: {}", passThroughParam);
        final Instant timestamp = event.timestamp();
        logger.debug("timestamp: {}", timestamp);

        logger.info("Received authentication for user '{}' and page '{}' with pass through param '{}' at '{}'", senderId, recipientId, passThroughParam,
                timestamp);
        sendTextMessage(senderId, "Authentication successful");
    }

    private void handleMessageEchoEvent(MessageEchoEvent event) {
        logger.debug("Handling MessageEchoEvent");
        final String senderId = event.senderId();
        logger.debug("senderId: {}", senderId);
        final String recipientId = event.recipientId();
        logger.debug("recipientId: {}", recipientId);
        final String messageId = event.messageId();
        logger.debug("messageId: {}", messageId);
        final Instant timestamp = event.timestamp();
        logger.debug("timestamp: {}", timestamp);

        logger.info("Received echo for message '{}' that has been sent to recipient '{}' by sender '{}' at '{}'", messageId, recipientId, senderId, timestamp);
        sendTextMessage(senderId, "MessageEchoEvent tapped");
    }

    private void handleMessageDeliveredEvent(MessageDeliveredEvent event) {
        logger.debug("Handling MessageDeliveredEvent");
        final String senderId = event.senderId();
        logger.debug("senderId: {}", senderId);
        final List<String> messageIds = event.messageIds().orElse(Collections.emptyList());
        final Instant watermark = event.watermark();
        logger.debug("watermark: {}", watermark);

        messageIds.forEach(messageId -> {
            logger.info("Received delivery confirmation for message '{}'", messageId);
        });

        logger.info("All messages before '{}' were delivered to user '{}'", watermark, senderId);
    }

    private void handleMessageReadEvent(MessageReadEvent event) {
        logger.debug("Handling MessageReadEvent");
        final String senderId = event.senderId();
        logger.debug("senderId: {}", senderId);
        final Instant watermark = event.watermark();
        logger.debug("watermark: {}", watermark);

        logger.info("All messages before '{}' were read by user '{}'", watermark, senderId);
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

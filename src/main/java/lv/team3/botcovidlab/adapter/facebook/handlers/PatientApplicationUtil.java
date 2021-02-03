package lv.team3.botcovidlab.adapter.facebook.handlers;

import com.github.messenger4j.Messenger;
import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.github.messenger4j.send.MessagePayload;
import com.github.messenger4j.send.MessagingType;
import com.github.messenger4j.send.NotificationType;
import com.github.messenger4j.send.message.TextMessage;
import com.github.messenger4j.send.message.quickreply.QuickReply;
import com.github.messenger4j.send.message.quickreply.TextQuickReply;
import com.github.messenger4j.send.recipient.IdRecipient;
import com.github.messenger4j.webhook.event.QuickReplyMessageEvent;
import com.github.messenger4j.webhook.event.TextMessageEvent;
import lombok.Getter;
import lombok.Setter;
import lv.team3.botcovidlab.adapter.facebook.MessengerPlatformCallbackHandler;
import lv.team3.botcovidlab.adapter.facebook.cache.FacebookPatientDataCache;
import lv.team3.botcovidlab.adapter.facebook.senders.Sender;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static lv.team3.botcovidlab.adapter.inputValidation.PatientInputValidation.*;
import static lv.team3.botcovidlab.entityManager.FirebaseService.savePatientDetails;

@Service
@Getter @Setter
public class PatientApplicationUtil{

    private final Messenger messenger;
    private final Sender sender;
    private final EventHandler eventHandler;

    private String patientId;
    private String patientName;
    private String patientLastName;
    private String patientPersonalCode;
    private String patientPhoneNumber;
    private String patientTemperature;
    private boolean patientContactPerson;
    private boolean patientHasCough;
    private boolean patientHasTroubleBreathing;
    private boolean patientHasHeadache;
    private final FacebookPatientDataCache facebookPatientDataCache;

    public PatientApplicationUtil(Messenger messenger, Sender sender, EventHandler eventHandler, FacebookPatientDataCache facebookPatientDataCache) {
        this.messenger = messenger;
        this.sender = sender;
        this.eventHandler = eventHandler;
        this.facebookPatientDataCache = facebookPatientDataCache;
    }

    /**
     * Method that handles the "TextMessageEvent" from Facebook messenger sender after "Apply for test" button is pressed
     *
     * @param event Facebook messenger event
     * @author Vladislavs Visnevskis
     */
    public void handleTestApplicationEvent(TextMessageEvent event) {

        MessengerPlatformCallbackHandler.logger.debug("Received TextMessageEvent: {}", event);
        final String messageId = event.messageId();
        final String messageText = event.text();
        final String senderId = event.senderId();
        final Instant timestamp = event.timestamp();

        MessengerPlatformCallbackHandler.logger.info("Received message '{}' with text '{}' from user '{}' at '{}'", messageId, messageText, senderId, timestamp);
        try {
            if (facebookPatientDataCache.getPatientData(Long.parseLong(event.senderId())).getName() != null
                    && facebookPatientDataCache.getPatientData(Long.parseLong(event.senderId())).getLastName() != null
                    && facebookPatientDataCache.getPatientData(Long.parseLong(event.senderId())).getPersonalCode() != null
                    && facebookPatientDataCache.getPatientData(Long.parseLong(event.senderId())).getPhoneNumber() != null) {
                if (validateTemperature(messageText)) {
                    facebookPatientDataCache.getPatientData(Long.parseLong(event.senderId())).setTemperature(messageText);
                    sendTextMessage(senderId, "Choose the appropriate answer");
                    sendQuickReplyContactButtons(senderId);
                }
                else sendTextMessage(senderId, "Incorrect input - temperature should be a number, separated with dot, within range 30.0°C and 45.0°C " + '\n' + "Try again");
            }
            if (facebookPatientDataCache.getPatientData(Long.parseLong(event.senderId())).getName() != null
                    && facebookPatientDataCache.getPatientData(Long.parseLong(event.senderId())).getLastName() != null
                    && facebookPatientDataCache.getPatientData(Long.parseLong(event.senderId())).getPersonalCode() != null
                    && facebookPatientDataCache.getPatientData(Long.parseLong(event.senderId())).getPhoneNumber() == null) {
                if (validatePhoneNumber(messageText)) {
                    facebookPatientDataCache.getPatientData(Long.parseLong(event.senderId())).setPhoneNumber(messageText);
                    sendTextMessage(senderId, "Enter your temperature");
                }
                else sendTextMessage(senderId, "Incorrect input - phone number should consist of 8 digits started with \"2\" or \"6\" " + '\n' + "Try again");
            }
            if (facebookPatientDataCache.getPatientData(Long.parseLong(event.senderId())).getName() != null
                    && facebookPatientDataCache.getPatientData(Long.parseLong(event.senderId())).getLastName() != null
                    && facebookPatientDataCache.getPatientData(Long.parseLong(event.senderId())).getPersonalCode() == null
                    && facebookPatientDataCache.getPatientData(Long.parseLong(event.senderId())).getPhoneNumber() == null) {
                if (validatePersonalCode(messageText)) {
                    facebookPatientDataCache.getPatientData(Long.parseLong(event.senderId())).setPersonalCode(messageText);
                    sendTextMessage(senderId, "Enter your phone number");
                }
                else sendTextMessage(senderId, "Incorrect input - personal code should contain 11 digits or 6 and 5 digits separated with \"-\" " + '\n' + "Try again");
            }
            if (facebookPatientDataCache.getPatientData(Long.parseLong(event.senderId())).getName() != null
                    && facebookPatientDataCache.getPatientData(Long.parseLong(event.senderId())).getLastName() == null
                    && facebookPatientDataCache.getPatientData(Long.parseLong(event.senderId())).getPersonalCode() == null
                    && facebookPatientDataCache.getPatientData(Long.parseLong(event.senderId())).getPhoneNumber() == null) {
                if(validateLastName(messageText)) {
                    facebookPatientDataCache.getPatientData(Long.parseLong(event.senderId())).setLastName(messageText);
                    sendTextMessage(senderId, "Enter your personal code");
                }
                else sendTextMessage(senderId, "Incorrect input - last name should contain only letters" + '\n' + "Try again");
            }
            if (facebookPatientDataCache.getPatientData(Long.parseLong(event.senderId())).getName() == null
                    && facebookPatientDataCache.getPatientData(Long.parseLong(event.senderId())).getLastName() == null
                    && facebookPatientDataCache.getPatientData(Long.parseLong(event.senderId())).getPersonalCode() == null
                    && facebookPatientDataCache.getPatientData(Long.parseLong(event.senderId())).getPhoneNumber() == null) {
                if(validateFirstName(messageText)) {
                    facebookPatientDataCache.getPatientData(Long.parseLong(event.senderId())).setName(messageText);
                    sendTextMessage(senderId, "Enter your last name");
                }
                else sendTextMessage(senderId, "Incorrect input - name should contain only letters and at least 3 of them"  + '\n' + "Try again");
            }
        } catch (MessengerApiException | MessengerIOException e) {
            eventHandler.handleSendException(e);
        }

    }

    /**
     * Method that sends the text message to Facebook messenger user
     *
     * @param recipientId Facebook messenger recipient identifier
     * @param text text, which will send to Facebook messenger recipient
     * @author Vladislavs Visnevskis
     */
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
            eventHandler.handleSendException(e);
        }
    }

    /**
     * Method that sends the quick reply buttons to user, to answer question "Are you contact person?"
     *
     * @param recipientId Facebook messenger recipient identifier
     * @author Vladislavs Visnevskis
     * @throws MessengerApiException if message is null
     * @throws MessengerIOException
     */
    public void sendQuickReplyContactButtons(String recipientId) throws MessengerApiException, MessengerIOException {
        List<QuickReply> quickReplies = new ArrayList<>();

        quickReplies.add(TextQuickReply.create("Yes", "contactYes"));
        quickReplies.add(TextQuickReply.create("No", "contactNo"));

        TextMessage message = TextMessage.create("Are you contact person?", of(quickReplies), empty());
        messenger.send(MessagePayload.create(recipientId, MessagingType.RESPONSE, message));
    }

    /**
     * Method that sends the quick reply buttons to user, to answer question "Do you have a cough?"
     *
     * @param recipientId Facebook messenger recipient identifier
     * @author Vladislavs Visnevskis
     * @throws MessengerApiException if message is null
     * @throws MessengerIOException
     */
    public void sendQuickReplyCoughButtons(String recipientId) throws MessengerApiException, MessengerIOException {
        List<QuickReply> quickReplies = new ArrayList<>();

        quickReplies.add(TextQuickReply.create("Yes", "coughYes"));
        quickReplies.add(TextQuickReply.create("No", "coughNo"));

        TextMessage message = TextMessage.create("Do you have a cough?", of(quickReplies), empty());
        messenger.send(MessagePayload.create(recipientId, MessagingType.RESPONSE, message));
    }

    /**
     * Method that sends the quick reply buttons to user, to answer question "Do you have trouble breathing?"
     *
     * @param recipientId Facebook messenger recipient identifier
     * @author Vladislavs Visnevskis
     * @throws MessengerApiException if message is null
     * @throws MessengerIOException
     */
    public void sendQuickReplyBreathButtons(String recipientId) throws MessengerApiException, MessengerIOException {
        List<QuickReply> quickReplies = new ArrayList<>();

        quickReplies.add(TextQuickReply.create("Yes", "breathYes"));
        quickReplies.add(TextQuickReply.create("No", "breathNo"));

        TextMessage message = TextMessage.create("Do you have trouble breathing?", of(quickReplies), empty());
        messenger.send(MessagePayload.create(recipientId, MessagingType.RESPONSE, message));
    }

    /**
     * Method that sends the quick reply buttons to user, to answer question "Do you have a headache?"
     *
     * @param recipientId Facebook messenger recipient identifier
     * @throws MessengerApiException if message is null
     * @author Vladislavs Visnevskis
     */
    public void sendQuickReplyHeadacheButtons(String recipientId) throws MessengerApiException, MessengerIOException {
        List<QuickReply> quickReplies = new ArrayList<>();

        quickReplies.add(TextQuickReply.create("Yes", "headYes"));
        quickReplies.add(TextQuickReply.create("No", "headNo"));

        TextMessage message = TextMessage.create("Do you have a headache?", of(quickReplies), empty());
        messenger.send(MessagePayload.create(recipientId, MessagingType.RESPONSE, message));
    }

    /**
     * Methods that handles the user's choice of quick reply buttons
     *
     * @param event Facebook messenger QuickReplyMessageEvent, after pressing quick reply button
     * @author Vladislavs Visnevskis
     */
    public void handleQuickReplyMessageApplyEvent(QuickReplyMessageEvent event) {

        final String payload = event.payload();
        final String senderId = event.senderId();
        final String messageId = event.messageId();

        try {
            if (payload.equals("contactYes")) {
                facebookPatientDataCache.getPatientData(Long.parseLong(event.senderId())).setContactPerson(true);
                sendQuickReplyCoughButtons(senderId);
            }
            if (payload.equals("contactNo")) {
                facebookPatientDataCache.getPatientData(Long.parseLong(event.senderId())).setContactPerson(false);
                sendQuickReplyCoughButtons(senderId);
            }
            if (payload.equals("coughYes")) {
                facebookPatientDataCache.getPatientData(Long.parseLong(event.senderId())).setHasCough(true);
                sendQuickReplyBreathButtons(senderId);
            }
            if (payload.equals("coughNo")) {
                facebookPatientDataCache.getPatientData(Long.parseLong(event.senderId())).setHasCough(false);
                sendQuickReplyBreathButtons(senderId);
            }
            if (payload.equals("breathYes")) {
                facebookPatientDataCache.getPatientData(Long.parseLong(event.senderId())).setHasTroubleBreathing(true);
                sendQuickReplyHeadacheButtons(senderId);
            }
            if (payload.equals("breathNo")) {
                facebookPatientDataCache.getPatientData(Long.parseLong(event.senderId())).setHasTroubleBreathing(false);
                sendQuickReplyHeadacheButtons(senderId);
            }
            if (payload.equals("headYes")) {
                facebookPatientDataCache.getPatientData(Long.parseLong(event.senderId())).setHasHeadache(true);
                facebookPatientDataCache.getPatientData(Long.parseLong(event.senderId())).setChatId(Long.parseLong(senderId));
                sendTextMessage(senderId, "Thank you for applying, " + facebookPatientDataCache.getPatientData(Long.parseLong(event.senderId())).getName() + '\n' + "We will contact you soon for confirmation");
                facebookPatientDataCache.getUserStates(Long.parseLong(event.senderId())).setApplyButton(false);
                try {
                    savePatientDetails(facebookPatientDataCache.getPatientData(Long.parseLong(event.senderId())));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (payload.equals("headNo")) {
                facebookPatientDataCache.getPatientData(Long.parseLong(event.senderId())).setHasHeadache(false);
                facebookPatientDataCache.getPatientData(Long.parseLong(event.senderId())).setChatId(Long.parseLong(senderId));
                sendTextMessage(senderId, "Thank you for applying, " + facebookPatientDataCache.getPatientData(Long.parseLong(event.senderId())).getName() + '\n' + "We will contact you soon for confirmation");
                facebookPatientDataCache.getUserStates(Long.parseLong(event.senderId())).setApplyButton(false);
                try {
                    savePatientDetails(facebookPatientDataCache.getPatientData(Long.parseLong(event.senderId())));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (MessengerApiException | MessengerIOException e) {
            eventHandler.handleSendException(e);
        }

    }

}

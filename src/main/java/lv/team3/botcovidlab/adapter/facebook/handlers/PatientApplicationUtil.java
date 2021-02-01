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
import lv.team3.botcovidlab.entityManager.Patient;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static lv.team3.botcovidlab.adapter.facebook.MessengerPlatformCallbackHandler.handleSendException;
import static lv.team3.botcovidlab.adapter.facebook.TotalStatUtil.*;

@Service
@Getter @Setter
public class PatientApplicationUtil {

    private final Messenger messenger;

    public boolean isApplicationPressed;

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
    Patient patient = new Patient();


    public PatientApplicationUtil(Messenger messenger) {
        this.messenger = messenger;
    }


    public void handleTestApplicationEvent(TextMessageEvent event) {
        MessengerPlatformCallbackHandler.logger.debug("Received TextMessageEvent: {}", event);

        final String messageId = event.messageId();
        final String messageText = event.text();
        final String senderId = event.senderId();
        final Instant timestamp = event.timestamp();

        MessengerPlatformCallbackHandler.logger.info("Received message '{}' with text '{}' from user '{}' at '{}'", messageId, messageText, senderId, timestamp);
        try {
            if (patient.getName() != null && patient.getLastName() != null && patient.getPersonalCode() != null && patient.getPhoneNumber() != null) {
                setPatientTemperature(messageText);
                sendTextMessage(senderId, "Choose the appropriate answer");
                sendQuickReplyContactButtons(senderId);
            }
            if (patient.getName() != null && patient.getLastName() != null && patient.getPersonalCode() != null && patient.getPhoneNumber() == null) {
                setPatientPhoneNumber(messageText);
                sendTextMessage(senderId, "Enter your temperature");
            }
            if (patient.getName() != null && patient.getLastName() != null && patient.getPersonalCode() == null && patient.getPhoneNumber() == null) {
                setPatientPersonalCode(messageText);
                sendTextMessage(senderId, "Enter your phone number");
            }
            if (patient.getName() != null && patient.getLastName() == null && patient.getPersonalCode() == null && patient.getPhoneNumber() == null) {
                setPatientLastName(messageText);
                sendTextMessage(senderId, "Enter your personal code");
            }
            if (patient.getName() == null && patient.getLastName() == null && patient.getPersonalCode() == null && patient.getPhoneNumber() == null) {
                setPatientName(messageText);
                sendTextMessage(senderId, "Enter your last name");
            }
        } catch (MessengerApiException | MessengerIOException e) {
            handleSendException(e);
        }

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

    public void sendQuickReplyContactButtons(String recipientId) throws MessengerApiException, MessengerIOException {
        List<QuickReply> quickReplies = new ArrayList<>();

        quickReplies.add(TextQuickReply.create("Yes", "contactYes"));
        quickReplies.add(TextQuickReply.create("No", "contactNo"));

        TextMessage message = TextMessage.create("Are you contact person?", of(quickReplies), empty());
        messenger.send(MessagePayload.create(recipientId, MessagingType.RESPONSE, message));
    }

    public void sendQuickReplyCoughButtons(String recipientId) throws MessengerApiException, MessengerIOException {
        List<QuickReply> quickReplies = new ArrayList<>();

        quickReplies.add(TextQuickReply.create("Yes", "coughYes"));
        quickReplies.add(TextQuickReply.create("No", "coughNo"));

        TextMessage message = TextMessage.create("Do you have a cough?", of(quickReplies), empty());
        messenger.send(MessagePayload.create(recipientId, MessagingType.RESPONSE, message));
    }

    public void sendQuickReplyBreathButtons(String recipientId) throws MessengerApiException, MessengerIOException {
        List<QuickReply> quickReplies = new ArrayList<>();

        quickReplies.add(TextQuickReply.create("Yes", "breathYes"));
        quickReplies.add(TextQuickReply.create("No", "breathNo"));

        TextMessage message = TextMessage.create("Do you have trouble breathing?", of(quickReplies), empty());
        messenger.send(MessagePayload.create(recipientId, MessagingType.RESPONSE, message));
    }

    public void sendQuickReplyHeadacheButtons(String recipientId) throws MessengerApiException, MessengerIOException {
        List<QuickReply> quickReplies = new ArrayList<>();

        quickReplies.add(TextQuickReply.create("Yes", "headYes"));
        quickReplies.add(TextQuickReply.create("No", "headNo"));

        TextMessage message = TextMessage.create("Do you have a headache?", of(quickReplies), empty());
        messenger.send(MessagePayload.create(recipientId, MessagingType.RESPONSE, message));
    }

    public void handleQuickReplyMessageApplyEvent(QuickReplyMessageEvent event) {

        final String payload = event.payload();
        final String senderId = event.senderId();
        final String messageId = event.messageId();

        try {
            if (payload.equals("contactYes")) {
                patient.setContactPerson(true);
                sendQuickReplyCoughButtons(senderId);
            }
            if (payload.equals("contactNo")) {
                patient.setContactPerson(false);
                sendQuickReplyCoughButtons(senderId);
            }
            if (payload.equals("coughYes")) {
                patient.setHasCough(true);
                sendQuickReplyBreathButtons(senderId);
            }
            if (payload.equals("coughNo")) {
                patient.setHasCough(false);
                sendQuickReplyBreathButtons(senderId);
            }
            if (payload.equals("breathYes")) {
                patient.setHasTroubleBreathing(true);
                sendQuickReplyHeadacheButtons(senderId);
            }
            if (payload.equals("breathNo")) {
                patient.setHasTroubleBreathing(false);
                sendQuickReplyHeadacheButtons(senderId);
            }
            if (payload.equals("headYes")) {
                patient.setHasHeadache(true);
                sendTextMessage(senderId, patientName + "Thank you for applying");
                setApplicationPressed(false);
                System.out.println(patient);
            }
            if (payload.equals("headNo")) {
                patient.setHasHeadache(false);
                sendTextMessage(senderId, patientName + "Thank you for applying");
                setApplicationPressed(false);
            }
        }
        catch (MessengerApiException | MessengerIOException e) {
            handleSendException(e);
        }

    }
}

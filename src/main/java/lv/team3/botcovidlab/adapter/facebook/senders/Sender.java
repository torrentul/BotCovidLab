package lv.team3.botcovidlab.adapter.facebook.senders;

import com.github.messenger4j.Messenger;
import com.github.messenger4j.common.WebviewHeightRatio;
import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
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
import com.github.messenger4j.send.message.template.button.Button;
import com.github.messenger4j.send.message.template.button.PostbackButton;
import com.github.messenger4j.send.message.template.button.UrlButton;
import com.github.messenger4j.send.recipient.IdRecipient;
import lv.team3.botcovidlab.adapter.facebook.cache.UserStates;
import lv.team3.botcovidlab.adapter.facebook.cache.PatientDataCache;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static lv.team3.botcovidlab.adapter.facebook.handlers.EventHandler.handleSendException;

@Service
public class Sender {

    private final Messenger messenger;
    private UserStates countryButton;
    private final PatientDataCache patientDataCache;

    public Sender(Messenger messenger,PatientDataCache patientDataCache) {
        this.messenger = messenger;
        this.patientDataCache = patientDataCache;
    }

    /**
     * @param recipientId Facebook messenger recipient identifier
     * Sends the choice buttons
     * @throws MessengerApiException if message is null
     * @Author Vladislavs Višņevskis
     */
    public void sendButtonMessage(String recipientId) throws MessengerApiException, MessengerIOException {
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

    /**
     * @param recipientId Facebook messenger recipient identifier
     * Sends the choice buttons
     * @throws MessengerApiException if message is null
     * @throws MalformedURLException to indicate that a malformed URL has occurred
     * @Author Vladislavs Višņevskis
     */
    public void sendSecondButtonMessage(String recipientId) throws MessengerApiException, MessengerIOException, MalformedURLException {
        final List<Button> buttons = Arrays.asList(
                UrlButton.create("Covid Symptoms", new URL("https://covid19.gov.lv/en/covid-19/about-covid-19/symptoms"), of(WebviewHeightRatio.COMPACT), of(false), empty(), empty()),
                PostbackButton.create("Apply for a test", "Apply")
        );

        final ButtonTemplate buttonTemplate = ButtonTemplate.create("Choose the option", buttons);
        final TemplateMessage templateMessage = TemplateMessage.create(buttonTemplate);
        final MessagePayload messagePayload = MessagePayload.create(recipientId, MessagingType.RESPONSE, templateMessage);
        this.messenger.send(messagePayload);
    }

    /**
     * @param recipientId Facebook messenger recipient identifier
     * Sends the quick reply buttons to choose the time period for Latvia
     * @throws MessengerApiException if message is null
     * @throws MalformedURLException to indicate that a malformed URL has occurred
     * @Author Vladislavs Višņevskis
     */
    public void sendQuickReplyLvButtons(String recipientId) throws MessengerApiException, MessengerIOException {
        List<QuickReply> quickReplies = new ArrayList<>();

        quickReplies.add(TextQuickReply.create("Yesterday", "lvYesterday"));
        quickReplies.add(TextQuickReply.create("Last 7 days", "lvSevenDays"));
        quickReplies.add(TextQuickReply.create("Last 30 days", "lvThirtyDays"));

        TextMessage message = TextMessage.create("Choose the period", of(quickReplies), empty());
        messenger.send(MessagePayload.create(recipientId, MessagingType.RESPONSE, message));
    }

    /**
     * @param recipientId Facebook messenger recipient identifier
     * Sends the quick reply buttons to choose the time period for the world
     * @throws MessengerApiException if message is null
     * @Author Vladislavs Višņevskis
     */
    public void sendQuickReplyWwButtons(String recipientId) throws MessengerApiException, MessengerIOException {
        List<QuickReply> quickReplies = new ArrayList<>();

        quickReplies.add(TextQuickReply.create("Yesterday", "wwYesterday"));
        quickReplies.add(TextQuickReply.create("Last 7 days", "wwSevenDays"));
        quickReplies.add(TextQuickReply.create("Last 30 days", "wwThirtyDays"));

        TextMessage message = TextMessage.create("Choose the period", of(quickReplies), empty());
        messenger.send(MessagePayload.create(recipientId, MessagingType.RESPONSE, message));
    }

    /**
     * @param recipientId Facebook messenger recipient identifier
     * Sends the quick reply buttons to choose the time period for specific country
     * @throws MessengerApiException if message is null
     * @Author Vladislavs Višņevskis
     */
    public void sendQuickReplyCountryButtons(String recipientId) throws MessengerApiException, MessengerIOException {
        List<QuickReply> quickReplies = new ArrayList<>();

        quickReplies.add(TextQuickReply.create("Yesterday", "countryYesterday"));
        quickReplies.add(TextQuickReply.create("Last 7 days", "countrySevenDays"));
        quickReplies.add(TextQuickReply.create("Last 30 days", "countryThirtyDays"));

        TextMessage message = TextMessage.create("Choose the period", of(quickReplies), empty());
        messenger.send(MessagePayload.create(recipientId, MessagingType.RESPONSE, message));
    }

    /**
     * @param recipientId Facebook messenger recipient identifier
     * @param text Facebook messenger recipient identifier
     * Sends the text message to recipient
     * @Author Vladislavs Višņevskis
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
            handleSendException(e);
        }
    }

}

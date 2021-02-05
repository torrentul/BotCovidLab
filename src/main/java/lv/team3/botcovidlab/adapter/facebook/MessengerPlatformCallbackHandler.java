package lv.team3.botcovidlab.adapter.facebook;

import com.github.messenger4j.Messenger;
import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.github.messenger4j.exception.MessengerVerificationException;
import lv.team3.botcovidlab.adapter.facebook.cache.FacebookPatientDataCache;
import lv.team3.botcovidlab.adapter.facebook.handlers.EventHandler;
import lv.team3.botcovidlab.adapter.facebook.handlers.PatientApplicationUtil;
import lv.team3.botcovidlab.adapter.facebook.senders.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;

import static com.github.messenger4j.Messenger.*;
import static java.util.Optional.*;

/**
 * Rest controller
 *
 * @author Vladislavs Visnevskis
 */
@RestController
@RequestMapping("/callback")
public class MessengerPlatformCallbackHandler{

    /**
     * Application logger
     */
    public static final Logger logger = LoggerFactory.getLogger(MessengerPlatformCallbackHandler.class);

    private final Messenger messenger;
    private final PatientApplicationUtil patientApplicationUtil;
    private final EventHandler eventHandler;
    private final Sender sender;
    private final FacebookPatientDataCache facebookPatientDataCache;


    /**
     * Class constructor
     *
     * @param messenger main messenger object
     * @param patientApplicationUtil handles the the occasions when Apply button pressed
     * @param eventHandler handles the incoming events
     * @param sender handles the outgoing events
     * @param facebookPatientDataCache saves the user states
     */
    @Autowired
    public MessengerPlatformCallbackHandler(final Messenger messenger, final PatientApplicationUtil patientApplicationUtil, final EventHandler eventHandler, final Sender sender, FacebookPatientDataCache facebookPatientDataCache) {
        this.messenger = messenger;
        this.patientApplicationUtil = patientApplicationUtil;
        this.eventHandler = eventHandler;
        this.sender = sender;
        this.facebookPatientDataCache = facebookPatientDataCache;
    }

    /**
     * Method that verifies Facebook webhook
     *
     * @param mode mode of requested parameter name
     * @param verifyToken verification token for connection
     * @param challenge challenge of requested parameter name
     * @return corresponding response
     * @author Vladislavs Visnevskis
     */
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
     * Method that handles the user event in the messenger
     *
     * @param payload the actual user message
     * @param signature the signature of the message
     * @throws MessengerApiException if message is null
     * @throws MalformedURLException to indicate that a malformed URL has occurred
     * @throws MessengerIOException input-output exception
     * @return the response for the request
     * @author Vladislavs Visnevskis
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> handleCallback(@RequestBody final String payload, @RequestHeader(SIGNATURE_HEADER_NAME) final String signature) throws MessengerApiException, MessengerIOException, MalformedURLException {
        logger.debug("Received Messenger Platform callback - payload: {} | signature: {}", payload, signature);
        try {
            this.messenger.onReceiveEvents(payload, of(signature), event -> {
                if (event.isTextMessageEvent() && !facebookPatientDataCache.getUserStates(Long.parseLong(event.senderId())).isApplyButton()) {
                    eventHandler.handleTextMessageEvent(event.asTextMessageEvent());
                } else if (event.isTextMessageEvent() && facebookPatientDataCache.getUserStates(Long.parseLong(event.senderId())).isApplyButton()) {
                    patientApplicationUtil.handleTestApplicationEvent(event.asTextMessageEvent());
                } else if (event.isPostbackEvent()) {
                    eventHandler.handlePostbackEvent(event.asPostbackEvent());
                } else if (event.isQuickReplyMessageEvent()  && !facebookPatientDataCache.getUserStates(Long.parseLong(event.senderId())).isApplyButton()) {
                    eventHandler.handleQuickReplyMessageEvent(event.asQuickReplyMessageEvent());
                } else if (event.isQuickReplyMessageEvent()  && facebookPatientDataCache.getUserStates(Long.parseLong(event.senderId())).isApplyButton()) {
                    patientApplicationUtil.handleQuickReplyMessageApplyEvent(event.asQuickReplyMessageEvent());
                } else {
                    eventHandler.handleFallbackEvent(event);
                }
            });
            logger.debug("Processed callback payload successfully");
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (MessengerVerificationException e) {
            logger.warn("Processing of callback payload failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

}

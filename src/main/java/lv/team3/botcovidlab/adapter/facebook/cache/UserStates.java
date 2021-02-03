package lv.team3.botcovidlab.adapter.facebook.cache;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * Class that contains user states in the messenger
 * @author Vladislavs Visnevsks
 */
@Component
@Getter
@Setter
public class UserStates {

    private long chatId;
    private boolean pressedButton;
    private String input;
    private boolean applyButton;

    /**
     *Empty constructor for creating instances
     */
    public UserStates() {
    }
}

package lv.team3.botcovidlab.adapter.facebook.cache;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class UserStates {

    private long chatId;
    private boolean pressedButton;
    private String input;
    private boolean applyButton;
}

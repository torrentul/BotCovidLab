package lv.team3.botcovidlab.adapter.telegram.cache;

import lv.team3.botcovidlab.adapter.telegram.BotStates;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * In-memory cache.
 * usersBotStates: user_id and user's bot state
 * usersProfileData: user_id  and user's profile data.
 */

@Component
public class UserDataCache implements DataCache {
    private Map<Integer, BotStates> usersBotStates = new HashMap<>();
    private Map<Integer, UserProfileData> usersProfileData = new HashMap<>();

    @Override
    public void setUsersCurrentBotState(int userId, BotStates botState) {
        usersBotStates.put(userId, botState);
    }

    @Override
    public void setUsersCurrentBotState(int userId, BotStates botState) {

    }

    @Override
    public BotStates getUsersCurrentBotState(int userId) {
        BotStates botState = usersBotStates.get(userId);
        if (botState == null) {
            botState = BotStates.DEFAULT;
        }

        return botState;
    }

    @Override
    public UserProfileData getUserProfileData(int userId) {
        UserProfileData userProfileData = usersProfileData.get(userId);
        if (userProfileData == null) {
            userProfileData = new UserProfileData();
        }
        return userProfileData;
    }

    @Override
    public void saveUserProfileData(int userId, UserProfileData userProfileData) {
        usersProfileData.put(userId, userProfileData);
    }
}

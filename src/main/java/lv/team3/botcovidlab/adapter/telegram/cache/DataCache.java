package lv.team3.botcovidlab.adapter.telegram.cache;


import lv.team3.botcovidlab.adapter.telegram.BotStates;

public interface DataCache {
    void setUsersCurrentBotState(int userId, BotStates botState);

    BotStates getUsersCurrentBotState(int userId);

    UserProfileData getUserProfileData(int userId);

    void saveUserProfileData(int userId, UserProfileData userProfileData);
}

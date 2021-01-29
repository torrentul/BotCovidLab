package lv.team3.botcovidlab.adapter.telegram.cache;


import lv.team3.botcovidlab.adapter.telegram.handlers.BotStates;
import lv.team3.botcovidlab.entityManager.Patient;

public interface DataCache {
    void setPatiensCurrentBotState(int userId, BotStates botState);

    BotStates getPatientsCurrentBotState(int userId);

    Patient getPatientData(int userId);

    void savePatientsProfileData(int userId, Patient patient);
}

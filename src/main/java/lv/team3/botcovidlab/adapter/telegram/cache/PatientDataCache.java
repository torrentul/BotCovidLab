package lv.team3.botcovidlab.adapter.telegram.cache;

import lv.team3.botcovidlab.adapter.telegram.handlers.BotStates;
import lv.team3.botcovidlab.entityManager.Patient;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * In-memory cache.
 * usersBotStates: user_id and user's bot state
 * usersProfileData: user_id  and user's profile data.
 */

@Component
public class PatientDataCache implements DataCache {
    private Map<Integer, BotStates> patientsBotStates = new HashMap<>();
    private Map<Integer, Patient> patientsData = new HashMap<>();



    @Override
    public void setPatiensCurrentBotState(int userId, BotStates botState) {
        patientsBotStates.put(userId, botState);

    }

    @Override
    public BotStates getPatientsCurrentBotState(int userId) {
        BotStates botState = patientsBotStates.get(userId);
        if (botState == null) {
            botState = BotStates.DEFAULT;
        }

        return botState;
    }

    @Override
    public Patient getPatientData(int userId) {
        Patient patient = patientsData.get(userId);
        if (patient == null) {
            patient = new Patient();
        }
        return patient;
    }

    @Override
    public void savePatientsProfileData(int userId, Patient patient) {
        patientsData.put(userId,patient);
    }
}

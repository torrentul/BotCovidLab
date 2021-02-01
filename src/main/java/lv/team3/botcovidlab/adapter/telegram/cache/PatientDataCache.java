package lv.team3.botcovidlab.adapter.telegram.cache;

import lv.team3.botcovidlab.adapter.telegram.state.BotStates;
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
public  class PatientDataCache  {
    private static Map<Long, BotStates> patientsBotStates = new HashMap<>();
    private static Map<Long, Patient> patientsData = new HashMap<>();



    public static void setPatiensCurrentBotState(Long userId, BotStates botState) {
        patientsBotStates.put(userId, botState);

    }

    public static BotStates getPatientsCurrentBotState(Long userId) {
        BotStates botState = patientsBotStates.get(userId);
        if (botState == null) {
            botState = BotStates.DEFAULT;
        }

        return botState;
    }

    public static Patient getPatientData(Long userId) {
        Patient patient = patientsData.get(userId);
        if (patient == null) {
            patient = new Patient();
            patient.setChatId((long) userId);
            savePatientsProfileData(userId,patient);
        }
        return patient;
    }

    public static void savePatientsProfileData(Long userId, Patient patient) {
        patientsData.put(userId,patient);
    }
}

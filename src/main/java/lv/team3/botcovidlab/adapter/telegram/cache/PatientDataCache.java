package lv.team3.botcovidlab.adapter.telegram.cache;

import lv.team3.botcovidlab.adapter.telegram.state.BotStates;
import lv.team3.botcovidlab.entityManager.Patient;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * In-memory cache.
 * patientsBotStates: chat_id and Patient's bot state
 * patientsData: chat_id  and Patient's profile data.
 * @author Vladislavs Kraslavskis
 */

@Component
public  class PatientDataCache  {
    private static Map<Long, BotStates> patientsBotStates = new HashMap<>();
    private static Map<Long, Patient> patientsData = new HashMap<>();


    /**
     * @param userId Requested users Telegram chat identifier
     * @param botState New Patient Bot State
     */
    public static void setPatiensCurrentBotState(Long userId, BotStates botState) {
        patientsBotStates.put(userId, botState);

    }
    /**
     * @param userId Telegram users chat identifier
     * @return Patients Bot State
     */
    public static BotStates getPatientsCurrentBotState(Long userId) {
        BotStates botState = patientsBotStates.get(userId);
        if (botState == null) {
            botState = BotStates.DEFAULT;
        }

        return botState;
    }
    /**
     * @param userId Telegram users chat identifier
     * @return Patients with requested chat id
     */
    public static Patient getPatientData(Long userId) {
        Patient patient = patientsData.get(userId);
        if (patient == null) {
            patient = new Patient();
            patient.setChatId((long) userId);
            savePatientsProfileData(userId,patient);
        }
        return patient;
    }
    /**
     * Saves patient in patientsData hashMap
     * @param userId Telegram users chat identifier
     */
    public static void savePatientsProfileData(Long userId, Patient patient) {
        patientsData.put(userId,patient);
    }
}

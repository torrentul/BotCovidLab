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
    private static Map<Long, BotStates> previousBotStates = new HashMap<>();
    private static Map<Long, String> countrySelected = new HashMap<>();
    private static Map<Long, Patient> patientsData = new HashMap<>();

    /**
     * @param userId Requested users Telegram chat identifier
     * @param country Represents country, which user selected to get data about.
     */

    public static void setPatiensCountry(Long userId, String country) {
        countrySelected.put(userId, country);

    }
    /**
     * @param userId Telegram users chat identifier
     * @return Name of the country. This param is used, to understand for which country stats, user is asking for.
     */
    public static String getPatientsCountry(Long userId) {
        String s = countrySelected.get(userId);
        if (s == null) {
            s = "Latvia";
        }

        return s;
    }


    /**
     * @param userId Requested users Telegram chat identifier
     * @param botState Represents current users state. This param is used, to understand what text bot should show to user.
     */

    public static void setPatiensCurrentBotState(Long userId, BotStates botState) {
        patientsBotStates.put(userId, botState);

    }
    /**
     * @param userId Telegram users chat identifier
     * @return Patients Bot State. This param is used, to understand in which state bot was before questionnarie started.
     */
    public static BotStates getPreviousBotState(Long userId) {
        BotStates botState = previousBotStates.get(userId);
        if (botState == null) {
            botState = BotStates.DEFAULT;
        }

        return botState;
    }
    /**
     * @param userId Requested users Telegram chat identifier
     * @param botState Represents current users state. This param is used, to understand what text bot should show to user.
     */

    public static void setPatiensPreviousBotState(Long userId, BotStates botState) {
        previousBotStates.put(userId, botState);

    }

    /**
     * @param userId Telegram users chat identifier
     * @return Patients Bot State. This param is used, to understand in which state is bot now.
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
     * @return Patient with requested chat id
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

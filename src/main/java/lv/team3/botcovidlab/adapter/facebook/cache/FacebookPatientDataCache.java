package lv.team3.botcovidlab.adapter.facebook.cache;

import lv.team3.botcovidlab.entityManager.Patient;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility that caches the test application and buttons state for each user
 *
 * @author Vladislavs Visnevskis
 */
@Component
public  class FacebookPatientDataCache {
    private Map<Long, Patient> patientsData = new HashMap<>();
    private Map<Long, UserStates> countryButtonData = new HashMap<>();

    /**
     * Method that return patient from map relating to user identifier
     * Or creates new patient in the map, if it does not exist
     *
     * @param senderId Facebook messenger sender identifier
     * @return Patients with requested sender id
     * @author Vladislavs Visnevskis
     */
    public Patient getPatientData(Long senderId) {
        Patient patient = patientsData.get(senderId);
        if (patient == null) {
            patient = new Patient();
            patient.setChatId((long) senderId);
            savePatientsProfileData(senderId, patient);
        }
        return patient;
    }


    /**
     * Saves patient in patientsData hashMap
     * @param senderId Facebook messenger sender identifier
     * @param patient Facebook messenger sender's created user
     * @author Vladislavs Visnevskis
     */
    public void savePatientsProfileData(Long senderId, Patient patient) {
        patientsData.put(senderId, patient);
    }


    /**
     * Method that return the current state of senders buttons
     *
     * @param senderId Facebook messenger sender identifier
     * @return state of sender "Country" button
     * @author Vladislavs Visnevskis
     */
    public UserStates getUserStates(Long senderId) {
        UserStates userStates = countryButtonData.get(senderId);
        if (userStates == null) {
            userStates = new UserStates();
            userStates.setChatId((long) senderId);
            saveUserStates(senderId, userStates);
        }
        return userStates;
    }

    /**
     * Saves user state in hashMap
     * @param senderId Facebook messenger sender identifier
     * @param userStates Facebook messenger sender state
     * @author Vladislavs Visnevskis
     */
    public void saveUserStates(Long senderId, UserStates userStates) {
        countryButtonData.put(senderId, userStates);
    }

}



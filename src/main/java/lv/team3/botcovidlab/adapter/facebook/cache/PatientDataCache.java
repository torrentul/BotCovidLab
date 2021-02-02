package lv.team3.botcovidlab.adapter.facebook.cache;

import lv.team3.botcovidlab.entityManager.Patient;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public  class PatientDataCache {
    private Map<Long, Patient> patientsData = new HashMap<>();
    private Map<Long, UserStates> countryButtonData = new HashMap<>();

    /**
     * @param senderId Facebook messenger sender identifier
     * @return Patients with requested sender id
     * @Author Vladislavs Višņevskis
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
     * @Author Vladislavs Višņevskis
     */
    public void savePatientsProfileData(Long senderId, Patient patient) {
        patientsData.put(senderId, patient);
    }


    /**
     * @param senderId Facebook messenger sender identifier
     * @return state of sender "Country" button
     * @Author Vladislavs Višņevskis
     */
    public UserStates getUserStates(Long senderId) {
        UserStates countryButton = countryButtonData.get(senderId);
        if (countryButton == null) {
            countryButton = new UserStates();
            countryButton.setChatId((long) senderId);
            saveUserStates(senderId, countryButton);
        }
        return countryButton;
    }

    /**
     * Saves user state in hashMap
     * @param senderId Facebook messenger sender identifier
     * @param userStates Facebook messenger sender state
     * @Author Vladislavs Višņevskis
     */
    public void saveUserStates(Long senderId, UserStates userStates) {
        countryButtonData.put(senderId, userStates);
    }

}



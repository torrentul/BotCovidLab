package lv.team3.botcovidlab.adapter.telegram.cache;

import lv.team3.botcovidlab.adapter.telegram.state.BotStates;
import lv.team3.botcovidlab.entityManager.Patient;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PatientDataCacheTest {
    BotStates states = BotStates.PROFILE_FILLED;
    ArrayList<BotStates> arrayList = new ArrayList<>();
    Patient patient = new Patient();

    @Test
    void setPatiensCurrentBotState() {
        fillArr(arrayList);
        for (int i =0; i<10;i++) {
            PatientDataCache.setPatiensCurrentBotState((long) i, arrayList.get(i));
            assertEquals(PatientDataCache.getPatientsCurrentBotState((long) i), arrayList.get(i));
        }
    }

    @Test
    void getPatientsCurrentBotState() {
        PatientDataCache.savePatientsProfileData((long) 123,new Patient());
        assertEquals(PatientDataCache.getPatientsCurrentBotState((long) 123),BotStates.DEFAULT);
    }

    @Test
    void getPatientData() {
        patient.setPhoneNumber("+371");
        PatientDataCache.savePatientsProfileData((long) 1,patient);
        Patient patientTmp = PatientDataCache.getPatientData((long) 1);
        assertEquals(patient.getPhoneNumber(),patientTmp.getPhoneNumber());
    }

    @Test
    void savePatientsProfileData() {
        patient.setPhoneNumber("+371");
        PatientDataCache.savePatientsProfileData((long) 1,patient);
        Patient patientTmp = PatientDataCache.getPatientData((long) 1);
        assertEquals(patient.getPhoneNumber(),patientTmp.getPhoneNumber());
    }


    static void fillArr(ArrayList<BotStates> arrayList){
        arrayList.add(BotStates.DEFAULT);
        arrayList.add(BotStates.QUESTION1);
        arrayList.add(BotStates.QUESTION2);
        arrayList.add(BotStates.QUESTION3);
        arrayList.add(BotStates.QUESTION4);
        arrayList.add(BotStates.QUESTION5);
        arrayList.add(BotStates.QUESTION6);
        arrayList.add(BotStates.QUESTION7);
        arrayList.add(BotStates.QUESTION8);
        arrayList.add(BotStates.QUESTION9);
    }
}
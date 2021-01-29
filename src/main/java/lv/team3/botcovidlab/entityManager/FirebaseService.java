package lv.team3.botcovidlab.entityManager;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class FirebaseService {

    public static final String COL_NAME = "patients";

    public static boolean isPatientFound(String personalCode) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(COL_NAME).document(personalCode);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        return document.exists();
    }

    public static String savePatientDetails(Patient patient) throws InterruptedException, ExecutionException {
        if (isPatientFound(patient.getPersonalCode())) {
            return "Patient with personal code " + patient.getPersonalCode() + " already exists in the database";
        } else {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection("patients")
                    .document(patient.getPersonalCode())
                    .set(patient);
            return collectionsApiFuture.get().getUpdateTime().toString();
        }
    }

    public static Patient createPatient(Long chatId, String name, String lastName,
                                        String personalCode, String temperature, boolean isContactPerson,
                                        boolean hasCough, boolean hasTroubleBreathing,
                                        boolean hasHeadache, String phoneNumber) throws ExecutionException, InterruptedException {

        if (!isPatientFound(personalCode)) {
            try {
                Patient patient = new Patient();
                patient.setChatId(chatId);
                patient.setName(name);
                patient.setLastName(lastName);
                patient.setPersonalCode(personalCode);
                patient.setTemperature(temperature);
                patient.setContactPerson(isContactPerson);
                patient.setHasCough(hasCough);
                patient.setHasTroubleBreathing(hasTroubleBreathing);
                patient.setHasHeadache(hasHeadache);
                patient.setPhoneNumber(phoneNumber);
                savePatientDetails(patient);
                return patient;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            System.out.println("Patient with personal code " + personalCode + " already exists in the database");
            return null;
        }
    }

    public static Patient getPatientDetails(String personalCode) throws InterruptedException, ExecutionException {
        Patient patient = null;
        if (isPatientFound(personalCode)) {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            DocumentReference documentReference = dbFirestore.collection(COL_NAME).document(personalCode);
            ApiFuture<DocumentSnapshot> future = documentReference.get();
            DocumentSnapshot document = future.get();
            patient = document.toObject(Patient.class);
        } else {
            System.out.println("Patient with personal code " + personalCode + " not found");
        }
        return patient;
    }

    public static String updatePatientDetails(Patient patient) throws InterruptedException, ExecutionException {

        if (isPatientFound(patient.getPersonalCode())) {
            FirestoreClient.getFirestore().collection(COL_NAME).document(patient.getPersonalCode()).set(patient);
        }
        return FirestoreClient.getFirestore().collection(COL_NAME).document(patient.getPersonalCode()).set(patient).get().getUpdateTime().toString();
    }

    public static String deletePatient(String personalCode) throws ExecutionException, InterruptedException {
        if (isPatientFound(personalCode)) {
            FirestoreClient.getFirestore().collection(COL_NAME).document(personalCode).delete();
        } else {
            System.out.println("Patient with personal code " + personalCode + " not found");
            return null;
        }

        return "Document with Patient ID " + personalCode + " has been deleted";
    }
}

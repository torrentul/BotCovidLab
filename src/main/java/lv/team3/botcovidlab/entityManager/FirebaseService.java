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

    public static String savePatientDetails(Patient patient) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection("patients")
                .document(patient.getPersonalCode())
                .set(patient);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public static Patient createPatient(Integer id, Long chatId, String name, String lastName,
                                        String personalCode, Double temperature, boolean isContactPerson,
                                        boolean hasCough, boolean hasTroubleBreathing,
                                        boolean hasHeadache, String phoneNumber) throws ExecutionException, InterruptedException {
        Patient patient = new Patient();
        patient.setId(id);
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
    }


    public static Patient getPatientDetails(String personalCode) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(COL_NAME).document(personalCode);
        ApiFuture<DocumentSnapshot> future = documentReference.get();

        DocumentSnapshot document = future.get();

        Patient patient = null;

        if (document.exists()) {
            patient = document.toObject(Patient.class);
            return patient;
        } else {
            return null;
        }
    }

    public static String updatePatientDetails(Patient patient) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(patient.getPersonalCode()).set(patient);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public static String deletePatient(String personalCode) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult = dbFirestore.collection(COL_NAME).document(personalCode).delete();
        return "Document with Patient ID " + personalCode + " has been deleted";
    }
}

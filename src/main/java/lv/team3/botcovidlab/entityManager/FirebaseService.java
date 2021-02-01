package lv.team3.botcovidlab.entityManager;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * FirebaseService is responsible for CRUD operations with database
 */
@Service
public class FirebaseService {

    public static final String COL_NAME = "patients";
    private static DocumentReference documentReference;
    private static ApiFuture<DocumentSnapshot> future;


    /**
     * @param personalCode Personal code, which is used as an entry ID in database
     * @return true, if patient is found, false - if patient does not exist in database
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static boolean isPatientFound(String personalCode) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        documentReference = dbFirestore.collection(COL_NAME).document(personalCode);
        future = documentReference.get();
        DocumentSnapshot document = future.get();
        return document.exists();
    }

    /**
     * @param patient Patient object to be saved in database
     * prints message in console about success of the method
     * @return Timestamp of the update in string format
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public static String savePatientDetails(Patient patient) throws InterruptedException, ExecutionException {
        String personalCode = patient.getPersonalCode();
        if (isPatientFound(personalCode)) {
            return "Patient with personal code " + personalCode + " already exists in the database";
        } else {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection("patients")
                    .document(personalCode)
                    .set(patient);
            System.out.println("Patient with personal code " + personalCode + " added to database");
            return collectionsApiFuture.get().getUpdateTime().toString();
        }
    }

    /**
     * @param chatId Id number registered in chat with Telegram chatbot
     * @param name name of patient
     * @param lastName last name of patient
     * @param personalCode Personal code, which is used as an entry ID in database
     * @param temperature Body temperature
     * @param isContactPerson boolean value if person has been in contact with COVID_19 patient
     * @param hasCough symptom of illness (cough)
     * @param hasTroubleBreathing symptom of illness (trouble breathing)
     * @param hasHeadache symptom of illness (headache)
     * @param phoneNumber contact telephone number
     * Method creates and also adds patient to the database
     * @return newly created patient object
     * @throws ExecutionException
     * @throws InterruptedException
     */
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

    /**
     * @param personalCode Personal code, which is used as an entry ID in database
     * @return patient object, {@code null} & message on console if patient is not found.
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public static Patient getPatientDetails(String personalCode) throws InterruptedException, ExecutionException {
        Patient patient = null;
        if (isPatientFound(personalCode)) {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            documentReference = dbFirestore.collection(COL_NAME).document(personalCode);
            future = documentReference.get();
            DocumentSnapshot document = future.get();
            patient = document.toObject(Patient.class);
        } else {
            System.out.println("Patient with personal code " + personalCode + " not found");
        }
        return patient;
    }

    /**
     * @param patient Patient object, which contains details needed to update
     * @return timestamp of updates in string format, {@code null} & message on console if patient not found
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public static String updatePatientDetails(Patient patient) throws InterruptedException, ExecutionException {
        String personalCode = patient.getPersonalCode();
        if (isPatientFound(personalCode)) {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            documentReference = dbFirestore.collection(COL_NAME).document(personalCode);
            documentReference.set(patient);
            return documentReference.set(patient).get().getUpdateTime().toString();
        } else {
            System.out.println("Patient with personal code " + personalCode + " not found");
            return null;
        }
    }

    /**
     * @param personalCode Personal code, which is used as an entry ID in database
     * @return String about success of the method, {@code null} & message on console if patient not found
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static String deletePatient(String personalCode) throws ExecutionException, InterruptedException {
        if (isPatientFound(personalCode)) {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            documentReference = dbFirestore.collection(COL_NAME).document(personalCode);
            documentReference.delete();
            return "Document with Patient ID " + personalCode + " has been deleted";
        } else {
            System.out.println("Patient with personal code " + personalCode + " not found");
            return null;
        }
    }

    /**
     * @param chatId chatId which is registered while using Telegram chatbot
     * @return Patient object, if found in database, {@code null} if patient not found
     * Prints a message on console about success of the method
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static Patient findByChatId(Long chatId) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> patientQuery = dbFirestore.collection(COL_NAME).whereEqualTo("chatId", chatId).get();
        List<QueryDocumentSnapshot> entry = patientQuery.get().getDocuments();
        if(entry.isEmpty()) {
            System.out.println("Patient with chatId " + chatId + " not found");
            return null;
        } else {
            System.out.println("Patient with chatId "+ chatId + " found!");
            return entry.get(0).toObject(Patient.class);
        }

    }
}

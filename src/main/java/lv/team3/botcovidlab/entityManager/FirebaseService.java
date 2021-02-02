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
     */
    public static boolean isPatientFound(String personalCode) {
        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            documentReference = dbFirestore.collection(COL_NAME).document(personalCode);
            future = documentReference.get();
            DocumentSnapshot document = future.get();
            return document.exists();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param patient Patient object to be saved in database
     *                prints message in console about success of the method
     * @return Timestamp of the update in string format
     */
    public static String savePatientDetails(Patient patient) {
        String personalCode = patient.getPersonalCode();
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param chatId              Id number registered in chat with Telegram chatbot
     * @param name                name of patient
     * @param lastName            last name of patient
     * @param personalCode        Personal code, which is used as an entry ID in database
     * @param temperature         Body temperature
     * @param isContactPerson     boolean value if person has been in contact with COVID_19 patient
     * @param hasCough            symptom of illness (cough)
     * @param hasTroubleBreathing symptom of illness (trouble breathing)
     * @param hasHeadache         symptom of illness (headache)
     * @param phoneNumber         contact telephone number
     *                            Method creates and also adds patient to the database
     * @return newly created patient object
     */
    public static Patient createPatient(Long chatId, String name, String lastName,
                                        String personalCode, String temperature, boolean isContactPerson,
                                        boolean hasCough, boolean hasTroubleBreathing,
                                        boolean hasHeadache, String phoneNumber) {
        try {
            if (!isPatientFound(personalCode)) {
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
            } else {
                System.out.println("Patient with personal code " + personalCode + " already exists in the database");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param personalCode Personal code, which is used as an entry ID in database
     * @return patient object, {@code null} and message on console if patient is not found.
     */
    public static Patient getPatientDetails(String personalCode) {
        Patient patient = null;
        try {
            if (isPatientFound(personalCode)) {
                Firestore dbFirestore = FirestoreClient.getFirestore();
                documentReference = dbFirestore.collection(COL_NAME).document(personalCode);
                future = documentReference.get();
                DocumentSnapshot document = future.get();
                patient = document.toObject(Patient.class);
            } else {
                System.out.println("Patient with personal code " + personalCode + " not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return patient;
    }

    /**
     * @param patient Patient object, which contains details needed to update
     * @return timestamp of updates in string format, {@code null} and message on console if patient not found
     */
    public static String updatePatientDetails(Patient patient) {
        String personalCode = patient.getPersonalCode();
        try {
            if (isPatientFound(personalCode)) {
                Firestore dbFirestore = FirestoreClient.getFirestore();
                documentReference = dbFirestore.collection(COL_NAME).document(personalCode);
                documentReference.set(patient);
                return documentReference.set(patient).get().getUpdateTime().toString();
            } else {
                System.out.println("Patient with personal code " + personalCode + " not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * @param personalCode Personal code, which is used as an entry ID in database
     * @return String about success of the method, {@code null} and message on console if patient not found
     */
    public static String deletePatient(String personalCode) {
        try {
            if (isPatientFound(personalCode)) {
                Firestore dbFirestore = FirestoreClient.getFirestore();
                documentReference = dbFirestore.collection(COL_NAME).document(personalCode);
                documentReference.delete();
                return "Document with Patient ID " + personalCode + " has been deleted";
            } else {
                System.out.println("Patient with personal code " + personalCode + " not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * @param chatId chatId which is registered while using Telegram chatbot
     * @return Patient object, if found in database, {@code null} if patient not found
     * Prints a message on console about success of the method
     */
    public static Patient findByChatId(Long chatId) {
        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            ApiFuture<QuerySnapshot> patientQuery = dbFirestore.collection(COL_NAME).whereEqualTo("chatId", chatId).get();
            List<QueryDocumentSnapshot> entry = patientQuery.get().getDocuments();
            if (!entry.isEmpty()) {
                System.out.println("Patient with chatId " + chatId + " found!");
                return entry.get(0).toObject(Patient.class);
            } else {
                System.out.println("Patient with chatId " + chatId + " not found");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }
}

package lv.team3.botcovidlab.entityManager;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PatientManager {

    EntityManagerFactory emf; //Factory class to create new Entity Manager
    EntityManager em; //entity manager to manage persistence
    EntityTransaction entityTransaction; //optional transaction

    public PatientManager() {
        emf = Persistence.createEntityManagerFactory("local-database");
        em = emf.createEntityManager();
        entityTransaction = em.getTransaction();
    }

    public Patient createPatient(Integer id, String name,
                                 String lastName, String personalCode,
                                 Double temperature, boolean isContactPerson, String phoneNumber) {
        Patient patient = new Patient();
        patient.setId(id);
        patient.setName(name);
        patient.setLastName(lastName);
        patient.setPersonalCode(personalCode);
        patient.setTemperature(temperature);
        patient.setIsContactPerson(isContactPerson);
        patient.setPhoneNumber(phoneNumber);
        persist(patient);
        return patient;

    }

    public void persist(Object o) {
        try {
            entityTransaction.begin();
            em.persist(o);
            entityTransaction.commit();
        } catch (Exception e){
            entityTransaction.rollback();
            e.printStackTrace();
        }
    }

    public Patient searchPatient (Integer id) {
        return em.find(Patient.class, id);
    }

    public void clearData() {
        entityTransaction.begin();
        em.createNativeQuery("delete from patient").executeUpdate();
        entityTransaction.commit();
    }

    public static void main(String[] args) throws IOException {
        FileInputStream refreshToken = new FileInputStream("C:\\Users\\Valters\\IdeaProjects\\BotCovidLab\\botcovidlabAdminAccess.json");

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(refreshToken))
                .setDatabaseUrl("https://botcovidlab-default-rtdb.firebaseio.com/")
                .build();

        FirebaseApp.initializeApp(options);

    }

}

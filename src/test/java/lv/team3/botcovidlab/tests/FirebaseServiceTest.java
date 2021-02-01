package lv.team3.botcovidlab.tests;

import lv.team3.botcovidlab.entityManager.FirebaseInitializer;
import lv.team3.botcovidlab.entityManager.FirebaseService;
import lv.team3.botcovidlab.entityManager.Patient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

class FirebaseServiceTest {
    private static FirebaseInitializer initializer;
    private static FirebaseService service;

    @BeforeAll
    static void setUp() {
        initializer = new FirebaseInitializer();
        initializer.initialize();
        service = new FirebaseService();
    }

    @Test
    void isPatientFound() throws ExecutionException, InterruptedException {
        assertTrue(service.isPatientFound("114455-66778"));
        assertFalse(service.isPatientFound("114455-66779"));
    }

    @Test
    void savePatientDetails() throws ExecutionException, InterruptedException {
        Patient myPatient = new Patient(220l, "Mary", "Smith",
                "111180-99887", "38.0",
                true, true, true,
                true, "99009900");
        service.savePatientDetails(myPatient);
        assertTrue(service.isPatientFound("111180-99887"));
    }

    @Test
    void createPatient() throws ExecutionException, InterruptedException {
        assertFalse(service.isPatientFound("121280-55555"));
        service.createPatient(180L, "Adam", "Hunt",
                "121280-55555", "36.7", true,
                true, true, true, "22779900");
        assertTrue(service.isPatientFound("121280-55555"));
    }

    @Test
    void getPatientDetails() throws ExecutionException, InterruptedException {
        Patient detailedPatient = new Patient(122l, "John", "Doe",
                "114455-66778", "36.7", true,
                true, true, false, "99887744");
        Patient testPatient = service.getPatientDetails("114455-66778");
        assertEquals(detailedPatient.getLastName(), testPatient.getLastName());
        assertEquals(detailedPatient.getPersonalCode(), testPatient.getPersonalCode());
        assertEquals(detailedPatient.getPhoneNumber(), testPatient.getPhoneNumber());
    }

    @Test
    void updatePatientDetails() throws ExecutionException, InterruptedException {
        Patient testPatient = service.getPatientDetails("114455-66778");
        String oldPhoneNumber = "99887744";
        String oldName = "John";
        String newPhoneNumber = "11223344";
        String newName = "David";
        testPatient.setPhoneNumber(newPhoneNumber);
        testPatient.setName(newName);
        service.updatePatientDetails(testPatient);
        assertNotEquals(oldPhoneNumber, testPatient.getPhoneNumber());
        assertNotEquals(oldName, testPatient.getName());
        assertEquals(newPhoneNumber, testPatient.getPhoneNumber());
        assertEquals(newName, testPatient.getName());
    }

    @Test
    void deletePatient() throws ExecutionException, InterruptedException {
        Patient myTestPatient = service.getPatientDetails("111180-99887");
        service.deletePatient(myTestPatient.getPersonalCode());
        Patient deletedPatient = service.getPatientDetails("111180-99887");
        assertNull(deletedPatient);
    }

    @Test
    void findByChatId() throws ExecutionException, InterruptedException {
        Patient testPatient = service.findByChatId(345l);
        assertEquals("Ann", testPatient.getName());
        assertEquals("Boil", testPatient.getLastName());
        assertEquals("121212-34343", testPatient.getPersonalCode());
        assertEquals("99887744", testPatient.getPhoneNumber());
    }

}
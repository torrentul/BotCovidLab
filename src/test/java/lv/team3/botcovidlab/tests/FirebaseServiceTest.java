package lv.team3.botcovidlab.tests;

import lv.team3.botcovidlab.entityManager.FirebaseInitializer;
import lv.team3.botcovidlab.entityManager.FirebaseService;
import lv.team3.botcovidlab.entityManager.Patient;
import org.junit.jupiter.api.*;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Need to have a test patient always present in database (for isPatientFound method) - Test-Patient Dont-Delete
 * Need to create a new patient (for createPatient method) - John Doe
 * Need to add readymade Patient object (for addPatient method) - Mary Smith
 * Use John Doe for positive getPatientDetails method test
 * Update Mary Smith - name and phone number (to Alice)
 * Use John Doe for searchByChatId method test
 * Delete Mary (now Alice) Smith (with deletePatient method)
 * AfterAll - delete John Doe
 */

@TestMethodOrder(MethodOrderer.Alphanumeric.class)
class FirebaseServiceTest {
    private final String NON_EXISTING_PERSONAL_CODE = "010180-11133";
    private static final String EXISTING_PERSONAL_CODE = "010180-11122";
    private static final String PERSONAL_CODE_JOHN_DOE = "121280-99900";
    private static final String PERSONAL_CODE_MARY_SMITH = "060620-55566";
    private static final Long CHAT_ID_MARY_SMITH = 555L;
    private static final Long CHAT_ID_JOHN_DOE = 111L;
    private static Patient patientToAdd;
    private static Patient testPatient;

    @BeforeAll
    static void setUp() {
        FirebaseInitializer initializer = new FirebaseInitializer();
        initializer.initialize();
        patientToAdd = new Patient(CHAT_ID_MARY_SMITH, "Mary", "Smith", PERSONAL_CODE_MARY_SMITH,
                "37.5", false, false,
                false, false, "55566655");
        testPatient = new Patient(999L, "Test-Patient", "Dont-Delete",
                EXISTING_PERSONAL_CODE, "36.7", true,
                true, true, true, "11223344");

    }

    @Test
    void testA_existingPatientFound() throws ExecutionException, InterruptedException {
        assertTrue(FirebaseService.isPatientFound(EXISTING_PERSONAL_CODE));
    }

    @Test
    void testB_nonExistingPatientNotFound() throws ExecutionException, InterruptedException {
        assertFalse(FirebaseService.isPatientFound(NON_EXISTING_PERSONAL_CODE));
    }

    @Test
    void testC_createNewPatientIfDoesNotExist() throws ExecutionException, InterruptedException {
        assertFalse(FirebaseService.isPatientFound(PERSONAL_CODE_JOHN_DOE));
        FirebaseService.createPatient(CHAT_ID_JOHN_DOE, "John", "Doe",
                PERSONAL_CODE_JOHN_DOE, "38.7", false,
                true, true, true, "99887700");
        assertTrue(FirebaseService.isPatientFound(PERSONAL_CODE_JOHN_DOE));
    }

    @Test
    void testD_failToCreateNewPatientWhenItExists() throws ExecutionException, InterruptedException {
        Patient patientToFail = FirebaseService.createPatient(999L, "Test-Patient", "Dont-Delete",
                EXISTING_PERSONAL_CODE, "36.7", true,
                true, true, true, "11223344");
        assertNull(patientToFail);
    }

    @Test
    void testE_savePatientDetails() throws ExecutionException, InterruptedException {
        assertFalse(FirebaseService.isPatientFound(PERSONAL_CODE_MARY_SMITH));
        FirebaseService.savePatientDetails(patientToAdd);
        assertTrue(FirebaseService.isPatientFound(PERSONAL_CODE_MARY_SMITH));
    }

    @Test
    void testF_doNotSavePatientIfExists() throws ExecutionException, InterruptedException {
        String result = FirebaseService.savePatientDetails(testPatient);
        assertEquals("Patient with personal code " + testPatient.getPersonalCode() +
                " already exists in the database", result);
    }

    @Test
    void testG_getPatientDetailsIfExists() throws ExecutionException, InterruptedException {
        Patient detailedPatient = new Patient(CHAT_ID_JOHN_DOE, "John", "Doe",
                PERSONAL_CODE_JOHN_DOE, "38.7", false,
                true, true, true, "99887700");
        Patient testPatient = FirebaseService.getPatientDetails(PERSONAL_CODE_JOHN_DOE);
        assertEquals(detailedPatient, testPatient);
    }

    @Test
    void testH_nonExistingPatientDetailsNull() throws ExecutionException, InterruptedException {
        Patient testPatient = FirebaseService.getPatientDetails(NON_EXISTING_PERSONAL_CODE);
        assertNull(testPatient);
    }

    @Test
    void testI_updatePatientDetails() throws ExecutionException, InterruptedException {
        Patient patientToUpdate = FirebaseService.getPatientDetails(PERSONAL_CODE_MARY_SMITH);
        String oldPhoneNumber = patientToUpdate.getPhoneNumber();
        String oldName = patientToUpdate.getName();
        String newPhoneNumber = "11223344";
        String newName = "Alice";
        patientToUpdate.setPhoneNumber(newPhoneNumber);
        patientToUpdate.setName(newName);
        FirebaseService.updatePatientDetails(patientToUpdate);
        assertNotEquals(oldPhoneNumber, patientToUpdate.getPhoneNumber());
        assertNotEquals(oldName, patientToUpdate.getName());
        assertEquals(newPhoneNumber, patientToUpdate.getPhoneNumber());
        assertEquals(newName, patientToUpdate.getName());
    }

    @Test
    void testJ_doNotUpdateNonExistingPatient() throws ExecutionException, InterruptedException {
        Patient nonExistingPatient = new Patient (440L, "Missing", "patient",
                "000000-11111", "36.0", false,
                false, false, false, "22222222");
        String updateResult = FirebaseService.updatePatientDetails(nonExistingPatient);
        assertNull(updateResult);
    }

    @Test
    void testK_findByChatId() throws ExecutionException, InterruptedException {
        Patient testPatient = FirebaseService.findByChatId(CHAT_ID_JOHN_DOE);
        assert testPatient != null;
        assertEquals("John", testPatient.getName());
        assertEquals("Doe", testPatient.getLastName());
        assertEquals(PERSONAL_CODE_JOHN_DOE, testPatient.getPersonalCode());
        assertEquals("99887700", testPatient.getPhoneNumber());
    }

    @Test
    void testL_doNotFindNonExistingPatient() throws ExecutionException, InterruptedException {
        Patient testPatient = FirebaseService.findByChatId(000L);
        assertNull(testPatient);
    }

    @Test
    void testM_deletePatient() throws ExecutionException, InterruptedException {
        Patient myTestPatient = FirebaseService.getPatientDetails(PERSONAL_CODE_MARY_SMITH);
        assertNotNull(myTestPatient);
        FirebaseService.deletePatient(myTestPatient.getPersonalCode());
        Patient deletedPatient = FirebaseService.getPatientDetails(PERSONAL_CODE_MARY_SMITH);
        assertNull(deletedPatient);
    }

    @AfterAll
    static void clearDatabase() throws ExecutionException, InterruptedException {
        FirebaseService.deletePatient(PERSONAL_CODE_JOHN_DOE);
    }

}
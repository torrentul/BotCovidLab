package lv.team3.botcovidlab.adapter.inputValidation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PatientInputValidationTest {


    @Test
    void shouldReturnFalseValidatingFirstName() {
        String input = "B1ll";
        assertFalse(PatientInputValidation.validateFirstName(input));
    }

    @Test
    void shouldReturnTrueValidatingFirstName() {
        String input = "Bill";
        assertTrue(PatientInputValidation.validateFirstName(input));
    }

    @Test
    void shouldReturnFalseValidatingLastName() {
        String input = "Gate$";
        assertFalse(PatientInputValidation.validateLastName(input));
    }

    @Test
    void shouldReturnTrueValidatingLastName() {
        String input = "Gates";
        assertTrue(PatientInputValidation.validateLastName(input));
    }

    @Test
    void shouldReturnFalseValidatingTemperature() {
        String input = "t36.6";
        assertFalse(PatientInputValidation.validateTemperature(input));
    }

    @Test
    void shouldReturnTrueValidatingTemperature() {
        String input = "37.2";
        assertTrue(PatientInputValidation.validateTemperature(input));
    }

    @Test
    void shouldReturnFalseValidatingPersonalCode() {
        String input = "123456+12345";
        assertFalse(PatientInputValidation.validatePersonalCode(input));
    }

    @Test
    void shouldReturnTrueValidatingPersonalCode() {
        String input = "123456-12345";
        assertTrue(PatientInputValidation.validatePersonalCode(input));
    }

    @Test
    void shouldReturnFalseValidatingPhoneNumber() {
        String input = "12345678";
        assertFalse(PatientInputValidation.validatePhoneNumber(input));
    }

    @Test
    void shouldReturnTrueValidatingPhoneNumber() {
        String input = "+37123456789";
        assertTrue(PatientInputValidation.validatePhoneNumber(input));
    }
}
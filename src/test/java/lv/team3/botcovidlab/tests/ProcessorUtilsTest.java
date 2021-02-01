package lv.team3.botcovidlab.tests;

import lv.team3.botcovidlab.utils.DateUtils.DateStructure;
import org.junit.jupiter.api.Test;

import static lv.team3.botcovidlab.processors.ProcessorUtils.*;
import static org.junit.jupiter.api.Assertions.*;

class ProcessorUtilsTest {

    @Test
        // TODO Write test
    void testStringFromURL() {
    }

    @Test
        // Writen
    void testIValidDateString() {
        String date = "2020-01-20T00:00:00Z";
        assertTrue(isValidDateString(date), "Date string 1 is not valid");
        date = "2019-01-20T00:00:00Z";
        assertFalse(isValidDateString(date), "Date string 2 is not valid");
        date = "2020-13-20T00:00:00Z";
        assertFalse(isValidDateString(date), "Date string 3 is not valid");
        date = "2020-12-32T00:00:00Z";
        assertFalse(isValidDateString(date), "Date string 4 is not valid");
        date = "2020-01-20T23:59:59Z";
        assertTrue(isValidDateString(date), "Date string 5 is not valid");
        date = "2020-01-20T24:59:59Z";
        assertFalse(isValidDateString(date), "Date string 5 is not valid");
        date = "2020-01-20T23:60:59Z";
        assertFalse(isValidDateString(date), "Date string 5 is not valid");
        date = "2020-01-20T20:00:60Z";
        assertFalse(isValidDateString(date), "Date string 5 is not valid");
        date = "2020-01-20T20:00:600Z";
        assertFalse(isValidDateString(date), "Date string 5 is not valid");
        date = "2020-001-20T20:00:00Z";
        assertFalse(isValidDateString(date), "Date string 5 is not valid");
        date = "2020-01-200T20:00:00Z";
        assertFalse(isValidDateString(date), "Date string 5 is not valid");
    }

    @Test
        // Writen
    void testisDateAfter() {
        DateStructure testDate = new DateStructure("2020-01-20T00:00:00Z");
        DateStructure dateBase = new DateStructure("2020-01-20T00:00:00Z");
        assertTrue(isDateAfter(testDate, dateBase, true), "Invalid date test 1");
        assertFalse(isDateAfter(testDate, dateBase, false), "Invalid date test 2");
        testDate = new DateStructure("2020-01-19T00:00:00Z");
        assertFalse(isDateAfter(testDate, dateBase, true), "Invalid date test 3");
        assertFalse(isDateAfter(testDate, dateBase, false), "Invalid date test 4");
        testDate = new DateStructure("2020-01-21T00:00:00Z");
        assertTrue(isDateAfter(testDate, dateBase, true), "Invalid date test 3");
        assertTrue(isDateAfter(testDate, dateBase, false), "Invalid date test 4");
    }

    @Test
        // Writen
    void testIsDateBefore() {
        DateStructure testDate = new DateStructure("2020-01-20T00:00:00Z");
        DateStructure dateBase = new DateStructure("2020-01-20T00:00:00Z");
        assertTrue(isDateBefore(testDate, dateBase, true), "Invalid date test 1");
        assertFalse(isDateBefore(testDate, dateBase, false), "Invalid date test 2");
        testDate = new DateStructure("2020-01-19T00:00:00Z");
        assertTrue(isDateBefore(testDate, dateBase, true), "Invalid date test 3");
        assertTrue(isDateBefore(testDate, dateBase, false), "Invalid date test 4");
        testDate = new DateStructure("2020-01-21T00:00:00Z");
        assertFalse(isDateBefore(testDate, dateBase, true), "Invalid date test 3");
        assertFalse(isDateBefore(testDate, dateBase, false), "Invalid date test 4");
    }

    @Test
        // Writen
    void testIsDateInRange() {
        DateStructure fromDate = new DateStructure("2020-02-10T00:00:00Z");
        DateStructure toDate = new DateStructure("2020-02-20T00:00:00Z");
        DateStructure test = new DateStructure("2020-02-10T00:00:00Z");
        for (int i = 5; i < 25; i++) {
            test.setDay(i);
            if (i >= 10 && i <= 20) {
                assertTrue(isDateInRange(test, fromDate, toDate, true), "Invalid date test 1");
            } else {
                assertFalse(isDateInRange(test, fromDate, toDate, true), "Invalid date test 1");
            }
        }
        for (int i = 5; i < 25; i++) {
            test.setDay(i);
            if (i >= 10 && i < 20) {
                assertTrue(isDateInRange(test, fromDate, toDate, true, false), "Invalid date test 2");
            } else {
                assertFalse(isDateInRange(test, fromDate, toDate, true, false), "Invalid date test 2");
            }
        }
        for (int i = 5; i < 25; i++) {
            test.setDay(i);
            if (i > 10 && i <= 20) {
                assertTrue(isDateInRange(test, fromDate, toDate, false, true), "Invalid date test 3");
            } else {
                assertFalse(isDateInRange(test, fromDate, toDate, false, true), "Invalid date test 3");
            }
        }
        for (int i = 5; i < 25; i++) {
            test.setDay(i);
            if (i > 10 && i < 20) {
                assertTrue(isDateInRange(test, fromDate, toDate, false), "Invalid date test 1");
            } else {
                assertFalse(isDateInRange(test, fromDate, toDate, false), "Invalid date test 1");
            }
        }
    }
}
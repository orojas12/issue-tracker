package dev.oscarrojas.issuetracker.util;

import org.junit.jupiter.api.Test;

import static dev.oscarrojas.issuetracker.util.RandomStringGenerator.*;
import static org.junit.jupiter.api.Assertions.*;

public class RandomStringGeneratorTest {

    @Test
    void getRandomString_stringHasSpecifiedLength() {
        String value = getRandomString(5);
        assertEquals(5, value.length());
        value = getRandomString(4);
        assertEquals(4, value.length());
    }

    @Test
    void getRandomString_stringContainsNoWhitespace() {
        String value = getRandomString(5);

        assertFalse(value.contains(" "));
    }

    @Test
    void getRandomString_generatedStringsAreNotEqual() {
        String value1 = getRandomString(5);
        String value2 = getRandomString(5);
        assertNotEquals(value1, value2);
    }
}

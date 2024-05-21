package dev.oscarrojas.issuetracker.data;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.*;

public class SQLiteLocalDateTimeConverterTest {

    @Test
    void convertToDatabaseColumn_returnsNullIfDateTimeIsNull() {
        var converter = new SQLiteLocalDateTimeConverter();
        String result = converter.convertToDatabaseColumn(null);
        assertNull(result);
    }

    @Test
    void convertToDatabaseColumn_returnsISO8601LocalDateTimeFormattedString() {
        var converter = new SQLiteLocalDateTimeConverter();
        var datetime = LocalDateTime.now();
        String result = converter.convertToDatabaseColumn(datetime);
        assertDoesNotThrow(() -> LocalDateTime.parse(result));
    }

    @Test
    void convertToDatabaseColumn_returnsLocalDateTimeConvertedToISOString() {
        var converter = new SQLiteLocalDateTimeConverter();
        var datetime1 = LocalDateTime.now();
        var datetime2 = LocalDateTime.now().plusSeconds(1);

        var result1 = LocalDateTime.parse(converter.convertToDatabaseColumn(datetime1));
        var result2 = LocalDateTime.parse(converter.convertToDatabaseColumn(datetime2));

        assertEquals(datetime1, result1);
        assertEquals(datetime2, result2);
    }

    @Test
    void convertToEntityAttribute_throwsExceptionIfInvalidString() {
        String timestamp = "2024-04-21T09:30:05+02:00"; // should be a valid local date time (no timezone or offset)
        var converter = new SQLiteLocalDateTimeConverter();
        assertThrows(DateTimeParseException.class, () -> converter.convertToEntityAttribute(timestamp));
    }

    @Test
    void convertToEntityAttribute_DateTimeStringConvertedToLocalDateTime() {
        String timestamp = "2024-04-21T09:30:05";
        var datetime = LocalDateTime.parse(timestamp);
        var converter = new SQLiteLocalDateTimeConverter();
        LocalDateTime result = converter.convertToEntityAttribute(timestamp);
        assertEquals(datetime, result);
    }

    @Test
    void convertToEntityAttribute_returnsNullIfDateTimeStringIsNull() {
        var converter = new SQLiteLocalDateTimeConverter();
        LocalDateTime result = converter.convertToEntityAttribute(null);
        assertNull(result);
    }
}


package dev.oscarrojas.issuetracker.util;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class SQLiteDateTimeConverterTest {

    @Test
    void convertToDatabaseColumn_returnsNullIfDateTimeIsNull() {
        var converter = new SQLiteDateTimeConverter();
        String result = converter.convertToDatabaseColumn(null);
        assertNull(result);
    }

    @Test
    void convertToDatabaseColumn_returnsISO8601DateTimeUTCFormattedString() {
        var converter = new SQLiteDateTimeConverter();
        Instant instant = Instant.now();
        String timestamp = converter.convertToDatabaseColumn(instant);
        AtomicReference<OffsetDateTime> datetime = new AtomicReference<>();
        // will throw exception if not a valid ISO 8601 date time string w/ offset
        assertDoesNotThrow(() -> datetime.set(OffsetDateTime.parse(timestamp)));
        assertEquals(ZoneOffset.UTC, datetime.get().getOffset());
    }

    @Test
    void convertToDatabaseColumn_returnsInstantConvertedToDateTimeString() {
        var converter = new SQLiteDateTimeConverter();
        Instant instant1 = Instant.now();
        Instant instant2 = Instant.now().plusSeconds(1);

        Instant result1 = Instant.parse(converter.convertToDatabaseColumn(instant1));
        Instant result2 = Instant.parse(converter.convertToDatabaseColumn(instant2));

        assertEquals(instant1, result1);
        assertEquals(instant2, result2);
    }

    @Test
    void convertToEntityAttribute_throwsExceptionIfInvalidISOString() {
        String timestamp = "2024-04-21T09:30:05"; // should include offset to be valid
        var converter = new SQLiteDateTimeConverter();
        assertThrows(DateTimeParseException.class, () -> converter.convertToEntityAttribute(timestamp));
    }

    @Test
    void convertToEntityAttribute_DateTimeStringConvertedToInstant() {
        String timestamp = "2024-04-21T09:30:05Z";
        Instant instant = Instant.parse(timestamp);
        var converter = new SQLiteDateTimeConverter();
        Instant result = converter.convertToEntityAttribute(timestamp);
        assertEquals(instant, result);
    }

    @Test
    void convertToEntityAttribute_returnsNullIfDateTimeStringIsNull() {
        var converter = new SQLiteDateTimeConverter();
        Instant result = converter.convertToEntityAttribute(null);
        assertNull(result);
    }
}

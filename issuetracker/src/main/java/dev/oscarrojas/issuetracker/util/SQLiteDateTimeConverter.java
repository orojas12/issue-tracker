package dev.oscarrojas.issuetracker.util;

import jakarta.persistence.AttributeConverter;

import java.time.Instant;

public class SQLiteDateTimeConverter implements AttributeConverter<Instant, String> {
    @Override
    public String convertToDatabaseColumn(Instant datetime) {
        return datetime.toString();
    }

    @Override
    public Instant convertToEntityAttribute(String datetime) {
        return Instant.parse(datetime);
    }
}

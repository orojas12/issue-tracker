package dev.oscarrojas.issuetracker.data;

import jakarta.persistence.AttributeConverter;

import java.time.Instant;

public class SQLiteInstantConverter implements AttributeConverter<Instant, String> {
    @Override
    public String convertToDatabaseColumn(Instant datetime) {
        return datetime != null ? datetime.toString() : null;
    }

    @Override
    public Instant convertToEntityAttribute(String datetime) {
        return datetime != null ? Instant.parse(datetime) : null;
    }
}

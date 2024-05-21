package dev.oscarrojas.issuetracker.data;

import jakarta.persistence.AttributeConverter;

import java.time.LocalDateTime;

public class SQLiteLocalDateTimeConverter implements AttributeConverter<LocalDateTime, String> {
    @Override
    public String convertToDatabaseColumn(LocalDateTime datetime) {
        return datetime != null ? datetime.toString() : null;
    }

    @Override
    public LocalDateTime convertToEntityAttribute(String datetime) {
        return datetime != null ? LocalDateTime.parse(datetime) : null;
    }
}

package de.llalon.cinematic.util.json;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class LenientDateTimeAdapter {

    private static final DateTimeFormatter ISO_OFFSET = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private static final DateTimeFormatter ISO_LOCAL_DATE_TIME = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final DateTimeFormatter ISO_LOCAL_DATE = DateTimeFormatter.ISO_LOCAL_DATE;

    // ===================== FROM JSON =====================

    @FromJson
    public OffsetDateTime fromJson(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        int i = 0;

        try {
            return OffsetDateTime.parse(value, ISO_OFFSET);
        } catch (DateTimeParseException ignored) {
            i++;
        }

        try {
            LocalDateTime ldt = LocalDateTime.parse(value, ISO_LOCAL_DATE_TIME);
            return ldt.atOffset(ZoneOffset.UTC);
        } catch (DateTimeParseException ignored) {
            i++;
        }

        try {
            LocalDate ld = LocalDate.parse(value, ISO_LOCAL_DATE);
            return ld.atStartOfDay().atOffset(ZoneOffset.UTC);
        } catch (DateTimeParseException ignored) {
            i++;
        }

        throw new DateTimeParseException("Unexpected format: " + value, value, i);
    }

    @FromJson
    public LocalDateTime fromJsonToLocalDateTime(String value) {
        OffsetDateTime odt = fromJson(value);
        return odt != null ? odt.toLocalDateTime() : null;
    }

    @FromJson
    public LocalDate fromJsonToLocalDate(String value) {
        OffsetDateTime odt = fromJson(value);
        return odt != null ? odt.toLocalDate() : null;
    }

    // ===================== TO JSON =====================

    @ToJson
    public String toJson(OffsetDateTime value) {
        return value != null ? value.format(ISO_OFFSET) : null;
    }

    @ToJson
    public String toJson(LocalDateTime value) {
        return value != null ? value.atOffset(ZoneOffset.UTC).format(ISO_OFFSET) : null;
    }

    @ToJson
    public String toJson(LocalDate value) {
        return value != null ? value.atStartOfDay().atOffset(ZoneOffset.UTC).format(ISO_OFFSET) : null;
    }
}

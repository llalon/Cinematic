package de.llalon.cinematic.util.json;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Moshi JSON adapter that leniently deserializes date/time strings into Java time types.
 *
 * <p>Attempts to parse the input string in the following order:</p>
 * <ol>
 *     <li>ISO offset date-time (e.g. {@code 2024-01-15T10:30:00Z})</li>
 *     <li>ISO local date-time (e.g. {@code 2024-01-15T10:30:00})</li>
 *     <li>ISO local date (e.g. {@code 2024-01-15})</li>
 * </ol>
 *
 * <p>Local values are normalised to UTC. {@code null} and empty strings produce {@code null}.</p>
 */
public final class LenientDateTimeAdapter {

    private static final DateTimeFormatter ISO_OFFSET = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private static final DateTimeFormatter ISO_LOCAL_DATE_TIME = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final DateTimeFormatter ISO_LOCAL_DATE = DateTimeFormatter.ISO_LOCAL_DATE;

    // ===================== FROM JSON =====================

    /**
     * Deserializes a date/time string to an {@link OffsetDateTime}.
     *
     * @param value the JSON string value
     * @return the parsed {@link OffsetDateTime}, or {@code null} if the value is null or empty
     * @throws java.time.format.DateTimeParseException if the value cannot be parsed in any supported format
     */
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

    /**
     * Deserializes a date/time string to a {@link LocalDateTime}.
     *
     * @param value the JSON string value
     * @return the parsed {@link LocalDateTime}, or {@code null} if the value is null or empty
     */
    @FromJson
    public LocalDateTime fromJsonToLocalDateTime(String value) {
        OffsetDateTime odt = fromJson(value);
        return odt != null ? odt.toLocalDateTime() : null;
    }

    /**
     * Deserializes a date/time string to a {@link LocalDate}.
     *
     * @param value the JSON string value
     * @return the parsed {@link LocalDate}, or {@code null} if the value is null or empty
     */
    @FromJson
    public LocalDate fromJsonToLocalDate(String value) {
        OffsetDateTime odt = fromJson(value);
        return odt != null ? odt.toLocalDate() : null;
    }

    // ===================== TO JSON =====================

    /**
     * Serializes an {@link OffsetDateTime} to an ISO offset date-time string.
     *
     * @param value the value to serialize
     * @return the ISO-formatted string, or {@code null} if the value is {@code null}
     */
    @ToJson
    public String toJson(OffsetDateTime value) {
        return value != null ? value.format(ISO_OFFSET) : null;
    }

    /**
     * Serializes a {@link LocalDateTime} to an ISO offset date-time string (normalised to UTC).
     *
     * @param value the value to serialize
     * @return the ISO-formatted string, or {@code null} if the value is {@code null}
     */
    @ToJson
    public String toJson(LocalDateTime value) {
        return value != null ? value.atOffset(ZoneOffset.UTC).format(ISO_OFFSET) : null;
    }

    /**
     * Serializes a {@link LocalDate} to an ISO offset date-time string (normalised to UTC midnight).
     *
     * @param value the value to serialize
     * @return the ISO-formatted string, or {@code null} if the value is {@code null}
     */
    @ToJson
    public String toJson(LocalDate value) {
        return value != null ? value.atStartOfDay().atOffset(ZoneOffset.UTC).format(ISO_OFFSET) : null;
    }
}

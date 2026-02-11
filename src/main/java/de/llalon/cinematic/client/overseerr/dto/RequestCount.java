package de.llalon.cinematic.client.overseerr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents request counts in Overseerr.
 */
@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestCount {
    private final Integer total;
    private final Integer movie;
    private final Integer tv;
    private final Integer pending;
    private final Integer approved;
    private final Integer declined;
    private final Integer processing;
    private final Integer available;
}

package de.llalon.cinematic.client.seerr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents request counts in Seerr.
 */
@Data
@AllArgsConstructor
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

package de.llalon.cinematic.client.overseerr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Represents request counts in Overseerr.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestCount {
    private Integer total;
    private Integer movie;
    private Integer tv;
    private Integer pending;
    private Integer approved;
    private Integer declined;
    private Integer processing;
    private Integer available;
}

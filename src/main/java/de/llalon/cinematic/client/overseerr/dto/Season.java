package de.llalon.cinematic.client.overseerr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Represents a TV season in Overseerr requests.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Season {
    private final Integer id;

    @JsonProperty("seasonNumber")
    private final Integer seasonNumber;

    private final Integer status;
}

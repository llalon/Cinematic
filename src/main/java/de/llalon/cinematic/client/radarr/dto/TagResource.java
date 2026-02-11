package de.llalon.cinematic.client.radarr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Represents a tag in Radarr.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TagResource {

    @JsonProperty("id")
    private final Integer id;

    @JsonProperty("label")
    private final String label;
}

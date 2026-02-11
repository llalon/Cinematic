package de.llalon.cinematic.client.sonarr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Represents a tag in Sonarr.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TagResource {

    @JsonProperty("id")
    private final Integer id;

    @JsonProperty("label")
    private final String label;
}

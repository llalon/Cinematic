package de.llalon.cinematic.client.overseerr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Represents pagination information in Overseerr API responses.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageInfo {
    private final Integer pages;
    private final Integer pageSize;
    private final Integer results;
    private final Integer page;
}

package de.llalon.cinematic.client.overseerr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents pagination information in Overseerr API responses.
 */
@Data
@AllArgsConstructor
public class PageInfo {
    private final Integer pages;
    private final Integer pageSize;
    private final Integer results;
    private final Integer page;
}

package de.llalon.cinematic.client.plex.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a single Plex identifier entry such as a GUID.
 */
@Data
@AllArgsConstructor
public class PlexId {
    private String id;
}

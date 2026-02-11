package de.llalon.cinematic.client.overseerr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents media information in Overseerr.
 *
 * Status values:
 * 1 = UNKNOWN
 * 2 = PENDING
 * 3 = PROCESSING
 * 4 = PARTIALLY_AVAILABLE
 * 5 = AVAILABLE
 * 6 = DELETED
 */
@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MediaInfo {
    private final Integer id;

    @JsonProperty("tmdbId")
    private final Integer tmdbId;

    @JsonProperty("tvdbId")
    private final Integer tvdbId;

    private final Integer status;

    private final List<MediaRequest> requests;

    @JsonProperty("createdAt")
    private final LocalDateTime createdAt;

    @JsonProperty("updatedAt")
    private final LocalDateTime updatedAt;
}

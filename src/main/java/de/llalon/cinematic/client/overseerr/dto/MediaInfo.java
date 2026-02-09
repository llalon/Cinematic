package de.llalon.cinematic.client.overseerr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class MediaInfo {
    private Integer id;

    @JsonProperty("tmdbId")
    private Integer tmdbId;

    @JsonProperty("tvdbId")
    private Integer tvdbId;

    private Integer status;

    private List<MediaRequest> requests;

    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;
}

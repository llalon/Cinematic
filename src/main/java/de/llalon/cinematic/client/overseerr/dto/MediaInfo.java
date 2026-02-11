package de.llalon.cinematic.client.overseerr.dto;

import com.squareup.moshi.Json;
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
public class MediaInfo {
    private final Integer id;

    @Json(name = "tmdbId")
    private final Integer tmdbId;

    @Json(name = "tvdbId")
    private final Integer tvdbId;

    private final Integer status;

    private final List<MediaRequest> requests;

    @Json(name = "createdAt")
    private final LocalDateTime createdAt;

    @Json(name = "updatedAt")
    private final LocalDateTime updatedAt;
}

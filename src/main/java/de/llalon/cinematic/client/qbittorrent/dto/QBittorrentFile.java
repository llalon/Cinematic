package de.llalon.cinematic.client.qbittorrent.dto;

import com.squareup.moshi.Json;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a file in a torrent.
 *
 * This DTO corresponds to the file info returned by the /api/v2/torrents/files endpoint.
 */
@Data
@AllArgsConstructor
public class QBittorrentFile {

    /** File name (including relative path) */
    private final String name;

    /** File size (bytes) */
    private final Long size;

    /** File progress (percentage/100) */
    @Json(name = "progress")
    private final Float progress;

    /** File priority */
    private final Integer priority;

    /** True if file is priority */
    @Json(name = "is_seed")
    private final Boolean isSeed;

    /** Piece range for the file */
    @Json(name = "piece_range")
    private final List<Integer> pieceRange;

    /** Availability of the file (percentage/100) */
    private final Float availability;
}

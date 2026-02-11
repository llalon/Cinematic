package de.llalon.cinematic.client.qbittorrent.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

/**
 * Represents a file in a torrent.
 *
 * This DTO corresponds to the file info returned by the /api/v2/torrents/files endpoint.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TorrentFile {

    /** File name (including relative path) */
    private final String name;

    /** File size (bytes) */
    private final Long size;

    /** File progress (percentage/100) */
    @JsonProperty("progress")
    private final Float progress;

    /** File priority */
    private final Integer priority;

    /** True if file is priority */
    @JsonProperty("is_seed")
    private final Boolean isSeed;

    /** Piece range for the file */
    @JsonProperty("piece_range")
    private final List<Integer> pieceRange;

    /** Availability of the file (percentage/100) */
    private final Float availability;
}

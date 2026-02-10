package de.llalon.cinematic.client.qbittorrent.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Represents a file in a torrent.
 *
 * This DTO corresponds to the file info returned by the /api/v2/torrents/files endpoint.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TorrentFileDTO {

    /** File name (including relative path) */
    private String name;

    /** File size (bytes) */
    private Long size;

    /** File progress (percentage/100) */
    @JsonProperty("progress")
    private Float progress;

    /** File priority */
    private Integer priority;

    /** True if file is priority */
    @JsonProperty("is_seed")
    private Boolean isSeed;

    /** Piece range for the file */
    @JsonProperty("piece_range")
    private int[] pieceRange;

    /** Availability of the file (percentage/100) */
    private Float availability;
}

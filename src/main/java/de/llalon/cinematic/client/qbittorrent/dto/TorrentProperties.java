package de.llalon.cinematic.client.qbittorrent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Represents generic properties for a torrent in qBittorrent.
 *
 * This DTO corresponds to the torrent properties returned by the /api/v2/torrents/properties endpoint.
 * Contains detailed information about a torrent's configuration and statistics.
 */
@Data
public class TorrentProperties {

    /** Torrent save path */
    @JsonProperty("save_path")
    private String savePath;

    /** Torrent creation date (Unix timestamp) */
    @JsonProperty("creation_date")
    private Long creationDate;

    /** Torrent piece size (bytes) */
    @JsonProperty("piece_size")
    private Long pieceSize;

    /** Torrent comment */
    private String comment;

    /** Total data wasted for torrent (bytes) */
    @JsonProperty("total_wasted")
    private Long totalWasted;

    /** Total data uploaded for torrent (bytes) */
    @JsonProperty("total_uploaded")
    private Long totalUploaded;

    /** Total data uploaded this session (bytes) */
    @JsonProperty("total_uploaded_session")
    private Long totalUploadedSession;

    /** Total data downloaded for torrent (bytes) */
    @JsonProperty("total_downloaded")
    private Long totalDownloaded;

    /** Total data downloaded this session (bytes) */
    @JsonProperty("total_downloaded_session")
    private Long totalDownloadedSession;

    /** Torrent upload limit (bytes/s) */
    @JsonProperty("up_limit")
    private Long upLimit;

    /** Torrent download limit (bytes/s) */
    @JsonProperty("dl_limit")
    private Long dlLimit;

    /** Torrent elapsed time (seconds) */
    @JsonProperty("time_elapsed")
    private Long timeElapsed;

    /** Torrent elapsed time while complete (seconds) */
    @JsonProperty("seeding_time")
    private Long seedingTime;

    /** Torrent connection count */
    @JsonProperty("nb_connections")
    private Integer nbConnections;

    /** Torrent connection count limit */
    @JsonProperty("nb_connections_limit")
    private Integer nbConnectionsLimit;

    /** Torrent share ratio */
    @JsonProperty("share_ratio")
    private Float shareRatio;

    /** When this torrent was added (unix timestamp) */
    @JsonProperty("addition_date")
    private Long additionDate;

    /** Torrent completion date (unix timestamp) */
    @JsonProperty("completion_date")
    private Long completionDate;

    /** Torrent creator */
    @JsonProperty("created_by")
    private String createdBy;

    /** Torrent average download speed (bytes/second) */
    @JsonProperty("dl_speed_avg")
    private Long dlSpeedAvg;

    /** Torrent download speed (bytes/second) */
    @JsonProperty("dl_speed")
    private Long dlSpeed;

    /** Torrent ETA (seconds) */
    private Long eta;

    /** Last seen complete date (unix timestamp) */
    @JsonProperty("last_seen")
    private Long lastSeen;

    /** Number of peers connected to */
    private Integer peers;

    /** Number of peers in the swarm */
    @JsonProperty("peers_total")
    private Integer peersTotal;

    /** Number of pieces owned */
    @JsonProperty("pieces_have")
    private Integer piecesHave;

    /** Number of pieces in the torrent */
    @JsonProperty("pieces_num")
    private Integer piecesNum;

    /** Torrent reannounce (seconds) */
    private Long reannounce;

    /** Number of seeds connected to */
    private Integer seeds;

    /** Number of seeds in the swarm */
    @JsonProperty("seeds_total")
    private Integer seedsTotal;

    /** Total size (bytes) */
    @JsonProperty("total_size")
    private Long totalSize;

    /** Torrent upload speed (bytes/second) */
    @JsonProperty("up_speed")
    private Long upSpeed;

    /** Torrent average upload speed (bytes/second) */
    @JsonProperty("up_speed_avg")
    private Long upSpeedAvg;
}

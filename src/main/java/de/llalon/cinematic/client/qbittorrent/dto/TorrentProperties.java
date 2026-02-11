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
    private final String savePath;

    /** Torrent creation date (Unix timestamp) */
    @JsonProperty("creation_date")
    private final Long creationDate;

    /** Torrent piece size (bytes) */
    @JsonProperty("piece_size")
    private final Long pieceSize;

    /** Torrent comment */
    private final String comment;

    /** Total data wasted for torrent (bytes) */
    @JsonProperty("total_wasted")
    private final Long totalWasted;

    /** Total data uploaded for torrent (bytes) */
    @JsonProperty("total_uploaded")
    private final Long totalUploaded;

    /** Total data uploaded this session (bytes) */
    @JsonProperty("total_uploaded_session")
    private final Long totalUploadedSession;

    /** Total data downloaded for torrent (bytes) */
    @JsonProperty("total_downloaded")
    private final Long totalDownloaded;

    /** Total data downloaded this session (bytes) */
    @JsonProperty("total_downloaded_session")
    private final Long totalDownloadedSession;

    /** Torrent upload limit (bytes/s) */
    @JsonProperty("up_limit")
    private final Long upLimit;

    /** Torrent download limit (bytes/s) */
    @JsonProperty("dl_limit")
    private final Long dlLimit;

    /** Torrent elapsed time (seconds) */
    @JsonProperty("time_elapsed")
    private final Long timeElapsed;

    /** Torrent elapsed time while complete (seconds) */
    @JsonProperty("seeding_time")
    private final Long seedingTime;

    /** Torrent connection count */
    @JsonProperty("nb_connections")
    private final Integer nbConnections;

    /** Torrent connection count limit */
    @JsonProperty("nb_connections_limit")
    private final Integer nbConnectionsLimit;

    /** Torrent share ratio */
    @JsonProperty("share_ratio")
    private final Float shareRatio;

    /** When this torrent was added (unix timestamp) */
    @JsonProperty("addition_date")
    private final Long additionDate;

    /** Torrent completion date (unix timestamp) */
    @JsonProperty("completion_date")
    private final Long completionDate;

    /** Torrent creator */
    @JsonProperty("created_by")
    private final String createdBy;

    /** Torrent average download speed (bytes/second) */
    @JsonProperty("dl_speed_avg")
    private final Long dlSpeedAvg;

    /** Torrent download speed (bytes/second) */
    @JsonProperty("dl_speed")
    private final Long dlSpeed;

    /** Torrent ETA (seconds) */
    private final Long eta;

    /** Last seen complete date (unix timestamp) */
    @JsonProperty("last_seen")
    private final Long lastSeen;

    /** Number of peers connected to */
    private final Integer peers;

    /** Number of peers in the swarm */
    @JsonProperty("peers_total")
    private final Integer peersTotal;

    /** Number of pieces owned */
    @JsonProperty("pieces_have")
    private final Integer piecesHave;

    /** Number of pieces in the torrent */
    @JsonProperty("pieces_num")
    private final Integer piecesNum;

    /** Torrent reannounce (seconds) */
    private final Long reannounce;

    /** Number of seeds connected to */
    private final Integer seeds;

    /** Number of seeds in the swarm */
    @JsonProperty("seeds_total")
    private final Integer seedsTotal;

    /** Total size (bytes) */
    @JsonProperty("total_size")
    private final Long totalSize;

    /** Torrent upload speed (bytes/second) */
    @JsonProperty("up_speed")
    private final Long upSpeed;

    /** Torrent average upload speed (bytes/second) */
    @JsonProperty("up_speed_avg")
    private final Long upSpeedAvg;
}

package de.llalon.cinematic.client.qbittorrent.dto;

import com.squareup.moshi.Json;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents generic properties for a torrent in qBittorrent.
 *
 * This DTO corresponds to the torrent properties returned by the /api/v2/torrents/properties endpoint.
 * Contains detailed information about a torrent's configuration and statistics.
 */
@Data
@AllArgsConstructor
public class TorrentProperties {

    /** Torrent save path */
    @Json(name = "save_path")
    private final String savePath;

    /** Torrent creation date (Unix timestamp) */
    @Json(name = "creation_date")
    private final Long creationDate;

    /** Torrent piece size (bytes) */
    @Json(name = "piece_size")
    private final Long pieceSize;

    /** Torrent comment */
    private final String comment;

    /** Total data wasted for torrent (bytes) */
    @Json(name = "total_wasted")
    private final Long totalWasted;

    /** Total data uploaded for torrent (bytes) */
    @Json(name = "total_uploaded")
    private final Long totalUploaded;

    /** Total data uploaded this session (bytes) */
    @Json(name = "total_uploaded_session")
    private final Long totalUploadedSession;

    /** Total data downloaded for torrent (bytes) */
    @Json(name = "total_downloaded")
    private final Long totalDownloaded;

    /** Total data downloaded this session (bytes) */
    @Json(name = "total_downloaded_session")
    private final Long totalDownloadedSession;

    /** Torrent upload limit (bytes/s) */
    @Json(name = "up_limit")
    private final Long upLimit;

    /** Torrent download limit (bytes/s) */
    @Json(name = "dl_limit")
    private final Long dlLimit;

    /** Torrent elapsed time (seconds) */
    @Json(name = "time_elapsed")
    private final Long timeElapsed;

    /** Torrent elapsed time while complete (seconds) */
    @Json(name = "seeding_time")
    private final Long seedingTime;

    /** Torrent connection count */
    @Json(name = "nb_connections")
    private final Integer nbConnections;

    /** Torrent connection count limit */
    @Json(name = "nb_connections_limit")
    private final Integer nbConnectionsLimit;

    /** Torrent share ratio */
    @Json(name = "share_ratio")
    private final Float shareRatio;

    /** When this torrent was added (unix timestamp) */
    @Json(name = "addition_date")
    private final Long additionDate;

    /** Torrent completion date (unix timestamp) */
    @Json(name = "completion_date")
    private final Long completionDate;

    /** Torrent creator */
    @Json(name = "created_by")
    private final String createdBy;

    /** Torrent average download speed (bytes/second) */
    @Json(name = "dl_speed_avg")
    private final Long dlSpeedAvg;

    /** Torrent download speed (bytes/second) */
    @Json(name = "dl_speed")
    private final Long dlSpeed;

    /** Torrent ETA (seconds) */
    private final Long eta;

    /** Last seen complete date (unix timestamp) */
    @Json(name = "last_seen")
    private final Long lastSeen;

    /** Number of peers connected to */
    private final Integer peers;

    /** Number of peers in the swarm */
    @Json(name = "peers_total")
    private final Integer peersTotal;

    /** Number of pieces owned */
    @Json(name = "pieces_have")
    private final Integer piecesHave;

    /** Number of pieces in the torrent */
    @Json(name = "pieces_num")
    private final Integer piecesNum;

    /** Torrent reannounce (seconds) */
    private final Long reannounce;

    /** Number of seeds connected to */
    private final Integer seeds;

    /** Number of seeds in the swarm */
    @Json(name = "seeds_total")
    private final Integer seedsTotal;

    /** Total size (bytes) */
    @Json(name = "total_size")
    private final Long totalSize;

    /** Torrent upload speed (bytes/second) */
    @Json(name = "up_speed")
    private final Long upSpeed;

    /** Torrent average upload speed (bytes/second) */
    @Json(name = "up_speed_avg")
    private final Long upSpeedAvg;
}

package de.llalon.cinematic.client.qbittorrent.dto;

import com.squareup.moshi.Json;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a torrent entry in qBittorrent.
 *
 * This DTO corresponds to the torrent info returned by the /api/v2/torrents/info endpoint.
 * Contains comprehensive information about a torrent's state, progress, and transfer statistics.
 */
@Data
@AllArgsConstructor
public class TorrentInfo {

    /** Time (Unix Epoch) when the torrent was added to the client */
    @Json(name = "added_on")
    private final Long addedOn;

    /** Amount of data left to download (bytes) */
    @Json(name = "amount_left")
    private final Long amountLeft;

    /** Whether this torrent is managed by Automatic Torrent Management */
    @Json(name = "auto_tmm")
    private final Boolean autoTmm;

    /** Percentage of file pieces currently available */
    private final Float availability;

    /** Category of the torrent */
    private final String category;

    /** Amount of transfer data completed (bytes) */
    private final Long completed;

    /** Time (Unix Epoch) when the torrent completed */
    @Json(name = "completion_on")
    private final Long completionOn;

    /** Absolute path of torrent content (root path for multifile torrents, absolute file path for singlefile torrents) */
    @Json(name = "content_path")
    private final String contentPath;

    /** Torrent download speed limit (bytes/s). -1 if unlimited. */
    @Json(name = "dl_limit")
    private final Long dlLimit;

    /** Torrent download speed (bytes/s) */
    @Json(name = "dlspeed")
    private final Long dlspeed;

    /** Amount of data downloaded */
    private final Long downloaded;

    /** Amount of data downloaded this session */
    @Json(name = "downloaded_session")
    private final Long downloadedSession;

    /** Torrent ETA (seconds) */
    private final Long eta;

    /** True if first last piece are prioritized */
    @Json(name = "f_l_piece_prio")
    private final Boolean flPiecePrio;

    /** True if force start is enabled for this torrent */
    @Json(name = "force_start")
    private final Boolean forceStart;

    /** Torrent hash */
    private final String hash;

    /** True if torrent is from a private final tracker */
    @Json(name = "isPrivate")
    private final Boolean isPrivate;

    /** Last time (Unix Epoch) when a chunk was downloaded/uploaded */
    @Json(name = "last_activity")
    private final Long lastActivity;

    /** Magnet URI corresponding to this torrent */
    @Json(name = "magnet_uri")
    private final String magnetUri;

    /** Maximum share ratio until torrent is stopped from seeding/uploading */
    @Json(name = "max_ratio")
    private final Float maxRatio;

    /** Maximum seeding time (seconds) until torrent is stopped from seeding */
    @Json(name = "max_seeding_time")
    private final Long maxSeedingTime;

    /** Torrent name */
    private final String name;

    /** Number of seeds in the swarm */
    @Json(name = "num_complete")
    private final Integer numComplete;

    /** Number of leechers in the swarm */
    @Json(name = "num_incomplete")
    private final Integer numIncomplete;

    /** Number of leechers connected to */
    @Json(name = "num_leechs")
    private final Integer numLeechs;

    /** Number of seeds connected to */
    @Json(name = "num_seeds")
    private final Integer numSeeds;

    /** Torrent priority. Returns -1 if queuing is disabled or torrent is in seed mode */
    private final Integer priority;

    /** Torrent progress (percentage/100) */
    private final Float progress;

    /** Torrent share ratio. Max ratio value: 9999. */
    private final Float ratio;

    /** Ratio limit */
    @Json(name = "ratio_limit")
    private final Float ratioLimit;

    /** Time until the next tracker reannounce */
    private final Integer reannounce;

    /** Path where this torrent's data is stored */
    @Json(name = "save_path")
    private final String savePath;

    /** Torrent elapsed time while complete (seconds) */
    @Json(name = "seeding_time")
    private final Long seedingTime;

    /** Seeding time limit */
    @Json(name = "seeding_time_limit")
    private final Long seedingTimeLimit;

    /** Time (Unix Epoch) when this torrent was last seen complete */
    @Json(name = "seen_complete")
    private final Long seenComplete;

    /** True if sequential download is enabled */
    @Json(name = "seq_dl")
    private final Boolean seqDl;

    /** Total size (bytes) of files selected for download */
    private final Long size;

    /** Torrent state. See TorrentState enum for possible values */
    private final TorrentState state;

    /** True if super seeding is enabled */
    @Json(name = "super_seeding")
    private final Boolean superSeeding;

    /** Comma-concatenated tag list of the torrent */
    private final String tags;

    /** Total active time (seconds) */
    @Json(name = "time_active")
    private final Long timeActive;

    /** Total size (bytes) of all file in this torrent (including unselected ones) */
    @Json(name = "total_size")
    private final Long totalSize;

    /** The first tracker with working status. Returns empty string if no tracker is working. */
    private final String tracker;

    /** Torrent upload speed limit (bytes/s). -1 if unlimited. */
    @Json(name = "up_limit")
    private final Long upLimit;

    /** Amount of data uploaded */
    private final Long uploaded;

    /** Amount of data uploaded this session */
    @Json(name = "uploaded_session")
    private final Long uploadedSession;

    /** Torrent upload speed (bytes/s) */
    @Json(name = "upspeed")
    private final Long upspeed;
}

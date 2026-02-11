package de.llalon.cinematic.client.qbittorrent.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Represents a torrent entry in qBittorrent.
 *
 * This DTO corresponds to the torrent info returned by the /api/v2/torrents/info endpoint.
 * Contains comprehensive information about a torrent's state, progress, and transfer statistics.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TorrentInfo {

    /** Time (Unix Epoch) when the torrent was added to the client */
    @JsonProperty("added_on")
    private final Long addedOn;

    /** Amount of data left to download (bytes) */
    @JsonProperty("amount_left")
    private final Long amountLeft;

    /** Whether this torrent is managed by Automatic Torrent Management */
    @JsonProperty("auto_tmm")
    private final Boolean autoTmm;

    /** Percentage of file pieces currently available */
    private final Float availability;

    /** Category of the torrent */
    private final String category;

    /** Amount of transfer data completed (bytes) */
    private final Long completed;

    /** Time (Unix Epoch) when the torrent completed */
    @JsonProperty("completion_on")
    private final Long completionOn;

    /** Absolute path of torrent content (root path for multifile torrents, absolute file path for singlefile torrents) */
    @JsonProperty("content_path")
    private final String contentPath;

    /** Torrent download speed limit (bytes/s). -1 if unlimited. */
    @JsonProperty("dl_limit")
    private final Long dlLimit;

    /** Torrent download speed (bytes/s) */
    @JsonProperty("dlspeed")
    private final Long dlspeed;

    /** Amount of data downloaded */
    private final Long downloaded;

    /** Amount of data downloaded this session */
    @JsonProperty("downloaded_session")
    private final Long downloadedSession;

    /** Torrent ETA (seconds) */
    private final Long eta;

    /** True if first last piece are prioritized */
    @JsonProperty("f_l_piece_prio")
    private final Boolean flPiecePrio;

    /** True if force start is enabled for this torrent */
    @JsonProperty("force_start")
    private final Boolean forceStart;

    /** Torrent hash */
    private final String hash;

    /** True if torrent is from a private final tracker */
    @JsonProperty("isPrivate")
    private final Boolean isPrivate;

    /** Last time (Unix Epoch) when a chunk was downloaded/uploaded */
    @JsonProperty("last_activity")
    private final Long lastActivity;

    /** Magnet URI corresponding to this torrent */
    @JsonProperty("magnet_uri")
    private final String magnetUri;

    /** Maximum share ratio until torrent is stopped from seeding/uploading */
    @JsonProperty("max_ratio")
    private final Float maxRatio;

    /** Maximum seeding time (seconds) until torrent is stopped from seeding */
    @JsonProperty("max_seeding_time")
    private final Long maxSeedingTime;

    /** Torrent name */
    private final String name;

    /** Number of seeds in the swarm */
    @JsonProperty("num_complete")
    private final Integer numComplete;

    /** Number of leechers in the swarm */
    @JsonProperty("num_incomplete")
    private final Integer numIncomplete;

    /** Number of leechers connected to */
    @JsonProperty("num_leechs")
    private final Integer numLeechs;

    /** Number of seeds connected to */
    @JsonProperty("num_seeds")
    private final Integer numSeeds;

    /** Torrent priority. Returns -1 if queuing is disabled or torrent is in seed mode */
    private final Integer priority;

    /** Torrent progress (percentage/100) */
    private final Float progress;

    /** Torrent share ratio. Max ratio value: 9999. */
    private final Float ratio;

    /** Ratio limit */
    @JsonProperty("ratio_limit")
    private final Float ratioLimit;

    /** Time until the next tracker reannounce */
    private final Integer reannounce;

    /** Path where this torrent's data is stored */
    @JsonProperty("save_path")
    private final String savePath;

    /** Torrent elapsed time while complete (seconds) */
    @JsonProperty("seeding_time")
    private final Long seedingTime;

    /** Seeding time limit */
    @JsonProperty("seeding_time_limit")
    private final Long seedingTimeLimit;

    /** Time (Unix Epoch) when this torrent was last seen complete */
    @JsonProperty("seen_complete")
    private final Long seenComplete;

    /** True if sequential download is enabled */
    @JsonProperty("seq_dl")
    private final Boolean seqDl;

    /** Total size (bytes) of files selected for download */
    private final Long size;

    /** Torrent state. See TorrentState enum for possible values */
    private final TorrentState state;

    /** True if super seeding is enabled */
    @JsonProperty("super_seeding")
    private final Boolean superSeeding;

    /** Comma-concatenated tag list of the torrent */
    private final String tags;

    /** Total active time (seconds) */
    @JsonProperty("time_active")
    private final Long timeActive;

    /** Total size (bytes) of all file in this torrent (including unselected ones) */
    @JsonProperty("total_size")
    private final Long totalSize;

    /** The first tracker with working status. Returns empty string if no tracker is working. */
    private final String tracker;

    /** Torrent upload speed limit (bytes/s). -1 if unlimited. */
    @JsonProperty("up_limit")
    private final Long upLimit;

    /** Amount of data uploaded */
    private final Long uploaded;

    /** Amount of data uploaded this session */
    @JsonProperty("uploaded_session")
    private final Long uploadedSession;

    /** Torrent upload speed (bytes/s) */
    @JsonProperty("upspeed")
    private final Long upspeed;
}

package de.llalon.cinematic.client.tautulli.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Represents a history entry in Tautulli.
 * Contains details about a completed streaming session.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class History {

    /**
     * Unique reference ID for this history entry.
     */
    @JsonProperty("reference_id")
    private Integer referenceId;

    /**
     * Tautulli row ID (internal database identifier).
     */
    @JsonProperty("row_id")
    private Integer rowId;

    /**
     * Timestamp when the session started (Unix epoch).
     */
    @JsonProperty("date")
    private Long date;

    /**
     * Timestamp when playback started (Unix epoch).
     */
    @JsonProperty("started")
    private Long started;

    /**
     * Timestamp when playback stopped (Unix epoch).
     */
    @JsonProperty("stopped")
    private Long stopped;

    /**
     * User ID of the viewer.
     */
    @JsonProperty("user_id")
    private Integer userId;

    /**
     * Username of the viewer.
     */
    @JsonProperty("user")
    private String user;

    /**
     * Friendly display name of the viewer.
     */
    @JsonProperty("friendly_name")
    private String friendlyName;

    /**
     * Media type: "movie", "episode", "track", "live", etc.
     */
    @JsonProperty("media_type")
    private String mediaType;

    /**
     * Rating key for the media item.
     */
    @JsonProperty("rating_key")
    private String ratingKey;

    /**
     * Parent rating key (for episodes: season; for tracks: album).
     */
    @JsonProperty("parent_rating_key")
    private String parentRatingKey;

    /**
     * Grandparent rating key (for episodes: show; for tracks: artist).
     */
    @JsonProperty("grandparent_rating_key")
    private String grandparentRatingKey;

    /**
     * Title of the media item.
     */
    @JsonProperty("title")
    private String title;

    /**
     * Parent title (season or album name).
     */
    @JsonProperty("parent_title")
    private String parentTitle;

    /**
     * Grandparent title (show or artist name).
     */
    @JsonProperty("grandparent_title")
    private String grandparentTitle;

    /**
     * Original title (if different).
     */
    @JsonProperty("original_title")
    private String originalTitle;

    /**
     * Full formatted title.
     */
    @JsonProperty("full_title")
    private String fullTitle;

    /**
     * Media index (episode number, track number).
     */
    @JsonProperty("media_index")
    private Integer mediaIndex;

    /**
     * Parent media index (season number, disc number).
     */
    @JsonProperty("parent_media_index")
    private Integer parentMediaIndex;

    /**
     * Thumbnail path for the media item.
     */
    @JsonProperty("thumb")
    private String thumb;

    /**
     * Year the media was released.
     */
    @JsonProperty("year")
    private Integer year;

    /**
     * Release date (YYYY-MM-DD).
     */
    @JsonProperty("originally_available_at")
    private String originallyAvailableAt;

    /**
     * Plex GUID for the media item.
     */
    @JsonProperty("guid")
    private String guid;

    /**
     * Platform the user was streaming from.
     */
    @JsonProperty("platform")
    private String platform;

    /**
     * Product name (Plex Web, Plex for iOS, etc.).
     */
    @JsonProperty("product")
    private String product;

    /**
     * Player device name.
     */
    @JsonProperty("player")
    private String player;

    /**
     * IP address of the streaming client.
     */
    @JsonProperty("ip_address")
    private String ipAddress;

    /**
     * Connection location: "lan" or "wan".
     */
    @JsonProperty("location")
    private String location;

    /**
     * Whether the connection was secure (HTTPS).
     */
    @JsonProperty("secure")
    private Integer secure;

    /**
     * Whether the connection was relayed through Plex.
     */
    @JsonProperty("relayed")
    private Integer relayed;

    /**
     * Transcode decision: "transcode", "copy", "direct play".
     */
    @JsonProperty("transcode_decision")
    private String transcodeDecision;

    /**
     * Number of times playback was paused.
     */
    @JsonProperty("paused_counter")
    private Integer pausedCounter;

    /**
     * Percentage of media watched.
     */
    @JsonProperty("percent_complete")
    private Integer percentComplete;

    /**
     * Watched status: 0 = unwatched, 1 = watched.
     */
    @JsonProperty("watched_status")
    private Integer watchedStatus;

    /**
     * Total duration of the media in milliseconds.
     */
    @JsonProperty("duration")
    private Long duration;

    /**
     * Actual playback duration in seconds.
     */
    @JsonProperty("play_duration")
    private Long playDuration;

    /**
     * Library section ID.
     */
    @JsonProperty("section_id")
    private String sectionId;

    /**
     * Plex machine identifier.
     */
    @JsonProperty("machine_id")
    private String machineId;

    /**
     * Session key (if session is still active).
     */
    @JsonProperty("session_key")
    private String sessionKey;

    /**
     * Whether this was a live stream.
     */
    @JsonProperty("live")
    private Integer live;

    /**
     * Current playback state (for active sessions).
     */
    @JsonProperty("state")
    private String state;

    /**
     * Number of items grouped together (if grouping is enabled).
     */
    @JsonProperty("group_count")
    private Integer groupCount;

    /**
     * Comma-separated list of grouped row IDs.
     */
    @JsonProperty("group_ids")
    private String groupIds;
}

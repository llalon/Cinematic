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
    private final Integer referenceId;

    /**
     * Tautulli row ID (internal database identifier).
     */
    @JsonProperty("row_id")
    private final Integer rowId;

    /**
     * Timestamp when the session started (Unix epoch).
     */
    @JsonProperty("date")
    private final Long date;

    /**
     * Timestamp when playback started (Unix epoch).
     */
    @JsonProperty("started")
    private final Long started;

    /**
     * Timestamp when playback stopped (Unix epoch).
     */
    @JsonProperty("stopped")
    private final Long stopped;

    /**
     * User ID of the viewer.
     */
    @JsonProperty("user_id")
    private final Integer userId;

    /**
     * Username of the viewer.
     */
    @JsonProperty("user")
    private final String user;

    /**
     * Friendly display name of the viewer.
     */
    @JsonProperty("friendly_name")
    private final String friendlyName;

    /**
     * Media type: "movie", "episode", "track", "live", etc.
     */
    @JsonProperty("media_type")
    private final String mediaType;

    /**
     * Rating key for the media item.
     */
    @JsonProperty("rating_key")
    private final String ratingKey;

    /**
     * Parent rating key (for episodes: season; for tracks: album).
     */
    @JsonProperty("parent_rating_key")
    private final String parentRatingKey;

    /**
     * Grandparent rating key (for episodes: show; for tracks: artist).
     */
    @JsonProperty("grandparent_rating_key")
    private final String grandparentRatingKey;

    /**
     * Title of the media item.
     */
    @JsonProperty("title")
    private final String title;

    /**
     * Parent title (season or album name).
     */
    @JsonProperty("parent_title")
    private final String parentTitle;

    /**
     * Grandparent title (show or artist name).
     */
    @JsonProperty("grandparent_title")
    private final String grandparentTitle;

    /**
     * Original title (if different).
     */
    @JsonProperty("original_title")
    private final String originalTitle;

    /**
     * Full formatted title.
     */
    @JsonProperty("full_title")
    private final String fullTitle;

    /**
     * Media index (episode number, track number).
     */
    @JsonProperty("media_index")
    private final Integer mediaIndex;

    /**
     * Parent media index (season number, disc number).
     */
    @JsonProperty("parent_media_index")
    private final Integer parentMediaIndex;

    /**
     * Thumbnail path for the media item.
     */
    @JsonProperty("thumb")
    private final String thumb;

    /**
     * Year the media was released.
     */
    @JsonProperty("year")
    private final Integer year;

    /**
     * Release date (YYYY-MM-DD).
     */
    @JsonProperty("originally_available_at")
    private final String originallyAvailableAt;

    /**
     * Plex GUID for the media item.
     */
    @JsonProperty("guid")
    private final String guid;

    /**
     * Platform the user was streaming from.
     */
    @JsonProperty("platform")
    private final String platform;

    /**
     * Product name (Plex Web, Plex for iOS, etc.).
     */
    @JsonProperty("product")
    private final String product;

    /**
     * Player device name.
     */
    @JsonProperty("player")
    private final String player;

    /**
     * IP address of the streaming client.
     */
    @JsonProperty("ip_address")
    private final String ipAddress;

    /**
     * Connection location: "lan" or "wan".
     */
    @JsonProperty("location")
    private final String location;

    /**
     * Whether the connection was secure (HTTPS).
     */
    @JsonProperty("secure")
    private final Integer secure;

    /**
     * Whether the connection was relayed through Plex.
     */
    @JsonProperty("relayed")
    private final Integer relayed;

    /**
     * Transcode decision: "transcode", "copy", "direct play".
     */
    @JsonProperty("transcode_decision")
    private final String transcodeDecision;

    /**
     * Number of times playback was paused.
     */
    @JsonProperty("paused_counter")
    private final Integer pausedCounter;

    /**
     * Percentage of media watched.
     */
    @JsonProperty("percent_complete")
    private final Integer percentComplete;

    /**
     * Watched status: 0 = unwatched, 1 = watched.
     */
    @JsonProperty("watched_status")
    private final Integer watchedStatus;

    /**
     * Total duration of the media in milliseconds.
     */
    @JsonProperty("duration")
    private final Long duration;

    /**
     * Actual playback duration in seconds.
     */
    @JsonProperty("play_duration")
    private final Long playDuration;

    /**
     * Library section ID.
     */
    @JsonProperty("section_id")
    private final String sectionId;

    /**
     * Plex machine identifier.
     */
    @JsonProperty("machine_id")
    private final String machineId;

    /**
     * Session key (if session is still active).
     */
    @JsonProperty("session_key")
    private final String sessionKey;

    /**
     * Whether this was a live stream.
     */
    @JsonProperty("live")
    private final Integer live;

    /**
     * Current playback state (for active sessions).
     */
    @JsonProperty("state")
    private final String state;

    /**
     * Number of items grouped together (if grouping is enabled).
     */
    @JsonProperty("group_count")
    private final Integer groupCount;

    /**
     * Comma-separated list of grouped row IDs.
     */
    @JsonProperty("group_ids")
    private final String groupIds;
}

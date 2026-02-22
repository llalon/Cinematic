package de.llalon.cinematic.client.tautulli.dto;

import com.squareup.moshi.Json;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a history entry in Tautulli.
 * Contains details about a completed streaming session.
 */
@Data
@AllArgsConstructor
public class History {

    /**
     * Unique reference ID for this history entry.
     */
    @Json(name = "reference_id")
    private final Integer referenceId;

    /**
     * Tautulli row ID (internal database identifier).
     */
    @Json(name = "row_id")
    private final Integer rowId;

    /**
     * Timestamp when the session started (Unix epoch).
     */
    @Json(name = "date")
    private final Long date;

    /**
     * Timestamp when playback started (Unix epoch).
     */
    @Json(name = "started")
    private final Long started;

    /**
     * Timestamp when playback stopped (Unix epoch).
     */
    @Json(name = "stopped")
    private final Long stopped;

    /**
     * User ID of the viewer.
     */
    @Json(name = "user_id")
    private final Integer userId;

    /**
     * Username of the viewer.
     */
    @Json(name = "user")
    private final String user;

    /**
     * Friendly display name of the viewer.
     */
    @Json(name = "friendly_name")
    private final String friendlyName;

    /**
     * Media type: "movie", "episode", "track", "live", etc.
     */
    @Json(name = "media_type")
    private final String mediaType;

    /**
     * Rating key for the media item.
     */
    @Json(name = "rating_key")
    private final String ratingKey;

    /**
     * Parent rating key (for episodes: season; for tracks: album).
     */
    @Json(name = "parent_rating_key")
    private final String parentRatingKey;

    /**
     * Grandparent rating key (for episodes: show; for tracks: artist).
     */
    @Json(name = "grandparent_rating_key")
    private final String grandparentRatingKey;

    /**
     * Title of the media item.
     */
    @Json(name = "title")
    private final String title;

    /**
     * Parent title (season or album name).
     */
    @Json(name = "parent_title")
    private final String parentTitle;

    /**
     * Grandparent title (show or artist name).
     */
    @Json(name = "grandparent_title")
    private final String grandparentTitle;

    /**
     * Original title (if different).
     */
    @Json(name = "original_title")
    private final String originalTitle;

    /**
     * Full formatted title.
     */
    @Json(name = "full_title")
    private final String fullTitle;

    /**
     * Media index (episode number, track number).
     */
    @Json(name = "media_index")
    private final String mediaIndex;

    /**
     * Parent media index (season number, disc number).
     */
    @Json(name = "parent_media_index")
    private final String parentMediaIndex;

    /**
     * Thumbnail path for the media item.
     */
    @Json(name = "thumb")
    private final String thumb;

    /**
     * Year the media was released.
     */
    @Json(name = "year")
    private final Integer year;

    /**
     * Release date (YYYY-MM-DD).
     */
    @Json(name = "originally_available_at")
    private final String originallyAvailableAt;

    /**
     * Plex GUID for the media item.
     */
    @Json(name = "guid")
    private final String guid;

    /**
     * Platform the user was streaming from.
     */
    @Json(name = "platform")
    private final String platform;

    /**
     * Product name (Plex Web, Plex for iOS, etc.).
     */
    @Json(name = "product")
    private final String product;

    /**
     * Player device name.
     */
    @Json(name = "player")
    private final String player;

    /**
     * IP address of the streaming client.
     */
    @Json(name = "ip_address")
    private final String ipAddress;

    /**
     * Connection location: "lan" or "wan".
     */
    @Json(name = "location")
    private final String location;

    /**
     * Whether the connection was secure (HTTPS).
     */
    @Json(name = "secure")
    private final Integer secure;

    /**
     * Whether the connection was relayed through Plex.
     */
    @Json(name = "relayed")
    private final Integer relayed;

    /**
     * Transcode decision: "transcode", "copy", "direct play".
     */
    @Json(name = "transcode_decision")
    private final String transcodeDecision;

    /**
     * Number of times playback was paused.
     */
    @Json(name = "paused_counter")
    private final Integer pausedCounter;

    /**
     * Percentage of media watched.
     */
    @Json(name = "percent_complete")
    private final Integer percentComplete;

    /**
     * Watched status: 0 = unwatched, 1 = watched.
     */
    @Json(name = "watched_status")
    private final Float watchedStatus;

    /**
     * Total duration of the media in milliseconds.
     */
    @Json(name = "duration")
    private final Long duration;

    /**
     * Actual playback duration in seconds.
     */
    @Json(name = "play_duration")
    private final Long playDuration;

    /**
     * Library section ID.
     */
    @Json(name = "section_id")
    private final String sectionId;

    /**
     * Plex machine identifier.
     */
    @Json(name = "machine_id")
    private final String machineId;

    /**
     * Session key (if session is still active).
     */
    @Json(name = "session_key")
    private final String sessionKey;

    /**
     * Whether this was a live stream.
     */
    @Json(name = "live")
    private final Integer live;

    /**
     * Current playback state (for active sessions).
     */
    @Json(name = "state")
    private final String state;

    /**
     * Number of items grouped together (if grouping is enabled).
     */
    @Json(name = "group_count")
    private final Integer groupCount;

    /**
     * Comma-separated list of grouped row IDs.
     */
    @Json(name = "group_ids")
    private final String groupIds;
}

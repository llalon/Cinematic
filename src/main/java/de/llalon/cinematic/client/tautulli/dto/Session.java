package de.llalon.cinematic.client.tautulli.dto;

import com.squareup.moshi.Json;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents an active streaming session in Tautulli.
 * Contains detailed information about what is being watched, by whom, and on what device.
 */
@Data
@AllArgsConstructor
public class Session {

    /**
     * Unique session key for this streaming session.
     */
    @Json(name = "session_key")
    private final String sessionKey;

    /**
     * Session ID.
     */
    @Json(name = "session_id")
    private final String sessionId;

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
     * User's avatar/thumbnail URL.
     */
    @Json(name = "user_thumb")
    private final String userThumb;

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
     * Platform the user is streaming from (Windows, Android, iOS, etc.).
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
     * Whether the connection is secure (HTTPS).
     */
    @Json(name = "secure")
    private final Integer secure;

    /**
     * Whether the connection is relayed through Plex.
     */
    @Json(name = "relayed")
    private final Integer relayed;

    /**
     * Transcode decision: "transcode", "copy", "direct play".
     */
    @Json(name = "transcode_decision")
    private final String transcodeDecision;

    /**
     * Current state: "playing", "paused", "buffering".
     */
    @Json(name = "state")
    private final String state;

    /**
     * Current playback position in milliseconds.
     */
    @Json(name = "view_offset")
    private final Long viewOffset;

    /**
     * Total duration of the media in milliseconds.
     */
    @Json(name = "duration")
    private final Long duration;

    /**
     * Percentage of media watched.
     */
    @Json(name = "percent_complete")
    private final Integer percentComplete;

    /**
     * Bandwidth usage in kbps.
     */
    @Json(name = "bandwidth")
    private final Integer bandwidth;

    /**
     * Video resolution (e.g., "1080", "720", "4k").
     */
    @Json(name = "video_resolution")
    private final String videoResolution;

    /**
     * Video codec (e.g., "h264", "hevc").
     */
    @Json(name = "video_codec")
    private final String videoCodec;

    /**
     * Audio codec (e.g., "aac", "ac3").
     */
    @Json(name = "audio_codec")
    private final String audioCodec;

    /**
     * Audio channels (e.g., "2.0", "5.1", "7.1").
     */
    @Json(name = "audio_channels")
    private final String audioChannels;

    /**
     * Stream container format (e.g., "mkv", "mp4").
     */
    @Json(name = "container")
    private final String container;

    /**
     * Library section ID.
     */
    @Json(name = "section_id")
    private final String sectionId;

    /**
     * Plex GUID for the media item.
     */
    @Json(name = "guid")
    private final String guid;

    /**
     * List of actors in the media.
     */
    @Json(name = "actors")
    private final List<String> actors;

    /**
     * List of directors.
     */
    @Json(name = "directors")
    private final List<String> directors;

    /**
     * List of writers.
     */
    @Json(name = "writers")
    private final List<String> writers;

    /**
     * List of genres.
     */
    @Json(name = "genres")
    private final List<String> genres;
}

package de.llalon.cinematic.client.tautulli.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

/**
 * Represents an active streaming session in Tautulli.
 * Contains detailed information about what is being watched, by whom, and on what device.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Session {

    /**
     * Unique session key for this streaming session.
     */
    @JsonProperty("session_key")
    private final String sessionKey;

    /**
     * Session ID.
     */
    @JsonProperty("session_id")
    private final String sessionId;

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
     * User's avatar/thumbnail URL.
     */
    @JsonProperty("user_thumb")
    private final String userThumb;

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
     * Platform the user is streaming from (Windows, Android, iOS, etc.).
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
     * Whether the connection is secure (HTTPS).
     */
    @JsonProperty("secure")
    private final Integer secure;

    /**
     * Whether the connection is relayed through Plex.
     */
    @JsonProperty("relayed")
    private final Integer relayed;

    /**
     * Transcode decision: "transcode", "copy", "direct play".
     */
    @JsonProperty("transcode_decision")
    private final String transcodeDecision;

    /**
     * Current state: "playing", "paused", "buffering".
     */
    @JsonProperty("state")
    private final String state;

    /**
     * Current playback position in milliseconds.
     */
    @JsonProperty("view_offset")
    private final Long viewOffset;

    /**
     * Total duration of the media in milliseconds.
     */
    @JsonProperty("duration")
    private final Long duration;

    /**
     * Percentage of media watched.
     */
    @JsonProperty("percent_complete")
    private final Integer percentComplete;

    /**
     * Bandwidth usage in kbps.
     */
    @JsonProperty("bandwidth")
    private final Integer bandwidth;

    /**
     * Video resolution (e.g., "1080", "720", "4k").
     */
    @JsonProperty("video_resolution")
    private final String videoResolution;

    /**
     * Video codec (e.g., "h264", "hevc").
     */
    @JsonProperty("video_codec")
    private final String videoCodec;

    /**
     * Audio codec (e.g., "aac", "ac3").
     */
    @JsonProperty("audio_codec")
    private final String audioCodec;

    /**
     * Audio channels (e.g., "2.0", "5.1", "7.1").
     */
    @JsonProperty("audio_channels")
    private final String audioChannels;

    /**
     * Stream container format (e.g., "mkv", "mp4").
     */
    @JsonProperty("container")
    private final String container;

    /**
     * Library section ID.
     */
    @JsonProperty("section_id")
    private final String sectionId;

    /**
     * Plex GUID for the media item.
     */
    @JsonProperty("guid")
    private final String guid;

    /**
     * List of actors in the media.
     */
    @JsonProperty("actors")
    private final List<String> actors;

    /**
     * List of directors.
     */
    @JsonProperty("directors")
    private final List<String> directors;

    /**
     * List of writers.
     */
    @JsonProperty("writers")
    private final List<String> writers;

    /**
     * List of genres.
     */
    @JsonProperty("genres")
    private final List<String> genres;
}

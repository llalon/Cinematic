package de.llalon.cinematic.client.tautulli.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

/**
 * Represents a Plex user in Tautulli.
 * Contains user account information, permissions, and sharing details.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    /**
     * Unique Plex user ID.
     */
    @JsonProperty("user_id")
    private Integer userId;

    /**
     * Plex username.
     */
    @JsonProperty("username")
    private String username;

    /**
     * Friendly display name.
     */
    @JsonProperty("friendly_name")
    private String friendlyName;

    /**
     * User's email address.
     */
    @JsonProperty("email")
    private String email;

    /**
     * User's avatar/thumbnail URL.
     */
    @JsonProperty("user_thumb")
    private String userThumb;

    /**
     * Alternative thumbnail field name.
     */
    @JsonProperty("thumb")
    private String thumb;

    /**
     * Whether the user is an admin.
     */
    @JsonProperty("is_admin")
    private Integer isAdmin;

    /**
     * Whether the user is a home user.
     */
    @JsonProperty("is_home_user")
    private Integer isHomeUser;

    /**
     * Whether the user is restricted (managed user).
     */
    @JsonProperty("is_restricted")
    private Integer isRestricted;

    /**
     * Whether the user is currently active.
     */
    @JsonProperty("is_active")
    private Integer isActive;

    /**
     * Whether to keep playback history for this user.
     */
    @JsonProperty("keep_history")
    private Integer keepHistory;

    /**
     * Whether to send notifications for this user.
     */
    @JsonProperty("do_notify")
    private Integer doNotify;

    /**
     * Whether to allow guest access.
     */
    @JsonProperty("allow_guest")
    private Integer allowGuest;

    /**
     * Whether the user is allowed to sync content.
     */
    @JsonProperty("is_allow_sync")
    private Integer isAllowSync;

    /**
     * List of shared library section IDs.
     */
    @JsonProperty("shared_libraries")
    private List<String> sharedLibraries;

    /**
     * Filter settings for all media types.
     */
    @JsonProperty("filter_all")
    private String filterAll;

    /**
     * Filter settings for movies.
     */
    @JsonProperty("filter_movies")
    private String filterMovies;

    /**
     * Filter settings for TV shows.
     */
    @JsonProperty("filter_tv")
    private String filterTv;

    /**
     * Filter settings for music.
     */
    @JsonProperty("filter_music")
    private String filterMusic;

    /**
     * Filter settings for photos.
     */
    @JsonProperty("filter_photos")
    private String filterPhotos;

    /**
     * Tautulli row ID (internal database identifier).
     */
    @JsonProperty("row_id")
    private Integer rowId;

    /**
     * Timestamp of when user was last seen (Unix epoch).
     */
    @JsonProperty("last_seen")
    private Long lastSeen;

    /**
     * Whether this is a deleted user.
     */
    @JsonProperty("deleted_user")
    private Integer deletedUser;
}

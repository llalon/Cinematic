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
    private final Integer userId;

    /**
     * Plex username.
     */
    @JsonProperty("username")
    private final String username;

    /**
     * Friendly display name.
     */
    @JsonProperty("friendly_name")
    private final String friendlyName;

    /**
     * User's email address.
     */
    @JsonProperty("email")
    private final String email;

    /**
     * User's avatar/thumbnail URL.
     */
    @JsonProperty("user_thumb")
    private final String userThumb;

    /**
     * Alternative thumbnail field name.
     */
    @JsonProperty("thumb")
    private final String thumb;

    /**
     * Whether the user is an admin.
     */
    @JsonProperty("is_admin")
    private final Integer isAdmin;

    /**
     * Whether the user is a home user.
     */
    @JsonProperty("is_home_user")
    private final Integer isHomeUser;

    /**
     * Whether the user is restricted (managed user).
     */
    @JsonProperty("is_restricted")
    private final Integer isRestricted;

    /**
     * Whether the user is currently active.
     */
    @JsonProperty("is_active")
    private final Integer isActive;

    /**
     * Whether to keep playback history for this user.
     */
    @JsonProperty("keep_history")
    private final Integer keepHistory;

    /**
     * Whether to send notifications for this user.
     */
    @JsonProperty("do_notify")
    private final Integer doNotify;

    /**
     * Whether to allow guest access.
     */
    @JsonProperty("allow_guest")
    private final Integer allowGuest;

    /**
     * Whether the user is allowed to sync content.
     */
    @JsonProperty("is_allow_sync")
    private final Integer isAllowSync;

    /**
     * List of shared library section IDs.
     */
    @JsonProperty("shared_libraries")
    private final List<String> sharedLibraries;

    /**
     * Filter settings for all media types.
     */
    @JsonProperty("filter_all")
    private final String filterAll;

    /**
     * Filter settings for movies.
     */
    @JsonProperty("filter_movies")
    private final String filterMovies;

    /**
     * Filter settings for TV shows.
     */
    @JsonProperty("filter_tv")
    private final String filterTv;

    /**
     * Filter settings for music.
     */
    @JsonProperty("filter_music")
    private final String filterMusic;

    /**
     * Filter settings for photos.
     */
    @JsonProperty("filter_photos")
    private final String filterPhotos;

    /**
     * Tautulli row ID (internal database identifier).
     */
    @JsonProperty("row_id")
    private final Integer rowId;

    /**
     * Timestamp of when user was last seen (Unix epoch).
     */
    @JsonProperty("last_seen")
    private final Long lastSeen;

    /**
     * Whether this is a deleted user.
     */
    @JsonProperty("deleted_user")
    private final Integer deletedUser;
}

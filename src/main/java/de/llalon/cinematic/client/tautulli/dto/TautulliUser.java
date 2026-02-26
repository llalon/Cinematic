package de.llalon.cinematic.client.tautulli.dto;

import com.squareup.moshi.Json;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a Plex user in Tautulli.
 * Contains user account information, permissions, and sharing details.
 */
@Data
@AllArgsConstructor
public class TautulliUser {

    /**
     * Unique Plex user ID.
     */
    @Json(name = "user_id")
    private final Integer userId;

    /**
     * Plex username.
     */
    @Json(name = "username")
    private final String username;

    /**
     * Friendly display name.
     */
    @Json(name = "friendly_name")
    private final String friendlyName;

    /**
     * User's email address.
     */
    @Json(name = "email")
    private final String email;

    /**
     * User's avatar/thumbnail URL.
     */
    @Json(name = "user_thumb")
    private final String userThumb;

    /**
     * Alternative thumbnail field name.
     */
    @Json(name = "thumb")
    private final String thumb;

    /**
     * Whether the user is an admin.
     */
    @Json(name = "is_admin")
    private final Integer isAdmin;

    /**
     * Whether the user is a home user.
     */
    @Json(name = "is_home_user")
    private final Integer isHomeUser;

    /**
     * Whether the user is restricted (managed user).
     */
    @Json(name = "is_restricted")
    private final Integer isRestricted;

    /**
     * Whether the user is currently active.
     */
    @Json(name = "is_active")
    private final Integer isActive;

    /**
     * Whether to keep playback history for this user.
     */
    @Json(name = "keep_history")
    private final Integer keepHistory;

    /**
     * Whether to send notifications for this user.
     */
    @Json(name = "do_notify")
    private final Integer doNotify;

    /**
     * Whether to allow guest access.
     */
    @Json(name = "allow_guest")
    private final Integer allowGuest;

    /**
     * Whether the user is allowed to sync content.
     */
    @Json(name = "is_allow_sync")
    private final Integer isAllowSync;

    /**
     * List of shared library section IDs.
     */
    @Json(name = "shared_libraries")
    private final List<String> sharedLibraries;

    /**
     * Filter settings for all media types.
     */
    @Json(name = "filter_all")
    private final String filterAll;

    /**
     * Filter settings for movies.
     */
    @Json(name = "filter_movies")
    private final String filterMovies;

    /**
     * Filter settings for TV shows.
     */
    @Json(name = "filter_tv")
    private final String filterTv;

    /**
     * Filter settings for music.
     */
    @Json(name = "filter_music")
    private final String filterMusic;

    /**
     * Filter settings for photos.
     */
    @Json(name = "filter_photos")
    private final String filterPhotos;

    /**
     * Tautulli row ID (internal database identifier).
     */
    @Json(name = "row_id")
    private final Integer rowId;

    /**
     * Timestamp of when user was last seen (Unix epoch).
     */
    @Json(name = "last_seen")
    private final Long lastSeen;

    /**
     * Whether this is a deleted user.
     */
    @Json(name = "deleted_user")
    private final Integer deletedUser;
}

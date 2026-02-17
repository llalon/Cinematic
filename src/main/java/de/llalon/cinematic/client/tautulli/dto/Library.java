package de.llalon.cinematic.client.tautulli.dto;

import com.squareup.moshi.Json;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a Plex library section in Tautulli.
 * Contains library metadata, statistics, and configuration.
 */
@Data
@AllArgsConstructor
public class Library {

    /**
     * Unique library section ID.
     */
    @Json(name = "section_id")
    private final String sectionId;

    /**
     * Plex server identifier.
     */
    @Json(name = "server_id")
    private final String serverId;

    /**
     * Library name/title.
     */
    @Json(name = "section_name")
    private final String sectionName;

    /**
     * Library type: "movie", "show", "artist", "photo".
     */
    @Json(name = "section_type")
    private final String sectionType;

    /**
     * Library thumbnail path.
     */
    @Json(name = "library_thumb")
    private final String libraryThumb;

    /**
     * Library background art path.
     */
    @Json(name = "library_art")
    private final String libraryArt;

    /**
     * Alternative art field.
     */
    @Json(name = "art")
    private final String art;

    /**
     * Alternative thumb field.
     */
    @Json(name = "thumb")
    private final String thumb;

    /**
     * Total number of items in the library.
     */
    @Json(name = "count")
    private final Integer count;

    /**
     * Number of parent items (shows, albums).
     */
    @Json(name = "parent_count")
    private final Integer parentCount;

    /**
     * Number of child items (episodes, tracks).
     */
    @Json(name = "child_count")
    private final Integer childCount;

    /**
     * Whether the library is currently active.
     */
    @Json(name = "is_active")
    private final Integer isActive;

    /**
     * Whether to keep history for this library.
     */
    @Json(name = "keep_history")
    private final Integer keepHistory;

    /**
     * Whether to send notifications when items are played.
     */
    @Json(name = "do_notify")
    private final Integer doNotify;

    /**
     * Whether to send notifications when new items are added.
     */
    @Json(name = "do_notify_created")
    private final Integer doNotifyCreated;

    /**
     * Timestamp of when the library was last accessed (Unix epoch).
     */
    @Json(name = "last_accessed")
    private final Long lastAccessed;

    /**
     * Title of the last played item from this library.
     */
    @Json(name = "last_played")
    private final String lastPlayed;

    /**
     * Total number of plays from this library.
     */
    @Json(name = "plays")
    private final Integer plays;

    /**
     * Total watch time in seconds.
     */
    @Json(name = "duration")
    private final Long duration;

    /**
     * Tautulli row ID (internal database identifier).
     */
    @Json(name = "row_id")
    private final Integer rowId;

    /**
     * Whether this is a deleted library section.
     */
    @Json(name = "deleted_section")
    private final Integer deletedSection;
}

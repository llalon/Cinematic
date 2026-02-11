package de.llalon.cinematic.client.tautulli.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Represents a Plex library section in Tautulli.
 * Contains library metadata, statistics, and configuration.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Library {

    /**
     * Unique library section ID.
     */
    @JsonProperty("section_id")
    private final String sectionId;

    /**
     * Plex server identifier.
     */
    @JsonProperty("server_id")
    private final String serverId;

    /**
     * Library name/title.
     */
    @JsonProperty("section_name")
    private final String sectionName;

    /**
     * Library type: "movie", "show", "artist", "photo".
     */
    @JsonProperty("section_type")
    private final String sectionType;

    /**
     * Library thumbnail path.
     */
    @JsonProperty("library_thumb")
    private final String libraryThumb;

    /**
     * Library background art path.
     */
    @JsonProperty("library_art")
    private final String libraryArt;

    /**
     * Alternative art field.
     */
    @JsonProperty("art")
    private final String art;

    /**
     * Alternative thumb field.
     */
    @JsonProperty("thumb")
    private final String thumb;

    /**
     * Total number of items in the library.
     */
    @JsonProperty("count")
    private final Integer count;

    /**
     * Number of parent items (shows, albums).
     */
    @JsonProperty("parent_count")
    private final Integer parentCount;

    /**
     * Number of child items (episodes, tracks).
     */
    @JsonProperty("child_count")
    private final Integer childCount;

    /**
     * Whether the library is currently active.
     */
    @JsonProperty("is_active")
    private final Integer isActive;

    /**
     * Whether to keep history for this library.
     */
    @JsonProperty("keep_history")
    private final Integer keepHistory;

    /**
     * Whether to send notifications when items are played.
     */
    @JsonProperty("do_notify")
    private final Integer doNotify;

    /**
     * Whether to send notifications when new items are added.
     */
    @JsonProperty("do_notify_created")
    private final Integer doNotifyCreated;

    /**
     * Timestamp of when the library was last accessed (Unix epoch).
     */
    @JsonProperty("last_accessed")
    private final Long lastAccessed;

    /**
     * Title of the last played item from this library.
     */
    @JsonProperty("last_played")
    private final String lastPlayed;

    /**
     * Total number of plays from this library.
     */
    @JsonProperty("plays")
    private final Integer plays;

    /**
     * Total watch time in seconds.
     */
    @JsonProperty("duration")
    private final Long duration;

    /**
     * Tautulli row ID (internal database identifier).
     */
    @JsonProperty("row_id")
    private final Integer rowId;

    /**
     * Whether this is a deleted library section.
     */
    @JsonProperty("deleted_section")
    private final Integer deletedSection;
}

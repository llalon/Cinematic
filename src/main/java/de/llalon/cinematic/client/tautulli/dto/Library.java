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
    private String sectionId;

    /**
     * Plex server identifier.
     */
    @JsonProperty("server_id")
    private String serverId;

    /**
     * Library name/title.
     */
    @JsonProperty("section_name")
    private String sectionName;

    /**
     * Library type: "movie", "show", "artist", "photo".
     */
    @JsonProperty("section_type")
    private String sectionType;

    /**
     * Library thumbnail path.
     */
    @JsonProperty("library_thumb")
    private String libraryThumb;

    /**
     * Library background art path.
     */
    @JsonProperty("library_art")
    private String libraryArt;

    /**
     * Alternative art field.
     */
    @JsonProperty("art")
    private String art;

    /**
     * Alternative thumb field.
     */
    @JsonProperty("thumb")
    private String thumb;

    /**
     * Total number of items in the library.
     */
    @JsonProperty("count")
    private Integer count;

    /**
     * Number of parent items (shows, albums).
     */
    @JsonProperty("parent_count")
    private Integer parentCount;

    /**
     * Number of child items (episodes, tracks).
     */
    @JsonProperty("child_count")
    private Integer childCount;

    /**
     * Whether the library is currently active.
     */
    @JsonProperty("is_active")
    private Integer isActive;

    /**
     * Whether to keep history for this library.
     */
    @JsonProperty("keep_history")
    private Integer keepHistory;

    /**
     * Whether to send notifications when items are played.
     */
    @JsonProperty("do_notify")
    private Integer doNotify;

    /**
     * Whether to send notifications when new items are added.
     */
    @JsonProperty("do_notify_created")
    private Integer doNotifyCreated;

    /**
     * Timestamp of when the library was last accessed (Unix epoch).
     */
    @JsonProperty("last_accessed")
    private Long lastAccessed;

    /**
     * Title of the last played item from this library.
     */
    @JsonProperty("last_played")
    private String lastPlayed;

    /**
     * Total number of plays from this library.
     */
    @JsonProperty("plays")
    private Integer plays;

    /**
     * Total watch time in seconds.
     */
    @JsonProperty("duration")
    private Long duration;

    /**
     * Tautulli row ID (internal database identifier).
     */
    @JsonProperty("row_id")
    private Integer rowId;

    /**
     * Whether this is a deleted library section.
     */
    @JsonProperty("deleted_section")
    private Integer deletedSection;
}

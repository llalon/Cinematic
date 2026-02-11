package de.llalon.cinematic.client.overseerr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * Represents a media request in Overseerr.
 *
 * Status values:
 * 1 = PENDING APPROVAL
 * 2 = APPROVED
 * 3 = DECLINED
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MediaRequest {
    private final Integer id;

    /**
     * Status of the request.
     * 1 = PENDING APPROVAL
     * 2 = APPROVED
     * 3 = DECLINED
     */
    private final Integer status;

    private final MediaInfo media;

    @JsonProperty("createdAt")
    private final LocalDateTime createdAt;

    @JsonProperty("updatedAt")
    private final LocalDateTime updatedAt;

    @JsonProperty("requestedBy")
    private final User requestedBy;

    @JsonProperty("modifiedBy")
    private final User modifiedBy;

    @JsonProperty("is4k")
    private final Boolean is4k;

    @JsonProperty("serverId")
    private final Integer serverId;

    @JsonProperty("profileId")
    private final Integer profileId;

    @JsonProperty("rootFolder")
    private final String rootFolder;

    @JsonProperty("mediaType")
    private final Integer mediaType;

    @JsonProperty("mediaId")
    private final Integer mediaId;

    @JsonProperty("tvdbId")
    private final String tvdbId;

    private final List<Season> seasons;
}

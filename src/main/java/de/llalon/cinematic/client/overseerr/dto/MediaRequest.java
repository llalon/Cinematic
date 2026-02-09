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
    private Integer id;

    /**
     * Status of the request.
     * 1 = PENDING APPROVAL
     * 2 = APPROVED
     * 3 = DECLINED
     */
    private Integer status;

    private MediaInfo media;

    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;

    @JsonProperty("requestedBy")
    private User requestedBy;

    @JsonProperty("modifiedBy")
    private User modifiedBy;

    @JsonProperty("is4k")
    private Boolean is4k;

    @JsonProperty("serverId")
    private Integer serverId;

    @JsonProperty("profileId")
    private Integer profileId;

    @JsonProperty("rootFolder")
    private String rootFolder;

    @JsonProperty("mediaType")
    private Integer mediaType;

    @JsonProperty("mediaId")
    private Integer mediaId;

    @JsonProperty("tvdbId")
    private String tvdbId;

    private List<Season> seasons;
}

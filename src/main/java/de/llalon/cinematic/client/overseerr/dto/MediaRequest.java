package de.llalon.cinematic.client.overseerr.dto;

import com.squareup.moshi.Json;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
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

    @Json(name = "createdAt")
    private final LocalDateTime createdAt;

    @Json(name = "updatedAt")
    private final LocalDateTime updatedAt;

    @Json(name = "requestedBy")
    private final User requestedBy;

    @Json(name = "modifiedBy")
    private final User modifiedBy;

    @Json(name = "is4k")
    private final Boolean is4k;

    @Json(name = "serverId")
    private final Integer serverId;

    @Json(name = "profileId")
    private final Integer profileId;

    @Json(name = "rootFolder")
    private final String rootFolder;

    @Json(name = "mediaType")
    private final Integer mediaType;

    @Json(name = "mediaId")
    private final Integer mediaId;

    @Json(name = "tvdbId")
    private final String tvdbId;

    private final List<Season> seasons;
}

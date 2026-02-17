package de.llalon.cinematic.client.overseerr.dto;

import com.squareup.moshi.Json;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Request body for creating a new media request in Overseerr.
 */
@Data
@AllArgsConstructor
public class CreateRequestBody {
    @Json(name = "mediaType")
    private final String mediaType; // "movie" or "tv"

    @Json(name = "mediaId")
    private final Integer mediaId;

    @Json(name = "tvdbId")
    private final String tvdbId;

    /**
     * List of season numbers to request.
     * For the request body, use simple Strings representing season numbers.
     */
    private final List<String> seasons; // or "all" string

    @Json(name = "is4k")
    private final Boolean is4k;

    @Json(name = "serverId")
    private final String serverId;

    @Json(name = "profileId")
    private final String profileId;

    @Json(name = "rootFolder")
    private final String rootFolder;

    @Json(name = "languageProfileId")
    private final String languageProfileId;

    @Json(name = "userId")
    private final String userId;
}

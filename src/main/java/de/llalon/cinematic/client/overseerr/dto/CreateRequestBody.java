package de.llalon.cinematic.client.overseerr.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * Request body for creating a new media request in Overseerr.
 */
@Data
@Builder
public class CreateRequestBody {
    @JsonProperty("mediaType")
    private final String mediaType; // "movie" or "tv"

    @JsonProperty("mediaId")
    private final Integer mediaId;

    @JsonProperty("tvdbId")
    private final String tvdbId;

    /**
     * List of season numbers to request.
     * For the request body, use simple Strings representing season numbers.
     */
    private final List<String> seasons; // or "all" string

    @JsonProperty("is4k")
    private final Boolean is4k;

    @JsonProperty("serverId")
    private final String serverId;

    @JsonProperty("profileId")
    private final String profileId;

    @JsonProperty("rootFolder")
    private final String rootFolder;

    @JsonProperty("languageProfileId")
    private final String languageProfileId;

    @JsonProperty("userId")
    private final String userId;
}

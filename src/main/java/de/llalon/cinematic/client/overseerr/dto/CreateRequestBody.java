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
    private String mediaType; // "movie" or "tv"

    @JsonProperty("mediaId")
    private Integer mediaId;

    @JsonProperty("tvdbId")
    private String tvdbId;

    /**
     * List of season numbers to request.
     * For the request body, use simple Strings representing season numbers.
     */
    private List<String> seasons; // or "all" string

    @JsonProperty("is4k")
    private Boolean is4k;

    @JsonProperty("serverId")
    private String serverId;

    @JsonProperty("profileId")
    private String profileId;

    @JsonProperty("rootFolder")
    private String rootFolder;

    @JsonProperty("languageProfileId")
    private String languageProfileId;

    @JsonProperty("userId")
    private String userId;
}

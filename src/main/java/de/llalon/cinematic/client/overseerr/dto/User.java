package de.llalon.cinematic.client.overseerr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a user in Overseerr.
 */
@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private final Integer id;
    private final String email;
    private final String username;

    @JsonProperty("plexToken")
    private final String plexToken;

    @JsonProperty("plexUsername")
    private final String plexUsername;

    @JsonProperty("userType")
    private final Integer userType;

    private final Long permissions;
    private final String avatar;

    @JsonProperty("createdAt")
    private final LocalDateTime createdAt;

    @JsonProperty("updatedAt")
    private final LocalDateTime updatedAt;

    @JsonProperty("requestCount")
    private final Integer requestCount;
}

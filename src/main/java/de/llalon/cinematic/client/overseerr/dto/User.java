package de.llalon.cinematic.client.overseerr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * Represents a user in Overseerr.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private Integer id;
    private String email;
    private String username;

    @JsonProperty("plexToken")
    private String plexToken;

    @JsonProperty("plexUsername")
    private String plexUsername;

    @JsonProperty("userType")
    private Integer userType;

    private Long permissions;
    private String avatar;

    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;

    @JsonProperty("requestCount")
    private Integer requestCount;
}

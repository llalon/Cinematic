package de.llalon.cinematic.client.overseerr.dto;

import com.squareup.moshi.Json;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a user in Overseerr.
 */
@Data
@AllArgsConstructor
public class User {
    private final Integer id;
    private final String email;
    private final String username;

    @Json(name = "plexToken")
    private final String plexToken;

    @Json(name = "plexUsername")
    private final String plexUsername;

    @Json(name = "userType")
    private final Integer userType;

    private final Long permissions;
    private final String avatar;

    @Json(name = "createdAt")
    private final LocalDateTime createdAt;

    @Json(name = "updatedAt")
    private final LocalDateTime updatedAt;

    @Json(name = "requestCount")
    private final Integer requestCount;
}

package de.llalon.cinematic.client.radarr.dto;

import com.squareup.moshi.Json;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a movie file in Radarr.
 */
@Data
@AllArgsConstructor
public class MovieFileResource {

    @Json(name = "id")
    private final Integer id;

    @Json(name = "movieId")
    private final Integer movieId;

    @Json(name = "relativePath")
    private final String relativePath;

    @Json(name = "path")
    private final String path;

    @Json(name = "size")
    private final Long size;

    @Json(name = "dateAdded")
    private final LocalDateTime dateAdded;

    @Json(name = "sceneName")
    private final String sceneName;

    @Json(name = "releaseGroup")
    private final String releaseGroup;

    @Json(name = "edition")
    private final String edition;

    @Json(name = "qualityCutoffNotMet")
    private final Boolean qualityCutoffNotMet;

    @Json(name = "originalFilePath")
    private final String originalFilePath;
}

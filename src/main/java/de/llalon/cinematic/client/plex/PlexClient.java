package de.llalon.cinematic.client.plex;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import de.llalon.cinematic.client.plex.config.PlexProperties;
import de.llalon.cinematic.client.plex.dto.*;
import de.llalon.cinematic.client.plex.exception.PlexApiException;
import de.llalon.cinematic.client.plex.exception.PlexClientException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

/**
 * Simple Plex API client.
 * <p>
 * This client mirrors patterns used by other service clients in the project and
 * provides basic HTTP helpers for calling the Plex API using an X-Plex-Token.
 */
@Slf4j
public class PlexClient {

    private static final Map<String, String> AGENT_PREFIXES = Map.of(
            "tmdb", "com.plexapp.agents.themoviedb",
            "imdb", "com.plexapp.agents.imdb",
            "tvdb", "com.plexapp.agents.thetvdb");

    private static final String PLEX_TOKEN_HEADER = "X-Plex-Token";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final String token;
    private final OkHttpClient httpClient;
    private final Moshi moshi;
    private final HttpUrl baseUrl;

    /**
     * Constructs a new {@code PlexClient}.
     *
     * @param httpClient the OkHttp client to use for requests
     * @param properties the Plex connection properties (URL and token)
     * @param moshi the Moshi instance for JSON deserialization
     * @throws IllegalArgumentException if the URL or token is invalid
     */
    public PlexClient(OkHttpClient httpClient, PlexProperties properties, Moshi moshi) {
        this.moshi = moshi;
        this.token = properties.getToken();
        this.httpClient = httpClient;
        this.baseUrl = HttpUrl.parse(properties.getUrl());
        if (this.baseUrl == null) {
            throw new IllegalArgumentException("Invalid Plex URL: " + properties.getUrl());
        }
        if (this.token == null || this.token.isEmpty()) {
            throw new IllegalArgumentException("Invalid Plex token");
        }
    }

    /**
     * Returns all media library sections available on the Plex server.
     *
     * @return a wrapped container holding all library {@link de.llalon.cinematic.client.plex.dto.PlexDirectory} entries
     */
    public PlexMediaContainerWrapper<PlexSectionsContainer> getSections() {
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegments("library")
                .addPathSegments("sections")
                .build();
        Type type = Types.newParameterizedType(PlexMediaContainerWrapper.class, PlexSectionsContainer.class);
        return get(url, type);
    }

    /**
     * Returns all media items in a specific library section, optionally including GUID metadata.
     *
     * @param sectionId the Plex library section key
     * @param mediaType the Plex media type filter (e.g. {@code "1"} for movies, {@code "2"} for shows)
     * @param includeGuids whether to include agent GUIDs (TMDB, TVDB, IMDB) for each item
     * @return a wrapped container holding the matching {@link de.llalon.cinematic.client.plex.dto.PlexMetadataContainer}
     */
    public PlexMediaContainerWrapper<PlexMetadataContainer> getSection(
            String sectionId, String mediaType, boolean includeGuids) {
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegments("library")
                .addPathSegments("sections")
                .addPathSegments(sectionId)
                .addPathSegments("all")
                .addQueryParameter("type", String.valueOf(mediaType)) // type=2 = TV shows
                .addQueryParameter("includeGuids", includeGuids ? "1" : "0") // include all GUIDs
                .build();

        Type type = Types.newParameterizedType(PlexMediaContainerWrapper.class, PlexMetadataContainer.class);

        return get(url, type);
    }

    private <T> T get(HttpUrl url, Type responseType) {
        Request request = new Request.Builder()
                .url(url)
                .header(PLEX_TOKEN_HEADER, token)
                .header("Accept", "application/json")
                .get()
                .build();

        return executeRequest(request, responseType);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private String toJson(Object body) {
        JsonAdapter adapter = moshi.adapter(body.getClass());
        return adapter.toJson(body);
    }

    private <T> T post(String path, Object body, Type responseType) {
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegments(path.replaceFirst("^/", ""))
                .build();

        try {
            RequestBody requestBody;
            if (body != null) {
                String jsonBody = toJson(body);
                requestBody = RequestBody.create(jsonBody, JSON);
            } else {
                requestBody = RequestBody.create("", JSON);
            }

            Request request = new Request.Builder()
                    .url(url)
                    .header(PLEX_TOKEN_HEADER, token)
                    .post(requestBody)
                    .build();

            return executeRequest(request, responseType);
        } catch (Exception e) {
            log.error("Failed to serialize request body for POST {}", path, e);
            throw new PlexClientException("Failed to serialize request body for POST " + path, e);
        }
    }

    private <T> T executeRequest(Request request, Type responseType) {
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful() && response.code() != 204) {
                String errorBody = response.body() != null ? response.body().string() : "";
                log.error("Plex API error: status={}, body={}", response.code(), errorBody);
                throw new PlexApiException(
                        "Plex API request failed: HTTP " + response.code(), response.code(), errorBody);
            }

            String responseBody = response.body() != null ? response.body().string() : "";
            if (responseBody.isEmpty() || response.code() == 204) {
                return null;
            }

            if (responseType == String.class) {
                return (T) responseBody;
            }

            try {
                JsonAdapter<T> adapter = (JsonAdapter<T>) moshi.adapter(responseType);
                return adapter.fromJson(responseBody);
            } catch (IOException parseException) {
                log.error("Failed to parse Plex response: {}", request.url(), parseException);
                throw new PlexClientException("Failed to parse Plex response: " + request.url(), parseException);
            }
        } catch (IOException e) {
            log.error("Failed to execute Plex request: {}", request.url(), e);
            throw new PlexApiException("Failed to execute Plex request: " + request.url(), e);
        }
    }
}

package de.llalon.cinematic.client.sonarr;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import de.llalon.cinematic.client.sonarr.config.SonarrProperties;
import de.llalon.cinematic.client.sonarr.dto.EpisodeFileResource;
import de.llalon.cinematic.client.sonarr.dto.EpisodeResource;
import de.llalon.cinematic.client.sonarr.dto.QueueResource;
import de.llalon.cinematic.client.sonarr.dto.QueueResourcePagingResource;
import de.llalon.cinematic.client.sonarr.dto.SeriesResource;
import de.llalon.cinematic.client.sonarr.dto.TagResource;
import de.llalon.cinematic.client.sonarr.exception.SonarrApiException;
import de.llalon.cinematic.client.sonarr.exception.SonarrClientException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

/**
 * Client service for interacting with the Sonarr API (v3/v4).
 *
 * Sonarr is a TV series management and monitoring tool.
 * This client provides type-safe access to Sonarr API endpoints.
 *
 * Authentication is handled via API key passed in X-Api-Key header.
 * All API calls are synchronous and blocking.
 *
 * Key features:
 * - Series management (CRUD operations)
 * - Episode monitoring and tracking
 * - Episode file management
 * - Tag-based automation control
 * - Idempotent operations safe for replay
 *
 * @see <a href="https://github.com/Sonarr/Sonarr">Sonarr GitHub</a>
 */
@Slf4j
public class SonarrClient {

    private static final String API_KEY_HEADER = "X-Api-Key";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final String apiKey;
    private final OkHttpClient httpClient;
    private final Moshi moshi;
    private final HttpUrl baseUrl;

    public SonarrClient(OkHttpClient httpClient, SonarrProperties properties, Moshi moshi) {
        this.moshi = moshi;
        this.apiKey = properties.getApiKey();
        this.httpClient = httpClient;
        this.baseUrl = HttpUrl.parse(properties.getUrl());
        if (this.baseUrl == null) {
            throw new IllegalArgumentException("Invalid Sonarr URL: " + properties.getUrl());
        }
        if (this.apiKey == null || this.apiKey.isEmpty()) {
            throw new IllegalArgumentException("Invalid API Key: " + properties.getApiKey());
        }
    }

    // ==================== SERIES ====================

    /**
     * Get all series in Sonarr.
     *
     * @return list of all series
     */
    public List<SeriesResource> getAllSeries() {
        log.debug("Fetching all series");
        Type type = Types.newParameterizedType(List.class, SeriesResource.class);
        return get("/api/v3/series", type);
    }

    /**
     * Get a specific series by ID.
     *
     * @param seriesId Series ID
     * @return series details
     */
    public SeriesResource getSeries(int seriesId) {
        log.debug("Fetching series with ID: {}", seriesId);
        return get("/api/v3/series/" + seriesId, SeriesResource.class);
    }

    /**
     * Get series by TVDB ID.
     *
     * @param tvdbId TVDB ID
     * @return list of matching series
     */
    public List<SeriesResource> getSeriesByTvdbId(int tvdbId) {
        log.debug("Fetching series with TVDB ID: {}", tvdbId);
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegments("api/v3/series")
                .addQueryParameter("tvdbId", String.valueOf(tvdbId))
                .build();
        Type type = Types.newParameterizedType(List.class, SeriesResource.class);
        return get(url, type);
    }

    /**
     * Update an existing series.
     * Idempotent operation - safe to call multiple times with same data.
     *
     * @param series Series resource to update
     * @return updated series
     */
    public SeriesResource updateSeries(SeriesResource series) {
        log.debug("Updating series with ID: {}", series.getId());
        return put("/api/v3/series/" + series.getId(), series, SeriesResource.class);
    }

    /**
     * Delete a series from Sonarr.
     *
     * @param seriesId Series ID
     * @param deleteFiles Whether to delete files from disk
     */
    public void deleteSeries(int seriesId, boolean deleteFiles) {
        log.debug("Deleting series with ID: {}, deleteFiles: {}", seriesId, deleteFiles);
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegments("api/v3/series/" + seriesId)
                .addQueryParameter("deleteFiles", String.valueOf(deleteFiles))
                .build();
        delete(url);
    }

    // ==================== EPISODES ====================

    /**
     * Get episodes for a specific series.
     *
     * @param seriesId Series ID
     * @return list of episodes
     */
    public List<EpisodeResource> getEpisodesBySeries(int seriesId) {
        log.debug("Fetching episodes for series ID: {}", seriesId);
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegments("api/v3/episode")
                .addQueryParameter("seriesId", String.valueOf(seriesId))
                .build();
        Type type = Types.newParameterizedType(List.class, EpisodeResource.class);
        return get(url, type);
    }

    /**
     * Get episodes for a specific season.
     *
     * @param seriesId Series ID
     * @param seasonNumber Season number
     * @return list of episodes
     */
    public List<EpisodeResource> getEpisodesBySeason(int seriesId, int seasonNumber) {
        log.debug("Fetching episodes for series ID: {}, season: {}", seriesId, seasonNumber);
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegments("api/v3/episode")
                .addQueryParameter("seriesId", String.valueOf(seriesId))
                .addQueryParameter("seasonNumber", String.valueOf(seasonNumber))
                .build();
        Type type = Types.newParameterizedType(List.class, EpisodeResource.class);
        return get(url, type);
    }

    /**
     * Get a specific episode by ID.
     *
     * @param episodeId Episode ID
     * @return episode details
     */
    public EpisodeResource getEpisode(int episodeId) {
        log.debug("Fetching episode with ID: {}", episodeId);
        return get("/api/v3/episode/" + episodeId, EpisodeResource.class);
    }

    /**
     * Update an existing episode.
     * Idempotent operation - safe to call multiple times with same data.
     *
     * @param episode Episode resource to update
     * @return updated episode
     */
    public EpisodeResource updateEpisode(EpisodeResource episode) {
        log.debug("Updating episode with ID: {}", episode.getId());
        return put("/api/v3/episode/" + episode.getId(), episode, EpisodeResource.class);
    }

    // ==================== EPISODE FILES ====================

    /**
     * Get all episode files for a specific series.
     *
     * @param seriesId Series ID
     * @return list of episode files
     */
    public List<EpisodeFileResource> getEpisodeFilesBySeries(int seriesId) {
        log.debug("Fetching episode files for series ID: {}", seriesId);
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegments("api/v3/episodefile")
                .addQueryParameter("seriesId", String.valueOf(seriesId))
                .build();
        Type type = Types.newParameterizedType(List.class, EpisodeFileResource.class);
        return get(url, type);
    }

    /**
     * Get a specific episode file by ID.
     *
     * @param episodeFileId Episode file ID
     * @return episode file details
     */
    public EpisodeFileResource getEpisodeFile(int episodeFileId) {
        log.debug("Fetching episode file with ID: {}", episodeFileId);
        return get("/api/v3/episodefile/" + episodeFileId, EpisodeFileResource.class);
    }

    /**
     * Update an existing episode file.
     * Idempotent operation - safe to call multiple times with same data.
     *
     * @param episodeFile Episode file resource to update
     * @return updated episode file
     */
    public EpisodeFileResource updateEpisodeFile(EpisodeFileResource episodeFile) {
        log.debug("Updating episode file with ID: {}", episodeFile.getId());
        return put("/api/v3/episodefile/" + episodeFile.getId(), episodeFile, EpisodeFileResource.class);
    }

    /**
     * Delete an episode file from Sonarr and disk.
     *
     * @param episodeFileId Episode file ID
     */
    public void deleteEpisodeFile(int episodeFileId) {
        log.debug("Deleting episode file with ID: {}", episodeFileId);
        delete(baseUrl.newBuilder()
                .addPathSegments("api/v3/episodefile/" + episodeFileId)
                .build());
    }

    // ==================== QUEUE ====================

    /**
     * Get all items in the download queue.
     * Queue items contain downloadId which can be correlated with download client (e.g., qBittorrent hash).
     *
     * @return paginated queue response
     */
    public QueueResourcePagingResource getQueue() {
        log.debug("Fetching download queue");
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegments("api/v3/queue")
                .addQueryParameter("pageSize", "1000")
                .addQueryParameter("includeSeries", "true")
                .build();
        return get(url, QueueResourcePagingResource.class);
    }

    /**
     * Get queue items for a specific series.
     *
     * @param seriesId Series ID
     * @return list of queue items for the series
     */
    public List<QueueResource> getQueueForSeries(int seriesId) {
        log.debug("Fetching queue items for series ID: {}", seriesId);
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegments("api/v3/queue/details")
                .addQueryParameter("seriesId", String.valueOf(seriesId))
                .addQueryParameter("includeSeries", "true")
                .build();
        Type type = Types.newParameterizedType(List.class, QueueResource.class);
        return get(url, type);
    }

    // ==================== TAGS ====================

    /**
     * Get all tags in Sonarr.
     * Tags are used to drive automation logic and policy enforcement.
     *
     * @return list of all tags
     */
    public List<TagResource> getAllTags() {
        log.debug("Fetching all tags");
        Type type = Types.newParameterizedType(List.class, TagResource.class);
        return get("/api/v3/tag", type);
    }

    /**
     * Get a specific tag by ID.
     *
     * @param tagId Tag ID
     * @return tag details
     */
    public TagResource getTag(int tagId) {
        log.debug("Fetching tag with ID: {}", tagId);
        return get("/api/v3/tag/" + tagId, TagResource.class);
    }

    /**
     * Create a new tag.
     * Idempotent - check if tag exists before creating to avoid duplicates.
     *
     * @param tag Tag resource to create
     * @return created tag with ID
     */
    public TagResource createTag(TagResource tag) {
        log.debug("Creating tag: {}", tag.getLabel());
        return post("/api/v3/tag", tag, TagResource.class);
    }

    /**
     * Update an existing tag.
     * Idempotent operation - safe to call multiple times with same data.
     *
     * @param tag Tag resource to update
     * @return updated tag
     */
    public TagResource updateTag(TagResource tag) {
        log.debug("Updating tag with ID: {}", tag.getId());
        return put("/api/v3/tag/" + tag.getId(), tag, TagResource.class);
    }

    /**
     * Delete a tag from Sonarr.
     *
     * @param tagId Tag ID
     */
    public void deleteTag(int tagId) {
        log.debug("Deleting tag with ID: {}", tagId);
        delete(baseUrl.newBuilder().addPathSegments("api/v3/tag/" + tagId).build());
    }

    // ==================== HTTP METHODS ====================

    /**
     * Execute a GET request to the Sonarr API.
     *
     * @param path API path
     * @param responseType Type for the expected response type
     * @param <T> The type of the response
     * @return parsed response
     */
    private <T> T get(String path, Type responseType) {
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegments(path.replaceFirst("^/", ""))
                .build();
        return get(url, responseType);
    }

    /**
     * Execute a GET request to the Sonarr API.
     *
     * @param url Full URL with query parameters
     * @param responseType Type for the expected response type
     * @param <T> The type of the response
     * @return parsed response
     */
    private <T> T get(HttpUrl url, Type responseType) {
        Request request = new Request.Builder()
                .url(url)
                .header(API_KEY_HEADER, apiKey)
                .get()
                .build();

        return executeRequest(request, responseType);
    }

    /**
     * Execute a POST request to the Sonarr API.
     *
     * @param path API path
     * @param body Request body object
     * @param responseType Type for the expected response type
     * @param <T> The type of the response
     * @return parsed response
     */
    private <T> T post(String path, Object body, Type responseType) {
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegments(path.replaceFirst("^/", ""))
                .build();

        try {
            String jsonBody = moshi.adapter(body.getClass()).toJson(body);
            RequestBody requestBody = RequestBody.create(jsonBody, JSON);
            Request request = new Request.Builder()
                    .url(url)
                    .header(API_KEY_HEADER, apiKey)
                    .post(requestBody)
                    .build();

            return executeRequest(request, responseType);
        } catch (Exception e) {
            log.error("Failed to serialize request body for POST {}", path, e);
            throw new SonarrClientException("Failed to serialize request body for POST " + path, e);
        }
    }

    /**
     * Execute a PUT request to the Sonarr API.
     *
     * @param path API path
     * @param body Request body object
     * @param responseType Type for the expected response type
     * @param <T> The type of the response
     * @return parsed response
     */
    private <T> T put(String path, Object body, Type responseType) {
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegments(path.replaceFirst("^/", ""))
                .build();

        try {
            String jsonBody = moshi.adapter(body.getClass()).toJson(body);
            RequestBody requestBody = RequestBody.create(jsonBody, JSON);
            Request request = new Request.Builder()
                    .url(url)
                    .header(API_KEY_HEADER, apiKey)
                    .put(requestBody)
                    .build();

            return executeRequest(request, responseType);
        } catch (Exception e) {
            log.error("Failed to serialize request body for PUT {}", path, e);
            throw new SonarrClientException("Failed to serialize request body for PUT " + path, e);
        }
    }

    /**
     * Execute a DELETE request to the Sonarr API.
     *
     * @param url Full URL with query parameters
     */
    private void delete(HttpUrl url) {
        Request request = new Request.Builder()
                .url(url)
                .header(API_KEY_HEADER, apiKey)
                .delete()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "";
                log.error("Sonarr API error: status={}, body={}", response.code(), errorBody);
                throw new SonarrApiException(
                        "Sonarr API request failed: HTTP " + response.code(), response.code(), errorBody);
            }
        } catch (IOException e) {
            log.error("Failed to execute DELETE request", e);
            throw new SonarrApiException("Failed to execute DELETE request", e);
        }
    }

    /**
     * Execute a request and parse the response.
     *
     * @param request HTTP request
     * @param responseType Type for the expected response type
     * @param <T> The type of the response
     * @return parsed response
     */
    private <T> T executeRequest(Request request, Type responseType) {
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "";
                log.error("Sonarr API error: status={}, body={}", response.code(), errorBody);
                throw new SonarrApiException(
                        "Sonarr API request failed: HTTP " + response.code(), response.code(), errorBody);
            }

            String responseBody = response.body() != null ? response.body().string() : "";
            if (responseBody.isEmpty()) {
                return null;
            }

            try {
                JsonAdapter<T> adapter = (JsonAdapter<T>) moshi.adapter(responseType);
                return adapter.fromJson(responseBody);
            } catch (IOException parseException) {
                log.error("Failed to parse Sonarr response: {}", request.url(), parseException);
                throw new SonarrClientException("Failed to parse Sonarr response: " + request.url(), parseException);
            }
        } catch (IOException e) {
            log.error("Failed to execute Sonarr request: {}", request.url(), e);
            throw new SonarrApiException("Failed to execute Sonarr request: " + request.url(), e);
        }
    }
}

package de.llalon.cinematic.client.radarr;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.llalon.cinematic.client.radarr.config.RadarrProperties;
import de.llalon.cinematic.client.radarr.dto.MovieFileResource;
import de.llalon.cinematic.client.radarr.dto.MovieResource;
import de.llalon.cinematic.client.radarr.dto.QueueResource;
import de.llalon.cinematic.client.radarr.dto.QueueResourcePagingResource;
import de.llalon.cinematic.client.radarr.dto.TagResource;
import de.llalon.cinematic.client.radarr.exception.RadarrApiException;
import de.llalon.cinematic.client.radarr.exception.RadarrClientParseException;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

/**
 * Client service for interacting with the Radarr API (v3).
 *
 * Radarr is a movie management and monitoring tool.
 * This client provides type-safe access to Radarr API endpoints.
 *
 * Authentication is handled via API key passed in X-Api-Key header.
 * All API calls are synchronous and blocking.
 *
 * Key features:
 * - Movie management (CRUD operations)
 * - Movie file management
 * - Tag-based automation control
 * - Idempotent operations safe for replay
 *
 * @see <a href="https://github.com/Radarr/Radarr">Radarr GitHub</a>
 */
@Slf4j
public class RadarrClient {

    private static final String API_KEY_HEADER = "X-Api-Key";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final String apiKey;
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final HttpUrl baseUrl;

    public RadarrClient(OkHttpClient httpClient, RadarrProperties properties, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.apiKey = properties.getApiKey();
        this.httpClient = httpClient;
        this.baseUrl = HttpUrl.parse(properties.getUrl());
        if (this.baseUrl == null) {
            throw new IllegalArgumentException("Invalid Radarr URL: " + properties.getUrl());
        }
        if (this.apiKey == null || this.apiKey.isEmpty()) {
            throw new IllegalArgumentException("Invalid API Key: " + properties.getApiKey());
        }
    }

    // ==================== MOVIES ====================

    /**
     * Get all movies in Radarr.
     *
     * @return list of all movies
     */
    public List<MovieResource> getAllMovies() {
        log.debug("Fetching all movies");
        return get("/api/v3/movie", new TypeReference<List<MovieResource>>() {});
    }

    /**
     * Get a specific movie by ID.
     *
     * @param movieId Movie ID
     * @return movie details
     */
    public MovieResource getMovie(int movieId) {
        log.debug("Fetching movie with ID: {}", movieId);
        return get("/api/v3/movie/" + movieId, new TypeReference<MovieResource>() {});
    }

    /**
     * Get movies by TMDB ID.
     *
     * @param tmdbId TMDB ID
     * @return list of matching movies
     */
    public List<MovieResource> getMoviesByTmdbId(int tmdbId) {
        log.debug("Fetching movies with TMDB ID: {}", tmdbId);
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegments("api/v3/movie")
                .addQueryParameter("tmdbId", String.valueOf(tmdbId))
                .build();
        return get(url, new TypeReference<List<MovieResource>>() {});
    }

    /**
     * Add a new movie to Radarr.
     * Idempotent - check if movie exists before adding to avoid duplicates.
     *
     * @param movie Movie resource to add
     * @return created movie with ID
     */
    public MovieResource addMovie(MovieResource movie) {
        log.debug("Adding movie: {}", movie.getTitle());
        return post("/api/v3/movie", movie, new TypeReference<MovieResource>() {});
    }

    /**
     * Update an existing movie.
     * Idempotent operation - safe to call multiple times with same data.
     *
     * @param movie Movie resource to update
     * @return updated movie
     */
    public MovieResource updateMovie(MovieResource movie) {
        log.debug("Updating movie with ID: {}", movie.getId());
        return put("/api/v3/movie/" + movie.getId(), movie, new TypeReference<MovieResource>() {});
    }

    /**
     * Delete a movie from Radarr.
     *
     * @param movieId Movie ID
     * @param deleteFiles Whether to delete files from disk
     * @param addImportExclusion Whether to add an import exclusion
     */
    public void deleteMovie(int movieId, boolean deleteFiles, boolean addImportExclusion) {
        log.debug(
                "Deleting movie with ID: {}, deleteFiles: {}, addImportExclusion: {}",
                movieId,
                deleteFiles,
                addImportExclusion);
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegments("api/v3/movie/" + movieId)
                .addQueryParameter("deleteFiles", String.valueOf(deleteFiles))
                .addQueryParameter("addImportExclusion", String.valueOf(addImportExclusion))
                .build();
        delete(url);
    }

    /**
     * Lookup a movie by TMDB ID.
     *
     * @param tmdbId TMDB ID
     * @return movie details
     */
    public MovieResource lookupMovieByTmdbId(int tmdbId) {
        log.debug("Looking up movie by TMDB ID: {}", tmdbId);
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegments("api/v3/movie/lookup/tmdb")
                .addQueryParameter("tmdbId", String.valueOf(tmdbId))
                .build();
        return get(url, new TypeReference<MovieResource>() {});
    }

    /**
     * Lookup a movie by IMDB ID.
     *
     * @param imdbId IMDB ID
     * @return movie details
     */
    public MovieResource lookupMovieByImdbId(String imdbId) {
        log.debug("Looking up movie by IMDB ID: {}", imdbId);
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegments("api/v3/movie/lookup/imdb")
                .addQueryParameter("imdbId", imdbId)
                .build();
        return get(url, new TypeReference<MovieResource>() {});
    }

    /**
     * Lookup movies by search term.
     *
     * @param term Search term
     * @return list of matching movies
     */
    public List<MovieResource> lookupMovies(String term) {
        log.debug("Looking up movies with term: {}", term);
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegments("api/v3/movie/lookup")
                .addQueryParameter("term", term)
                .build();
        return get(url, new TypeReference<List<MovieResource>>() {});
    }

    // ==================== MOVIE FILES ====================

    /**
     * Get all movie files for a specific movie.
     *
     * @param movieId Movie ID
     * @return list of movie files
     */
    public List<MovieFileResource> getMovieFilesByMovie(int movieId) {
        log.debug("Fetching movie files for movie ID: {}", movieId);
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegments("api/v3/moviefile")
                .addQueryParameter("movieId", String.valueOf(movieId))
                .build();
        return get(url, new TypeReference<List<MovieFileResource>>() {});
    }

    /**
     * Get a specific movie file by ID.
     *
     * @param movieFileId Movie file ID
     * @return movie file details
     */
    public MovieFileResource getMovieFile(int movieFileId) {
        log.debug("Fetching movie file with ID: {}", movieFileId);
        return get("/api/v3/moviefile/" + movieFileId, new TypeReference<MovieFileResource>() {});
    }

    /**
     * Update an existing movie file.
     * Idempotent operation - safe to call multiple times with same data.
     *
     * @param movieFile Movie file resource to update
     * @return updated movie file
     */
    public MovieFileResource updateMovieFile(MovieFileResource movieFile) {
        log.debug("Updating movie file with ID: {}", movieFile.getId());
        return put("/api/v3/moviefile/" + movieFile.getId(), movieFile, new TypeReference<MovieFileResource>() {});
    }

    /**
     * Delete a movie file from Radarr and disk.
     *
     * @param movieFileId Movie file ID
     */
    public void deleteMovieFile(int movieFileId) {
        log.debug("Deleting movie file with ID: {}", movieFileId);
        delete(baseUrl.newBuilder()
                .addPathSegments("api/v3/moviefile/" + movieFileId)
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
                .addQueryParameter("includeMovie", "true")
                .build();
        return get(url, new TypeReference<QueueResourcePagingResource>() {});
    }

    /**
     * Get queue items for a specific movie.
     *
     * @param movieId Movie ID
     * @return list of queue items for the movie
     */
    public List<QueueResource> getQueueForMovie(int movieId) {
        log.debug("Fetching queue items for movie ID: {}", movieId);
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegments("api/v3/queue/details")
                .addQueryParameter("movieId", String.valueOf(movieId))
                .addQueryParameter("includeMovie", "true")
                .build();
        return get(url, new TypeReference<List<QueueResource>>() {});
    }

    // ==================== TAGS ====================

    /**
     * Get all tags in Radarr.
     * Tags are used to drive automation logic and policy enforcement.
     *
     * @return list of all tags
     */
    public List<TagResource> getAllTags() {
        log.debug("Fetching all tags");
        return get("/api/v3/tag", new TypeReference<List<TagResource>>() {});
    }

    /**
     * Get a specific tag by ID.
     *
     * @param tagId Tag ID
     * @return tag details
     */
    public TagResource getTag(int tagId) {
        log.debug("Fetching tag with ID: {}", tagId);
        return get("/api/v3/tag/" + tagId, new TypeReference<TagResource>() {});
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
        return post("/api/v3/tag", tag, new TypeReference<TagResource>() {});
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
        return put("/api/v3/tag/" + tag.getId(), tag, new TypeReference<TagResource>() {});
    }

    /**
     * Delete a tag from Radarr.
     *
     * @param tagId Tag ID
     */
    public void deleteTag(int tagId) {
        log.debug("Deleting tag with ID: {}", tagId);
        delete(baseUrl.newBuilder().addPathSegments("api/v3/tag/" + tagId).build());
    }

    // ==================== HTTP METHODS ====================

    /**
     * Execute a GET request to the Radarr API.
     *
     * @param path API path
     * @param responseType TypeReference for the expected response type
     * @param <T> The type of the response
     * @return parsed response
     */
    private <T> T get(String path, TypeReference<T> responseType) {
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegments(path.replaceFirst("^/", ""))
                .build();
        return get(url, responseType);
    }

    /**
     * Execute a GET request to the Radarr API.
     *
     * @param url Full URL with query parameters
     * @param responseType TypeReference for the expected response type
     * @param <T> The type of the response
     * @return parsed response
     */
    private <T> T get(HttpUrl url, TypeReference<T> responseType) {
        Request request = new Request.Builder()
                .url(url)
                .header(API_KEY_HEADER, apiKey)
                .get()
                .build();

        return executeRequest(request, responseType);
    }

    /**
     * Execute a POST request to the Radarr API.
     *
     * @param path API path
     * @param body Request body object
     * @param responseType TypeReference for the expected response type
     * @param <T> The type of the response
     * @return parsed response
     */
    private <T> T post(String path, Object body, TypeReference<T> responseType) {
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegments(path.replaceFirst("^/", ""))
                .build();

        try {
            String jsonBody = objectMapper.writeValueAsString(body);
            RequestBody requestBody = RequestBody.create(jsonBody, JSON);
            Request request = new Request.Builder()
                    .url(url)
                    .header(API_KEY_HEADER, apiKey)
                    .post(requestBody)
                    .build();

            return executeRequest(request, responseType);
        } catch (IOException e) {
            log.error("Failed to serialize request body for POST {}", path, e);
            throw new RadarrClientParseException("Failed to serialize request body for POST " + path, e);
        }
    }

    /**
     * Execute a PUT request to the Radarr API.
     *
     * @param path API path
     * @param body Request body object
     * @param responseType TypeReference for the expected response type
     * @param <T> The type of the response
     * @return parsed response
     */
    private <T> T put(String path, Object body, TypeReference<T> responseType) {
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegments(path.replaceFirst("^/", ""))
                .build();

        try {
            String jsonBody = objectMapper.writeValueAsString(body);
            RequestBody requestBody = RequestBody.create(jsonBody, JSON);
            Request request = new Request.Builder()
                    .url(url)
                    .header(API_KEY_HEADER, apiKey)
                    .put(requestBody)
                    .build();

            return executeRequest(request, responseType);
        } catch (IOException e) {
            log.error("Failed to serialize request body for PUT {}", path, e);
            throw new RadarrClientParseException("Failed to serialize request body for PUT " + path, e);
        }
    }

    /**
     * Execute a DELETE request to the Radarr API.
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
                log.error("Radarr API error: status={}, body={}", response.code(), errorBody);
                throw new RadarrApiException(
                        "Radarr API request failed: HTTP " + response.code(), response.code(), errorBody);
            }
        } catch (IOException e) {
            log.error("Failed to execute DELETE request", e);
            throw new RadarrApiException("Failed to execute DELETE request", e);
        }
    }

    /**
     * Execute a request and parse the response.
     *
     * @param request HTTP request
     * @param responseType TypeReference for the expected response type
     * @param <T> The type of the response
     * @return parsed response
     */
    private <T> T executeRequest(Request request, TypeReference<T> responseType) {
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "";
                log.error("Radarr API error: status={}, body={}", response.code(), errorBody);
                throw new RadarrApiException(
                        "Radarr API request failed: HTTP " + response.code(), response.code(), errorBody);
            }

            String responseBody = response.body() != null ? response.body().string() : "";
            if (responseBody.isEmpty()) {
                return null;
            }

            try {
                return objectMapper.readValue(responseBody, responseType);
            } catch (IOException parseException) {
                log.error("Failed to parse Radarr response: {}", request.url(), parseException);
                throw new RadarrClientParseException(
                        "Failed to parse Radarr response: " + request.url(), parseException);
            }
        } catch (IOException e) {
            log.error("Failed to execute Radarr request: {}", request.url(), e);
            throw new RadarrApiException("Failed to execute Radarr request: " + request.url(), e);
        }
    }
}

package de.llalon.cinematic.client.radarr;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import de.llalon.cinematic.client.radarr.config.RadarrProperties;
import de.llalon.cinematic.client.radarr.dto.MovieFileResource;
import de.llalon.cinematic.client.radarr.dto.MovieResource;
import de.llalon.cinematic.client.radarr.dto.QueueResourcePagingResource;
import de.llalon.cinematic.client.radarr.dto.RadarrTag;
import de.llalon.cinematic.client.radarr.exception.RadarrApiException;
import de.llalon.cinematic.client.radarr.exception.RadarrClientException;
import java.io.IOException;
import java.lang.reflect.Type;
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
    private final Moshi moshi;
    private final HttpUrl baseUrl;

    /**
     * Constructs a new {@code RadarrClient}.
     *
     * @param httpClient the OkHttp client to use for requests
     * @param properties the Radarr connection properties (URL and API key)
     * @param moshi the Moshi instance for JSON deserialization
     * @throws IllegalArgumentException if the URL or API key is invalid
     */
    public RadarrClient(OkHttpClient httpClient, RadarrProperties properties, Moshi moshi) {
        this.moshi = moshi;
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
        Type type = Types.newParameterizedType(List.class, MovieResource.class);
        return get("/api/v3/movie", type);
    }

    /**
     * Get a specific movie by ID.
     *
     * @param movieId Movie ID
     * @return movie details
     */
    public MovieResource getMovie(int movieId) {
        log.debug("Fetching movie with ID: {}", movieId);
        return get("/api/v3/movie/" + movieId, MovieResource.class);
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
        Type type = Types.newParameterizedType(List.class, MovieResource.class);
        return get(url, type);
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
        return post("/api/v3/movie", movie, MovieResource.class);
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
        return put("/api/v3/movie/" + movie.getId(), movie, MovieResource.class);
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
        return get(url, MovieResource.class);
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
        return get(url, MovieResource.class);
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
        Type type = Types.newParameterizedType(List.class, MovieResource.class);
        return get(url, type);
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
        Type type = Types.newParameterizedType(List.class, MovieFileResource.class);
        return get(url, type);
    }

    /**
     * Get a specific movie file by ID.
     *
     * @param movieFileId Movie file ID
     * @return movie file details
     */
    public MovieFileResource getMovieFile(int movieFileId) {
        log.debug("Fetching movie file with ID: {}", movieFileId);
        return get("/api/v3/moviefile/" + movieFileId, MovieFileResource.class);
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
        return put("/api/v3/moviefile/" + movieFile.getId(), movieFile, MovieFileResource.class);
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
     * @param page page number
     * @param pageSize size of page to fetch
     * @param includeMovie if true will include movie details object
     *
     * @return paginated queue response
     */
    public QueueResourcePagingResource getQueue(int page, int pageSize, boolean includeMovie) {
        log.debug("Fetching queue resources for page {} and size {}", page, pageSize);

        final HttpUrl url = baseUrl.newBuilder()
                .addPathSegments("api/v3/queue")
                .addQueryParameter("page", String.valueOf(page))
                .addQueryParameter("pageSize", String.valueOf(pageSize))
                .addQueryParameter("includeMovie", String.valueOf(includeMovie))
                .build();

        return get(url, QueueResourcePagingResource.class);
    }

    /**
     * Remove a queue item from Radarr, optionally adding it to the blocklist.
     *
     * <p>The torrent is not removed from the download client by Radarr when
     * {@code removeFromClient} is {@code false} — handle client-side deletion separately.</p>
     *
     * @param id              queue item ID
     * @param blocklist       if {@code true}, the release is added to the blocklist so Radarr
     *                        will not download it again
     * @param removeFromClient if {@code true}, Radarr will also instruct the download client
     *                        to remove the download
     * @param skipRedownload  if {@code true}, Radarr will not trigger a search for a
     *                        replacement after blacklisting
     */
    public void deleteQueueItem(int id, boolean blocklist, boolean removeFromClient, boolean skipRedownload) {
        log.debug(
                "Deleting queue item id={}, blocklist={}, removeFromClient={}, skipRedownload={}",
                id,
                blocklist,
                removeFromClient,
                skipRedownload);
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegments("api/v3/queue/" + id)
                .addQueryParameter("blocklist", String.valueOf(blocklist))
                .addQueryParameter("removeFromClient", String.valueOf(removeFromClient))
                .addQueryParameter("skipRedownload", String.valueOf(skipRedownload))
                .build();
        delete(url);
    }

    // ==================== TAGS ====================

    /**
     * Get all tags in Radarr.
     * Tags are used to drive automation logic and policy enforcement.
     *
     * @return list of all tags
     */
    public List<RadarrTag> getAllTags() {
        log.debug("Fetching all tags");
        Type type = Types.newParameterizedType(List.class, RadarrTag.class);
        return get("/api/v3/tag", type);
    }

    /**
     * Get a specific tag by ID.
     *
     * @param tagId Tag ID
     * @return tag details
     */
    public RadarrTag getTag(int tagId) {
        log.debug("Fetching tag with ID: {}", tagId);
        return get("/api/v3/tag/" + tagId, RadarrTag.class);
    }

    /**
     * Create a new tag.
     * Idempotent - check if tag exists before creating to avoid duplicates.
     *
     * @param tag Tag resource to create
     * @return created tag with ID
     */
    public RadarrTag createTag(RadarrTag tag) {
        log.debug("Creating tag: {}", tag.getLabel());
        return post("/api/v3/tag", tag, RadarrTag.class);
    }

    /**
     * Update an existing tag.
     * Idempotent operation - safe to call multiple times with same data.
     *
     * @param tag Tag resource to update
     * @return updated tag
     */
    public RadarrTag updateTag(RadarrTag tag) {
        log.debug("Updating tag with ID: {}", tag.getId());
        return put("/api/v3/tag/" + tag.getId(), tag, RadarrTag.class);
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
     * Execute a GET request to the Radarr API.
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
     * Serialize an object to JSON string using Moshi.
     * Uses raw types to avoid generic type capture issues.
     *
     * @param body the object to serialize
     * @return JSON string representation
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private String toJson(Object body) {
        JsonAdapter adapter = moshi.adapter(body.getClass());
        return adapter.toJson(body);
    }

    /**
     * Execute a POST request to the Radarr API.
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
            String jsonBody = toJson(body);
            RequestBody requestBody = RequestBody.create(jsonBody, JSON);
            Request request = new Request.Builder()
                    .url(url)
                    .header(API_KEY_HEADER, apiKey)
                    .post(requestBody)
                    .build();

            return executeRequest(request, responseType);
        } catch (Exception e) {
            log.error("Failed to serialize request body for POST {}", path, e);
            throw new RadarrClientException("Failed to serialize request body for POST " + path, e);
        }
    }

    /**
     * Execute a PUT request to the Radarr API.
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
            String jsonBody = toJson(body);
            RequestBody requestBody = RequestBody.create(jsonBody, JSON);
            Request request = new Request.Builder()
                    .url(url)
                    .header(API_KEY_HEADER, apiKey)
                    .put(requestBody)
                    .build();

            return executeRequest(request, responseType);
        } catch (Exception e) {
            log.error("Failed to serialize request body for PUT {}", path, e);
            throw new RadarrClientException("Failed to serialize request body for PUT " + path, e);
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
     * @param responseType Type for the expected response type
     * @param <T> The type of the response
     * @return parsed response
     */
    private <T> T executeRequest(Request request, Type responseType) {
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
                JsonAdapter<T> adapter = (JsonAdapter<T>) moshi.adapter(responseType);
                return adapter.fromJson(responseBody);
            } catch (IOException parseException) {
                log.error("Failed to parse Radarr response: {}", request.url(), parseException);
                throw new RadarrClientException("Failed to parse Radarr response: " + request.url(), parseException);
            }
        } catch (IOException e) {
            log.error("Failed to execute Radarr request: {}", request.url(), e);
            throw new RadarrApiException("Failed to execute Radarr request: " + request.url(), e);
        }
    }
}

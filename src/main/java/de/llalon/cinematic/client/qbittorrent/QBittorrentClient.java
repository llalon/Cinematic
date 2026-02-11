package de.llalon.cinematic.client.qbittorrent;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.llalon.cinematic.client.qbittorrent.config.QBittorrentProperties;
import de.llalon.cinematic.client.qbittorrent.dto.TorrentFile;
import de.llalon.cinematic.client.qbittorrent.dto.TorrentInfo;
import de.llalon.cinematic.client.qbittorrent.dto.TorrentProperties;
import de.llalon.cinematic.client.qbittorrent.exception.QBittorrentApiException;
import de.llalon.cinematic.client.qbittorrent.exception.QBittorrentClientException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

/**
 * Client service for interacting with the qBittorrent WebUI API (v2).
 *
 * qBittorrent is a BitTorrent client with a web interface for remote management.
 * This client provides type-safe access to qBittorrent API endpoints.
 *
 * Authentication is cookie-based - the client automatically logs in and maintains
 * the session cookie for subsequent requests.
 * All API calls are synchronous and blocking.
 *
 * Key features:
 * - Cookie-based session management
 * - Torrent management (list, pause, resume, delete)
 * - Tag-based organization
 * - Torrent properties retrieval
 * - Idempotent operations safe for replay
 *
 * @see <a href="https://github.com/qbittorrent/qBittorrent/wiki/WebUI-API-(qBittorrent-4.1)">qBittorrent WebUI API</a>
 */
@Slf4j
public class QBittorrentClient {

    private static final MediaType FORM_URLENCODED = MediaType.get("application/x-www-form-urlencoded");

    private final String username;
    private final String password;
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final HttpUrl baseUrl;
    private volatile String sessionCookie;

    public QBittorrentClient(OkHttpClient httpClient, QBittorrentProperties properties, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.username = properties.getUsername();
        this.password = properties.getPassword();
        this.httpClient = httpClient;
        this.baseUrl = HttpUrl.parse(properties.getUrl());
        if (this.baseUrl == null) {
            throw new IllegalArgumentException("Invalid qBittorrent URL: " + properties.getUrl());
        }
        if (this.username == null || this.username.isEmpty()) {
            throw new IllegalArgumentException("Invalid username: " + properties.getUsername());
        }
        if (this.password == null || this.password.isEmpty()) {
            throw new IllegalArgumentException("Invalid password");
        }
    }

    // ==================== AUTHENTICATION ====================

    /**
     * Authenticate with qBittorrent and obtain session cookie.
     * This method is called automatically when needed.
     *
     * @throws QBittorrentApiException if authentication fails
     */
    private void login() {
        log.debug("Logging in to qBittorrent");

        HttpUrl url = baseUrl.newBuilder().addPathSegments("api/v2/auth/login").build();

        RequestBody body = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Referer", baseUrl.toString())
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.code() == 403) {
                String errorBody = response.body() != null ? response.body().string() : "";
                throw new QBittorrentApiException(
                        "Authentication failed: IP may be banned for too many failed login attempts", 403, errorBody);
            }

            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "";
                throw new QBittorrentApiException(
                        "Login failed with HTTP " + response.code(), response.code(), errorBody);
            }

            // Extract SID cookie from response
            List<String> cookies = response.headers("Set-Cookie");
            for (String cookie : cookies) {
                if (cookie.startsWith("SID=")) {
                    this.sessionCookie = cookie.split(";")[0];
                    log.debug("Successfully logged in to qBittorrent");
                    return;
                }
            }

            throw new QBittorrentApiException("Login successful but no SID cookie received", 200, "");
        } catch (IOException e) {
            throw new QBittorrentApiException("Failed to login to qBittorrent", e);
        }
    }

    /**
     * Logout from qBittorrent (invalidate session).
     * Optional - called automatically on JVM shutdown if needed.
     */
    public void logout() {
        if (sessionCookie == null) {
            return;
        }

        log.debug("Logging out from qBittorrent");

        HttpUrl url = baseUrl.newBuilder().addPathSegments("api/v2/auth/logout").build();

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create("", null))
                .header("Cookie", sessionCookie)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                log.debug("Successfully logged out from qBittorrent");
                sessionCookie = null;
            }
        } catch (IOException e) {
            log.warn("Failed to logout from qBittorrent", e);
        }
    }

    /**
     * Ensure we have a valid session cookie, login if needed.
     */
    private void ensureAuthenticated() {
        if (sessionCookie == null) {
            login();
        }
    }

    // ==================== TORRENT MANAGEMENT ====================

    /**
     * Get list of all torrents.
     * Idempotent operation - safe to call multiple times.
     *
     * @return list of all torrents
     */
    public List<TorrentInfo> getTorrents() {
        log.debug("Fetching all torrents");
        return get("/api/v2/torrents/info", Map.of(), new TypeReference<List<TorrentInfo>>() {});
    }

    /**
     * Get list of torrents with optional filters.
     * Idempotent operation - safe to call multiple times.
     *
     * @param filter Filter by state (all, downloading, seeding, completed, stopped, active, inactive, running, stalled, errored)
     * @param category Filter by category
     * @param tag Filter by tag
     * @return list of filtered torrents
     */
    public List<TorrentInfo> getTorrents(String filter, String category, String tag) {
        log.debug("Fetching torrents with filter: {}, category: {}, tag: {}", filter, category, tag);

        HttpUrl.Builder urlBuilder = baseUrl.newBuilder().addPathSegments("api/v2/torrents/info");

        if (filter != null && !filter.isEmpty()) {
            urlBuilder.addQueryParameter("filter", filter);
        }
        if (category != null && !category.isEmpty()) {
            urlBuilder.addQueryParameter("category", category);
        }
        if (tag != null && !tag.isEmpty()) {
            urlBuilder.addQueryParameter("tag", tag);
        }

        return get(urlBuilder.build(), new TypeReference<List<TorrentInfo>>() {});
    }

    /**
     * Get generic properties of a torrent.
     *
     * @param hash Torrent hash
     * @return torrent properties
     */
    public TorrentProperties getTorrentProperties(String hash) {
        log.debug("Fetching properties for torrent: {}", hash);
        return get("/api/v2/torrents/properties", Map.of("hash", hash), new TypeReference<TorrentProperties>() {});
    }

    /**
     * Get files of a torrent.
     *
     * @param hash Torrent hash
     * @return list of torrent files
     */
    public List<TorrentFile> getTorrentFiles(String hash) {
        log.debug("Fetching files for torrent: {}", hash);
        return get("/api/v2/torrents/files", Map.of("hash", hash), new TypeReference<List<TorrentFile>>() {});
    }

    /**
     * Pause one or more torrents.
     * Idempotent operation - pausing an already paused torrent is safe.
     *
     * @param hashes Collection of torrent hashes, or a singleton with "all" for all torrents
     */
    public void pauseTorrents(Collection<String> hashes) {
        String hashesParam = joinHashes(hashes);
        log.debug("Pausing torrents: {}", hashesParam);
        post("/api/v2/torrents/pause", Map.of("hashes", hashesParam));
    }

    /**
     * Resume one or more torrents.
     * Idempotent operation - resuming an already active torrent is safe.
     *
     * @param hashes Collection of torrent hashes, or a singleton with "all" for all torrents
     */
    public void resumeTorrents(Collection<String> hashes) {
        String hashesParam = joinHashes(hashes);
        log.debug("Resuming torrents: {}", hashesParam);
        post("/api/v2/torrents/resume", Map.of("hashes", hashesParam));
    }

    /**
     * Delete one or more torrents.
     * NOT idempotent - calling on already deleted torrent may fail.
     *
     * @param hashes Collection of torrent hashes, or a singleton with "all" for all torrents
     * @param deleteFiles If true, delete downloaded data as well
     */
    public void deleteTorrents(Collection<String> hashes, boolean deleteFiles) {
        String hashesParam = joinHashes(hashes);
        log.debug("Deleting torrents: {} (deleteFiles: {})", hashesParam, deleteFiles);
        post("/api/v2/torrents/delete", Map.of("hashes", hashesParam, "deleteFiles", String.valueOf(deleteFiles)));
    }

    /**
     * Recheck one or more torrents.
     *
     * @param hashes Collection of torrent hashes, or a singleton with "all" for all torrents
     */
    public void recheckTorrents(Collection<String> hashes) {
        String hashesParam = joinHashes(hashes);
        log.debug("Rechecking torrents: {}", hashesParam);
        post("/api/v2/torrents/recheck", Map.of("hashes", hashesParam));
    }

    /**
     * Reannounce one or more torrents.
     *
     * @param hashes Collection of torrent hashes, or a singleton with "all" for all torrents
     */
    public void reannounceTorrents(Collection<String> hashes) {
        String hashesParam = joinHashes(hashes);
        log.debug("Reannouncing torrents: {}", hashesParam);
        post("/api/v2/torrents/reannounce", Map.of("hashes", hashesParam));
    }

    /**
     * Set one or more torrents to maximum priority (top of queue).
     * Idempotent operation - setting already top-priority torrent is safe.
     * Requires queueing to be enabled.
     *
     * @param hashes Collection of torrent hashes, or a singleton with "all" for all torrents
     */
    public void setTopPriority(Collection<String> hashes) {
        String hashesParam = joinHashes(hashes);
        log.debug("Setting top priority for torrents: {}", hashesParam);
        post("/api/v2/torrents/topPrio", Map.of("hashes", hashesParam));
    }

    /**
     * Set one or more torrents to minimum priority (bottom of queue).
     * Idempotent operation - setting already bottom-priority torrent is safe.
     * Requires queueing to be enabled.
     *
     * @param hashes Collection of torrent hashes, or a singleton with "all" for all torrents
     */
    public void setBottomPriority(Collection<String> hashes) {
        String hashesParam = joinHashes(hashes);
        log.debug("Setting bottom priority for torrents: {}", hashesParam);
        post("/api/v2/torrents/bottomPrio", Map.of("hashes", hashesParam));
    }

    /**
     * Increase priority of one or more torrents.
     * Idempotent operation - safe to call multiple times.
     * Requires queueing to be enabled.
     *
     * @param hashes Collection of torrent hashes, or a singleton with "all" for all torrents
     */
    public void increasePriority(Collection<String> hashes) {
        String hashesParam = joinHashes(hashes);
        log.debug("Increasing priority for torrents: {}", hashesParam);
        post("/api/v2/torrents/increasePrio", Map.of("hashes", hashesParam));
    }

    /**
     * Decrease priority of one or more torrents.
     * Idempotent operation - safe to call multiple times.
     * Requires queueing to be enabled.
     *
     * @param hashes Collection of torrent hashes, or a singleton with "all" for all torrents
     */
    public void decreasePriority(Collection<String> hashes) {
        String hashesParam = joinHashes(hashes);
        log.debug("Decreasing priority for torrents: {}", hashesParam);
        post("/api/v2/torrents/decreasePrio", Map.of("hashes", hashesParam));
    }

    // ==================== CATEGORIES ====================

    /**
     * Get all categories.
     *
     * @return map of category name to category info
     */
    public Map<String, Object> getAllCategories() {
        log.debug("Fetching all categories");
        return get("/api/v2/torrents/categories", Map.of(), new TypeReference<Map<String, Object>>() {});
    }

    /**
     * Set torrent category.
     * Idempotent operation - safe to set same category multiple times.
     *
     * @param hashes Collection of torrent hashes, or a singleton with "all" for all torrents
     * @param category Category name
     */
    public void setTorrentCategory(Collection<String> hashes, String category) {
        String hashesParam = joinHashes(hashes);
        log.debug("Setting category {} for torrents: {}", category, hashesParam);
        post(
                "/api/v2/torrents/setCategory",
                Map.of(
                        "hashes", hashesParam,
                        "category", category));
    }

    // ==================== TAGS ====================

    /**
     * Get all tags.
     *
     * @return list of all tags
     */
    public List<String> getAllTags() {
        log.debug("Fetching all tags");
        return get("/api/v2/torrents/tags", Map.of(), new TypeReference<List<String>>() {});
    }

    /**
     * Add tags to torrents.
     * Idempotent operation - adding existing tags is safe.
     *
     * @param hashes Collection of torrent hashes, or a singleton with "all" for all torrents
     * @param tags Collection of tags
     */
    public void addTorrentTags(Collection<String> hashes, Collection<String> tags) {
        String hashesParam = joinHashes(hashes);
        String tagsParam = joinTags(tags);
        log.debug("Adding tags {} to torrents: {}", tagsParam, hashesParam);
        post(
                "/api/v2/torrents/addTags",
                Map.of(
                        "hashes", hashesParam,
                        "tags", tagsParam));
    }

    /**
     * Remove tags from torrents.
     * Idempotent operation - removing non-existent tags is safe.
     *
     * @param hashes Collection of torrent hashes, or a singleton with "all" for all torrents
     * @param tags Collection of tags
     */
    public void removeTorrentTags(Collection<String> hashes, Collection<String> tags) {
        String hashesParam = joinHashes(hashes);
        String tagsParam = joinTags(tags);
        log.debug("Removing tags {} from torrents: {}", tagsParam, hashesParam);
        post(
                "/api/v2/torrents/removeTags",
                Map.of(
                        "hashes", hashesParam,
                        "tags", tagsParam));
    }

    /**
     * Create new tags.
     * Idempotent operation - creating existing tags is safe.
     *
     * @param tags Collection of tags
     */
    public void createTags(Collection<String> tags) {
        String tagsParam = joinTags(tags);
        log.debug("Creating tags: {}", tagsParam);
        post("/api/v2/torrents/createTags", Map.of("tags", tagsParam));
    }

    /**
     * Delete tags.
     *
     * @param tags Collection of tags
     */
    public void deleteTags(Collection<String> tags) {
        String tagsParam = joinTags(tags);
        log.debug("Deleting tags: {}", tagsParam);
        post("/api/v2/torrents/deleteTags", Map.of("tags", tagsParam));
    }
    /**
     * Join torrent hashes with | as required by qBittorrent API.
     */
    private String joinHashes(Collection<String> hashes) {
        if (hashes == null || hashes.isEmpty()) {
            throw new IllegalArgumentException("hashes collection must not be null or empty");
        }
        // If "all" is present, use it directly
        if (hashes.size() == 1 && "all".equals(hashes.iterator().next())) {
            return "all";
        }
        return String.join("|", hashes);
    }

    /**
     * Join tags with , as required by qBittorrent API.
     */
    private String joinTags(Collection<String> tags) {
        if (tags == null || tags.isEmpty()) {
            throw new IllegalArgumentException("tags collection must not be null or empty");
        }
        return String.join(",", tags);
    }

    // ==================== HTTP HELPERS ====================

    /**
     * Execute GET request with path and query parameters.
     */
    private <T> T get(String path, Map<String, String> params, TypeReference<T> typeReference) {
        HttpUrl.Builder urlBuilder = baseUrl.newBuilder().addPathSegments(path.replaceFirst("^/", ""));
        params.forEach(urlBuilder::addQueryParameter);
        return get(urlBuilder.build(), typeReference);
    }

    /**
     * Execute GET request with full URL.
     */
    private <T> T get(HttpUrl url, TypeReference<T> typeReference) {
        ensureAuthenticated();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Cookie", sessionCookie)
                .build();

        log.debug("GET {}", url);

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.code() == 403 || response.code() == 401) {
                // Session may have expired, try re-authenticating once
                log.debug("Authentication failed, re-authenticating");
                sessionCookie = null;
                ensureAuthenticated();

                // Retry request with new session
                request = request.newBuilder().header("Cookie", sessionCookie).build();
                try (Response retryResponse = httpClient.newCall(request).execute()) {
                    return handleResponse(retryResponse, typeReference);
                }
            }

            return handleResponse(response, typeReference);
        } catch (IOException e) {
            throw new QBittorrentApiException("GET request failed: " + url, e);
        }
    }

    /**
     * Execute POST request with form parameters.
     */
    private void post(String path, Map<String, String> params) {
        ensureAuthenticated();

        HttpUrl url = baseUrl.newBuilder()
                .addPathSegments(path.replaceFirst("^/", ""))
                .build();

        FormBody.Builder formBuilder = new FormBody.Builder();
        params.forEach(formBuilder::add);

        Request request = new Request.Builder()
                .url(url)
                .post(formBuilder.build())
                .header("Cookie", sessionCookie)
                .build();

        log.debug("POST {} with params: {}", url, params);

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.code() == 403 || response.code() == 401) {
                // Session may have expired, try re-authenticating once
                log.debug("Authentication failed, re-authenticating");
                sessionCookie = null;
                ensureAuthenticated();

                // Retry request with new session
                request = request.newBuilder().header("Cookie", sessionCookie).build();
                try (Response retryResponse = httpClient.newCall(request).execute()) {
                    if (!retryResponse.isSuccessful()) {
                        String errorBody = retryResponse.body() != null
                                ? retryResponse.body().string()
                                : "";
                        throw new QBittorrentApiException(
                                "POST request failed with HTTP " + retryResponse.code() + ": " + url,
                                retryResponse.code(),
                                errorBody);
                    }
                    return;
                }
            }

            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "";
                throw new QBittorrentApiException(
                        "POST request failed with HTTP " + response.code() + ": " + url, response.code(), errorBody);
            }
        } catch (IOException e) {
            throw new QBittorrentApiException("POST request failed: " + url, e);
        }
    }

    /**
     * Handle HTTP response and deserialize JSON.
     */
    private <T> T handleResponse(Response response, TypeReference<T> typeReference) throws IOException {
        if (!response.isSuccessful()) {
            String errorBody = response.body() != null ? response.body().string() : "";
            throw new QBittorrentApiException(
                    "Request failed with HTTP " + response.code(), response.code(), errorBody);
        }

        if (response.body() == null) {
            throw new QBittorrentApiException("Response body is null", response.code(), "");
        }

        String responseBody = response.body().string();
        log.debug("Response: {}", responseBody);

        try {
            return objectMapper.readValue(responseBody, typeReference);
        } catch (IOException e) {
            log.error("Failed to parse qBittorrent response", e);
            throw new QBittorrentClientException("Failed to parse qBittorrent response", e);
        }
    }
}

package de.llalon.cinematic.client.tautulli;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import de.llalon.cinematic.client.tautulli.config.TautulliProperties;
import de.llalon.cinematic.client.tautulli.dto.*;
import de.llalon.cinematic.client.tautulli.exception.TautulliApiException;
import de.llalon.cinematic.client.tautulli.exception.TautulliClientException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Client service for interacting with the Tautulli API.
 *
 * Tautulli is a monitoring and tracking tool for Plex Media Server.
 * This client provides type-safe access to Tautulli API endpoints.
 *
 * Authentication is handled via API key passed as query parameter.
 * All API calls are synchronous and blocking.
 *
 * Key features:
 * - Real-time activity monitoring (current streams/sessions)
 * - Historical play data (watch history with filtering)
 * - User management and statistics
 * - Library information and statistics
 * - Idempotent operations safe for replay
 *
 * @see <a href="https://github.com/Tautulli/Tautulli/wiki/Tautulli-API-Reference">Tautulli API Reference</a>
 */
@Slf4j
public class TautulliClient {

    private final String apiKey;
    private final OkHttpClient httpClient;
    private final Moshi moshi;
    private final HttpUrl baseUrl;

    public TautulliClient(OkHttpClient httpClient, TautulliProperties properties, Moshi moshi) {
        this.moshi = moshi;
        this.apiKey = properties.getApiKey();
        this.httpClient = httpClient;
        this.baseUrl = HttpUrl.parse(properties.getUrl());
        if (this.baseUrl == null) {
            throw new IllegalArgumentException("Invalid Tautulli URL: " + properties.getUrl());
        }
        if (this.apiKey == null || this.apiKey.isEmpty()) {
            throw new IllegalArgumentException("Invalid API Key: " + properties.getApiKey());
        }
    }

    // ==================== ACTIVITY / SESSIONS ====================

    /**
     * Get the current activity on the Plex Media Server.
     * Returns all active streaming sessions with detailed information.
     *
     * @return current activity including all active sessions
     */
    public Activity getActivity() {
        log.debug("Fetching current activity");
        return executeCommand("get_activity", Map.of(), Activity.class);
    }

    /**
     * Get a specific session by session key.
     *
     * @param sessionKey The session key to retrieve
     * @return activity containing the specific session
     */
    public Activity getActivity(String sessionKey) {
        log.debug("Fetching activity for session key: {}", sessionKey);
        return executeCommand("get_activity", Map.of("session_key", sessionKey), Activity.class);
    }

    /**
     * Get a specific session by session ID.
     *
     * @param sessionId The session ID to retrieve
     * @return activity containing the specific session
     */
    public Activity getActivityBySessionId(String sessionId) {
        log.debug("Fetching activity for session ID: {}", sessionId);
        return executeCommand("get_activity", Map.of("session_id", sessionId), Activity.class);
    }

    // ==================== HISTORY ====================

    /**
     * Get the Tautulli watch history.
     * Returns a paginated list of completed streaming sessions.
     *
     * @param start Row to start from (0-based)
     * @param length Number of items to return
     * @return paginated history response
     */
    public TableResponse<History> getHistory(int start, int length) {
        log.debug("Fetching history: start={}, length={}", start, length);
        Map<String, Object> params = new HashMap<>();
        params.put("start", start);
        params.put("length", length);
        Type type = Types.newParameterizedType(TableResponse.class, History.class);
        return executeCommand("get_history", params, type);
    }

    /**
     * Get watch history for a specific user.
     *
     * @param userId User ID to filter by
     * @param start Row to start from (0-based)
     * @param length Number of items to return
     * @return paginated history response
     */
    public TableResponse<History> getHistoryByUser(int userId, int start, int length) {
        log.debug("Fetching history for user {}: start={}, length={}", userId, start, length);
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("start", start);
        params.put("length", length);
        Type type = Types.newParameterizedType(TableResponse.class, History.class);
        return executeCommand("get_history", params, type);
    }

    /**
     * Get watch history for a specific media item.
     *
     * @param ratingKey Rating key of the media item
     * @param start Row to start from (0-based)
     * @param length Number of items to return
     * @return paginated history response
     */
    public TableResponse<History> getHistoryByRatingKey(String ratingKey, int start, int length) {
        log.debug("Fetching history for rating key {}: start={}, length={}", ratingKey, start, length);
        Map<String, Object> params = new HashMap<>();
        params.put("rating_key", ratingKey);
        params.put("start", start);
        params.put("length", length);
        Type type = Types.newParameterizedType(TableResponse.class, History.class);
        return executeCommand("get_history", params, type);
    }

    // ==================== USERS ====================

    /**
     * Get a list of all users that have access to the Plex server.
     *
     * @return list of all users
     */
    public List<User> getUsers() {
        log.debug("Fetching all users");
        Type type = Types.newParameterizedType(List.class, User.class);
        return executeCommand("get_users", Map.of(), type);
    }

    /**
     * Get detailed information about a specific user.
     *
     * @param userId User ID to retrieve
     * @return user details
     */
    public User getUser(int userId) {
        log.debug("Fetching user with ID: {}", userId);
        return executeCommand("get_user", Map.of("user_id", userId), User.class);
    }

    /**
     * Get a list of all user names and IDs.
     * Returns a lightweight list without full user details.
     *
     * @return list of users with just ID and friendly name
     */
    public List<User> getUserNames() {
        log.debug("Fetching user names");
        Type type = Types.newParameterizedType(List.class, User.class);
        return executeCommand("get_user_names", Map.of(), type);
    }

    // ==================== LIBRARIES ====================

    /**
     * Get a list of all library sections on the Plex server.
     *
     * @return list of all libraries
     */
    public List<Library> getLibraries() {
        log.debug("Fetching all libraries");
        Type type = Types.newParameterizedType(List.class, Library.class);
        return executeCommand("get_libraries", Map.of(), type);
    }

    /**
     * Get a list of library section names and IDs.
     * Returns a lightweight list without full library details.
     *
     * @return list of libraries with just ID, name, and type
     */
    public List<Library> getLibraryNames() {
        log.debug("Fetching library names");
        Type type = Types.newParameterizedType(List.class, Library.class);
        return executeCommand("get_library_names", Map.of(), type);
    }

    /**
     * Get detailed information about a specific library.
     *
     * @param sectionId Library section ID
     * @return library details
     */
    public Library getLibrary(String sectionId) {
        log.debug("Fetching library with section ID: {}", sectionId);
        return executeCommand("get_library", Map.of("section_id", sectionId), Library.class);
    }

    // ==================== GENERIC COMMAND EXECUTION ====================

    /**
     * Execute a raw Tautulli API command with custom parameters.
     * Useful for commands not yet implemented in this client.
     *
     * This is the core method that all other API methods delegate to.
     * It handles request construction, error handling, and response parsing.
     *
     * @param command The Tautulli API command name (e.g., "get_activity", "get_history")
     * @param params Map of query parameters to include in the request
     * @param responseType Type for the expected response data type
     * @param <T> The type of the response data
     * @return the parsed response data
     */
    public <T> T executeCommand(String command, Map<String, Object> params, Type responseType) {
        log.debug("Executing Tautulli command: {} with params: {}", command, params);

        // Build the URL with API key and command
        HttpUrl.Builder urlBuilder = baseUrl.newBuilder()
                .addPathSegment("api")
                .addPathSegment("v2")
                .addQueryParameter("apikey", apiKey)
                .addQueryParameter("cmd", command);

        // Add all additional parameters
        params.forEach((key, value) -> {
            if (value != null) {
                urlBuilder.addQueryParameter(key, value.toString());
            }
        });

        HttpUrl url = urlBuilder.build();

        // Build and execute the request
        Request request = new Request.Builder().url(url).get().build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body().string();
                log.error("Tautulli API error: status={}, body={}", response.code(), errorBody);
                throw new TautulliApiException(
                        "Tautulli API request failed: HTTP " + response.code(), response.code(), errorBody);
            }

            String responseBody = response.body().string();
            return parseResponse(responseBody, responseType, command);

        } catch (IOException e) {
            log.error("Failed to execute Tautulli command: {}", command, e);
            throw new TautulliApiException("Failed to execute Tautulli command: " + command, e);
        }
    }

    // ==================== HELPER METHODS ====================

    /**
     * Parse JSON response body and extract data from Tautulli's envelope format.
     * Tautulli wraps all responses in a standard envelope with result status and data.
     *
     * @param responseBody Raw JSON string from API
     * @param responseType Target type for deserialization
     * @param command Command name for error logging
     * @param <T> Target type
     * @return parsed object
     */
    private <T> T parseResponse(String responseBody, Type responseType, String command) {
        try {
            // Validate the response
            if (responseBody == null || responseBody.isEmpty()) {
                throw new TautulliClientException("Received empty response from Tautulli", null);
            }

            final Type envelopeType = Types.newParameterizedType(TautulliResponse.class, responseType);

            final JsonAdapter<TautulliResponse<T>> adapter = moshi.adapter(envelopeType);

            final TautulliResponse<T> rawResponse = adapter.fromJson(responseBody);

            Objects.requireNonNull(rawResponse, "rawResponse is null");

            if (!rawResponse.isSuccess()) {
                String errorMessage = rawResponse.getMessage() != null ? rawResponse.getMessage() : "Unknown error";
                throw new TautulliApiException("Tautulli API returned error: " + errorMessage, -1, responseBody);
            }

            // Get the data node and convert it to the expected type
            final Object data = rawResponse.getData();

            if (data == null) {
                return null;
            }

            return rawResponse.getData();
        } catch (Exception e) {
            log.error("Unexpected error parsing Tautulli response for command: {}", command, e);
            throw new TautulliClientException("Failed to parse Tautulli response: " + command, e);
        }
    }
}

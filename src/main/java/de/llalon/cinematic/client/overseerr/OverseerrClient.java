package de.llalon.cinematic.client.overseerr;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import de.llalon.cinematic.client.overseerr.config.OverseerrProperties;
import de.llalon.cinematic.client.overseerr.dto.*;
import de.llalon.cinematic.client.overseerr.exception.OverseerrApiException;
import de.llalon.cinematic.client.overseerr.exception.OverseerrClientException;
import java.io.IOException;
import java.lang.reflect.Type;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

/**
 * Client service for interacting with the Overseerr API (v1).
 *
 * Overseerr is a request management and media discovery tool that works with Plex,
 * Sonarr, and Radarr. This client provides type-safe access to Overseerr API endpoints.
 *
 * Authentication is handled via API key passed in X-Api-Key header.
 * All API calls are synchronous and blocking.
 *
 * Key features:
 * - User management and quota tracking
 * - Request creation, approval, and tracking
 * - Media information retrieval
 * - Idempotent operations safe for replay
 *
 * @see <a href="https://docs.overseerr.dev/">Overseerr Documentation</a>
 */
@Slf4j
public class OverseerrClient {

    private static final String API_KEY_HEADER = "X-Api-Key";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final String apiKey;
    private final OkHttpClient httpClient;
    private final Moshi moshi;
    private final HttpUrl baseUrl;

    public OverseerrClient(OkHttpClient httpClient, OverseerrProperties properties, Moshi moshi) {
        this.moshi = moshi;
        this.apiKey = properties.getApiKey();
        this.httpClient = httpClient;
        this.baseUrl = HttpUrl.parse(properties.getUrl());
        if (this.baseUrl == null) {
            throw new IllegalArgumentException("Invalid Overseerr URL: " + properties.getUrl());
        }
        if (this.apiKey == null || this.apiKey.isEmpty()) {
            throw new IllegalArgumentException("Invalid API Key: " + properties.getApiKey());
        }
    }

    // ==================== USERS ====================

    /**
     * Get all users in Overseerr.
     *
     * @param take Number of results per page (optional)
     * @param skip Number of results to skip (optional)
     * @param sort Sort order: created, updated, requests, displayname (optional)
     * @return paged results of users
     */
    public PagedResults<User> getAllUsers(Integer take, Integer skip, String sort) {
        log.debug("Fetching all users with take={}, skip={}, sort={}", take, skip, sort);
        HttpUrl.Builder urlBuilder = baseUrl.newBuilder().addPathSegments("api/v1/user");
        if (take != null) {
            urlBuilder.addQueryParameter("take", String.valueOf(take));
        }
        if (skip != null) {
            urlBuilder.addQueryParameter("skip", String.valueOf(skip));
        }
        if (sort != null) {
            urlBuilder.addQueryParameter("sort", sort);
        }
        Type type = Types.newParameterizedType(PagedResults.class, User.class);
        return get(urlBuilder.build(), type);
    }

    /**
     * Get all users in Overseerr with default pagination.
     *
     * @return paged results of users
     */
    public PagedResults<User> getAllUsers() {
        return getAllUsers(null, null, null);
    }

    /**
     * Get a specific user by ID.
     *
     * @param userId User ID
     * @return user details
     */
    public User getUser(int userId) {
        log.debug("Fetching user with ID: {}", userId);
        return get("/api/v1/user/" + userId, User.class);
    }

    /**
     * Create a new user.
     * Requires MANAGE_USERS permission.
     *
     * @param user User object with email, username, and permissions
     * @return created user with ID
     */
    public User createUser(User user) {
        log.debug("Creating user: {}", user.getEmail());
        return post("/api/v1/user", user, User.class);
    }

    /**
     * Update an existing user.
     * Idempotent operation - safe to call multiple times with same data.
     * Requires MANAGE_USERS permission.
     *
     * @param userId User ID
     * @param user User object with updated fields
     * @return updated user
     */
    public User updateUser(int userId, User user) {
        log.debug("Updating user with ID: {}", userId);
        return put("/api/v1/user/" + userId, user, User.class);
    }

    /**
     * Delete a user from Overseerr.
     * Requires MANAGE_USERS permission.
     *
     * @param userId User ID
     * @return deleted user details
     */
    public User deleteUser(int userId) {
        log.debug("Deleting user with ID: {}", userId);
        Type type = Types.newParameterizedType(PagedResults.class, User.class);
        return delete("/api/v1/user/" + userId, type);
    }

    // ==================== REQUESTS ====================

    /**
     * Get all requests in Overseerr.
     * Returns all requests if user has ADMIN or MANAGE_REQUESTS permissions.
     * Otherwise, only returns the logged-in user's requests.
     *
     * @param take Number of results per page (optional)
     * @param skip Number of results to skip (optional)
     * @param filter Filter by status: all, approved, available, pending, processing, unavailable, failed, deleted,
     *     completed (optional)
     * @param sort Sort order: added, modified (optional)
     * @param requestedBy Filter by user ID (optional)
     * @return paged results of media requests
     */
    public PagedResults<MediaRequest> getAllRequests(
            Integer take, Integer skip, String filter, String sort, Integer requestedBy) {
        log.debug(
                "Fetching all requests with take={}, skip={}, filter={}, sort={}, requestedBy={}",
                take,
                skip,
                filter,
                sort,
                requestedBy);
        HttpUrl.Builder urlBuilder = baseUrl.newBuilder().addPathSegments("api/v1/request");
        if (take != null) {
            urlBuilder.addQueryParameter("take", String.valueOf(take));
        }
        if (skip != null) {
            urlBuilder.addQueryParameter("skip", String.valueOf(skip));
        }
        if (filter != null) {
            urlBuilder.addQueryParameter("filter", filter);
        }
        if (sort != null) {
            urlBuilder.addQueryParameter("sort", sort);
        }
        if (requestedBy != null) {
            urlBuilder.addQueryParameter("requestedBy", String.valueOf(requestedBy));
        }
        Type type = Types.newParameterizedType(PagedResults.class, MediaRequest.class);
        return get(urlBuilder.build(), type);
    }

    /**
     * Get all requests with default parameters.
     *
     * @return paged results of media requests
     */
    public PagedResults<MediaRequest> getAllRequests() {
        return getAllRequests(null, null, null, null, null);
    }

    /**
     * Get a specific request by ID.
     *
     * @param requestId Request ID
     * @return request details
     */
    public MediaRequest getRequest(int requestId) {
        log.debug("Fetching request with ID: {}", requestId);
        return get("/api/v1/request/" + requestId, MediaRequest.class);
    }

    /**
     * Get requests for a specific user.
     *
     * @param userId User ID
     * @param take Number of results per page (optional)
     * @param skip Number of results to skip (optional)
     * @return paged results of user's media requests
     */
    public PagedResults<MediaRequest> getUserRequests(int userId, Integer take, Integer skip) {
        log.debug("Fetching requests for user ID: {} with take={}, skip={}", userId, take, skip);
        HttpUrl.Builder urlBuilder = baseUrl.newBuilder().addPathSegments("api/v1/user/" + userId + "/requests");
        if (take != null) {
            urlBuilder.addQueryParameter("take", String.valueOf(take));
        }
        if (skip != null) {
            urlBuilder.addQueryParameter("skip", String.valueOf(skip));
        }
        Type type = Types.newParameterizedType(PagedResults.class, MediaRequest.class);
        return get(urlBuilder.build(), type);
    }

    /**
     * Get requests for a specific user with default pagination.
     *
     * @param userId User ID
     * @return paged results of user's media requests
     */
    public PagedResults<MediaRequest> getUserRequests(int userId) {
        return getUserRequests(userId, null, null);
    }

    /**
     * Get request counts.
     * Returns the number of pending and approved requests.
     *
     * @return request count statistics
     */
    public RequestCount getRequestCount() {
        log.debug("Fetching request counts");
        return get("/api/v1/request/count", RequestCount.class);
    }

    /**
     * Create a new media request.
     * Requires REQUEST permission.
     * If user has ADMIN or AUTO_APPROVE permissions, request is automatically approved.
     * Idempotent - check if request exists before creating to avoid duplicates.
     *
     * @param requestBody Request body with mediaType, mediaId, and optional parameters
     * @return created request with ID
     */
    public MediaRequest createRequest(CreateRequestBody requestBody) {
        log.debug("Creating request for media ID: {} type: {}", requestBody.getMediaId(), requestBody.getMediaType());
        return post("/api/v1/request", requestBody, MediaRequest.class);
    }

    /**
     * Update an existing request.
     * Idempotent operation - safe to call multiple times with same data.
     * Requires MANAGE_REQUESTS permission.
     *
     * @param requestId Request ID
     * @param requestBody Request body with updated fields
     * @return updated request
     */
    public MediaRequest updateRequest(int requestId, CreateRequestBody requestBody) {
        log.debug("Updating request with ID: {}", requestId);
        return put("/api/v1/request/" + requestId, requestBody, MediaRequest.class);
    }

    /**
     * Delete a request from Overseerr.
     * If user has MANAGE_REQUESTS permission, any request can be removed.
     * Otherwise, only pending requests can be removed.
     *
     * @param requestId Request ID
     */
    public void deleteRequest(int requestId) {
        log.debug("Deleting request with ID: {}", requestId);
        delete("/api/v1/request/" + requestId);
    }

    /**
     * Approve a request.
     * Requires MANAGE_REQUESTS permission or ADMIN.
     *
     * @param requestId Request ID
     * @return updated request with approved status
     */
    public MediaRequest approveRequest(int requestId) {
        log.debug("Approving request with ID: {}", requestId);
        return post("/api/v1/request/" + requestId + "/approve", null, MediaRequest.class);
    }

    /**
     * Decline a request.
     * Requires MANAGE_REQUESTS permission or ADMIN.
     *
     * @param requestId Request ID
     * @return updated request with declined status
     */
    public MediaRequest declineRequest(int requestId) {
        log.debug("Declining request with ID: {}", requestId);
        return post("/api/v1/request/" + requestId + "/decline", null, MediaRequest.class);
    }

    /**
     * Retry a failed request.
     * Resends requests to Sonarr or Radarr.
     * Requires MANAGE_REQUESTS permission or ADMIN.
     *
     * @param requestId Request ID
     * @return updated request
     */
    public MediaRequest retryRequest(int requestId) {
        log.debug("Retrying request with ID: {}", requestId);
        return post("/api/v1/request/" + requestId + "/retry", null, MediaRequest.class);
    }

    // ==================== MEDIA ====================

    /**
     * Get all media in Overseerr.
     *
     * @param take Number of results per page (optional)
     * @param skip Number of results to skip (optional)
     * @param filter Filter by status (optional)
     * @param sort Sort order (optional)
     * @return paged results of media
     */
    public PagedResults<MediaInfo> getAllMedia(Integer take, Integer skip, String filter, String sort) {
        log.debug("Fetching all media with take={}, skip={}, filter={}, sort={}", take, skip, filter, sort);
        HttpUrl.Builder urlBuilder = baseUrl.newBuilder().addPathSegments("api/v1/media");
        if (take != null) {
            urlBuilder.addQueryParameter("take", String.valueOf(take));
        }
        if (skip != null) {
            urlBuilder.addQueryParameter("skip", String.valueOf(skip));
        }
        if (filter != null) {
            urlBuilder.addQueryParameter("filter", filter);
        }
        if (sort != null) {
            urlBuilder.addQueryParameter("sort", sort);
        }
        Type type = Types.newParameterizedType(PagedResults.class, MediaInfo.class);
        return get(urlBuilder.build(), type);
    }

    /**
     * Get all media with default parameters.
     *
     * @return paged results of media
     */
    public PagedResults<MediaInfo> getAllMedia() {
        return getAllMedia(null, null, null, null);
    }

    /**
     * Delete media from Overseerr.
     *
     * @param mediaId Media ID
     */
    public void deleteMedia(int mediaId) {
        log.debug("Deleting media with ID: {}", mediaId);
        delete("/api/v1/media/" + mediaId);
    }

    /**
     * Update media status.
     * Sets status to available or unavailable.
     *
     * @param mediaId Media ID
     * @param status Status: "available" or "unavailable"
     * @return updated media info
     */
    public MediaInfo updateMediaStatus(int mediaId, String status) {
        log.debug("Updating media ID: {} to status: {}", mediaId, status);
        return post("/api/v1/media/" + mediaId + "/" + status, null, MediaInfo.class);
    }

    // ==================== HTTP METHODS ====================

    /**
     * Execute a GET request to the Overseerr API.
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
     * Execute a GET request to the Overseerr API.
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
     * Execute a POST request to the Overseerr API.
     *
     * @param path API path
     * @param body Request body object (can be null)
     * @param responseType Type for the expected response type
     * @param <T> The type of the response
     * @return parsed response
     */
    private <T> T post(String path, Object body, Type responseType) {
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegments(path.replaceFirst("^/", ""))
                .build();

        try {
            RequestBody requestBody;
            if (body != null) {
                String jsonBody = moshi.adapter(body.getClass()).toJson(body);
                requestBody = RequestBody.create(jsonBody, JSON);
            } else {
                requestBody = RequestBody.create("", JSON);
            }

            Request request = new Request.Builder()
                    .url(url)
                    .header(API_KEY_HEADER, apiKey)
                    .post(requestBody)
                    .build();

            return executeRequest(request, responseType);
        } catch (Exception e) {
            log.error("Failed to serialize request body for POST {}", path, e);
            throw new OverseerrClientException("Failed to serialize request body for POST " + path, e);
        }
    }

    /**
     * Execute a PUT request to the Overseerr API.
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
            throw new OverseerrClientException("Failed to serialize request body for PUT " + path, e);
        }
    }

    /**
     * Execute a DELETE request to the Overseerr API.
     *
     * @param path API path
     */
    private void delete(String path) {
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegments(path.replaceFirst("^/", ""))
                .build();
        delete(url, null);
    }

    /**
     * Execute a DELETE request to the Overseerr API with response body.
     *
     * @param path API path
     * @param responseType Type for the expected response type
     * @param <T> The type of the response
     * @return parsed response
     */
    private <T> T delete(String path, Type responseType) {
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegments(path.replaceFirst("^/", ""))
                .build();
        return delete(url, responseType);
    }

    /**
     * Execute a DELETE request to the Overseerr API.
     *
     * @param url Full URL
     * @param responseType Type for the expected response type (can be null)
     * @param <T> The type of the response
     * @return parsed response or null
     */
    private <T> T delete(HttpUrl url, Type responseType) {
        Request request = new Request.Builder()
                .url(url)
                .header(API_KEY_HEADER, apiKey)
                .delete()
                .build();

        if (responseType != null) {
            return executeRequest(request, responseType);
        } else {
            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful() && response.code() != 204) {
                    String errorBody = response.body() != null ? response.body().string() : "";
                    log.error("Overseerr API error: status={}, body={}", response.code(), errorBody);
                    throw new OverseerrApiException(
                            "Overseerr API request failed: HTTP " + response.code(), response.code(), errorBody);
                }
            } catch (IOException e) {
                log.error("Failed to execute DELETE request", e);
                throw new OverseerrApiException("Failed to execute DELETE request", e);
            }
            return null;
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
            if (!response.isSuccessful() && response.code() != 204) {
                String errorBody = response.body() != null ? response.body().string() : "";
                log.error("Overseerr API error: status={}, body={}", response.code(), errorBody);
                throw new OverseerrApiException(
                        "Overseerr API request failed: HTTP " + response.code(), response.code(), errorBody);
            }

            String responseBody = response.body() != null ? response.body().string() : "";
            if (responseBody.isEmpty() || response.code() == 204) {
                return null;
            }

            try {
                JsonAdapter<T> adapter = (JsonAdapter<T>) moshi.adapter(responseType);
                return adapter.fromJson(responseBody);
            } catch (IOException parseException) {
                log.error("Failed to parse Overseerr response: {}", request.url(), parseException);
                throw new OverseerrClientException(
                        "Failed to parse Overseerr response: " + request.url(), parseException);
            }
        } catch (IOException e) {
            log.error("Failed to execute Overseerr request: {}", request.url(), e);
            throw new OverseerrApiException("Failed to execute Overseerr request: " + request.url(), e);
        }
    }
}

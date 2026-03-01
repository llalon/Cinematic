package de.llalon.cinematic.client.plex.exception;

import lombok.Getter;

/**
 * Exception thrown when Plex API calls fail.
 */
@Getter
public class PlexApiException extends RuntimeException {

    private final Integer statusCode;
    private final String responseBody;

    /**
     * Creates an exception for a failed HTTP response.
     *
     * @param message human-readable error description
     * @param statusCode HTTP status code returned by the Plex API
     * @param responseBody raw response body (may be empty)
     */
    public PlexApiException(String message, int statusCode, String responseBody) {
        super(message);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    /**
     * Creates an exception wrapping an underlying I/O or network failure.
     *
     * @param message human-readable error description
     * @param cause the underlying exception
     */
    public PlexApiException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = null;
        this.responseBody = null;
    }

    /**
     * Returns {@code true} if the error is potentially transient and the request may be retried.
     * Rate-limit (429) and server-error (5xx) responses are considered retryable.
     *
     * @return {@code true} if retrying the request may succeed
     */
    public boolean isRetryable() {
        return statusCode != null && (statusCode == 429 || statusCode >= 500);
    }
}

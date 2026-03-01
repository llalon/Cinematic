package de.llalon.cinematic.client.overseerr.exception;

import lombok.Getter;

/**
 * Exception thrown when Overseerr API calls fail.
 * This indicates remote API errors such as HTTP errors, network failures,
 * authentication failures, or rate limiting.
 */
@Getter
public class OverseerrApiException extends RuntimeException {

    private final Integer statusCode;
    private final String responseBody;

    /**
     * Creates an exception for a failed HTTP response.
     *
     * @param message human-readable error description
     * @param statusCode HTTP status code returned by the Overseerr API
     * @param responseBody raw response body (may be empty)
     */
    public OverseerrApiException(String message, int statusCode, String responseBody) {
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
    public OverseerrApiException(String message, Throwable cause) {
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

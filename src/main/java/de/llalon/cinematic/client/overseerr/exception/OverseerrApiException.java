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

    public OverseerrApiException(String message, int statusCode, String responseBody) {
        super(message);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public OverseerrApiException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = null;
        this.responseBody = null;
    }

    public boolean isRetryable() {
        return statusCode != null && (statusCode == 429 || statusCode >= 500);
    }
}

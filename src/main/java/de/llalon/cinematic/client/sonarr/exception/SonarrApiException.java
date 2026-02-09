package de.llalon.cinematic.client.sonarr.exception;

import lombok.Getter;

/**
 * Exception thrown when Sonarr API returns an error response.
 * This indicates remote API errors such as HTTP errors, network failures,
 * authentication failures, or rate limiting.
 */
@Getter
public class SonarrApiException extends RuntimeException {

    private final Integer statusCode;
    private final String responseBody;

    public SonarrApiException(String message, int statusCode, String responseBody) {
        super(message);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public SonarrApiException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = null;
        this.responseBody = null;
    }

    public boolean isRetryable() {
        return statusCode != null && (statusCode == 429 || statusCode >= 500);
    }
}

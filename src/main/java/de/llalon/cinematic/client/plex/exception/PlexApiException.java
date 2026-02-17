package de.llalon.cinematic.client.plex.exception;

import lombok.Getter;

/**
 * Exception thrown when Plex API calls fail.
 */
@Getter
public class PlexApiException extends RuntimeException {

    private final Integer statusCode;
    private final String responseBody;

    public PlexApiException(String message, int statusCode, String responseBody) {
        super(message);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public PlexApiException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = null;
        this.responseBody = null;
    }

    public boolean isRetryable() {
        return statusCode != null && (statusCode == 429 || statusCode >= 500);
    }
}

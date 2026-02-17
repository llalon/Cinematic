package de.llalon.cinematic.client.qbittorrent.exception;

import lombok.Getter;

/**
 * Exception thrown when qBittorrent API requests fail.
 * This indicates remote API errors such as HTTP errors, network failures,
 * authentication failures, or rate limiting.
 */
@Getter
public class QBittorrentApiException extends RuntimeException {

    private final Integer statusCode;
    private final String responseBody;

    public QBittorrentApiException(String message, int statusCode, String responseBody) {
        super(message);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public QBittorrentApiException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = null;
        this.responseBody = null;
    }

    public boolean isRetryable() {
        return statusCode != null && (statusCode == 429 || statusCode >= 500);
    }
}

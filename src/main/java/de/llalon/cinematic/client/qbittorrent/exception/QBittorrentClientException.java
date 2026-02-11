package de.llalon.cinematic.client.qbittorrent.exception;

/**
 * Exception thrown when qBittorrent client fails to parse or serialize data.
 * This indicates local client errors such as JSON serialization/deserialization failures,
 * schema mismatches, or invalid configuration.
 *
 * These errors are never retryable and typically indicate bugs or breaking API changes.
 */
public class QBittorrentClientException extends RuntimeException {

    public QBittorrentClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public boolean isRetryable() {
        return false;
    }
}

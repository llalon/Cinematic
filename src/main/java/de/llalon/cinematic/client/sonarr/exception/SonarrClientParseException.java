package de.llalon.cinematic.client.sonarr.exception;

/**
 * Exception thrown when Sonarr client fails to parse or serialize data.
 * This indicates local client errors such as JSON serialization/deserialization failures,
 * schema mismatches, or invalid configuration.
 *
 * These errors are never retryable and typically indicate bugs or breaking API changes.
 */
public class SonarrClientParseException extends RuntimeException {

    public SonarrClientParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public boolean isRetryable() {
        return false;
    }
}

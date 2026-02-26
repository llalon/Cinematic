package de.llalon.cinematic.client.plex.exception;

/**
 * Exception thrown when client-side errors occur (serialization/parsing/etc).
 */
public class PlexClientException extends RuntimeException {
    public PlexClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlexClientException(String message) {
        super(message);
    }

    public boolean isRetryable() {
        return false;
    }
}

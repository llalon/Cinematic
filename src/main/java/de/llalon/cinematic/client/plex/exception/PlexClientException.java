package de.llalon.cinematic.client.plex.exception;

/**
 * Exception thrown when client-side errors occur (serialization/parsing/etc).
 */
public class PlexClientException extends RuntimeException {
    /**
     * Creates an exception wrapping a local client-side failure.
     *
     * @param message human-readable error description
     * @param cause the underlying parse or serialization exception
     */
    public PlexClientException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates an exception for a local client-side failure without a cause.
     *
     * @param message human-readable error description
     */
    public PlexClientException(String message) {
        super(message);
    }

    /**
     * Always returns {@code false} — local client errors are not retryable.
     *
     * @return {@code false}
     */
    public boolean isRetryable() {
        return false;
    }
}

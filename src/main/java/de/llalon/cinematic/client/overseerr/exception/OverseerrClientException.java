package de.llalon.cinematic.client.overseerr.exception;

/**
 * Exception thrown when Overseerr client fails to parse or serialize data.
 * This indicates local client errors such as JSON serialization/deserialization failures,
 * schema mismatches, or invalid configuration.
 *
 * These errors are never retryable and typically indicate bugs or breaking API changes.
 */
public class OverseerrClientException extends RuntimeException {

    /**
     * Creates an exception wrapping a local client-side failure.
     *
     * @param message human-readable error description
     * @param cause the underlying parse or serialization exception
     */
    public OverseerrClientException(String message, Throwable cause) {
        super(message, cause);
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

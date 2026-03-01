package de.llalon.cinematic.domain;

/**
 * Exception thrown when an operation requires a client that has not been configured
 * in the {@link ClientContext}.
 *
 * <p>This is thrown eagerly when a domain method attempts to use a service client
 * that was not provided or could not be auto-configured from environment variables.</p>
 */
public class ClientNotConfiguredException extends RuntimeException {

    /**
     * Creates a new {@code ClientNotConfiguredException} with the given message.
     *
     * @param message description of which client is missing
     */
    public ClientNotConfiguredException(String message) {
        super(message);
    }
}

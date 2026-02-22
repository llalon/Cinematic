package de.llalon.cinematic.domain;

public class ClientNotConfiguredException extends RuntimeException {
    public ClientNotConfiguredException(String message) {
        super(message);
    }
}

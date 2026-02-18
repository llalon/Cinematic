package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.overseerr.dto.MediaRequest;

public class Request extends DomainModel {

    private final MediaRequest overseerrRequest;

    Request(ClientContext ctx, MediaRequest overseerrRequest) {
        super(ctx);
        this.overseerrRequest = overseerrRequest;
    }

    public User user() {
        return new User(ctx, overseerrRequest.getRequestedBy());
    }
}

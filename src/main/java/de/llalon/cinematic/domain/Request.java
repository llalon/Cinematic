package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.overseerr.dto.MediaRequest;
import lombok.experimental.Delegate;

public class Request extends DomainModel {

    @Delegate
    private final MediaRequest overseerrRequest;

    Request(ClientContext ctx, MediaRequest overseerrRequest) {
        super(ctx);
        this.overseerrRequest = overseerrRequest;
    }
}

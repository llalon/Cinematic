package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.overseerr.dto.MediaRequest;
import lombok.experimental.Delegate;
import org.jetbrains.annotations.NotNull;

public class Request extends DomainModel {

    @Delegate
    @NotNull
    private final MediaRequest overseerrRequest;

    Request(@NotNull ClientContext ctx, @NotNull MediaRequest overseerrRequest) {
        super(ctx);
        this.overseerrRequest = overseerrRequest;
    }

    /**
     * Returns the user who submitted this request.
     *
     * @return the requesting User
     */
    @NotNull
    public User user() {
        return new User(ctx, overseerrRequest.getRequestedBy());
    }
}

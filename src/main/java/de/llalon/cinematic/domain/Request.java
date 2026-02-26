package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.overseerr.dto.MediaRequest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class Request extends DomainModel {

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

    public String getTvdbId() {
        return this.overseerrRequest.getTvdbId();
    }

    public Integer getStatus() {
        return this.overseerrRequest.getStatus();
    }
}

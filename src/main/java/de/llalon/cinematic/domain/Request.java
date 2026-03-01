package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.overseerr.dto.MediaRequest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

/**
 * Domain representation of a media request made in Overseerr.
 *
 * <p>Provides access to the {@link User} who submitted the request plus
 * identifying metadata such as TVDB ID and request status.</p>
 */
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

    /**
     * Returns the TVDB identifier associated with this request, if applicable.
     *
     * @return the TVDB ID, or {@code null} if not a TV request
     */
    public String getTvdbId() {
        return this.overseerrRequest.getTvdbId();
    }

    /**
     * Returns the Overseerr status code for this request
     * (e.g. 1 = pending, 2 = approved, 3 = declined).
     *
     * @return the request status code
     */
    public Integer getStatus() {
        return this.overseerrRequest.getStatus();
    }
}

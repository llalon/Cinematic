package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.seerr.dto.MediaRequest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

/**
 * Domain representation of a media request made in Seerr.
 *
 * <p>Provides access to the {@link User} who submitted the request plus
 * identifying metadata such as TVDB ID and request status.</p>
 */
@Slf4j
public class Request extends DomainModel {

    @NotNull
    private final MediaRequest seerrRequest;

    Request(@NotNull ClientContext ctx, @NotNull MediaRequest seerrRequest) {
        super(ctx);
        this.seerrRequest = seerrRequest;
    }

    /**
     * Returns the user who submitted this request.
     *
     * @return the requesting User
     */
    @NotNull
    public User user() {
        return new User(ctx, seerrRequest.getRequestedBy());
    }

    /**
     * Returns the TVDB identifier associated with this request, if applicable.
     *
     * @return the TVDB ID, or {@code null} if not a TV request
     */
    public String getTvdbId() {
        return this.seerrRequest.getTvdbId();
    }

    /**
     * Returns the Seerr status code for this request
     * (e.g. 1 = pending, 2 = approved, 3 = declined).
     *
     * @return the request status code
     */
    public Integer getStatus() {
        return this.seerrRequest.getStatus();
    }
}

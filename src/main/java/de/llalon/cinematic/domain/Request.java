package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.seerr.dto.MediaRequest;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;

/**
 * Domain representation of a media request made in Seerr.
 *
 * <p>Provides access to the {@link User} who submitted the request plus
 * identifying metadata such as TVDB ID and request status.</p>
 */
@Slf4j
public class Request extends DomainModel {

    @NonNull
    private final MediaRequest seerrRequest;

    Request(@NonNull ClientContext ctx, @NonNull MediaRequest seerrRequest) {
        super(ctx);
        this.seerrRequest = seerrRequest;
    }

    /**
     * Returns the user who submitted this request.
     *
     * @return the requesting User
     */
    @NonNull
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

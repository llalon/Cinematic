package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.seerr.dto.SeerrUser;
import de.llalon.cinematic.client.tautulli.dto.TautulliUser;
import de.llalon.cinematic.util.collections.StreamUtils;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Domain representation of a user that may exist in one or both of Seerr and Tautulli.
 *
 * <p>Users are identified by email address which acts as the common key across systems.
 * Navigation methods expose the user's media {@link Request}s from Seerr and
 * their watch history ({@link Watches}) from Tautulli.</p>
 */
@Slf4j
public class User extends DomainModel {

    @Getter
    @NotNull
    private final String email;

    @Nullable // if the user does not exist in seerr
    @Getter(value = AccessLevel.PROTECTED, lazy = true)
    private final SeerrUser seerrUser = fetchSeerrUser();

    @Nullable // if the user does not exist in tautulli
    @Getter(value = AccessLevel.PROTECTED, lazy = true)
    private final TautulliUser tautulliUser = fetchTautulliUser();

    User(@NotNull ClientContext ctx, @NotNull TautulliUser tautulliUser) {
        super(ctx);
        this.email = tautulliUser.getEmail();
    }

    User(@NotNull ClientContext ctx, @NotNull SeerrUser seerrUser) {
        super(ctx);
        this.email = seerrUser.getEmail();
    }

    User(@NotNull ClientContext ctx, @NotNull String email) {
        super(ctx);
        this.email = email;
    }

    /**
     * Returns the watch history for this user from Tautulli, sorted by most recent first.
     * All media types are included. Results are lazily paged.
     *
     * @return an iterable of Watches objects sorted by date descending
     */
    @NotNull
    public Iterable<Watches> watches() {
        return () -> {
            try {
                final var userId =
                        Objects.requireNonNull(this.fetchTautulliUser()).getUserId();
                return tautulliHistoryByUser(userId)
                        .map(history -> new Watches(ctx, history))
                        .iterator();
            } catch (NullPointerException e) {
                log.warn("User {} does not exist in tautulli.", this.email);
                return StreamUtils.emptyIterator();
            }
        };
    }

    /**
     * Returns the media requests made by this user in Seerr.
     *
     * @return an iterable of Request objects
     */
    @NotNull
    public Iterable<Request> requests() {
        return () -> {
            try {
                final Integer userId =
                        Objects.requireNonNull(this.getSeerrUser()).getId();
                return seerrRequestsByUser(userId)
                        .map(request -> new Request(ctx, request))
                        .iterator();
            } catch (NullPointerException e) {
                log.warn("User {} does not exist in overseer.", this.email);
                return StreamUtils.emptyIterator();
            }
        };
    }

    @Nullable
    private TautulliUser fetchTautulliUser() {
        return super.tautulliUsers()
                .filter(u -> this.getEmail().equalsIgnoreCase(u.getEmail()))
                .findAny()
                .orElse(null);
    }

    @Nullable
    private SeerrUser fetchSeerrUser() {
        return super.seerrUsers()
                .filter(u -> this.getEmail().equalsIgnoreCase(u.getEmail()))
                .findAny()
                .orElse(null);
    }
}

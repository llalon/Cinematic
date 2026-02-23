package de.llalon.cinematic.domain;

import de.llalon.cinematic.util.collections.OffsetPagedIterable;
import de.llalon.cinematic.util.collections.StreamUtils;
import java.util.Objects;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Slf4j
public class User extends DomainModel {

    @Getter
    @NotNull
    private final String email;

    User(@NotNull ClientContext ctx, @NotNull de.llalon.cinematic.client.tautulli.dto.User tautulliUser) {
        super(ctx);
        this.email = tautulliUser.getEmail();
    }

    User(@NotNull ClientContext ctx, @NotNull de.llalon.cinematic.client.overseerr.dto.User overseerrUser) {
        super(ctx);
        this.email = overseerrUser.getEmail();
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
                return new OffsetPagedIterable<>((take, skip) -> ctx.getTautulliClient()
                                .getHistoryByUser(userId, skip, take)
                                .getData())
                        .stream().map(history -> new Watches(ctx, history)).iterator();
            } catch (NullPointerException e) {
                log.warn("User {} does not exist in tautulli.", this.email);
                return StreamUtils.emptyIterator();
            }
        };
    }

    /**
     * Returns the media requests made by this user in Overseerr.
     *
     * @return an iterable of Request objects
     */
    @NotNull
    public Iterable<Request> requests() {
        return () -> {
            try {
                final Integer userId =
                        Objects.requireNonNull(this.fetchOverseerrUser()).getId();
                return new OffsetPagedIterable<>((take, skip) -> ctx.getOverseerrClient()
                                .getAllRequests(take, skip, null, null, userId)
                                .getResults())
                        .stream().map(request -> new Request(ctx, request)).iterator();
            } catch (NullPointerException e) {
                log.warn("User {} does not exist in overseer.", this.email);
                return StreamUtils.emptyIterator();
            }
        };
    }

    @Nullable
    private de.llalon.cinematic.client.tautulli.dto.User fetchTautulliUser() {
        return ctx.getTautulliClient().getUsers().stream()
                .filter(u -> this.email.equalsIgnoreCase(u.getEmail()))
                .findAny()
                .orElse(null);
    }

    @Nullable
    private de.llalon.cinematic.client.overseerr.dto.User fetchOverseerrUser() {
        return new OffsetPagedIterable<>((take, skip) ->
                        ctx.getOverseerrClient().getAllUsers(take, skip, null).getResults())
                .stream()
                        .filter(u -> this.email.equalsIgnoreCase(u.getEmail()))
                        .findAny()
                        .orElse(null);
    }
}

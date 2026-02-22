package de.llalon.cinematic.domain;

import de.llalon.cinematic.util.collections.OffsetPagedIterable;
import de.llalon.cinematic.util.collections.StreamUtils;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Slf4j
public class User extends DomainModel {

    @NotNull
    private final String email;

    public String getEmail() {
        if (this.overseerrUser == null || this.overseerrUser.getEmail() == null) {
            return email;
        }
        return this.overseerrUser.getEmail();
    }

    // ToDo: Lazy load these if missing....
    @Nullable
    private final de.llalon.cinematic.client.overseerr.dto.User overseerrUser;

    @Nullable
    private final de.llalon.cinematic.client.tautulli.dto.User tautulliUser;

    public User(@NotNull ClientContext ctx, @NotNull de.llalon.cinematic.client.tautulli.dto.User tautulliUser) {
        super(ctx);
        this.email = tautulliUser.getEmail();
        this.tautulliUser = tautulliUser;
        this.overseerrUser = null;
    }

    User(@NotNull ClientContext ctx, @NotNull de.llalon.cinematic.client.overseerr.dto.User overseerrUser) {
        super(ctx);
        this.email = overseerrUser.getEmail();
        this.overseerrUser = overseerrUser;
        this.tautulliUser = null;
    }

    User(@NotNull ClientContext ctx, @NotNull String email) {
        super(ctx);
        this.email = email;
        this.overseerrUser = null;
        this.tautulliUser = null;
    }

    private Optional<Integer> getOverseerrUserId() {
        if (overseerrUser != null && overseerrUser.getId() != null) {
            return Optional.of(overseerrUser.getId());
        }

        // ToDo: Lazy load it to the field

        return new OffsetPagedIterable<>((take, skip) ->
                        ctx.getOverseerrClient().getAllUsers(take, skip, null).getResults())
                .stream()
                        .filter(u -> this.email.equalsIgnoreCase(u.getEmail()))
                        .map(de.llalon.cinematic.client.overseerr.dto.User::getId)
                        .findAny();
    }

    /**
     * Returns the media requests made by this user in Overseerr.
     *
     * @return an iterable of Request objects
     */
    @NotNull
    public Iterable<Request> requests() {
        return () -> {
            final Integer userId = this.getOverseerrUserId().get();
            return new OffsetPagedIterable<>((take, skip) -> ctx.getOverseerrClient()
                            .getAllRequests(take, skip, null, null, userId)
                            .getResults())
                    .stream().map(request -> new Request(ctx, request)).iterator();
        };
    }

    private Optional<Integer> getTautulliUserId() {
        if (this.tautulliUser != null && this.tautulliUser.getUserId() != null) {
            return Optional.of(tautulliUser.getUserId());
        }

        // ToDo: Lazy load it to the field

        return ctx.getTautulliClient().getUsers().stream()
                .filter(u -> this.email.equalsIgnoreCase(u.getEmail()))
                .map(de.llalon.cinematic.client.tautulli.dto.User::getUserId)
                .findAny();
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
            final var userId = this.getTautulliUserId();

            if (userId.isEmpty()) {
                log.warn("User {} not found in tautulli. Can't fetch watch history!", this.email);
                return StreamUtils.emptyIterator();
            }

            return new OffsetPagedIterable<>((take, skip) -> ctx.getTautulliClient()
                            .getHistoryByUser(userId.get(), skip, take)
                            .getData())
                    .stream().map(history -> new Watches(ctx, history)).iterator();
        };
    }
}

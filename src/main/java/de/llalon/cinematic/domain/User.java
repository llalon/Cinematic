package de.llalon.cinematic.domain;

import de.llalon.cinematic.util.collections.OffsetPagedIterable;
import de.llalon.cinematic.util.collections.StreamUtils;
import java.util.Optional;
import lombok.experimental.Delegate;
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

    @Nullable // ToDo: Lazy load if Plex user is provided. Also possible to be null if the overseer user doesn't exist.
    @Delegate
    private final de.llalon.cinematic.client.overseerr.dto.User overseerrUser;

    User(@NotNull ClientContext ctx, @NotNull de.llalon.cinematic.client.overseerr.dto.User overseerrUser) {
        super(ctx);
        this.email = overseerrUser.getEmail();
        this.overseerrUser = overseerrUser;
    }

    User(@NotNull ClientContext ctx, @NotNull String email) {
        super(ctx);
        this.email = email;
        this.overseerrUser = null;
    }

    private Optional<Integer> getOverseerrUserId() {
        if (Optional.ofNullable(overseerrUser).isPresent()) {
            return Optional.ofNullable(overseerrUser.getId());
        }
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

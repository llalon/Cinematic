package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.tautulli.dto.History;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

/**
 * Domain representation of a single watch history entry recorded by Tautulli.
 *
 * <p>Provides access to the {@link User} who watched the item as well as
 * basic playback metadata such as title, rating key, and watched percentage.</p>
 */
@Slf4j
public class Watches extends DomainModel {

    @NotNull
    private final History tautulliHistory;

    Watches(@NotNull ClientContext ctx, @NotNull History tautulliHistory) {
        super(ctx);
        this.tautulliHistory = tautulliHistory;
    }

    /**
     * Returns the user who watched this item.
     *
     * @return the watching {@link User}
     */
    @NotNull
    public User user() {
        return new User(ctx, tautulliUserById(this.tautulliHistory.getUserId()));
    }

    /**
     * Returns the title of the media item that was watched.
     *
     * @return the media title
     */
    public String getTitle() {
        return this.tautulliHistory.getTitle();
    }

    /**
     * Returns the Plex rating key uniquely identifying the watched media item.
     *
     * @return the Plex rating key
     */
    public String getRatingKey() {
        return this.tautulliHistory.getRatingKey();
    }

    /**
     * Returns the fraction of the item that was watched (0.0 to 1.0).
     *
     * @return the watched status fraction
     */
    public Float getWatchedStatus() {
        return this.tautulliHistory.getWatchedStatus();
    }
}

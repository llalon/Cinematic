package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.plex.dto.PlexMedia;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

/**
 * Domain representation of a single media version (e.g. 1080p, 4K) within a Plex item.
 *
 * <p>A Plex item may contain multiple media versions when the user has stored several
 * quality copies. Each {@code MediaVersion} exposes the video and audio characteristics
 * of one version and provides a {@link #delete()} mutation to remove it from Plex.</p>
 */
@Slf4j
public class MediaVersion extends DomainModel {

    @NotNull
    private final PlexMedia plexMedia;

    @NotNull
    private final String ratingKey;

    MediaVersion(@NotNull ClientContext ctx, @NotNull PlexMedia plexMedia, @NotNull String ratingKey) {
        super(ctx);

        this.plexMedia = plexMedia;
        this.ratingKey = ratingKey;
    }

    /**
     * Deletes this media version from Plex.
     *
     * <p>The Plex server must have "Allow media deletion" enabled. After deletion,
     * this object should no longer be used.</p>
     */
    public void delete() {
        log.info("Deleting Plex media version: ratingKey={}, mediaId={}", ratingKey, plexMedia.getId());
        ctx.getPlexClient().deleteMedia(ratingKey, plexMedia.getId());
    }

    public String videoCodec() {
        return this.plexMedia.getVideoCodec();
    }

    public String videoResolution() {
        return this.plexMedia.getVideoResolution();
    }

    public String audioCodec() {
        return this.plexMedia.getAudioCodec();
    }

    public String container() {
        return this.plexMedia.getContainer();
    }
}

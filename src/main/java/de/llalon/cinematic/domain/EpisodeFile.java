package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.sonarr.dto.EpisodeFileResource;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Domain representation of a media file sourced from Sonarr.
 */
@Slf4j
public final class EpisodeFile extends MediaFile {

    EpisodeFile(@NotNull ClientContext ctx, @NotNull EpisodeFileResource episodeFile) {
        this(ctx, episodeFile, null);
    }

    EpisodeFile(@NotNull ClientContext ctx, @NotNull EpisodeFileResource episodeFile, @Nullable Integer episodeNumber) {
        super(
                ctx,
                Source.SONARR,
                episodeFile.getId(),
                null,
                episodeFile.getSeriesId(),
                episodeFile.getSeasonNumber(),
                episodeNumber,
                episodeFile.getRelativePath(),
                episodeFile.getPath(),
                episodeFile.getSize(),
                episodeFile.getDateAdded(),
                episodeFile.getSceneName(),
                episodeFile.getReleaseGroup(),
                episodeFile.getQualityCutoffNotMet(),
                episodeFile.getQuality(),
                episodeFile.getQualityWeight(),
                null,
                episodeFile.getOriginalFilePath(),
                episodeFile.getMediaInfo() == null ? null : new MediaFormat(ctx, episodeFile.getMediaInfo()));
    }

    /**
     * Deletes this imported file through Sonarr.
     */
    @Override
    public void delete() {
        log.debug("Deleting {} media file with ID: {}", source, getId());
        ctx.getSonarrClient().deleteEpisodeFile(getId());
        invalidateCache(Caches.SONARR_EPISODE, Caches.SONARR_EPISODE_FILE);
    }
}

package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.radarr.dto.MovieFileResource;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;

/**
 * Domain representation of a media file sourced from Radarr.
 */
@Slf4j
public final class MovieFile extends MediaFile {

    MovieFile(@NonNull ClientContext ctx, @NonNull MovieFileResource movieFile) {
        super(
                ctx,
                Source.RADARR,
                movieFile.getId(),
                movieFile.getMovieId(),
                null,
                null,
                null,
                movieFile.getRelativePath(),
                movieFile.getPath(),
                movieFile.getSize(),
                movieFile.getDateAdded(),
                movieFile.getSceneName(),
                movieFile.getReleaseGroup(),
                movieFile.getQualityCutoffNotMet(),
                movieFile.getQuality(),
                null,
                movieFile.getCustomFormatScore(),
                movieFile.getOriginalFilePath(),
                movieFile.getMediaInfo() == null ? null : new MediaFormat(ctx, movieFile.getMediaInfo()));
    }

    /**
     * Deletes this imported file through Radarr.
     */
    @Override
    public void delete() {
        log.debug("Deleting {} media file with ID: {}", source, getId());
        ctx.getRadarrClient().deleteMovieFile(getId());
        invalidateCache(Caches.RADARR_MOVIE);
    }
}

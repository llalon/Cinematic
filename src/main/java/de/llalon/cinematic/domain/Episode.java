package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.sonarr.dto.EpisodeFileResource;
import de.llalon.cinematic.client.sonarr.dto.EpisodeResource;
import de.llalon.cinematic.util.collections.StreamUtils;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;

/**
 * Domain representation of a TV episode sourced from Sonarr.
 *
 * <p>Provides access to episode-level metadata and the media format for the imported episode file.</p>
 */
@Slf4j
public class Episode extends DomainModel {

    @NonNull
    private final EpisodeResource sonarrEpisode;

    Episode(@NonNull ClientContext ctx, @NonNull EpisodeResource sonarrEpisode) {
        super(ctx);
        this.sonarrEpisode = sonarrEpisode;
    }

    /**
     * Returns the series this episode belongs to.
     *
     * @return the parent Series
     */
    @NonNull
    public Series series() {
        if (sonarrEpisode.getSeries() != null) {
            return new Series(ctx, sonarrEpisode.getSeries());
        }

        return new Series(ctx, sonarrSeriesById(sonarrEpisode.getSeriesId()));
    }

    /**
     * Returns the imported media files associated with this episode.
     *
     * @return an iterable of MediaFile objects
     */
    @NonNull
    public Iterable<EpisodeFile> files() {
        return () -> {
            if (sonarrEpisode.getEpisodeFile() != null) {
                return Stream.of(new EpisodeFile(ctx, sonarrEpisode.getEpisodeFile(), sonarrEpisode.getEpisodeNumber()))
                        .iterator();
            }

            if (sonarrEpisode.getEpisodeFileId() == null || sonarrEpisode.getEpisodeFileId() == 0) {
                return StreamUtils.emptyIterator();
            }

            EpisodeFileResource episodeFile = sonarrEpisodeFile(sonarrEpisode.getEpisodeFileId());
            return Stream.of(new EpisodeFile(ctx, episodeFile, sonarrEpisode.getEpisodeNumber()))
                    .iterator();
        };
    }

    /**
     * Returns the media file formats associated with this episode.
     *
     * @return an iterable of MediaFormat objects
     */
    @NonNull
    public Iterable<MediaFormat> formats() {
        return () -> StreamSupport.stream(files().spliterator(), false)
                .map(MediaFile::format)
                .filter(Objects::nonNull)
                .iterator();
    }

    /**
     * Returns the episode title.
     *
     * @return the episode title
     */
    public String getTitle() {
        return this.sonarrEpisode.getTitle();
    }

    /**
     * Returns the season number.
     *
     * @return the season number
     */
    public Integer getSeasonNumber() {
        return this.sonarrEpisode.getSeasonNumber();
    }

    /**
     * Returns the episode number within the season.
     *
     * @return the episode number
     */
    public Integer getEpisodeNumber() {
        return this.sonarrEpisode.getEpisodeNumber();
    }

    /**
     * Returns whether a file exists on disk for this episode.
     *
     * @return {@code true} if a file is present, {@code false} otherwise
     */
    public Boolean getHasFile() {
        return this.sonarrEpisode.getHasFile();
    }

    /**
     * Returns whether this episode is monitored in Sonarr.
     *
     * @return {@code true} if monitored, {@code false} otherwise
     */
    public Boolean getMonitored() {
        return this.sonarrEpisode.getMonitored();
    }

    /**
     * Returns the episode air date.
     *
     * @return the air date, or {@code null} if unavailable
     */
    public String getAirDate() {
        return this.sonarrEpisode.getAirDate();
    }

    /**
     * Returns the episode air date in UTC.
     *
     * @return the UTC air date, or {@code null} if unavailable
     */
    public LocalDateTime getAirDateUtc() {
        return this.sonarrEpisode.getAirDateUtc();
    }
}

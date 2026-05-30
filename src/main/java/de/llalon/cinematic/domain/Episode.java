package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.sonarr.dto.EpisodeFileResource;
import de.llalon.cinematic.client.sonarr.dto.EpisodeResource;
import java.time.LocalDateTime;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

/**
 * Domain representation of a TV episode sourced from Sonarr.
 *
 * <p>Provides access to episode-level metadata and the media format for the imported episode file.</p>
 */
@Slf4j
public class Episode extends DomainModel {

    @NotNull
    private final EpisodeResource sonarrEpisode;

    Episode(@NotNull ClientContext ctx, @NotNull EpisodeResource sonarrEpisode) {
        super(ctx);
        this.sonarrEpisode = sonarrEpisode;
    }

    /**
     * Returns the media file formats associated with this episode.
     *
     * @return an iterable of MediaFormat objects
     */
    @NotNull
    public Iterable<MediaFormat> formats() {
        return () -> {
            if (sonarrEpisode.getEpisodeFile() != null
                    && sonarrEpisode.getEpisodeFile().getMediaInfo() != null) {
                return Stream.of(sonarrEpisode.getEpisodeFile())
                        .map(episodeFile -> new MediaFormat(ctx, episodeFile.getMediaInfo()))
                        .iterator();
            }

            if (sonarrEpisode.getEpisodeFileId() == null || sonarrEpisode.getEpisodeFileId() == 0) {
                return Stream.<MediaFormat>empty().iterator();
            }

            EpisodeFileResource episodeFile = sonarrEpisodeFile(sonarrEpisode.getEpisodeFileId());
            if (episodeFile.getMediaInfo() == null) {
                return Stream.<MediaFormat>empty().iterator();
            }

            return Stream.of(new MediaFormat(ctx, episodeFile.getMediaInfo())).iterator();
        };
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

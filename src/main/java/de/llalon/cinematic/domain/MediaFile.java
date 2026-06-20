package de.llalon.cinematic.domain;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * Domain representation of one imported media file.
 */
@Getter
abstract class MediaFile extends DomainModel {

    protected enum Source {
        RADARR,
        SONARR
    }

    @Getter(AccessLevel.NONE)
    @NonNull
    protected final Source source;

    @NonNull
    private final Integer id;

    @Nullable
    private final Integer movieId;

    @Nullable
    private final Integer seriesId;

    @Nullable
    private final Integer seasonNumber;

    @Nullable
    private final Integer episodeNumber;

    @Nullable
    private final String relativePath;

    @Nullable
    private final String path;

    @Nullable
    private final Long size;

    @Nullable
    private final LocalDateTime dateAdded;

    @Nullable
    private final String sceneName;

    @Nullable
    private final String releaseGroup;

    @Nullable
    private final Boolean qualityCutoffNotMet;

    @Nullable
    private final Object quality;

    @Nullable
    private final Integer qualityWeight;

    @Nullable
    private final Integer customFormatScore;

    @Nullable
    private final String originalFilePath;

    @Nullable
    private final MediaFormat format;

    protected MediaFile(
            @NonNull ClientContext ctx,
            @NonNull Source source,
            @NonNull Integer id,
            @Nullable Integer movieId,
            @Nullable Integer seriesId,
            @Nullable Integer seasonNumber,
            @Nullable Integer episodeNumber,
            @Nullable String relativePath,
            @Nullable String path,
            @Nullable Long size,
            @Nullable LocalDateTime dateAdded,
            @Nullable String sceneName,
            @Nullable String releaseGroup,
            @Nullable Boolean qualityCutoffNotMet,
            @Nullable Object quality,
            @Nullable Integer qualityWeight,
            @Nullable Integer customFormatScore,
            @Nullable String originalFilePath,
            @Nullable MediaFormat format) {
        super(ctx);
        this.source = source;
        this.id = id;
        this.movieId = movieId;
        this.seriesId = seriesId;
        this.seasonNumber = seasonNumber;
        this.episodeNumber = episodeNumber;
        this.relativePath = relativePath;
        this.path = path;
        this.size = size;
        this.dateAdded = dateAdded;
        this.sceneName = sceneName;
        this.releaseGroup = releaseGroup;
        this.qualityCutoffNotMet = qualityCutoffNotMet;
        this.quality = quality;
        this.qualityWeight = qualityWeight;
        this.customFormatScore = customFormatScore;
        this.originalFilePath = originalFilePath;
        this.format = format;
    }

    @Nullable
    public MediaFormat format() {
        return this.format;
    }

    @Nullable
    public MediaFormat getFormat() {
        return format();
    }

    /**
     * Deletes this imported file through the owning source application.
     */
    public abstract void delete();
}

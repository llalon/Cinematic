package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.radarr.dto.RadarrMediaInfoResource;
import de.llalon.cinematic.client.sonarr.dto.SonarrMediaInfoResource;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

/**
 * Domain representation of the audio and video format metadata for one media file.
 */
@Slf4j
public class MediaFormat extends DomainModel {

    @Nullable
    private final String videoCodec;

    @Nullable
    private final String audioCodec;

    @Nullable
    private final String resolution;

    @Nullable
    private final Double audioChannels;

    @Nullable
    private final String audioLanguages;

    @Nullable
    private final String videoDynamicRange;

    MediaFormat(
            ClientContext ctx,
            @Nullable String videoCodec,
            @Nullable String audioCodec,
            @Nullable String resolution,
            @Nullable Double audioChannels,
            @Nullable String audioLanguages,
            @Nullable String videoDynamicRange) {
        super(ctx);
        this.videoCodec = videoCodec;
        this.audioCodec = audioCodec;
        this.resolution = resolution;
        this.audioChannels = audioChannels;
        this.audioLanguages = audioLanguages;
        this.videoDynamicRange = videoDynamicRange;
    }

    MediaFormat(ClientContext ctx, RadarrMediaInfoResource mediaInfo) {
        this(
                ctx,
                mediaInfo.getVideoCodec(),
                mediaInfo.getAudioCodec(),
                mediaInfo.getResolution(),
                mediaInfo.getAudioChannels(),
                mediaInfo.getAudioLanguages(),
                mediaInfo.getVideoDynamicRange());
    }

    MediaFormat(ClientContext ctx, SonarrMediaInfoResource mediaInfo) {
        this(
                ctx,
                mediaInfo.getVideoCodec(),
                mediaInfo.getAudioCodec(),
                mediaInfo.getResolution(),
                mediaInfo.getAudioChannels(),
                mediaInfo.getAudioLanguages(),
                mediaInfo.getVideoDynamicRange());
    }

    /**
     * Returns the video codec reported by the source client.
     *
     * @return the video codec, or {@code null} if unavailable
     */
    @Nullable
    public String getVideoCodec() {
        return videoCodec;
    }

    /**
     * Returns the video format reported by the source client.
     *
     * @return the video format, or {@code null} if unavailable
     */
    @Nullable
    public String getVideoFormat() {
        return videoCodec;
    }

    /**
     * Returns the audio codec reported by the source client.
     *
     * @return the audio codec, or {@code null} if unavailable
     */
    @Nullable
    public String getAudioCodec() {
        return audioCodec;
    }

    /**
     * Returns the audio format reported by the source client.
     *
     * @return the audio format, or {@code null} if unavailable
     */
    @Nullable
    public String getAudioFormat() {
        return audioCodec;
    }

    /**
     * Returns the media resolution, such as {@code 1080p} or {@code 2160p}.
     *
     * @return the resolution, or {@code null} if unavailable
     */
    @Nullable
    public String getResolution() {
        return resolution;
    }

    /**
     * Returns the number of audio channels.
     *
     * @return the audio channel count, or {@code null} if unavailable
     */
    @Nullable
    public Double getAudioChannels() {
        return audioChannels;
    }

    /**
     * Returns the audio languages reported by the source client.
     *
     * @return the audio languages, or {@code null} if unavailable
     */
    @Nullable
    public String getAudioLanguages() {
        return audioLanguages;
    }

    /**
     * Returns the dynamic range reported for the video stream, such as {@code HDR} or {@code SDR}.
     *
     * @return the video dynamic range, or {@code null} if unavailable
     */
    @Nullable
    public String getVideoDynamicRange() {
        return videoDynamicRange;
    }
}

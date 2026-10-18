package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.lidarr.dto.TrackFileResource;

/** Imported audio file owned by Lidarr. */
public class TrackFile extends MediaFile {
    private final TrackFileResource resource;

    TrackFile(ClientContext ctx, TrackFileResource resource) {
        super(
                ctx,
                Source.LIDARR,
                resource.getId(),
                null,
                null,
                null,
                null,
                null,
                resource.getPath(),
                resource.getSize(),
                resource.getDateAdded(),
                resource.getSceneName(),
                resource.getReleaseGroup(),
                resource.getQualityCutoffNotMet(),
                resource.getQuality(),
                resource.getQualityWeight(),
                resource.getCustomFormatScore(),
                null,
                resource.getMediaInfo() == null ? null : new MediaFormat(ctx, resource.getMediaInfo()));
        this.resource = resource;
    }

    /** Returns the parent artist. */
    public Artist artist() {
        return new Artist(ctx, lidarrArtistById(resource.getArtistId()));
    }
    /** Returns the parent album. */
    public Album album() {
        return new Album(ctx, lidarrAlbumById(resource.getAlbumId()));
    }
    /** Returns the Lidarr artist ID. */
    public Integer getArtistId() {
        return resource.getArtistId();
    }
    /** Returns the Lidarr album ID. */
    public Integer getAlbumId() {
        return resource.getAlbumId();
    }

    @Override
    public void delete() {
        ctx.getLidarrClient().deleteTrackFile(resource.getId());
        invalidateCache(Caches.LIDARR_TRACK_FILE, Caches.LIDARR_TRACK);
    }
}

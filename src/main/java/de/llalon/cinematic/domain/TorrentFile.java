package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.qbittorrent.dto.QBittorrentFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Domain representation of a file within a torrent managed by qBittorrent.
 *
 * <p>Provides access to the name, size, and download progress of a single file
 * inside a torrent. Obtained by iterating {@link Torrent#files()}.</p>
 */
public class TorrentFile extends DomainModel {

    @NotNull
    private final QBittorrentFile dto;

    TorrentFile(@NotNull ClientContext ctx, @NotNull QBittorrentFile dto) {
        super(ctx);
        this.dto = dto;
    }

    /**
     * Returns the relative file name (including subdirectory path) within the torrent.
     *
     * @return the file name/path
     */
    @Nullable
    public String getName() {
        return dto.getName();
    }

    /**
     * Returns the size of this file in bytes.
     *
     * @return file size
     */
    @Nullable
    public Long getSize() {
        return dto.getSize();
    }

    /**
     * Returns the download progress of this file as a fraction between 0.0 and 1.0.
     *
     * @return download progress
     */
    @Nullable
    public Float getProgress() {
        return dto.getProgress();
    }
}

package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.qbittorrent.dto.TorrentFileDTO;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@RequiredArgsConstructor
public class TorrentFile {
    @Delegate
    private final TorrentFileDTO torrentFileDTO;
}

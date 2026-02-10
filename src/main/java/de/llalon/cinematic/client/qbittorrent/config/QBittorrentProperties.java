package de.llalon.cinematic.client.qbittorrent.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Configuration properties for qBittorrent WebUI API.
 *
 * Properties are loaded from application.yaml with prefix "services.qbittorrent".
 * Values can be overridden via environment variables.
 */
@Data
@Builder
@AllArgsConstructor
public class QBittorrentProperties {

    private final String url;
    private final String username;
    private final String password;

    public static QBittorrentProperties fromEnvironment() {
        return QBittorrentProperties.builder()
                .url(System.getenv("QBITTORRENT_URL"))
                .username(System.getenv("QBITTORRENT_USERNAME"))
                .password(System.getenv("QBITTORRENT_PASSWORD"))
                .build();
    }
}

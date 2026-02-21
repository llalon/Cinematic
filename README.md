# Cinematic

## About

A JVM library that unifies media server APIs into a single navigable domain model.

It integrates:

- Plex
- Tautulli
- Overseerr
- Sonarr
- Radarr
- qBittorrent

## Use Cases

**Automation Workflows**: Build custom automation that spans multiple services—manage torrents based on watch history,
tag media based on requests, cleanup old downloads.

**Reporting & Analytics**: Aggregate data across your entire media stack without writing API integration code.

**Custom Tooling**: Build CLI tools, web dashboards, or scheduled jobs that orchestrate your media server
infrastructure.

## Requirements

- Java JDK 11+

## Usage

### Configuration

By default api clients will be configured from environment variables

```bash
TAUTULLI_URL=http://localhost:8181
TAUTULLI_API_KEY=your-tautulli-api-key

OVERSEERR_URL=http://localhost:5055
OVERSEERR_API_KEY=your-overseerr-api-key

SONARR_URL=http://localhost:8989
SONARR_API_KEY=your-sonarr-api-key

RADARR_URL=http://localhost:7878
RADARR_API_KEY=your-radarr-api-key

QBITTORRENT_URL=http://localhost:8080
QBITTORRENT_USERNAME=admin
QBITTORRENT_PASSWORD=your-qbittorrent-password

PLEX_URL=http://localhost:32400
PLEX_API_KEY=your-plex-token
```

```java
Library library = new Library();
```

or provide custom credentials in code:

```java
Library library = new Library(ClientContext.builder()
        .plexProperties(PlexProperties.builder()
                .url("http://localhost:32400")
                .token("test")
                .build())
        .sonarrProperties(SonarrProperties.builder()
                .url("http://localhost:8989")
                .apiKey("test")
                .build())
        .radarrProperties(RadarrProperties.builder()
                .url("http://localhost:7878")
                .apiKey("test")
                .build())
        .qbittorrentProperties(QBittorrentProperties.builder()
                .url("http://localhost:7878")
                .username("user")
                .password("pass")
                .build())
        .tautulliProperties(TautulliProperties.builder()
                .url("http://localhost:8181")
                .apiKey("test")
                .build())
        .overseerrProperties(OverseerrProperties.builder()
                .url("http://localhost:5055")
                .apiKey("test")
                .build())
        .build());
```

The Library is the root of the Cinematic domain model. It acts as the entry point to all your media data—movies, series, requests, torrents, watch history etc. 

```java
Iterable<Movie> movies = library.movies();
Iterable<Series> series = library.series();
```

From there, each object is self-referential: every Movie, Series, Request, etc knows how to access its related items. For example, a Movie can provide its associated torrents, requests, and watches directly.

```java
Iterable<Torrent> torrentsForMovie = movie.torrents();
Iterable<Request> requestsForMovie = movie.requests();
Iterable<Watches> movieWatchHistory = movie.watches();
```

This creates a navigable, connected domain, where you can traverse your media ecosystem naturally, without calling APIs manually or managing IDs.

Data is fetched lazily, so each object only loads what you actually use, keeping operations efficient.

See examples in `examples/`.
# Copilot Instructions

## Project Overview

Cinematic is a JVM library that provides api clients and a rich domain model for media server arr APIs.

It integrates:

- Plex
- Tautulli
- Overseerr
- Sonarr
- Radarr
- qBittorrent

The goal is to expose a single navigable domain graph that:

- Feels like an in-memory object model
- Hides all HTTP clients and API DTOs
- Supports lazy paging via Iterable
- Enables powerful automation workflows
- Keeps orchestration logic outside entities

The domain has a single entry point: Library. From Library, everything is reachable.

All objects are rich.
All relations return other domain objects.
No DTOs escape the domain boundary.

## Build and Verification

- The project uses maven wrapper
- After adding or modifying code, verify the project continues to build successfully.
- Ensure all tests pass as part of the build.
- Ensure code is formatted using spotless

## Useful Commands

| Maven Command           | Description            |
|:------------------------|:-----------------------|
| `./mvnw package`        | Build the application. |
| `./mvnw test`           | Run tests.             |
| `./mvnw compile`        | Compile.               |
| `./mvnw spotless:apply` | Apply code formatting. |

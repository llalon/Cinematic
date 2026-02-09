# Copilot Instructions

## Project Overview

Cinematic is a jvm library that provides api clients and a rich domain model for media server arr APIs.

It integrates:

- Plex
- Tautulli
- Overseerr
- Sonarr
- Radarr
- qBittorrent

These APIs:

- Do not follow REST conventions
- Have inconsistent schemas
- Use different identifiers for the same media
- Require orchestration across systems

The domain model must hide this complexity completely.

## Project Architecture Patterns

- ActiveRecord
- Rich Domain Model
- Thin HTTP clients
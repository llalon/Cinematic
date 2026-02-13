---
description: 'Guidelines for creating the rich domain model'
applyTo: '**/*.java, **/*.kt'
---

1. Architectural Structure

The domain must be organized around a single root object called Library.

The Library is:

- The only entry point into the domain graph
- Responsible for discovery of aggregates
- Not responsible for business workflows
- Not responsible for cross-aggregate orchestration

All other domain objects must be reachable from the Library through navigation.

2. Domain Graph Philosophy

The system must behave like a connected object graph over distributed services.

Every aggregate must:

- Expose related aggregates
- Encapsulate its own state
- Allow controlled mutations of itself
- Avoid embedding workflow logic

Navigation must feel natural and composable across:

- Movies
- Series
- Episodes & Seasons
- Torrents
- Files
- Tags
- Requests
- Users

Traversal depth should not be artificially restricted.

3. Strict DTO Isolation

API DTOs from external systems must:

- Exist only inside thin HTTP clients
- Be converted immediately at the domain boundary
- Never be stored inside domain aggregates
- Never be returned from domain methods

The domain layer must not depend on API schemas.

All mapping between transport models and domain models must occur at the boundary.

4. Iterable-Only Collections

All relationships returning multiple objects must:

- Return Iterable<T>
- Support lazy evaluation
- Exhaust paginated APIs transparently

The domain must never expose:

- List
- Set
- Collection
- Stream

Paging must be handled internally and lazily. No eager full data loading.

5. Aggregate Responsibilities

Each aggregate must:

- Represent a meaningful domain concept
- Own its own identity
- Encapsulate related cross-system identifiers
- Expose navigational relationships
- Allow self-contained mutations

Aggregates must not:

- Iterate over the entire domain
- Perform multi-aggregate policy decisions
- Embed automation workflows
- Coordinate other aggregates

6. Mutations vs Orchestration

Mutations are allowed when they are intrinsic to the aggregate:

- Deleting itself
- Modifying its own tags
- Changing its own priority
- Blacklisting a release
- Updating its own state

Orchestration must remain external:

- Scanning all torrents
- Enforcing tagging policies
- Applying conditional bulk deletions
- Cross-checking watch history
- Coordinating multi-system actions

The domain enables workflows.
It does not execute workflows.

7. Identity and Value Objects

All identifiers must be represented by meaningful value objects where appropriate.

Avoid primitive obsession.

Cross-system identifiers must be encapsulated in a dedicated identity value object that:

- Is immutable
- Holds identifiers from multiple systems
- Is used only for correlation

Value objects must:

- Be immutable
- Contain no behavior beyond identity/state
- Not depend on infrastructure

8. Client Access

All aggregates must depend on a ClientContext.

The ClientContext:

- Provides access to thin HTTP clients
- Is injected into aggregates
- Must not be globally accessed
- Must not be static

Aggregates use it only to fulfill their own responsibilities.

9. Tag Model

Tags must be represented as domain concepts, not raw strings.

A tag aggregate must:

- Represent a logical grouping
- Provide navigation to related movies, series, and torrents
- Avoid containing orchestration logic

Tag identity must be stable and system-agnostic.

10. Torrent Model

A torrent must:

- Expose metadata such as tracker and files
- Expose related media objects
- Support tag manipulation
- Support priority changes
- Support deletion

It must not determine when those actions should occur.

11. Media Model (Movies and Series)

Media aggregates must:

- Expose watch state
- Expose related torrents
- Expose related tags
- Expose request information
- Support self-deletion
- Support release blacklisting

They must not embed decision logic about when those actions should be triggered.

12. Request and User Model

Requests must:

- Represent requester information
- Represent approval state
- Provide navigation to related media

Users must:

- Represent watch history
- Provide identity
- Remain independent of orchestration logic

13. Design Goals

The domain must:

- Feel like an in-memory object graph
- Be safe to traverse at scale
- Hide infrastructure details completely
- Support deep navigation
- Enable complex automation workflows externally
- Avoid service-layer bloat
- Remain expressive and rich

14. Anti-Patterns to Avoid

Do not implement:

- Anemic domain entities
- Service classes that replicate navigation
- DTO-returning methods
- Static domain utilities
- Cross-aggregate workflow methods
- Eager full-page data loading
- Business rules embedded in aggregates

15. Overall Philosophy

This domain layer is a rich abstraction over distributed media systems.

It must:

- Encapsulate infrastructure
- Provide relational depth
- Allow controlled mutation
- Enable automation
- Remain clean, predictable, and composable

All complexity in workflows should emerge from composing the domain graph — not from embedding orchestration logic
inside the model.
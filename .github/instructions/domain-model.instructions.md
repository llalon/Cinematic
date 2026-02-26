---
description: 'Guidelines for creating the rich domain model'
applyTo: '**/*.java, **/*.kt'
---

# Architectural Structure

- The domain must be organized around a single root object called Library. 
- All other domain objects must be reachable from the Library through navigation.

# Domain Graph Philosophy

The system must behave like a connected object graph over distributed services.

Every domain object must:

- Expose related domain objects
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

# Strict DTO Isolation

API DTOs from external systems must:

- Exist only inside thin HTTP clients
- Be converted immediately at the domain boundary
- Never be stored inside domain aggregates
- Never be returned from domain methods

The domain layer must not depend on API schemas.

All mapping between transport models and domain models must occur at the boundary.

# Iterable-Only Collections

All relationships returning multiple objects must:

- Return Iterable<T>
- Support lazy evaluation wherever possible
- Exhaust paginated APIs transparently

The domain must never publicly expose:

- List
- Set
- Collection
- Stream

Paging must be handled internally and lazily. No eager full data loading.

# Domain Object Responsibilities

Each domain object must:

- Represent a meaningful domain concept
- Own its own identity
- Encapsulate related cross-system identifiers
- Expose navigational relationships
- Allow self-contained mutations

# Mutations vs Orchestration

Mutations are allowed when they are intrinsic to the aggregate:

- Deleting itself
- Modifying its own tags
- Changing its own priority
- Blacklisting a release
- Updating its own state

Orchestration must remain external:

- Enforcing tagging policies
- Applying conditional bulk deletions
- Cross-checking watch history
- Coordinating multi-system actions

The domain enables workflows.
It does not execute workflows.

# Implementation Details

Domain objects must extend the abstract base class `DomainModel`.

# Caching

- API calls should be cached using the central context jcache.
- Only API calls should be cached.

# Identity and Value Objects

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

# Design Goals

The domain must:

- Feel like an in-memory object graph
- Be safe to traverse at scale
- Hide infrastructure details completely
- Support deep navigation
- Enable complex automation workflows externally
- Avoid service-layer bloat
- Remain expressive and rich

# Anti-Patterns to Avoid

Do not implement:

- Anemic domain entities
- Service classes that replicate navigation
- DTO-returning methods
- Static domain utilities
- Cross-aggregate workflow methods
- Eager full-page data loading
- Business rules embedded in aggregates

# Overall Philosophy

This domain layer is a rich abstraction over distributed media systems.

It must:

- Encapsulate infrastructure
- Provide relational depth
- Allow controlled mutation
- Enable automation
- Remain clean, predictable, and composable

All complexity in workflows should emerge from composing the domain graph — not from embedding orchestration logic
inside the model.
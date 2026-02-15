---
description: 'Guidelines for general java code'
applyTo: '**/*.java, **/*.kt'
---

# Compatibility

All code in this library must work in jRuby and other jvm languages with maximum compatibility.

- Do not use records
- Do not use any reflection at all
- Do not expose stream API to library consumers
- Be compiled with bytecode target JVM 11
- Avoid APIs introduced after Java 11
- Remain compatible with JRuby
- Remain compatible with Android (API 26+ baseline unless otherwise specified)
- Avoid assumptions about JVM vendor or HotSpot-only features
- Kotlin jvmTarget = 11
- Java --release 11
- Do not use Java 17+ APIs
- Do not use preview features
- No var in public APIs
- No record, sealed (Java), or pattern matching
- Kotlin 2.x language features that compile to JVM 11 bytecode
- Java 11 standard library
- Public API must be Java-friendly
- No Kotlin-only types in public signatures:
- Avoid kotlin.Result
- Avoid default interface methods unless explicitly required
- Avoid inline classes/value classes in public API
- Prefer simple POJOs / data classes
- Prefer interfaces over abstract classes
- Avoid static initialization side effects
- Avoid method overloading ambiguity
- Prefer distinct method names over overloads
- Avoid Kotlin default arguments in public APIs
- Avoid companion-object factory overload confusion
- Use standard Java collections in public API
- Avoid use of Optional in public API (JRuby interop is awkward)
- Avoid reflection-heavy APIs
- Avoid java.nio.file in public APIs
- Avoid dynamic proxies
- Avoid ServiceLoader unless optional
- Avoid large transitive dependencies

Design for dynamic invocation safety.

# General Practices

- Always use `{}` with all conditionals
- Utilize stream API internally in the library
- Use `final` liberally
- Produce Java 17 compatible bytecode
- Avoid APIs introduced after Java 17 unless explicitly allowed
- Prefer standard JDK APIs over third-party dependencies
- Prefer immutable objects
- Use final fields
- Prefer Iterable/lazy data structures
- Avoid unnecessary eager loading; prefer lazy evaluation
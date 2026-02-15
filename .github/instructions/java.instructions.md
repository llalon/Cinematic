---
description: 'Guidelines for general java code'
applyTo: '**/*.java, **/*.kt'
---

# Compatibility

All code in this library must work in jRuby and other jvm languages with maximum compatibility.

- Do not use records
- Do not use any reflection at all
- Do not expose stream API to library consumers

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
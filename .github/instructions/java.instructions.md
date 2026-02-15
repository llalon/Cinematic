---
description: 'Guidelines for general java code'
applyTo: '**/*.java, **/*.kt'
---

# Compatibility

All code in this library must work in jRuby and other jvm languages with maximum compatibility.

- Do not use records
- Do not use any reflection

# General Practices

- Always use `{}` with all conditionals
- Utilize stream api internally in the library
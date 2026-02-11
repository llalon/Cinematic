---
description: 'Guidelines for creating services api client code'
applyTo: '**/*.java, **/*.kt'
---

# General Instructions

- Use OkHttp as the http client
- Use synchronous blocking calls - do not use reactive patterns
- Clients must be thread safe

# JSON serialization/deserialization

- Use Moshi for JSON serialization/deserialization
- Use typed DTOs for responses and requests including typed enums where appropriate
- Methods must return typed DTO Response with response body
- Method must accept typed DTO requests with request parameters and/or body
- DTOs must use lombok `@Data` and `@JsonIgnoreProperties(ignoreUnknown = true)`
- DTOs must contain all final members
- DTOs must use collection objects instead of arrays
- DTOs must boxed objects instead of primitives

# Logging

- Log at DEBUG level for successful operations
- Log at ERROR level for all exceptions (before throwing)
- Include relevant context: URLs, IDs, operation names
- Never log sensitive data (API keys, passwords, tokens)

# Constructor Requirements

- Constructors must accept an OkHttpClient along with the url and any api token credentials or config required
- Validate all required parameters (URL, API keys, credentials)
- Throw `IllegalArgumentException` for invalid configuration
- Fail fast - don't defer validation to first API call

# Documentation

- Include class-level JavaDoc describing the service
- Document all public methods with params, returns, and throws
- Include idempotency notes for state-changing operations
- Reference official API documentation URLs

# Error Handling

## Exception Hierarchy

Each API client must define **two types of exceptions** to distinguish between remote API failures and local client
errors:

### 1. API Exception (Remote Errors)

For failures originating from the external service:

- HTTP errors (4xx, 5xx status codes)
- Network failures (timeouts, connection refused)
- Authentication/authorization failures
- Rate limiting

**Characteristics:**

- Extend from `RuntimeException`
- Include `statusCode` (Integer, nullable) and `responseBody` (String, nullable)
- Implement `isRetryable()` method to indicate if retry is appropriate
- Preserve the original cause (IOException, etc.)

**Example:**

```java

@Getter
public class OverseerrApiException extends RuntimeException {
    private final Integer statusCode;
    private final String responseBody;

    public OverseerrApiException(String message, int statusCode, String responseBody) {
        super(message);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public OverseerrApiException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = null;
        this.responseBody = null;
    }

    public boolean isRetryable() {
        return statusCode != null && (statusCode == 429 || statusCode >= 500);
    }
}
```

### 2. Client Parse Exception (Local Errors)

For failures in the client code itself:

- JSON serialization failures (request body)
- JSON deserialization failures (response parsing)
- Schema mismatches
- Invalid configuration

**Characteristics:**

- Extend from `RuntimeException`
- Always preserve the original cause (JsonProcessingException, etc.)
- Never retryable - indicates bugs or breaking API changes

**Example:**

```java
public class OverseerrClientParseException extends RuntimeException {
    public OverseerrClientParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public boolean isRetryable() {
        return false;
    }
}
```

## Exception Usage Patterns

### Throw ApiException for:

- HTTP error responses: `throw new [Service]ApiException("Request failed", statusCode, responseBody)`
- Network errors: `throw new [Service]ApiException("Network failure", cause)`
- Timeouts: `throw new [Service]ApiException("Request timeout", cause)`

### Throw ClientParseException for:

- Request serialization:
  `catch (JsonProcessingException e) { throw new [Service]ClientParseException("Failed to serialize request", e) }`
- Response deserialization:
  `catch (JsonProcessingException e) { throw new [Service]ClientParseException("Failed to parse response", e) }`
- Schema validation failures

## Error Context Requirements

All exceptions must include:

1. **Descriptive message** - include the URL, operation, or entity involved
2. **HTTP status code** (for API exceptions) - enables differentiated retry logic
3. **Response body** (for API exceptions) - aids debugging and audit logging
4. **Original cause** - preserve stack traces via exception chaining

## Retry Guidance

API exceptions must implement logic to determine retryability:

- **Retryable**: 429 (rate limit), 503 (service unavailable), 504 (gateway timeout), network errors
- **Non-retryable**: 400 (bad request), 401 (unauthorized), 403 (forbidden), 404 (not found)
- **Client parse exceptions**: Never retryable

This distinction enables event consumers to make intelligent retry decisions in the event-driven automation platform.

- Define two types of exceptions to distinguish between remote API failures and local client errors
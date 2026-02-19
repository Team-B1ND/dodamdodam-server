# AGENTS.md — dodamdodam-server

## Project Overview

Kotlin/Spring Boot multi-module microservices project. Organization: `com.b1nd.dodamdodam`.

**Tech stack**: Kotlin 2.1.0, Spring Boot 3.3.5, JVM 21, Gradle Kotlin DSL, MySQL, gRPC, Kafka, Flyway.

## Module Structure

```
app/                          # Standalone Kotlin JVM app (not a Spring Boot service)
utils/                        # Shared utilities
buildSrc/                     # Gradle convention plugins
core/
  core-common/                # Base exceptions, Response wrapper, coroutine helpers
  core-jpa/                   # BaseTimeEntity, JPA allOpen config
  core-grpc/                  # Protobuf definitions, gRPC exception handler
  core-kafka/                 # Kafka producer, config
  core-security/              # Passport/JWT, security filters, @EnableDodamSecurity
services/
  service-auth/               # Auth service (servlet, JWT, gRPC client)
  service-user/               # User service (servlet, JPA, gRPC server, port 8081)
  service-gateway/            # API gateway (reactive/WebFlux, R2DBC, Redis, Spring Cloud Gateway)
  service-inapp/              # In-app service (WIP/empty)
```

Convention plugins in `buildSrc/`:
- `buildsrc.convention.kotlin-jvm` — Basic Kotlin JVM + JUnit5
- `buildsrc.convention.spring-boot-application` — Runnable services (Spring Boot + allOpen/noArg for JPA)
- `buildsrc.convention.spring-boot-library` — Core library modules (Spring BOM, no bootJar)

## Build / Test / Run Commands

```bash
# Build
./gradlew build                    # Build all modules
./gradlew clean                    # Clean all build outputs
./gradlew check                    # Run all checks including tests

# Run a specific service
./gradlew :services:service-auth:bootRun
./gradlew :services:service-user:bootRun
./gradlew :services:service-gateway:bootRun

# Test
./gradlew test                     # Run all tests across all modules
./gradlew :services:service-auth:test                          # Test single module
./gradlew :services:service-auth:test --tests "com.b1nd.dodamdodam.auth.SomeTest"         # Single test class
./gradlew :services:service-auth:test --tests "com.b1nd.dodamdodam.auth.SomeTest.method"  # Single test method

# gRPC proto generation
./gradlew :core:core-grpc:generateProto
```

No linter (ktlint/detekt) or CI/CD is configured. No Docker setup exists.

## Architecture — Layered (per service module)

```
presentation/       # REST controllers, gRPC controllers
application/        # Use cases, request/response DTOs, mappers
domain/             # Services, entities, repositories, exceptions, enumerations
infrastructure/     # External clients, configs, security, exception handlers
```

## Naming Conventions

| Concept | Pattern | Example |
|---|---|---|
| JPA Entity | `XxxEntity` (class, not data class) | `UserEntity`, `PrincipalEntity` |
| R2DBC Entity | `XxxEntity` (data class) | `RouteEntity` |
| Repository | `XxxRepository` interface | `UserRepository`, `PrincipalRepository` |
| Domain Service | `XxxService` with `@Service` | `UserService`, `PrincipalService` |
| Use Case | `XxxUseCase` with `@Component` | `AuthUseCase`, `UserUseCase` |
| REST Controller | `XxxController` with `@RestController` | `AuthController` |
| gRPC Controller | `XxxGrpcController` with `@GrpcService` | `UserGrpcController` |
| Request DTO | `XxxRequest` (data class) | `LoginRequest`, `StudentRegisterRequest` |
| Response DTO | `XxxResponse` (data class) | `LoginResponse`, `ExchangePassportResponse` |
| Exception | Descriptive name extending `BasicException` | `UserNotFoundException` |
| Exception Codes | `XxxExceptionCode` enum | `PrincipalExceptionStatusCode`, `UserExceptionCode` |
| Enum | `XxxType` | `StatusType`, `RoleType` |
| Mapper | Extension functions in `XxxMapper.kt` | `StudentRegisterRequest.toUserEntity()` |
| Config class | `XxxConfig` with `@Configuration` | `SecurityConfig`, `GrpcConfig` |
| Properties | `XxxProperties` with `@ConfigurationProperties` | `JwtProperties`, `AuthProperties` |

## Package Naming

Base: `com.b1nd.dodamdodam.{service-name}.{layer}.{domain}.{sublayer}`

Examples:
```
com.b1nd.dodamdodam.auth.presentation.auth          # REST controllers
com.b1nd.dodamdodam.auth.application.auth.data       # DTOs (request/, response/ subdirs)
com.b1nd.dodamdodam.auth.domain.principal.service    # Domain services
com.b1nd.dodamdodam.auth.domain.principal.entity     # JPA entities
com.b1nd.dodamdodam.auth.domain.principal.exception  # Domain exceptions
com.b1nd.dodamdodam.auth.infrastructure.security     # Infrastructure implementations
com.b1nd.dodamdodam.core.common.exception.base       # Core shared code
```

## Code Style

- **Constructor injection** via primary constructor — never use `@Autowired`
- **No wildcard imports** — each import on its own line
- Spring Boot apps use `scanBasePackages = ["com.b1nd.dodamdodam"]`
- JPA entities are `class` (not `data class`), with `@Id` as body property (`val id: Long? = null`)
- R2DBC entities can be `data class`
- DTOs (request/response) are always `data class`
- Use `@Transactional(rollbackFor = [Exception::class])` on use cases (or `rollbackOn`)
- Repositories: Spring Data JPA interfaces extending `JpaRepository<Entity, Long>`
- Single-expression functions preferred when body is short
- `companion object` factory methods for response DTOs (e.g., `LoginResponse.fromJwtPair()`)
- Extension functions for request-to-entity mapping (in separate `XxxMapper.kt`)

## Error Handling

The project uses a structured exception hierarchy:

```
ExceptionCode (interface)           — status: HttpStatus, message: String
  └─ XxxExceptionCode (enum)        — per-domain codes (e.g., UserExceptionCode, PrincipalExceptionStatusCode)

BasicException (open class)         — extends RuntimeException, holds ExceptionCode
  └─ UserNotFoundException          — one class per error, extends BasicException
  └─ PasswordIncorrectException

GlobalExceptionHandler (interface)  — defines handleDodamException()
  └─ AuthExceptionHandler           — per-service @RestControllerAdvice implementation

Response<T> (data class)            — standard API response wrapper { status, message, data }
```

**To add a new error:**
1. Add enum entry in the domain's `XxxExceptionCode` with `HttpStatus` and Korean message
2. Create exception class extending `BasicException(XxxExceptionCode.YOUR_CODE)`
3. Throw it from domain/application layer — handler catches `BasicException` automatically

**gRPC errors**: `GrpcExceptionHandler` (ServerInterceptor) catches `BasicException` and maps HTTP status → gRPC status.

**Gateway errors**: `GlobalErrorWebExceptionHandler` (reactive) returns JSON error responses.

## Concurrency Model

- **service-auth, service-user**: Servlet (blocking). Use `runBlocking` for coroutine interop.
- **service-gateway**: Reactive (WebFlux). Uses `kotlinx-coroutines-reactor`, `suspend` functions.
- **gRPC controllers**: Always `suspend` functions. Use `CoroutineBlockingExecutor` to run JPA calls.
- `BlockingExecutor` interface + `CoroutineBlockingExecutor` impl for dispatching blocking I/O in coroutines.

## Database

- **MySQL** across all services
- **JPA** for service-auth, service-user (blocking)
- **R2DBC** for service-gateway (reactive)
- **Flyway** for migrations — location: `src/main/resources/database/migration/`
- Migration naming: `V{YYYYMMDD}__description.sql` (e.g., `V20260217__create_routes_table.sql`)
- JPA entities extend `BaseTimeEntity` (provides `createdAt`, `modifiedAt` with `@EntityListeners`)
- `allOpen` plugin configured for `@Entity`, `@MappedSuperclass`, `@Embeddable`

## Dependency Management

- Version catalog at `gradle/libs.versions.toml` — all versions declared centrally
- Use `libs.xxx` accessors in `build.gradle.kts` (e.g., `libs.springBootStarterData.jpa`)
- Bundles: `libs.bundles.kotlinxEcosystem` (datetime + serialization + coroutines)
- Inter-module deps use `project(":core:core-common")` syntax
- Core modules use `api()` for transitive exposure, services use `implementation()`

## Application Configuration

- Each service has `application.yml` + `application-local.yml` (profile-based)
- Default profile: `local`
- Env vars in `application.yml` use `${ENV_VAR}` syntax
- Sensitive config (DB creds, JWT keys) via `.env` file (gitignored)

## Proto / gRPC

- Proto files in `core/core-grpc/src/main/proto/{service}/{service}.proto`
- Java package: `com.b1nd.dodamdodam.grpc.{service}`
- Use `option java_multiple_files = true`
- Kotlin gRPC stubs via `grpc-kotlin-stub`
- Import alias when proto and domain names conflict: `import ... as XxxGrpcRequest`

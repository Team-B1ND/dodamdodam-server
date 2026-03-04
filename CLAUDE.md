# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

```bash
# Build (skip tests)
./gradlew :dodam-application:dodam-rest-api:build -x test --parallel

# Run tests
./gradlew test

# Run a single test class
./gradlew :MODULE_PATH:test --tests "fully.qualified.ClassName"

# Run the application
./gradlew :dodam-application:dodam-rest-api:bootRun
```

The CI pipeline builds with `./gradlew :dodam-application:dodam-rest-api:build -x test --parallel`.

## Architecture

This is a **Gradle multi-module** Spring Boot 3.0.4 project (Java 17) — a dormitory management system (도담도담) for Korean schools.

### Module Structure

- **dodam-system-core** — Shared utilities, exception base classes (`ExceptionCode` interface, `CustomException`)
- **dodam-system-domain/dodam-domain-rds** — JPA entities, repositories, domain services (`@Service`) for MySQL
- **dodam-system-domain/dodam-domain-redis** — Redis models and repositories (Lettuce, master-slave topology)
- **dodam-in-system-available/** — External integration clients (Firebase, Discord webhook, Gabia SMS, Google SMTP, AWS S3, NEIS meals, YouTube, Melon charts, QR codes, etc.) all built on Spring WebClient
- **dodam-application/dodam-rest-api** — Main REST API application (controllers, use cases, security)
- **dodam-application/dodam-internal-process/dodam-process-listener** — Event listeners
- **dodam-system-sync/dodam-sync-rds-redis** — Cache synchronization between RDS and Redis

### Layered Architecture (per domain feature in dodam-rest-api)

```
presentation/  → @RestController, injects use cases
application/   → @Component use cases, @Transactional orchestration, Req/Res DTOs
```
Domain logic lives in **dodam-domain-rds**:
```
service/       → @Service, business rules, repository calls, throws custom exceptions
repository/    → JpaRepository interfaces
entity/        → @Entity classes extending BaseEntity (createdAt/modifiedAt auditing)
```

### Key Patterns

- **Response wrappers**: `Response.ok(message)` / `ResponseData.ok(message, data)` with status + message + optional data
- **Exception handling**: Custom exceptions implement `ExceptionCode` (status, name, message). Global `@RestControllerAdvice` catches them. Errors also sent to Discord via webhook.
- **Security**: JWT token filter chain → `TokenFilter` → `TokenExceptionFilter`. Four roles: `STUDENT`, `TEACHER`, `PARENT`, `ADMIN`. Endpoint authorization in `SecurityConfig`.
- **Entities** use Lombok (`@Getter`, `@Builder`, `@NoArgsConstructor`) and contain business logic methods.
- **External clients** are each in their own module under `dodam-in-system-available/`.

### Main Domains

Club, Member (Student/Teacher/Parent), Division, Schedule, OutSleeping, OutGoing, Point, Recruitment, Notice, Bus, Banner, NightStudy, WakeUpSong

### Infrastructure

- **Database**: MySQL (JPA, Hibernate ddl-auto: none)
- **Cache**: Redis master-slave (1 master, 2 slaves)
- **Monitoring**: Prometheus metrics on port 8090
- **Deployment**: Docker → DockerHub → EC2 via GitHub Actions CD
- **App port**: 8080, timezone: Asia/Seoul

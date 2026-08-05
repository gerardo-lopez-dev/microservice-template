# Microservice Template

[![CI](https://github.com/gerardo-lopez-dev/microservice-template/actions/workflows/ci.yml/badge.svg)](https://github.com/gerardo-lopez-dev/microservice-template/actions/workflows/ci.yml)
[![Coverage](.github/badges/jacoco.svg)](https://github.com/gerardo-lopez-dev/microservice-template/actions/workflows/ci.yml)

Spring Boot 4.1 microservice template with Java 21, PostgreSQL, Docker support, and CI/CD pipeline.

## Tech Stack

- Java 21
- Spring Boot 4.1
- Spring Data JPA
- PostgreSQL (H2 for local profile)
- Docker multi-stage build
- JaCoCo (code coverage)
- Spotless (code formatting)

## Prerequisites

- JDK 21
- Maven 3.9+ (or use included `mvnw`)
- Docker & Docker Compose (optional)
- PostgreSQL (for dev/prod profiles)

## Project Structure

```
src/main/java/com/template/microservicetemplate/
├── MicroserviceTemplateApplication.java
├── domain/                     # Pure domain — no framework deps
│   ├── model/
│   │   ├── entity/             # Domain entities (records/POJOs)
│   │   └── valueobject/        # Value objects
│   ├── port/
│   │   ├── inbound/            # Use case interfaces
│   │   └── outbound/           # Repository/gateway interfaces
│   └── service/                # Domain service implementations
├── application/                # Use cases, DTOs, mappers
│   ├── usecase/
│   ├── dto/
│   │   ├── request/
│   │   └── response/
│   └── mapper/
└── infrastructure/             # Framework code (Spring, JPA)
    ├── config/
    └── adapter/
        ├── inbound/rest/       # REST controllers
        └── outbound/persistence/ # JPA repositories + entities
src/main/resources/
├── application.yaml            # Base config
├── application-local.yaml      # Local (H2 in-memory)
├── application-dev.yaml        # Dev (PostgreSQL)
├── application-prod.yaml       # Production (PostgreSQL)
└── db/migration/               # Flyway migrations
```

## Running the Application

### Local (H2 in-memory)

```bash
./mvnw spring-boot:run
```

The app starts with the `local` profile by default using an H2 in-memory database.
H2 console available at http://localhost:8080/h2-console.

### Dev profile

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

Requires PostgreSQL running. Configure connection via environment variables or `.env` file.

### With Docker Compose

```bash
docker compose up
```

Starts the app with the `dev` profile and a PostgreSQL 16 instance.

## Environment Variables

| Variable     | Description                  | Default                              |
|--------------|------------------------------|--------------------------------------|
| `DB_URL`     | JDBC connection URL          | jdbc:postgresql://localhost:5432/dev_db |
| `DB_USERNAME`| Database username            | postgres                             |
| `DB_PASSWORD`| Database password            | postgres                             |
| `SERVER_PORT`| Server port                  | 8080                                 |
| `LOG_LEVEL`  | Root logging level           | INFO                                 |

Copy `.env.example` to `.env` and adjust values as needed.

## Build & Test

```bash
# Run tests
./mvnw verify -B

# Check formatting
./mvnw spotless:check -B

# Auto-fix formatting
./mvnw spotless:apply -B

# Generate JaCoCo coverage report
./mvnw verify -B
# Report: target/site/jacoco/index.html
```

## CI/CD

GitHub Actions workflow (`.github/workflows/ci.yml`) runs on push to `main` and
pull requests:

| Job | Depends on | What it does |
|---|---|---|
| `lint` | — | Checks formatting with Spotless |
| `test` | `lint` | Runs unit tests, uploads JaCoCo data |
| `build` | `test` | Compiles and packages without re-running tests |
| `coverage` | `test` | Generates and commits coverage badge |
| `docker` | `build` | Builds Docker image (main + tags only) |

## Docker

Multi-stage Dockerfile:

- **Build stage**: `eclipse-temurin:21-jdk` - compiles and packages
- **Runtime stage**: `eclipse-temurin:21-jre` - minimal runtime image

```bash
docker build -t microservice-template .
docker run -p 8080:8080 microservice-template
```

## Actuator Endpoints

- `/actuator/health` - Health check
- `/actuator/info` - App info

Exposed endpoints vary by profile (local/dev expose `health,info`, prod only `health`).

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

El template sigue **Arquitectura Hexagonal** (Puertos y Adaptadores). Tres capas principales
con reglas de dependencia estrictas:

| Capa | Paquete | Propósito |
|---|---|---|
| **domain** | `domain/` | Lógica de negocio pura. Sin dependencias de frameworks. |
| **application** | `application/` | Casos de uso, DTOs, mappers. Orquesta el dominio. |
| **infrastructure** | `infrastructure/` | Adaptadores concretos: Spring, JPA, REST, mensajería. |

#### Detalle de paquetes

| Paquete | Propósito | Ejemplo |
|---|---|---|
| `domain.model.entity` | Entidades de dominio puras (sin anotaciones de framework) | `Product` |
| `domain.model.valueobject` | Value Objects inmutables | `Money` |
| `domain.model.event` | Eventos de dominio | (vacío, futuro) |
| `domain.port.inbound` | Puertos de entrada (interfaces de caso de uso) | `CreateProductUseCase` |
| `domain.port.outbound` | Puertos de salida (repositorios, gateways) | `ProductRepository` |
| `domain.service.impl` | Servicios de dominio | (vacío, futuro) |
| `application.usecase` | Implementaciones de casos de uso | `CreateProductUseCaseImpl` |
| `application.dto.request` | DTOs de entrada | `CreateProductRequest` |
| `application.dto.response` | DTOs de salida | `ProductResponse` |
| `application.mapper` | Mappers entre entidades y DTOs | `ProductMapper` |
| `infrastructure` | Punto de entrada `@SpringBootApplication` | `MicroserviceTemplateApplication` |
| `infrastructure.config` | Configuraciones de Spring | `BeanConfig`, `DbHealthIndicator`, `EnvironmentVariableValidator` |
| `infrastructure.adapter.inbound.rest` | Controladores REST | `ProductController` |
| `infrastructure.adapter.inbound.messaging` | Consumers de mensajería | (vacío, futuro) |
| `infrastructure.adapter.outbound.persistence` | Entidades JPA, repositorios Spring Data, adaptadores | `ProductJpaEntity`, `ProductJpaRepository`, `SpringProductRepository` |
| `infrastructure.adapter.outbound.messaging` | Producers de mensajería | (vacío, futuro) |
| `infrastructure.adapter.outbound.external` | Clientes de servicios externos (Feign, etc.) | (vacío, futuro) |

Árbol completo de paquetes:

```
src/main/java/com/template/microservicetemplate/
├── domain/                                # Capa de dominio
│   ├── model/
│   │   ├── entity/                        # Entidades de dominio (puras, sin JPA)
│   │   ├── valueobject/                   # Value Objects (Money, Address, etc.)
│   │   └── event/                         # Eventos de dominio
│   ├── port/
│   │   ├── inbound/                       # Puertos de entrada (interfaces de caso de uso)
│   │   └── outbound/                      # Puertos de salida (repositorios, gateways)
│   └── service/
│       └── impl/                          # Implementación de servicios de dominio
├── application/                           # Capa de aplicación
│   ├── usecase/                           # Implementaciones de casos de uso
│   ├── dto/
│   │   ├── request/                       # DTOs de entrada
│   │   └── response/                      # DTOs de salida
│   └── mapper/                            # Mappers entre entidades y DTOs
└── infrastructure/                        # Capa de infraestructura
    ├── MicroserviceTemplateApplication.java  # Punto de entrada (@SpringBootApplication)
    ├── config/                            # Configuraciones de Spring
    └── adapter/
        ├── inbound/
        │   ├── rest/                      # Controladores REST
        │   └── messaging/                 # Consumers de mensajería
        └── outbound/
            ├── persistence/               # Entidades JPA + repositorios Spring Data + adaptadores
            ├── messaging/                 # Producers de mensajería
            └── external/                  # Clientes de servicios externos (Feign, etc.)
```

### Reglas de Dependencia

```
domain       → no importa de application ni infrastructure
application  → puede importar de domain, no de infrastructure
infrastructure → puede importar de domain y application
```

El dominio es el centro. La infraestructura implementa los contratos que el dominio define.
Las clases de ejemplo (`Product`, `Money`, `ProductController`, etc.) están marcadas con
`Código de ejemplo` en su Javadoc: elimínalas o reemplázalas al implementar tu dominio real.

### Renaming the Template

Al clonar este template para un nuevo microservicio, renombra:

1. **Directorio del paquete base**: renombra `microservicetemplate` en
   `src/main/java/com/template/` y `src/test/java/com/template/`.
2. **Declaraciones `package`**: actualiza el `package` en todos los archivos `.java`.
3. **`<groupId>` en `pom.xml`**: cambia `com.template` por tu groupId.
4. **Clase principal**: renombra `MicroserviceTemplateApplication` y actualiza todas las
   referencias (tests, `@SpringBootApplication`, `main` method).
5. **Configuración Docker**: actualiza el nombre de la imagen en `docker-compose.yml` y
   referencias en el `Dockerfile`.

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

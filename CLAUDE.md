# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

NevisWealth v2 — a Spring Boot 4.0.5 REST API for managing clients and their documents, backed by MongoDB. Java 25, Gradle 9.4.1.

## Build & Run Commands

```bash
./gradlew build          # Compile, test, and package
./gradlew bootRun        # Run the application
./gradlew test           # Run all tests
./gradlew test --tests "com.neviswealth.app.MainTests"  # Run a single test class
./gradlew clean build    # Clean rebuild
```

## Architecture

The project follows **Hexagonal Architecture** (ports & adapters) with three distinct layers:

```
core/                         # Inner hexagon — no framework dependencies
├── domain/                   # Domain records (Client, Document)
├── port/inbound/             # Use case interfaces (e.g. CreateClientUseCasePort)
│   └── dto/                  # Use case input/output records
└── port/outbound/            # Persistence port interfaces (e.g. ClientPersistencePort)

app/service/                  # Application layer — implements inbound ports
                              # (ClientService, DocumentService)

adapter/                      # Outer hexagon — framework-specific implementations
├── http/inbound/             # REST controllers + request/response DTOs
│   ├── dto/                  # HTTP-specific records (CreateClientRequestBody, ClientResponse)
│   └── mapper/               # HttpDtoMapper<TDomain, THttpDto> interface
└── persistence/outbound/     # MongoDB persistence adapters
    ├── model/                # MongoDB @Document models (ClientModel, DocumentModel)
    ├── mapper/               # ModelMapper<TDomain, TModel> interface + implementations
    └── repository/           # Spring Data MongoRepository interfaces
```

**Key conventions:**
- Domain objects are Java `record` types (immutable value objects)
- Ports are interfaces; adapters are `@Component`/`@Service`/`@RestController` implementations
- Two mapper interfaces bridge layers: `HttpDtoMapper` (domain ↔ HTTP DTO) and `ModelMapper` (domain ↔ persistence model)
- Persistence models are plain classes (not records) with final fields and manual getters
- Documents are nested under clients: `POST /clients/{clientId}/documents`
- Use case port inputs are records in `core/port/inbound/dto/`

## Tech Stack

- **Framework:** Spring Boot 4.0.5 (Spring WebMVC)
- **Database:** MongoDB (Spring Data MongoDB)
- **HTTP Client:** Spring RestClient
- **API Docs:** SpringDoc OpenAPI (`springdoc-openapi-starter-webmvc-ui`)
- **Testing:** JUnit 5 via `spring-boot-starter-*-test` dependencies
- **Build:** Gradle (Kotlin DSL), wrapper included

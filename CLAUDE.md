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
├── domain/                   # Domain records (Client, Document, SearchResult)
├── exception/                # CoreException + CoreExceptionCode enum
├── port/inbound/             # Use case interfaces + input records
├── port/outbound/            # Persistence, AI, and utility port interfaces
└── service/                  # Application services — implements inbound ports
                              # (ClientService, DocumentService, SearchService)

adapter/                      # Outer hexagon — framework-specific implementations
├── http/inbound/             # REST controllers + request/response DTOs
│   ├── config/               # OpenApiConfig
│   ├── dto/                  # HTTP-specific records (CreateClientRequestBody, ClientResponse, ErrorResponse)
│   └── mapper/               # HttpDtoMapper<TDomain, THttpDto> interface
├── persistence/outbound/     # MongoDB persistence adapters
│   ├── config/               # MongoConfig, MongoIndexInitializer (vector search index)
│   ├── model/                # MongoDB @Document models (ClientModel, DocumentModel)
│   ├── mapper/               # ModelMapper<TDomain, TModel> interface + implementations
│   └── repository/           # Spring Data MongoRepository interfaces
├── ai/outbound/              # OpenAI adapters (LLM summarization, embeddings)
│   ├── config/               # OpenAiConfig (RestClient bean)
│   └── dto/                  # OpenAI API request/response records
└── common/outbound/          # Utility adapters (SystemClockAdapter, RandomUuidGenerator)
```

**Key conventions:**
- Domain objects are Java `record` types (immutable value objects)
- Ports are interfaces; adapters are `@Component`/`@Service`/`@RestController` implementations
- Two mapper interfaces bridge layers: `HttpDtoMapper` (domain ↔ HTTP DTO) and `ModelMapper` (domain ↔ persistence model)
- Persistence models are plain classes (not records) with final fields and manual getters
- Documents are nested under clients: `POST /clients/{clientId}/documents`
- Use case port inputs are records in `core/port/inbound/`

## Tech Stack

- **Framework:** Spring Boot 4.0.5 (Spring WebMVC)
- **Database:** MongoDB (Spring Data MongoDB) with Atlas Vector Search
- **AI:** OpenAI (embeddings via `text-embedding-3-small`, chat/summarization via `gpt-5.4-mini`)
- **Validation:** Jakarta Validation (Bean Validation / JSR 380)
- **HTTP Client:** Spring RestClient
- **API Docs:** SpringDoc OpenAPI (`springdoc-openapi-starter-webmvc-ui`)
- **Testing:** JUnit 5 via `spring-boot-starter-*-test` dependencies
- **Build:** Gradle (Kotlin DSL), wrapper included
- **Local Dev:** Docker Compose (MongoDB Atlas local image on port 27017)

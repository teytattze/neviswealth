# Discussion

## Assumptions

- A prototype scoped to days, not weeks — a working solution is more important than a perfectly optimized one
- Single-user, no authentication — focus is on the core domain (client + document management, AI-powered search)
- OpenAI API is available and reliable — no local model fallback
- Local development only — Docker Compose with MongoDB Atlas local image, no cloud deployment

## Trade-offs & Decisions

### 1. MongoDB over SQL

- **Why:** Flexible schema suits a rapidly evolving prototype; native vector search support via Atlas avoids a separate
  search engine
- **Trade-off:** No referential integrity between `clients` and `documents` collections
- **Mitigation:** Application-layer validation (e.g., `DocumentController` verifies client exists before creating a
  document); for stronger guarantees, use MongoDB multi-document transactions

### 2. Hexagonal architecture over layered architecture

- **Why:** Port abstractions keep the domain framework-free and fully unit-testable with mocks; adapters are swappable (
  e.g., replace MongoDB with PostgreSQL without touching core)
- **Trade-off:** More files and interfaces than a simple layered architecture for a small prototype
- **Mitigation:** N/A

### 3. Embeddings generated at write-time

- **Why:** Documents are search-ready immediately; no embedding latency at query time
- **Trade-off:** Every document pays the OpenAI embedding cost upfront; embeddings become stale if the model changes
- **Mitigation:** An async re-indexing job to regenerate embeddings on model change, or store a model version alongside
  the embedding for selective refresh

### 4. Summaries generated at read-time (not persisted)

- **Why:** Summaries are always fresh and contextual to the search query; the `includeSummary` flag makes them opt-in
- **Trade-off:** N+1 LLM calls per search when summaries are requested, repeated cost for the same documents
- **Mitigation:** Cache summaries keyed by `(documentId, queryHash)` with a TTL, or batch summarization via
  parallel/async calls

### 5. Synchronous HTTP (RestClient) over async (WebClient)

- **Why:** Simpler code, straightforward debugging, easier to reason about error flows
- **Trade-off:** Threads block during OpenAI API calls, limiting throughput under load
- **Mitigation:** Enable virtual threads (Java 21+) to scale blocking calls without thread exhaustion, or switch to
  `WebClient` for the AI adapter only

### 6. Java records for domain objects

- **Why:** Immutability by default, zero boilerplate, pattern-matching friendly
- **Trade-off:** Updates require copy methods (e.g., `Document.withSummary()`)
- **Mitigation:** N/A

### 7. Centralized exception codes with technology-specific prefixes

- **Why:** Codes like `MONGO_DUPLICATE_KEY` and `OPEN_AI_RATE_LIMITED` make the error origin immediately clear in logs
  and API responses
- **Trade-off:** Each adapter must manually map infrastructure exceptions to `CoreExceptionCode`
- **Mitigation:** N/A

### 8. Text index (clients) + vector index (documents)

- **Why:** Fit-for-purpose — keyword search suits structured client fields; semantic similarity suits unstructured
  document content
- **Trade-off:** Two different search paradigms to maintain and explain
- **Mitigation:** Unify under vector search for both entities if semantic search for clients becomes a requirement

## Potential Improvements

- **Optimistic locking** — prevent silent overwrites on concurrent updates (MongoDB document versioning)
- **Observability** — structured logging, request tracing, and metrics (Micrometer + OpenTelemetry)
- **Caching** — cache embeddings, summaries, and frequent search queries to reduce OpenAI costs and latency
- **Authentication & authorization** — JWT or OAuth2 with role-based access; multi-tenancy support
- **Async/parallel summarization** — reduce N+1 latency by summarizing documents concurrently
- **Pagination** — search results are currently hard-capped at 10 documents with no offset/cursor support
- **Retry with backoff** — resilience for transient OpenAI failures (rate limits, timeouts)
- **Integration tests** — current tests are unit-only; add tests against a real MongoDB instance (Testcontainers)

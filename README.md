# NevisWealth v2

A Spring Boot REST API for managing clients and their documents, backed by MongoDB.

## Prerequisites

Before you begin, make sure you have the following installed on your machine:

### 1. Java 25

This project requires **Java 25**. Download and install JDK 25
from [Oracle](https://www.oracle.com/uk/java/technologies/downloads/#java25).

Verify the installation:

```bash
java --version
```

### 2. Docker

You need Docker to run the MongoDB database locally.

- **macOS:** Install [Docker Desktop](https://www.docker.com/products/docker-desktop/)

After installing, verify Docker is running:

```bash
docker --version
docker compose version
```

### 3. OpenAI API Key (optional)

The app includes AI-powered features (document summarization and semantic search) that require an OpenAI API key. **The
app will start without one**, but those features won't work.

If you have a key, export it in your terminal:

```bash
export OPENAI_API_KEY=sk-your-key-here
```

> **Note:** You do **not** need to install Gradle. The project includes a Gradle wrapper (`./gradlew`) which
> automatically downloads the correct Gradle version for you.

## Getting Started

### Step 1 — Clone the repository

```bash
git clone https://github.com/teytattze/neviswealth.git
cd neviswealth
```

### Step 2 — Start MongoDB

This starts a local MongoDB instance using Docker. It automatically creates the required database and collections (
`clients`, `documents`).

```bash
docker compose up -d
```

Check that it's running:

```bash
docker compose ps
```

You should see the `neviswealth-mongodb` container with status `Up`.

### Step 3 — Build the project

This compiles the code and runs the tests:

```bash
./gradlew build
```

> **First time?** The Gradle wrapper will download Gradle 9.4.1 and all project dependencies. This may take a few
> minutes on the first run.

### Step 4 — Run the application

```bash
./gradlew bootRun
```

The API starts on **http://localhost:8080**.

### Step 5 — Verify it's working

Open your browser and go to:

```
http://localhost:8080/swagger-ui
```

You should see the Swagger UI page listing all available API endpoints.

## Useful Commands

| Command                                                  | What it does                                    |
|----------------------------------------------------------|-------------------------------------------------|
| `./gradlew build`                                        | Compile, run tests, and package                 |
| `./gradlew bootRun`                                      | Start the application                           |
| `./gradlew test`                                         | Run all tests                                   |
| `./gradlew test --tests "com.neviswealth.app.MainTests"` | Run a single test class                         |
| `./gradlew clean build`                                  | Delete build artifacts and rebuild from scratch |

## Stopping Everything

**Stop the application:** Press `Ctrl + C` in the terminal where `./gradlew bootRun` is running.

**Stop MongoDB:**

```bash
docker compose down
```

To also delete the database data:

```bash
docker compose down -v
```

## Example

**Request:**

```bash
curl -X 'GET' \
  'http://localhost:8080/search?query=who%20is%20John%20doe&includeSummary=true' \
  -H 'accept: */*'
```

**Response:**

```json
{
  "clients": [
    {
      "id": "4c0085d2-cacb-477b-98b7-a820b645308d",
      "createdAt": "2026-04-02T21:24:31.390Z",
      "updatedAt": "2026-04-02T21:24:31.390Z",
      "firstName": "John",
      "lastName": "Doe",
      "email": "john.doe@example.com",
      "description": "I am the example guy",
      "socialLinks": [
        "https://linkedin.com/in/johndoe"
      ]
    }
  ],
  "documents": [
    {
      "id": "06638b2b-248e-4063-8535-8b1b5b564826",
      "createdAt": "2026-04-02T22:52:15.090Z",
      "updatedAt": "2026-04-02T22:52:15.090Z",
      "title": "The founder's family",
      "content": "John doe is part of Tey family. He is Tat Tze's cousin and they grew up together in Malaysia.",
      "summary": "John Doe is part of the Tey family. He is Tat Tze’s cousin, and they grew up together in Malaysia.",
      "clientId": "4c0085d2-cacb-477b-98b7-a820b645308d"
    }
  ]
}
```
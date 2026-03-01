# scorm-engine

## Scope
Spring Boot REST backend for SCORM course lifecycle and runtime tracking:
- course import and manifest parsing (SCORM 1.2 / 2004)
- user + enrollment management
- launch issuance (launch URL/token)
- runtime commit ingestion
- terminate/flush to persistent progress snapshots

## Runtime
- Default HTTP port: `8080`
- OpenAPI UI: `http://localhost:8080/swagger-ui`

## External Dependencies
- Postgres (`5432`) for canonical persistence
- Redis (`6379`) for launch runtime state
- MinIO (`9000`) for uploaded/extracted package objects

## Architecture
```mermaid
graph LR
  API[REST Controllers] --> ORCH[Orchestrators / Application Services]
  ORCH --> PERSIST[Postgres]
  ORCH --> STATE[Redis]
  ORCH --> STORAGE[MinIO]
  ORCH --> TOKEN[JWT Token Service]
```

## Request Flow (Launch Runtime)
```mermaid
sequenceDiagram
  participant LMS as LMS/Client
  participant ENG as Engine API
  participant REDIS as Redis
  participant PG as Postgres

  LMS->>ENG: POST /api/v1/launches {userId, courseId}
  ENG->>PG: validate enrollment + create/open launch/attempt
  ENG->>REDIS: seed runtime launch state
  ENG-->>LMS: launchUrl + launchToken + attemptId

  loop runtime commit
    LMS->>ENG: POST /api/v1/runtime/launches/{launchId}/commit
    ENG->>REDIS: update runtime payload + sequencing
  end

  LMS->>ENG: POST /api/v1/launches/{launchId}/terminate
  ENG->>REDIS: read final runtime state
  ENG->>PG: persist attempt snapshots/progress + close launch
  ENG-->>LMS: 200 OK
```

## Local Run
```bash
cd ../central-docker-infrastructure/infrastructure
docker compose up --build
```

## Engine-only Build
```bash
docker build -t scorm-engine:local .
```

## Technical Docs
- Runbook: `docs/run/README.md`
- Contracts/architecture: `docs/memory/*`

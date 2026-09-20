# Athens Tours

REST API for an Athens sightseeing tour agency — browsing tours, tour guides and
schedules, and booking seats on them. Built as the final project for the ΟΠΑ
Coding Factory Java/Spring track.

## Tech stack

- Java 21
- Spring Boot 3.5.10 (Web, Data JPA, Security, Validation)
- Spring Security 6 — stateless JWT authentication, role/capability-based authorization
- Hibernate / Spring Data JPA
- Flyway — versioned DB migrations, applied automatically on startup
- MySQL 8.0 (runs in Docker)
- springdoc-openapi 2.9.1 — Swagger / OpenAPI 3 documentation
- Lombok
- JUnit 5, Mockito, AssertJ — unit tests
- Gradle (wrapper included, no local Gradle install needed)

## Architecture

Layered backend: `api` (REST controllers) → `service` (business logic) →
`repository` (Spring Data JPA) → `model` (JPA entities). DTOs at the API
boundary via `dto`/`mapper`, centralized error handling in `core`.

Roles: `ADMIN`, `GUIDE`, `CUSTOMER`, each with a set of capabilities
(`MANAGE_TOURS`, `CREATE_BOOKING`, `VIEW_OWN_BOOKINGS`, etc.) enforced with
`@PreAuthorize` on individual endpoints. Soft-delete is used throughout
(`deleted`/`deletedAt` on every entity) instead of hard deletes.

## Prerequisites

- JDK 21
- Docker Desktop (for the MySQL database)
- Git

## 1. Clone the repository

```
git clone <this-repo-url>
cd athens-tours
```

## 2. Start the database

The app expects a MySQL 8.0 container named `athens-tours-mysql`, exposed on
host port **3307** (so it doesn't clash with a local MySQL install on the
default 3306):

```
docker run -d --name athens-tours-mysql ^
  -p 3307:3306 ^
  -e MYSQL_ROOT_PASSWORD=root ^
  -e MYSQL_DATABASE=athens_tours_dev ^
  -e MYSQL_USER=tours_user ^
  -e MYSQL_PASSWORD=tours_password ^
  mysql:8.0
```

(`^` is the Windows `cmd.exe` line-continuation character — on macOS/Linux
replace it with `\`, or just put the whole command on one line.)

Give it a few seconds to finish initializing before starting the app. You can
check it's healthy with:

```
docker ps
```

Flyway takes care of the schema and seed data (roles, capabilities, tour
categories, etc.) automatically the first time the application starts —
there's nothing to run manually against the database.

## 3. Run the application

The app reads the database port from an environment variable, `MYSQL_PORT`,
which **must** be set to `3307` to match the Docker port mapping above (it
defaults to `3306`, MySQL's standard port, which is *not* what the container
is published on here).

**From IntelliJ:** open the `AthensToursApplication` run configuration →
Environment variables → `MYSQL_PORT=3307`, then just Run.

**From the command line:**

```
set MYSQL_PORT=3307
gradlew.bat bootRun
```

(macOS/Linux: `MYSQL_PORT=3307 ./gradlew bootRun`)

The app starts on **http://localhost:8080**.

On first startup, a default admin account is seeded automatically if one
doesn't already exist:

```
username: admin
password: Passw0rd!
```

(Local development only — this is not meant to ship to a real deployment as-is.)

## API documentation (Swagger)

Once the app is running:

- Swagger UI: http://localhost:8080/swagger-ui/index.html
- Raw OpenAPI spec: http://localhost:8080/v3/api-docs

To call protected endpoints from Swagger UI: run `POST /api/v1/auth/authenticate`
with a valid username/password, copy the `token` from the response, click
**Authorize** (top right) and paste it in (no need to type `Bearer ` — Swagger
adds that itself).

## Postman collection

A Postman collection (`athens-tours.postman_collection.json`) covering the
full API — auth, tours, guides, schedules, bookings, and both the admin and
customer/guide authorization boundaries — is included in the repository.
Import it into Postman, set the `baseUrl` variable to `http://localhost:8080`,
and either run individual requests or the whole thing through the Collection
Runner (the requests are chained via collection variables — captured tokens
and created-entity UUIDs get reused by later requests, so run them in order).

## Running the tests

```
gradlew.bat test
```

(macOS/Linux: `./gradlew test`)

Covers the service layer with Mockito-based unit tests (business rules such
as booking capacity enforcement and booking ownership checks).

## Frontend

A React frontend consuming this API is in progress and will live in a
separate `frontend/` directory. The backend's CORS configuration already
allows the typical local dev server origins (`http://localhost:3000` and
`http://localhost:5173`).

## Configuration reference

| Variable | Default | Purpose |
|---|---|---|
| `MYSQL_PORT` | `3306` | Port the app connects to MySQL on — set to `3307` for the Docker setup above |
| `SERVER_PORT` | `8080` | Port the application itself listens on |
| `BCRYPT_STRENGTH` | `12` | Password hashing cost factor |

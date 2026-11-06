# Athens Tours

Full-stack application (REST API + React frontend) for an Athens sightseeing tour agency — browsing tours, tour guides and
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

## Build and deploy

### Backend

Build an executable JAR (the tests run as part of the build):

```
gradlew.bat build
```

(macOS/Linux: `./gradlew build`)

The JAR is created in `build/libs/` as `athens-tours-0.0.1-SNAPSHOT.jar`
(ignore the `-plain.jar`). To run it, with the database from step 2 already
up:

```
set MYSQL_PORT=3307
java -jar build\libs\athens-tours-0.0.1-SNAPSHOT.jar
```

(macOS/Linux: `MYSQL_PORT=3307 java -jar build/libs/athens-tours-0.0.1-SNAPSHOT.jar`)

The database connection and the JWT key can be overridden with the environment
variables listed in the configuration table below, so the same JAR can be
deployed to any environment without changing the code.

### Frontend

```
cd frontend
npm install
npm run build
```

The production build is created in `frontend/dist/` as static files, which can
be served by any web server (for example Nginx). Set `VITE_API_URL` before
building so that the frontend points to the deployed backend.

## Running the tests

```
gradlew.bat test
```

(macOS/Linux: `./gradlew test`)

Covers the service layer with Mockito-based unit tests (business rules such
as booking capacity enforcement and booking ownership checks).

## Frontend

A React single-page application in the `frontend/` directory consumes this API.
It supports four kinds of users:

- **Visitors** browse tours, tour guides and available dates.
- **Customers** register, log in, book a seat on a tour date and cancel their
  bookings (My Bookings). A customer cannot hold two active bookings for the
  same tour date.
- **Guides** see their own schedule.
- **Admins** manage tours, guides and schedules, and view all bookings.

Tech: React 19, TypeScript, Vite, React Router, React Hook Form + Zod,
Tailwind CSS v4, shadcn/ui.

### Run the frontend

Prerequisite: Node.js 20.19+ (or 22+). Start the backend first (see above),
then in a second terminal:

```
cd frontend
npm install
npm run dev
```

Open **http://localhost:5173**.

The API URL defaults to `http://localhost:8080/api/v1`. To change it, copy
`.env.example` to `.env` and edit `VITE_API_URL`.

To create a production build: `npm run build`.

## Configuration reference

| Variable | Default | Purpose |
|---|---|---|
| `MYSQL_PORT` | `3306` | Port the app connects to MySQL on — set to `3307` for the Docker setup above |
| `SERVER_PORT` | `8080` | Port the application itself listens on |
| `BCRYPT_STRENGTH` | `12` | Password hashing cost factor |
| `MYSQL_HOST` | `localhost` | Database host |
| `MYSQL_DB` | `athens_tours_dev` | Database name |
| `MYSQL_USER` | `tours_user` | Database user |
| `MYSQL_PASSWORD` | `tours_password` | Database password (local dev default) |
| `JWT_SECRET` | dev key | Base64 key used to sign JWTs. Override it outside local development |
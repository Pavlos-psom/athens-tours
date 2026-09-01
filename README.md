# Athens Tours

REST API για γραφείο ξεναγήσεων στην Αθήνα: Spring Boot + MySQL, layered architecture (Repository /
Service / Controller), JWT authentication/authorization, domain model με Tour / TourGuide /
TourSchedule / Booking.

Το πλήρες ER diagram του domain model είναι εδώ: *(link προς το artifact)*.

## Stack

- Java 21, Spring Boot 3.5, Gradle
- Spring Web, Spring Data JPA, Spring Security, Bean Validation
- MySQL 8 (μέσω Docker), Flyway migrations
- JWT (jjwt), springdoc-openapi (Swagger UI)
- JUnit 5, AssertJ, H2 (για tests)

## Δομή packages

```
com.athenstours
 ├── model/              JPA entities (+ model/static_data για lookup entities π.χ. Category)
 ├── repository/         Spring Data JPA repositories
 ├── service/            Business logic, interface + impl ανά entity
 ├── mapper/             Entity <-> DTO conversion
 ├── dto/                Request/response records (InsertDTO / UpdateDTO / ReadOnlyDTO)
 ├── api/                REST controllers
 ├── security/           JWT filter, SecurityConfiguration, entry point / denied handler
 └── core/
      ├── exceptions/    Custom exceptions + Global Exception Handler
      ├── enums/         Enums (π.χ. BookingStatus)
      └── filters/       Dynamic query filter DTOs
```

Resources: `src/main/resources/db/migration` — Flyway scripts (V1__..., V2__..., ...).

## Πώς τρέχει τοπικά

### 1. Βάση δεδομένων (Docker)

```bash
docker run --name athens-tours-mysql \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=athens_tours_dev \
  -e MYSQL_USER=tours_user \
  -e MYSQL_PASSWORD=tours_password \
  -p 3306:3306 \
  -d mysql:8.0
```

Αν προτιμάς να μη χρησιμοποιήσεις Docker, φτιάξε τη βάση χειροκίνητα (π.χ. με MySQL Workbench) με
τα ίδια στοιχεία, ή άλλαξε τα `MYSQL_*` env vars / το `application-dev.properties`.

### 2. Άνοιγμα στο IntelliJ

- File → Open → επίλεξε τον φάκελο `athens-tours`.
- Άφησε το IntelliJ να κάνει sync το Gradle project (θα κατεβάσει μόνο του τα dependencies).
- Βεβαιώσου ότι το Project SDK είναι Java 21 (File → Project Structure).

### 3. Build & Run

```bash
./gradlew clean build
./gradlew bootRun
```

Ή απλά πάτα Run πάνω στο `AthensToursApplication`.

> Σημείωση: αυτό το project δεν έχει ακόμα Gradle wrapper jar (χρειάζεται δίκτυο για να κατέβει).
> Το IntelliJ το φτιάχνει μόνο του με sync· αν θες να τρέξεις `./gradlew` από τερματικό πριν το
> ανοίξεις στο IntelliJ, τρέξε πρώτα `gradle wrapper` (αν έχεις τοπικά εγκατεστημένο Gradle).

### 4. Έλεγχος

- Health check: `GET http://localhost:8080/actuator/health`
- Swagger UI (μόλις προστεθούν endpoints): `http://localhost:8080/swagger-ui.html`

## Σειρά υλοποίησης

1. `Role`, `Capability`, `roles_capabilities` — auth lookup πίνακες + Flyway seed data
2. `User` + Spring Security / JWT
3. `Category`, `Tour`
4. `TourGuide`
5. `TourSchedule`
6. `Booking`
7. JUnit tests ανά layer, entity-by-entity

## Build & deploy (θα συμπληρωθεί)

Αυτό το section θα ενημερώνεται σταδιακά ώστε στο τέλος να περιγράφει αναλυτικά build & deploy,
όπως ζητά η εκφώνηση.

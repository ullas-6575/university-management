# University Management

A Spring Boot web application scaffold for managing university administrators, teachers, and students. The repository includes Thymeleaf page templates and MVC/REST controller files.

## Maven Coordinates

- Group ID: `com.ullas`
- Artifact ID: `University-management`
- Version: `0.0.1-SNAPSHOT`
- Java package: `com.ullas.University.management`
- Main class: `com.ullas.University.management.UniversityManagementApplication`

## Project Status

The application uses Spring MVC, Thymeleaf, Spring Security, Spring Data JPA, and H2 for local development. H2 is configured as an in-memory database, so all records are discarded when the application stops. This is a development setup, not a production persistence configuration.

Initial accounts are not created by default. To enable the optional local seed accounts, set strong passwords for all three roles; never commit these values.

## Requirements

- Java 17
- Docker Engine with the Docker Compose plugin, if using containers

The Maven wrapper is included, so a separate Maven installation is not required for local builds.

## Run Locally

From the repository root:

```bash
./mvnw test
./mvnw spring-boot:run
```

To seed accounts for a local session, set passwords in the shell before starting the application:

```bash
export APP_SEED_ENABLED=true
export APP_SEED_ADMIN_PASSWORD='replace-with-a-unique-password-12-chars-or-more'
export APP_SEED_TEACHER_PASSWORD='replace-with-another-unique-password'
export APP_SEED_STUDENT_PASSWORD='replace-with-a-third-unique-password'
./mvnw spring-boot:run
```

When seeding is enabled on an empty database, the logins are `admin`, `teacher`, and `student`, each using its corresponding environment password. After startup, open [http://localhost:8080](http://localhost:8080).

## Run With Docker Compose

Set the same four `APP_SEED_*` environment variables before Compose if you want seed accounts, then run:

```bash
docker compose config
docker compose up --build
```

Compose publishes the application on `127.0.0.1:8080` by default. Change the host port with `APP_PORT`, for example:

```bash
APP_PORT=8081 docker compose up --build
```

Stop the foreground service with `Ctrl+C`, then remove its container and network with:

```bash
docker compose down
```

Compose binds the service to loopback, runs the process as a non-root user, and applies container resource and filesystem restrictions. A successful `docker compose config` validates configuration only; it does not prove application behavior or production security.

## Tests

Run the test suite with:

```bash
./mvnw test
```

The test suite includes six service unit tests for account updates and admin deletion rules, plus Spring MVC integration tests for page rendering, authentication, role boundaries, and admin creation through both the form and REST API. GitHub Actions runs `./mvnw --batch-mode --no-transfer-progress verify` on Java 17 for pushes, pull requests, and manual dispatches.

## Security Notes

The application has database-backed authentication, BCrypt password encoding, role checks, and CSRF protection configured. This is not a production security certification: review authorization scope, operational secrets, database durability, TLS, and security tests before exposing it publicly or using real university data.

# University Management System

A Java 17 Spring Boot web application for managing university users with three roles: Admin, Teacher, and Student. The project includes Thymeleaf MVC pages, role-based Spring Security, JPA repositories, and REST CRUD endpoints for each resource type.

## Tech stack

- Java 17
- Spring Boot 4.1.2
- Spring MVC + Thymeleaf
- Spring Security
- Spring Data JPA
- H2 in-memory database
- Docker Compose
- Maven Wrapper

## Roles and permissions

| Role    | Admins | Teachers | Students |
| ------- | ------ | -------- | -------- |
| ADMIN   | CRUD   | CRUD     | CRUD     |
| TEACHER | Denied | CRUD     | Denied   |
| STUDENT | Denied | Denied   | CRUD     |

The access rules are enforced in the Spring Security configuration and controller-level `@PreAuthorize` checks.

## Demo users

When the app starts with seed mode enabled, these accounts are created automatically:

| Role    | Username  | Password   |
| ------- | --------- | ---------- |
| Admin   | `admin`   | `Admin1`   |
| Teacher | `teacher` | `Teacher1` |
| Student | `student` | `Student1` |

These are safe local-development demo credentials and are intentionally short enough for lab use.

## Project structure

- `src/main/java/com/ullas/University/management` — application code
- `src/main/resources/templates/templates` — Thymeleaf pages
- `src/main/resources/static/css` — static CSS assets
- `src/test/java` — unit and integration tests
- `Dockerfile` — container image for the app
- `compose.yaml` — Docker Compose setup
- `.github/workflows/ci.yml` — CI workflow for Java 17 verification

## Requirements

- Java 17
- Maven Wrapper included in the repo
- Docker + Compose plugin (optional, for containerized runs)

## Run locally with Maven

From the project root:

```bash
cd /media/ullas-biswas-shontu/New\ Volume/code/swe/University-management
./mvnw spring-boot:run
```

Open the app in a browser:

```text
http://localhost:8080
```

If port 8080 is already in use, start on another port:

```bash
SERVER_PORT=8081 ./mvnw spring-boot:run
```

Then open:

```text
http://localhost:8081
```

## Run locally with seeded demo accounts

To create the demo admin/teacher/student users automatically:

```bash
cd /media/ullas-biswas-shontu/New\ Volume/code/swe/University-management
APP_SEED_ENABLED=true ./mvnw spring-boot:run
```

Then log in with:

- Username: `admin` / Password: `Admin1`
- Username: `teacher` / Password: `Teacher1`
- Username: `student` / Password: `Student1`

The login screen is available at:

```text
http://localhost:8080/login
```

or:

```text
http://localhost:8081/login
```

## Run with Docker Compose

From the repo root:

```bash
cd /media/ullas-biswas-shontu/New\ Volume/code/swe/University-management
docker compose up --build
```

The app is published on `127.0.0.1:8080` by default. To use a different host port:

```bash
echo 'APP_PORT=8081' > .env
docker compose up --build
```

If you want seeded users in Docker too:

```bash
APP_SEED_ENABLED=true docker compose up --build
```

To stop the app:

```bash
docker compose down
```

## Test the project

Run the full suite:

```bash
./mvnw test
```

The project includes:

- unit tests for account hashing and normalization
- service tests for admin deletion rules
- integration tests for app startup, login, security, and role access

## CI

GitHub Actions runs the project against Java 17 and validates the app with Maven tests. The workflow also checks Docker Compose configuration so that broken container settings are detected before deployment.

## Notes

- The app uses H2 in memory for local development. Data is temporary and resets when the app stops.
- Passwords must be printable ASCII and at least 6 characters long.
- This project is intended for local lab/demo use, not for production deployment without further hardening.

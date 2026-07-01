# AGENTS.md

## Scope

- This file applies to the entire `backend/` directory.
- This module is a Spring Boot 4 backend using JPA, Spring Security, JWT, and Flyway.
- Treat `backend/` as its own boundary: do not modify frontend code or root-level files unless explicitly requested.

## Obiettivo operativo

- Prefer small patches that fit the existing architecture: `controller -> service -> repository -> model/dto`.
- Before changing code, read the files already involved in the real execution flow instead of introducing new abstractions.
- Do not rename packages, entities, endpoints, or config keys without a concrete and verifiable reason.

## Struttura del codice

- `config/` and `security/`: authentication, CORS, JWT filters, REST entry points.
- `controller/`: HTTP orchestration only, no heavy domain logic.
- `service/`: application logic and business rules.
- `repository/`: JPA queries and projections.
- `model/`: persisted entities.
- `dto/`: API payloads and input/output records.
- `src/main/resources/db/migration/`: canonical source for runtime schema and seed data.

## Configuration and Profiles

- `src/main/resources/application.properties` is local/dev configuration and must stay out of git.
- `src/main/resources/application-example.properties` is committable documentation/config example.
- `src/main/resources/application-prod.properties` is committable only if it contains environment-variable placeholders and no hardcoded secrets.
- In `prod`, do not replace `spring.jpa.hibernate.ddl-auto=validate` with schema-mutating modes.

## Database and Flyway

- The real backend database is MySQL.
- The migrations in `db/migration/` are the source of truth for the runtime schema.
- Do not rely on H2 to validate MySQL-specific migrations.
- If you reintroduce database integration tests, use real MySQL or an equivalent MySQL-compatible environment. Do not create tests that run Flyway on H2 when migrations use MySQL-only syntax.
- When changing schema or seed data, update Flyway first, then align entities, repositories, and services.

## Security

- Do not open or relax endpoints in `SecurityConfig` without checking the impact on `/api/auth/**`, `/api/ordini/**`, and `/api/admin/**`.
- Keep the backend stateless: do not introduce server-side sessions accidentally.
- Never commit secrets, tokens, real passwords, or production fallbacks.
- If you change JWT handling, auth cookies, or CORS, always validate both local defaults and `prod` behavior.

## API and Contracts

- Keep payloads stable unless an explicit breaking change is requested.
- If you change DTOs or controller responses, update the related services and error handlers as well.
- For REST application errors, reuse the existing flow in `ApiExceptionHandler` instead of returning ad hoc errors directly from controllers.

## Dependencies

- Do not add new dependencies if the result can be achieved with the existing stack.
- If you modify `pom.xml`, prefer Spring Boot 4 artifacts that are consistent with what the project already uses.
- Avoid leaving temporary test or tooling dependencies in `pom.xml` after debugging or experimentation.

## Verification

- After backend changes, run at least `mvn test` if the suite exists and is relevant to the change.
- If you cannot run a real verification step, state that explicitly in the final result.
- If the change affects schema, security, or configuration, also verify application startup or explain why that was not possible.

## Editing Rules

- Do not introduce broad refactors while fixing a localized bug.
- Do not remove or rewrite historical seed or migration files just to make a local test pass.
- Do not change naming, indentation, or style in unrelated files.
- Add comments only when they clarify something non-obvious; avoid narrative comments.

## When Working Here

- Start from the real files involved, not from assumptions.
- If you find an environment issue, clearly distinguish it from a code issue.
- If a change exists only to support temporary testing, do not leave it in the final tree without a permanent justification.

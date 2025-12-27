# Repository Guidelines

## Project Structure & Module Organization
- Maven project root with wrapper scripts `mvnw`/`mvnw.cmd` and `pom.xml`.
- Expected source layout follows standard Maven conventions:
  - `src/main/java` for application code.
  - `src/main/resources` for configuration and static assets.
  - `src/test/java` for unit/integration tests.
- If you add new modules, register them in `pom.xml` as Maven modules and keep shared code in a common module.

## Build, Test, and Development Commands
- `./mvnw clean package`: compile and package the app into a JAR.
- `./mvnw test`: run the test suite.
- `./mvnw spring-boot:run`: run the Spring Boot app locally.
- `./mvnw -DskipTests package`: build without running tests (use sparingly).

## Coding Style & Naming Conventions
- Java version: 21 (see `pom.xml`).
- Indentation: 4 spaces; braces on the same line (standard Java style).
- Package naming: `com.oneself.<feature>`.
- Class naming: `PascalCase`; methods and variables: `camelCase`.
- Prefer small, focused classes; keep Spring components annotated clearly (`@Service`, `@Controller`, etc.).

## Testing Guidelines
- Tests live under `src/test/java` and mirror the package structure of `src/main/java`.
- Naming: `*Test` for unit tests (e.g., `UserServiceTest`).
- Run locally with `./mvnw test`. Add tests for new features and bug fixes.

## Commit & Pull Request Guidelines
- Git history is not available in this repository, so no specific commit convention can be inferred.
- Use clear, imperative commit messages (e.g., "Add user registration endpoint").
- Pull requests should include a concise description, scope of change, and any relevant screenshots or logs.

## Configuration Tips
- Keep environment-specific settings in `application-*.yml` under `src/main/resources`.
- Avoid committing secrets; use environment variables or local overrides.

# GitHub Actions CI Pipeline Summary

##  What the Pipeline Does

This CI pipeline automates the following steps for a Maven-based Java application using OpenJDK 21:

1. Triggers on `push` and `pull_request` events on the `main` branch.
2. Checks out the source code.
3. Sets up Java 21 using Temurin distribution.
4. Caches Maven dependencies to speed up builds.
5. Builds the project with `mvn clean install`.
6. Runs unit tests with `mvn test`.
7. Analyzes code quality using SonarCloud.
8. Uploads the generated `.jar` file as a build artifact.

---

##  Tools Integrated

- **Maven**: Java build tool used to compile, test, and package the application.
- **SonarCloud**: Code quality and security analysis tool.
- **GitHub Actions**: Continuous integration and automation.

---

##  Secrets and Environment Variables

The following GitHub secrets are used:

- `SONAR_TOKEN`: Token generated from SonarCloud for authentication.
- `SONAR_PROJECT_KEY`: Unique project key in SonarCloud.
- `SONAR_ORGANIZATION`: Organization ID on SonarCloud.

These should be added under **Repository Settings → Secrets and Variables → Actions**.

---

##  Artifact Storage

After the build, the resulting `.jar` file located in `target/` is uploaded using the `actions/upload-artifact` action. It can be downloaded from the workflow run summary under the **Artifacts** section.

---




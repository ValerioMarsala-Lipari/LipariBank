# Lipari Bank

Backend application for the Lipari Bank project.

## Requirements

* Java 21
* Maven

## Tech Stack

* Java 21
* Spring Boot 4.1.1
* Spring MVC
* Lombok
* Maven

## Getting Started

### Build

Build the project using Maven:

```bash
./mvnw clean package
```

On Windows:

```powershell
.\mvnw.cmd clean package
```

### Run

Start the application with:

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Alternatively, after building the project, run the generated JAR:

```bash
java -jar target/bank-0.0.1-SNAPSHOT.jar
```

## Tests

Run the test suite with:

```bash
./mvnw test
```

On Windows:

```powershell
.\mvnw.cmd test
```

## Development

The project includes Spring Boot DevTools for development-time support.

Lombok is configured as an annotation processor for both main and test sources.

## Project Information

| Property    | Value            |
| ----------- | ---------------- |
| Group ID    | `com.lipari`     |
| Artifact ID | `bank`           |
| Version     | `0.0.1-SNAPSHOT` |
| Java        | `21`             |
| Spring Boot | `4.1.1`          |
| Build tool  | Maven            |

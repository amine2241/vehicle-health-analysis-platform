# Vehicle Health Analysis Platform

A microservices-based application built with Java and Spring Boot to analyze vehicle telemetry data and generate health reports.

## Overview

The platform receives vehicle telemetry data, evaluates the vehicle's condition, and generates a health report containing a health score, status, and alerts.

## Architecture

The application consists of two microservices:

- **Telemetry Service**: Receives and stores vehicle telemetry data. Sends data to the Vehicle Analysis Service for analysis.
- **Vehicle Analysis Service**: Analyzes vehicle data, calculates health scores, generates alerts, and stores health reports.

## Technologies

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL
- Docker & Docker Compose
- Maven
- JUnit 5
- Mockito
- MockMvc
- WireMock

## Features

- Vehicle telemetry data ingestion
- Asynchronous communication between microservices
- Vehicle health score calculation
- Tire pressure, battery voltage, engine temperature, and mileage analysis
- Vehicle recall information retrieval
- Health report persistence
- REST API endpoints for health reports and alerts
- Unit and integration testing

## Getting Started

### Prerequisites

- Java
- Maven
- Docker
- Docker Compose

### Run the Application

1. Clone the repository:

```bash
git clone <repository-url>
cd <project-directory>
```

2. Start the databases:

```bash
docker compose up -d
```

3. Start the Telemetry Service:

```bash
cd telemetryservice
./mvnw spring-boot:run
```

4. Start the Vehicle Analysis Service in a separate terminal:

```bash
cd vehicleanalysisservice
./mvnw spring-boot:run
```

Ensure that database configurations and service URLs are correctly configured in the application properties.

## API Endpoints

### Vehicle Analysis Service

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/analysis` | Analyze vehicle data |
| GET | `/api/v1/vehicles/{vin}/health-report` | Retrieve the latest health report |
| GET | `/api/v1/vehicles/{vin}/alerts` | Retrieve vehicle alerts |

## Testing

The project includes:

- **JUnit 5** for writing automated tests
- **Mockito** for mocking dependencies
- **MockMvc** for testing REST controllers
- **WireMock** for mocking external HTTP APIs

Run tests using:

```bash
./mvnw test
```

## Project Purpose

This project was developed to practice backend development, microservices architecture, REST APIs, database integration, asynchronous communication, and automated testing with Spring Boot.

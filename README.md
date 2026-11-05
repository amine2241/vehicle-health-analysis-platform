# Vehicle Health Analysis Platform

This is my solution to the Renault vehicle health use case assignment. It consists of two Spring Boot microservices: one receives telemetry from connected vehicles, while the other calculates health scores and generates alerts.

Both microservices are containerized using Docker, and the entire platform, including its two PostgreSQL databases, is orchestrated with Docker Compose.

## How it works

1. A vehicle sends its data to `POST /api/v1/telemetry`. Since I don't have a real vehicle, I simulate requests using curl and automated tests.
2. The telemetry service saves the data and immediately responds with `202 Accepted`. Analysis happens asynchronously, so the vehicle does not wait for the result.
3. The Vehicle Analysis Service integrates with an external API to retrieve vehicle recall campaigns. Since a real recall provider is not configured, the application currently falls back to an empty recall list.
4. The health report and alerts can then be retrieved through the API.

## Technical choices

- **Two microservices, two databases.** Each service has its own PostgreSQL database, avoiding shared tables and allowing independent development.
- **Asynchronous communication.** The telemetry service communicates with the analysis service using Spring's `RestClient` and `@Async`.
- **Rule-based scoring.** Each scoring rule is implemented independently and returns a penalty and, when applicable, an alert. The calculator starts at 100, applies penalties, and keeps the final score between 0 and 100.
- **External recall API.** WireMock simulates the external API in tests, avoiding dependencies on external services.
- **Docker containerization.** Each Spring Boot microservice has its own Dockerfile and Docker image. Docker Compose orchestrates both services and their PostgreSQL databases.

## Scoring rules

The assignment doesn't specify thresholds, so I defined the following assumptions. They are not official Renault thresholds.

| Rule | Condition | Penalty |
|---|---|---|
| Mileage | Over 100,000 / 150,000 / 200,000 km | -5 / -10 / -15 |
| Tire pressure (lowest tire) | Under 1.8 / under 1.5 bar | -8 / -15 |
| Battery | Under 12.0 / under 11.5 V | -10 / -20 |
| Engine temperature | Over 100 / over 110 °C | -10 / -20 |
| Fuel | Under 10% / under 5% | -2 / -5 |
| Error codes | 5 points per code, maximum 25 | Up to -25 |
| Recall campaigns | 10 points per campaign, maximum 30 | Up to -30 |

The final score determines the vehicle status:

- `HEALTHY`: 80 and above
- `ATTENTION`: 50–79
- `CRITICAL`: below 50

## Running the project

### Prerequisites

- Java 21 or the Java version configured in the project
- Docker Desktop (including Docker Compose)

### 1. Clone the repository

```bash
git clone <repository-url>
cd <project-directory>
```

### 2. Build the Spring Boot JARs

On Windows PowerShell:

```powershell
cd telemetry-service
.\mvnw.cmd clean package -DskipTests

cd ../vehicle-analysis-service
.\mvnw.cmd clean package -DskipTests

cd ..
```

On macOS/Linux, use `./mvnw` instead of `.\mvnw.cmd`.

### 3. Build the Docker images

From the project root:

```bash
docker build -t telemetry-service:1.0 ./telemetry-service
docker build -t vehicle-analysis-service:1.0 ./vehicle-analysis-service
```

### 4. Start the entire platform

```bash
docker compose up -d
```

Docker Compose starts four containers:

- `telemetry-service` — port 8081
- `vehicle-analysis-service` — port 8082
- `telemetry-db` — PostgreSQL, port 5433 on the host
- `analysis-db` — PostgreSQL, port 5434 on the host

Check container status:

```bash
docker compose ps
```

View application logs:

```bash
docker compose logs -f
```

Stop the platform:

```bash
docker compose down
```

The PostgreSQL data is persisted in Docker volumes, so stopping the containers does not delete the database contents. To also delete the stored database data, use `docker compose down -v`.

The database credentials are hardcoded in the Compose configuration for development convenience. In a production environment, I would use environment variables or a secrets manager.

## Trying it

I developed this on Windows, so the following examples use PowerShell commands (`curl.exe --%`). On macOS/Linux, use plain `curl` and single quotes around JSON.

Send simulated vehicle telemetry to the telemetry service. You should receive `202 Accepted`:

```powershell
curl.exe --% -i -X POST http://localhost:8081/api/v1/telemetry -H "Content-Type: application/json" -d "{\"vin\":\"VF123456789\",\"mileageKm\":120000,\"frontLeftTirePressure\":1.6,\"frontRightTirePressure\":2.2,\"rearLeftTirePressure\":2.1,\"rearRightTirePressure\":2.2,\"batteryVoltage\":11.8,\"engineTempCelsius\":105,\"fuelLevelPercent\":8,\"errorCodes\":[\"P0301\"]}"
```

After asynchronous processing, retrieve the health report and alerts:

```powershell
curl.exe --% -i http://localhost:8082/api/v1/vehicles/VF123456789/health-report

curl.exe --% -i http://localhost:8082/api/v1/vehicles/VF123456789/alerts
```

You can also call the analysis service directly, without going through the telemetry service. It expects tire pressures in a nested object:

```powershell
curl.exe --% -i -X POST http://localhost:8082/api/v1/analysis -H "Content-Type: application/json" -d "{\"vin\":\"VF987654321\",\"mileageKm\":120000,\"tirePressures\":{\"frontLeft\":1.6,\"frontRight\":2.2,\"rearLeft\":2.1,\"rearRight\":2.2},\"batteryVoltage\":11.8,\"engineTempCelsius\":105,\"fuelLevelPercent\":8,\"errorCodes\":[\"P0301\"]}"
```

For this request, retrieve the report using VIN `VF987654321`.

## API

| Service | Method | Endpoint | Description |
|---|---|---|---|
| Telemetry | POST | `/api/v1/telemetry` | Receives vehicle data and returns 202 |
| Analysis | POST | `/api/v1/analysis` | Analyzes vehicle data |
| Analysis | GET | `/api/v1/vehicles/{vin}/health-report` | Retrieves the latest health report |
| Analysis | GET | `/api/v1/vehicles/{vin}/alerts` | Retrieves alerts from the latest report |

## Tests

Run the tests separately in each microservice directory:

```bash
./mvnw test
```

On Windows PowerShell:

```powershell
.\mvnw.cmd test
```

- **Unit tests:** Cover scoring rules and the calculator, as well as analysis service logic using Mockito.
- **Integration tests:** Use MockMvc to verify HTTP endpoints, valid payloads, persistence interactions, and invalid JSON responses.
- **WireMock:** Simulates external API responses and HTTP communication in tests.

## What I would improve with more time

- Replace asynchronous REST communication with a message broker such as Kafka and introduce retry mechanisms.
- Add health checks to Docker Compose to improve service startup reliability.
- Add more validation tests for invalid values (negative mileage, fuel above 100%, invalid VIN format).
- Improve logging and monitoring for inter-service communication.
- Add comments for readability.
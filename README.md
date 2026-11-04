# Vehicle Health Analysis Platform

This is my solution to the Renault vehicle health use case assignment. It's two Spring Boot micro services: one receives telemetry from connected cars, the other works out a health score and alerts for each vehicle.

## How it works

1. A vehicle sends its data to `POST /api/v1/telemetry`. I don't have a real car, so I simulate it with curl and with tests (example below).
2. The telemetry service saves the data and answers `202 Accepted` right away. The analysis happens in the background, so the car never waits for it.
3. The vehicle analysis service asks an external API for the vehicle's recall campaigns (it returns an empty list because I don't have an external API), calculates the health score, and saves the report.
4. Anyone can then read the report and the alerts through the API.

## Choices I made

- **Two micro services, two databases.** Each service has its own PostgreSQL database, so they don't share tables and can change independently.
- **Simple async call between services.** The telemetry service calls the analysis service with Spring's `RestClient` and `@Async`. It was the simplest option that fits the two days I had.
- **One class per scoring rule.** Each rule only looks at the vehicle data and returns a penalty and, if needed, an alert. The calculator starts at 100, adds up the penalties, and keeps the result between 0 and 100. This makes every rule easy to test on its own, and adding a rule doesn't touch the others.
- **Recall data comes from an external API.** In the tests I replace it with WireMock, so the tests don't depend on anything outside the project.

## Scoring rules

The assignment doesn't give any thresholds, so I picked them myself. They're my assumptions, not Renault's.

| Rule | Condition | Penalty |
|---|---|---|
| Mileage | over 100,000 / 150,000 / 200,000 km | -5 / -10 / -15 |
| Tire pressure (lowest tire) | under 1.8 / under 1.5 bar | -8 / -15 |
| Battery | under 12.0 / under 11.5 V | -10 / -20 |
| Engine temperature | over 100 / over 110 °C | -10 / -20 |
| Fuel | under 10% / under 5% | -2 / -5 |
| Error codes | 5 points per code, max 25 | up to -25 |
| Recall campaigns | 10 points per campaign, max 30 | up to -30 |

The final score gives a status: 80 and above is `HEALTHY`, 50 to 79 is `ATTENTION`, below 50 is `CRITICAL`.

## Running it

You need Java, Maven and Docker.

1. Clone the repo:

```bash
git clone <repository-url>
cd <project-directory>
```

2. Start the databases:

```bash
docker compose up -d
```

3. Start the telemetry service:

```bash
cd telemetryservice
./mvnw spring-boot:run
```

4. In another terminal, start the vehicle analysis service:

```bash
cd vehicleanalysisservice
./mvnw spring-boot:run
```

The database settings and the URL the telemetry service uses to reach the analysis service are in each service's `application.properties`. I hardcoded sensible like database passwords values for simplicity but in aproduction environment I would put them in a .env file.

## Trying it

I developed this on Windows, so these are PowerShell commands (`curl.exe --%`). On macOS or Linux, use plain `curl` and single quotes around the JSON.

Send a fake vehicle's data to the telemetry service. You should get a `202`:

```powershell
curl.exe --% -i -X POST http://localhost:8081/api/v1/telemetry -H "Content-Type: application/json" -d "{\"vin\":\"VF123456789\",\"mileageKm\":120000,\"frontLeftTirePressure\":1.6,\"frontRightTirePressure\":2.2,\"rearLeftTirePressure\":2.1,\"rearRightTirePressure\":2.2,\"batteryVoltage\":11.8,\"engineTempCelsius\":105,\"fuelLevelPercent\":8,\"errorCodes\":[\"P0301\"]}"
```

A moment later, read the results for that vehicle:

```powershell
curl.exe --% -i http://localhost:8082/api/v1/vehicles/VF123456789/health-report
curl.exe --% -i http://localhost:8082/api/v1/vehicles/VF123456789/alerts
```

You can also call the analysis service directly, without going through the telemetry service. Note that it expects tire pressures in a nested object:

```powershell
curl.exe --% -i -X POST http://localhost:8082/api/v1/analysis -H "Content-Type: application/json" -d "{\"vin\":\"VF987654321\",\"mileageKm\":120000,\"tirePressures\":{\"frontLeft\":1.6,\"frontRight\":2.2,\"rearLeft\":2.1,\"rearRight\":2.2},\"batteryVoltage\":11.8,\"engineTempCelsius\":105,\"fuelLevelPercent\":8,\"errorCodes\":[\"P0301\"]}"
```

For that last one, read the report with the VIN you sent (`VF987654321`).

## API

| Service | Method | Endpoint | What it does |
|---|---|---|---|
| Telemetry | POST | `/api/v1/telemetry` | Receives vehicle data, returns 202 |
| Analysis | POST | `/api/v1/analysis` | Analyzes vehicle data (called by the telemetry service) |
| Analysis | GET | `/api/v1/vehicles/{vin}/health-report` | Latest health report |
| Analysis | GET | `/api/v1/vehicles/{vin}/alerts` | Alerts from the latest report |

## Tests

Run everything with:

```bash
./mvnw test
```

- **Unit tests** cover the scoring rules and the calculator, and the analysis service with Mockito (recall client and repository are mocked).
- **Integration tests** use MockMvc to check that a valid payload returns 202 and is saved, and that broken JSON returns 400.
- **WireMock** plays the external API, in the tests for the recall client and for the telemetry service's call to the analysis service.

## What I would do with more time

- Replace the async REST call with a message broker like kafka, and add retries.
- Put both services in Docker container, so one command starts the whole platform.
- Add more validation tests for bad values (negative mileage, fuel over 100%, wrong VIN format).
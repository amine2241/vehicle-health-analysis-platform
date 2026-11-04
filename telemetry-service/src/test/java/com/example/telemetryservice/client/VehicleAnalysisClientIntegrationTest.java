package com.example.telemetryservice.client;

import com.example.telemetryservice.entity.Telemetry;
import com.example.telemetryservice.mapper.VehicleDataMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class VehicleAnalysisClientIntegrationTest {
    private WireMockServer wireMockServer;
    private VehicleAnalysisClient vehicleAnalysisClient;

    @BeforeEach
    void setUp() {

        wireMockServer = new WireMockServer(0);
        wireMockServer.start();

        vehicleAnalysisClient = new VehicleAnalysisClient(
                wireMockServer.baseUrl(),
                new VehicleDataMapper()
        );
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void shouldSendMappedTelemetryToAnalysisService() {

        wireMockServer.stubFor(post(urlEqualTo("/api/v1/analysis"))
                .willReturn(aResponse().withStatus(200)));

        Telemetry telemetry = new Telemetry();

        telemetry.setVin("VF123456789");
        telemetry.setMileageKm(120000);

        telemetry.setFrontLeftTirePressure(1.6);
        telemetry.setFrontRightTirePressure(2.2);
        telemetry.setRearLeftTirePressure(2.1);
        telemetry.setRearRightTirePressure(2.2);

        telemetry.setBatteryVoltage(11.8);
        telemetry.setEngineTempCelsius(105);
        telemetry.setFuelLevelPercent(8);
        telemetry.setErrorCodes(List.of("P0301"));

        vehicleAnalysisClient.sendForAnalysis(telemetry);

        wireMockServer.verify(
                postRequestedFor(urlEqualTo("/api/v1/analysis"))
                        .withRequestBody(equalToJson("""
                            {
                              "vin": "VF123456789",
                              "mileageKm": 120000,
                              "tirePressures": {
                                "frontLeft": 1.6,
                                "frontRight": 2.2,
                                "rearLeft": 2.1,
                                "rearRight": 2.2
                              },
                              "batteryVoltage": 11.8,
                              "engineTempCelsius": 105,
                              "fuelLevelPercent": 8,
                              "errorCodes": ["P0301"]
                            }
                            """))
        );
    }
    @Test
    void shouldThrowExceptionWhenAnalysisServiceReturnsServerError() {

        wireMockServer.stubFor(post(urlEqualTo("/api/v1/analysis"))
                .willReturn(aResponse().withStatus(500)));

        Telemetry telemetry = new Telemetry();
        telemetry.setVin("VF123456789");

        org.junit.jupiter.api.Assertions.assertThrows(
                org.springframework.web.client.RestClientResponseException.class,
                () -> vehicleAnalysisClient.sendForAnalysis(telemetry)
        );}
}

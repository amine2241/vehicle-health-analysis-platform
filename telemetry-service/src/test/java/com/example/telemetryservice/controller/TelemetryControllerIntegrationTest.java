package com.example.telemetryservice.controller;

import com.example.telemetryservice.client.VehicleAnalysisClient;
import com.example.telemetryservice.entity.Telemetry;
import com.example.telemetryservice.repository.TelemetryRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TelemetryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TelemetryRepository telemetryRepository;

    @MockitoBean
    private VehicleAnalysisClient vehicleAnalysisClient;

    @Test
    void shouldAcceptAndPersistTelemetry() throws Exception {

        String json = """
                {
                  "vin": "VF123456789",
                  "mileageKm": 120000,
                  "frontLeftTirePressure": 1.6,
                  "frontRightTirePressure": 2.2,
                  "rearLeftTirePressure": 2.1,
                  "rearRightTirePressure": 2.2,
                  "batteryVoltage": 11.8,
                  "engineTempCelsius": 105,
                  "fuelLevelPercent": 8,
                  "errorCodes": ["P0301"]
                }
                """;

        mockMvc.perform(post("/api/v1/telemetry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isAccepted());

        assertEquals(1, telemetryRepository.count());

        verify(vehicleAnalysisClient).sendForAnalysis(any(Telemetry.class));
    }

    @Test
    void shouldReturnBadRequestForMalformedJson() throws Exception {

        mockMvc.perform(post("/api/v1/telemetry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "vin":
                                }
                                """))
                .andExpect(status().isBadRequest());

        assertEquals(0, telemetryRepository.count());
    }
}
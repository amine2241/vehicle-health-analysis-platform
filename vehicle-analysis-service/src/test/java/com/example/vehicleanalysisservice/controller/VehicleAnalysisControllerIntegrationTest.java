package com.example.vehicleanalysisservice.controller;

import com.example.vehicleanalysisservice.client.RecallApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class VehicleAnalysisControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecallApiClient recallApiClient;

    @BeforeEach
    void setUp() {
        when(recallApiClient.getRecalls(anyString()))
                .thenReturn(List.of());
    }

    private final String vehicleJson = """
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
              "errorCodes": ["P0301"],
              "recalls": []
            }
            """;

    private void submitVehicleData() throws Exception {
        mockMvc.perform(post("/api/v1/analysis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(vehicleJson))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAnalyzeVehicleAndReturnHealthReport() throws Exception {

        mockMvc.perform(post("/api/v1/analysis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(vehicleJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vin").value("VF123456789"))
                .andExpect(jsonPath("$.score").value(60))
                .andExpect(jsonPath("$.status").value("ATTENTION"))
                .andExpect(jsonPath("$.alerts").isArray());
    }

    @Test
    void shouldReturnLatestHealthReport() throws Exception {

        submitVehicleData();

        mockMvc.perform(get("/api/v1/vehicles/{vin}/health-report",
                        "VF123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vin").value("VF123456789"))
                .andExpect(jsonPath("$.score").value(60))
                .andExpect(jsonPath("$.status").value("ATTENTION"));
    }

    @Test
    void shouldReturnVehicleAlerts() throws Exception {

        submitVehicleData();

        mockMvc.perform(get("/api/v1/vehicles/{vin}/alerts",
                        "VF123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").value("Elevated mileage"));
    }
}
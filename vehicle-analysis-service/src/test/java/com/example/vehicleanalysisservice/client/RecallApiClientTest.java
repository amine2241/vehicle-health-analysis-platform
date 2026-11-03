package com.example.vehicleanalysisservice.client;

import com.example.vehicleanalysisservice.entity.Recall;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RecallApiClientTest {

    private static WireMockServer wireMockServer;

    private RecallApiClient recallApiClient;

    @BeforeAll
    static void startWireMock() {
        wireMockServer = new WireMockServer(options().dynamicPort());
        wireMockServer.start();
    }

    @AfterAll
    static void stopWireMock() {
        wireMockServer.stop();
    }

    @BeforeEach
    void setUp() {
        wireMockServer.resetAll();

        recallApiClient = new RecallApiClient(
                wireMockServer.baseUrl()
        );
    }
    @Test
    void shouldReturnRecallsFromExternalApi() {

        wireMockServer.stubFor(
                get(urlEqualTo("/api/recalls/VF1TEST1234567890"))
                        .willReturn(okJson("""
                                [
                                  {
                                    "campaignId": "R001",
                                    "description": "Engine issue",
                                    "active": true
                                  },
                                  {
                                    "campaignId": "R002",
                                    "description": "Brake issue",
                                    "active": true
                                  }
                                ]
                                """))
        );

        List<Recall> recalls =
                recallApiClient.getRecalls("VF1TEST1234567890");

        assertEquals(2, recalls.size());
        assertEquals("R001", recalls.get(0).campaignId());
        assertEquals("Engine issue", recalls.get(0).description());
        assertTrue(recalls.get(0).active());

        assertEquals("R002", recalls.get(1).campaignId());
        assertTrue(recalls.get(1).active());

        wireMockServer.verify(
                1,
                getRequestedFor(
                        urlEqualTo("/api/recalls/VF1TEST1234567890")
                )
        );
    }
}

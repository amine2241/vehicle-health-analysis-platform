package com.example.vehicleanalysisservice.client;

import com.example.vehicleanalysisservice.entity.Recall;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;
@Component
public class RecallApiClient {
    private final RestClient restClient;

    public RecallApiClient(
            @Value("${recall.api.base-url}") String baseUrl
    ) {
        this.restClient = RestClient.create(baseUrl);
    }

    public List<Recall> getRecalls(String vin) {
        try {
            List<Recall> recalls = restClient.get()
                    .uri("/api/recalls/{vin}", vin)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<Recall>>() {});

            return recalls == null ? List.of() : recalls;

        } catch (RestClientException e) {
            System.err.println("Recall API unavailable. Continuing without recalls.");
            return List.of();
        }
    }
}

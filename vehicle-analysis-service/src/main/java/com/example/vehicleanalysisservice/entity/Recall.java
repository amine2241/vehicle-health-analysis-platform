package com.example.vehicleanalysisservice.entity;

public record Recall(
        String campaignId,
        String description,
        boolean active
) {
}
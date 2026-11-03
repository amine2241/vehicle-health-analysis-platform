package com.example.vehicleanalysisservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "health_reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HealthReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vin;

    private int score;

    @Enumerated(EnumType.STRING)
    private HealthStatus status;
    private List<String> alerts = new ArrayList<>();
}


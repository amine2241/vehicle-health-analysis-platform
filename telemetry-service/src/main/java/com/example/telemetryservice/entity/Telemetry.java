package com.example.telemetryservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "telemetry")
public class Telemetry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String vin;

    @NotNull
    @PositiveOrZero
    private Integer mileageKm;

    @NotNull
    @Positive
    private Double frontLeftTirePressure;

    @NotNull
    @Positive
    private Double frontRightTirePressure;

    @NotNull
    @Positive
    private Double rearLeftTirePressure;

    @NotNull
    @Positive
    private Double rearRightTirePressure;

    @NotNull
    @Positive
    private Double batteryVoltage;

    @NotNull
    @PositiveOrZero
    private Integer engineTempCelsius;

    @NotNull
    @Min(0)
    @Max(100)
    private Integer fuelLevelPercent;

    @ElementCollection
    private List<String> errorCodes = new ArrayList<>();
}
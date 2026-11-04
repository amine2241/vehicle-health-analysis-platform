package com.example.telemetryservice.entity;

import jakarta.persistence.*;
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

    private String vin;

    private int mileageKm;

    private double frontLeftTirePressure;
    private double frontRightTirePressure;
    private double rearLeftTirePressure;
    private double rearRightTirePressure;

    private double batteryVoltage;

    private int engineTempCelsius;

    private int fuelLevelPercent;
    @ElementCollection
    private List<String> errorCodes = new ArrayList<>();
}

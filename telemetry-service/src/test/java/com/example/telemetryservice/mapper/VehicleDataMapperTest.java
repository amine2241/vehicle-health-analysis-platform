package com.example.telemetryservice.mapper;

import com.example.telemetryservice.dto.VehicleDataRequest;
import com.example.telemetryservice.entity.Telemetry;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VehicleDataMapperTest {
    private final VehicleDataMapper mapper = new VehicleDataMapper();

    @Test
    void shouldMapTelemetryToVehicleDataRequest() {

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
        telemetry.setErrorCodes(List.of("P0301", "P0302"));

        VehicleDataRequest result = mapper.toRequest(telemetry);

        assertEquals("VF123456789", result.vin());
        assertEquals(120000, result.mileageKm());

        assertEquals(1.6, result.tirePressures().frontLeft());
        assertEquals(2.2, result.tirePressures().frontRight());
        assertEquals(2.1, result.tirePressures().rearLeft());
        assertEquals(2.2, result.tirePressures().rearRight());

        assertEquals(11.8, result.batteryVoltage());
        assertEquals(105, result.engineTempCelsius());
        assertEquals(8, result.fuelLevelPercent());

        assertEquals(List.of("P0301", "P0302"), result.errorCodes());
    }
}

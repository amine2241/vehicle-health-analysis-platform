package com.example.telemetryservice.mapper;

import com.example.telemetryservice.dto.VehicleDataRequest;
import com.example.telemetryservice.entity.Telemetry;
import org.springframework.stereotype.Component;

@Component
public class VehicleDataMapper {

    public VehicleDataRequest toRequest(Telemetry telemetry) {

        VehicleDataRequest.TirePressures tirePressures =
                new VehicleDataRequest.TirePressures(
                        telemetry.getFrontLeftTirePressure(),
                        telemetry.getFrontRightTirePressure(),
                        telemetry.getRearLeftTirePressure(),
                        telemetry.getRearRightTirePressure()
                );

        return new VehicleDataRequest(
                telemetry.getVin(),
                telemetry.getMileageKm(),
                tirePressures,
                telemetry.getBatteryVoltage(),
                telemetry.getEngineTempCelsius(),
                telemetry.getFuelLevelPercent(),
                telemetry.getErrorCodes()
        );
    }
}
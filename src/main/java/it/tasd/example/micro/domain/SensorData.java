package it.tasd.example.micro.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class SensorData {
    public SensorData(String sensorUUID, BigDecimal value, ZonedDateTime zdt) {
        this.sensorUUID = sensorUUID;
        this.value = value;
        this.zdt = zdt;
    }

    public SensorData(Sensor sensor, BigDecimal defaultValue){
        this.sensorUUID = sensor.getUuid();
        this.value = defaultValue;
        this.zdt = ZonedDateTime.now();
    }
    private String sensorUUID;

    public BigDecimal getValue() {
        return value;
    }

    private BigDecimal value;

    public String getSensorUUID() {
        return sensorUUID;
    }


    public ZonedDateTime getZdt() {
        return zdt;
    }

    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm:ss.SSS")
    private ZonedDateTime zdt;
}

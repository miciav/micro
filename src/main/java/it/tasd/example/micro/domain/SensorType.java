package it.tasd.example.micro.domain;

import java.math.BigDecimal;

public enum SensorType {
    TEMPERATURE,
    PRESSION,
    HUMIDITY;

    BigDecimal getDefaultValue(SensorType sensorType){

        switch (sensorType){
            case TEMPERATURE:
                return BigDecimal.valueOf(20.0);
            case HUMIDITY:
                return  BigDecimal.valueOf(30.0);
            case PRESSION:
                return BigDecimal.valueOf(100);
            default:
                BigDecimal.valueOf(10);
        }

        return null;
    }

}

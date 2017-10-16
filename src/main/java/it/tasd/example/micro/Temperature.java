package it.tasd.example.micro;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;



public class Temperature {
    private String sensorId;


    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    private BigDecimal value;

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }



    public ZonedDateTime getZdt() {
        return zdt;
    }

    public void setZdt(ZonedDateTime zdt) {
        this.zdt = zdt;
    }
    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm:ss.SSS")
    private ZonedDateTime zdt;
}

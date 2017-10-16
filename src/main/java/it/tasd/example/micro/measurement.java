package it.tasd.example.micro;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("measure")
public class measurement {

    private Sensor sensor;

    public measurement(Sensor sensor) {
        this.sensor = sensor;
    }

    @GetMapping("/temperature")
    Temperature getTemperature(){
        return sensor.getNextValue();
    }
}

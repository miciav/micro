package it.tasd.example.micro;

import it.tasd.example.micro.domain.Sensor;
import it.tasd.example.micro.domain.SensorReading;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class SensorController {

    private SensorService sensorService;

    private SensorRepository sensorRepository;

    public SensorController(SensorService sensorService, SensorRepository sensorRepository) {
        this.sensorService = sensorService;
        this.sensorRepository = sensorRepository;
    }

    @GetMapping("/sensors")
    Collection<Sensor> getAllSensors(){
        return sensorRepository.findAll();
    }

    @GetMapping("/sensors/{sensorId}")
    ResponseEntity<Sensor> getSensor(@PathVariable("sensorId") Integer sensorid){
        return sensorRepository.findById(sensorid).map(ResponseEntity::ok).orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/temperature")
    SensorReading getTemperature(){
        return sensorService.getNextValue();
    }
}

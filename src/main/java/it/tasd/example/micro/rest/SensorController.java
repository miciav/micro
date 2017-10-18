package it.tasd.example.micro.rest;

import it.tasd.example.micro.domain.Sensor;
import it.tasd.example.micro.domain.SensorData;
import it.tasd.example.micro.service.SensorService;
import it.tasd.example.micro.service.SensorValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping(value = "/api")
public class SensorController {

    @Autowired
    private SensorService sensorService;

    @Autowired
    private SensorValueService sensorValueService;


    @GetMapping("/sensors")
    Collection<Sensor> getAllSensors() {
        return sensorService.getAllSensors();
    }

    @GetMapping("/sensors/{sensorId}")
    ResponseEntity<Sensor> getSensor(@PathVariable("sensorId") Integer sensorid) {
        return sensorService
                .getSensor(sensorid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/sensors/{sensorId}/value")
    ResponseEntity<SensorData> getSensorReading(@PathVariable("sensorId") Integer sensorid) {
      return sensorService.getSensor(sensorid)
        .map(s->sensorValueService.getNextValue(s.getUuid()))
        .map(ResponseEntity::ok).orElse(ResponseEntity.noContent().build());

    }

}

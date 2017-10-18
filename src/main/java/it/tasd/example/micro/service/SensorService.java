package it.tasd.example.micro.service;

import it.tasd.example.micro.domain.Sensor;
import it.tasd.example.micro.repository.SensorRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class SensorService {
    private SensorRepository sensorRepository;

    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    public Collection<Sensor> getAllSensors(){
        return sensorRepository.findAll();
    }
    public Optional<Sensor> getSensor(Integer SensorId){
        return sensorRepository.findById(SensorId);
    }


}

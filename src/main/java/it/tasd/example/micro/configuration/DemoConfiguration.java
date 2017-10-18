package it.tasd.example.micro.configuration;

import it.tasd.example.micro.domain.Sensor;
import it.tasd.example.micro.domain.SensorType;
import it.tasd.example.micro.repository.SensorRepository;
import it.tasd.example.micro.service.SensorValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.UUID;
import java.util.stream.Stream;

@Configuration
@Profile("Demo")
public class DemoConfiguration {
    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private SensorValueService sensorValueService;

    @Bean
    public CommandLineRunner sensors() {
    return     args->{
            Sensor sensor1 = new Sensor();
            sensor1.setId(1);
            sensor1.setUuid(UUID.randomUUID().toString());
            sensor1.setDescription("Temperature sensor");
            sensor1.setType(SensorType.TEMPERATURE);
            Sensor sensor2 = new Sensor();
            sensor2.setId(2);
            sensor2.setUuid(UUID.randomUUID().toString());
            sensor2.setType(SensorType.PRESSION);
            sensor2.setDescription("Pression sensor");
            Stream.of(sensor1, sensor2)
                    .peek(sensorValueService::saveSensor)
                    .forEach(sensorRepository::saveAndFlush);





        };
    }

}

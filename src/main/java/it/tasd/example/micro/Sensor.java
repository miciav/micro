package it.tasd.example.micro;


import org.springframework.stereotype.Service;

import javax.print.event.PrintEvent;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class Sensor {
    private final String sensorID = UUID.randomUUID().toString();
    private final Random random = new Random();
    private final BigDecimal startingTemp = BigDecimal.valueOf(20.0).setScale(2, BigDecimal.ROUND_HALF_DOWN);
    private Temperature previousTemp;
    public Sensor() {
        previousTemp = new Temperature();
        previousTemp.setSensorId(UUID.randomUUID().toString());
        previousTemp.setValue(startingTemp);
        previousTemp.setZdt(ZonedDateTime.now());
    }

    Temperature getNextValue(){

        Temperature t = new Temperature();
        t.setSensorId(previousTemp.getSensorId());
        BigDecimal bd;
        if (previousTemp.getValue().compareTo(BigDecimal.valueOf(21.5)) < 0) {
            bd = previousTemp.getValue().add(BigDecimal.valueOf(random.nextDouble())).setScale(2, BigDecimal.ROUND_HALF_DOWN);
        }
        else bd = previousTemp.getValue().subtract(BigDecimal.valueOf(random.nextDouble())).setScale(2, BigDecimal.ROUND_HALF_DOWN);
        t.setValue(bd);
        t.setZdt(ZonedDateTime.now());
        previousTemp = t;

        return t;
    }

}

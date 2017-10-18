package it.tasd.example.micro.service;


import it.tasd.example.micro.domain.Sensor;
import it.tasd.example.micro.domain.SensorData;
import it.tasd.example.micro.domain.SensorType;
import it.tasd.example.micro.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import static it.tasd.example.micro.domain.SensorType.TEMPERATURE;

@Service
public class SensorValueService {

    private String sensorID;
    private final Random random = new Random();
    private final BigDecimal startingTemp = BigDecimal.valueOf(20.0).setScale(2, BigDecimal.ROUND_HALF_DOWN);

    private Map<String, List<SensorData>> previousDataMap;

    public SensorValueService() {
        previousDataMap = new LinkedHashMap<>();
    }

    public void saveSensor(Sensor sensor) {
        previousDataMap.put(sensor.getUuid(), createFirstSensorData(sensor));
    }

    private static List<SensorData> createFirstSensorData(Sensor sensor) {
        switch (sensor.getType()) {
            case TEMPERATURE:
                return Stream.of(new SensorData(sensor, BigDecimal.valueOf(20.0))).collect(Collectors.toList());
            case PRESSION:
                return Stream.of(new SensorData(sensor, BigDecimal.valueOf(100))).collect(Collectors.toList());
            case HUMIDITY:
                return Stream.of(new SensorData(sensor, BigDecimal.valueOf(30))).collect(Collectors.toList());
            default:
                return new ArrayList<SensorData>();
        }
    }

    public SensorData getNextValue(String UUID) {
        List<SensorData> sensorDataList = previousDataMap.get(UUID);
        int posMediano = (int) (Math.ceil(((double) sensorDataList.size()) / 2.0));

        Supplier<Stream<Double>> orderedSensorDataStreamSupplier = () ->sensorDataList.stream().map(s -> s.getValue().doubleValue()).sorted();
        //Double median1 = orderedSensorDataStreamSupplier.get().limit(posMediano).max((o1, o2) -> o1.compareTo(o2)).orElse(0.0);

        Double baseValue = sensorDataList.get(0).getValue().doubleValue();

        Map<Boolean, List<Double>> ordered = orderedSensorDataStreamSupplier.get().collect(Collectors.partitioningBy(o -> o > baseValue));

        SensorData lastSensorData = sensorDataList.get(sensorDataList.size() - 1);
        BigDecimal newValue;
        int sizeTrue = ordered.get(true).size();
        int sizeFalse = ordered.get(false).size();
        int diffSize = sizeTrue-sizeFalse;
        int absDiffSize = Math.abs(sizeTrue-sizeFalse);
        if ((absDiffSize < 10 && diffSize >0) || absDiffSize>10 && diffSize <0 )
            newValue = lastSensorData.getValue().add(BigDecimal.valueOf(random.nextDouble())).setScale(2, RoundingMode.HALF_EVEN);
        else
            newValue = lastSensorData.getValue().subtract(BigDecimal.valueOf(random.nextDouble())).setScale(2, RoundingMode.HALF_EVEN);

        SensorData newSenorData = new SensorData(UUID, newValue, ZonedDateTime.now());
        sensorDataList.add(newSenorData);
        return newSenorData;
    }

}

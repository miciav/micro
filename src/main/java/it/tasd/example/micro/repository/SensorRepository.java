package it.tasd.example.micro.repository;

import it.tasd.example.micro.domain.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Integer> {

    Collection<Sensor> findByUuid(String Uuid);
}

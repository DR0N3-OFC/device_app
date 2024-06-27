package br.edu.utfpr.deviceapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.deviceapi.model.Sensor;

public interface SensorRepository extends JpaRepository<Sensor, Long>{

}

package br.edu.utfpr.deviceapi.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.utfpr.deviceapi.dto.SensorDTO;
import br.edu.utfpr.deviceapi.exception.NotFoundException;
import br.edu.utfpr.deviceapi.model.Sensor;
import br.edu.utfpr.deviceapi.repository.SensorRepository;



@Service
public class SensorService {
    @Autowired
    private SensorRepository sensorRepository;


    /**
     * Inserir uma sensor no DB.
     * @return
     */
    public Sensor create(SensorDTO dto){
        var sensor = new Sensor();
        sensor.setCreated_at(LocalDateTime.now());
        sensor.setUpdated_at(LocalDateTime.now());

        BeanUtils.copyProperties(dto, sensor);

        // Persistir no Banco de dados
        return sensorRepository.save(sensor);
    }


    /**
     * Buscar no banco de dados todas.
     * @return
     */
    public List<Sensor> getAll() {
        return sensorRepository.findAll();
    }

    /**
     * Buscar um sensor pelo ID.
     * @param id
     * @return
     */
    public Optional<Sensor> getById(long id) {
        return sensorRepository.findById(id);
    }

    public Sensor update(long id, SensorDTO dto) throws NotFoundException {
        var res = sensorRepository.findById(id);

        if(res.isEmpty()) {
            throw new NotFoundException("Sensor " + id + " não existe.");
        }

        var sensor = res.get();
        sensor.setNome(dto.nome());
        sensor.setTipo(dto.tipo());
        sensor.setDispositivo(dto.dispositivo());
        sensor.setUpdated_at(LocalDateTime.now());

        return sensorRepository.save(sensor);
    }

    public void delete(long id) throws NotFoundException {
        var res = sensorRepository.findById(id);

        if(res.isEmpty()) {
            throw new NotFoundException("Sensor " + id + " não existe.");
        }

        sensorRepository.delete(res.get());
    }
}

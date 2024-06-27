package br.edu.utfpr.deviceapi.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.utfpr.deviceapi.dto.AtuadorDTO;
import br.edu.utfpr.deviceapi.exception.NotFoundException;
import br.edu.utfpr.deviceapi.model.Atuador;
import br.edu.utfpr.deviceapi.repository.AtuadorRepository;



@Service
public class AtuadorService {
    @Autowired
    private AtuadorRepository atuadorRepository;


    /**
     * Inserir uma atuador no DB.
     * @return
     */
    public Atuador create(AtuadorDTO dto){
        var atuador = new Atuador();
        BeanUtils.copyProperties(dto, atuador);

        atuador.setCreated_at(LocalDateTime.now());
        atuador.setUpdated_at(LocalDateTime.now());
        // Persistir no Banco de dados
        return atuadorRepository.save(atuador);
    }


    /**
     * Buscar no banco de dados todas.
     * @return
     */
    public List<Atuador> getAll() {
        return atuadorRepository.findAll();
    }

    /**
     * Buscar um atuador pelo ID.
     * @param id
     * @return
     */
    public Optional<Atuador> getById(long id) {
        return atuadorRepository.findById(id);
    }

    public Atuador update(long id, AtuadorDTO dto) throws NotFoundException {
        var res = atuadorRepository.findById(id);

        if(res.isEmpty()) {
            throw new NotFoundException("Atuador " + id + " não existe.");
        }

        var atuador = res.get();
        atuador.setNome(dto.nome());
        atuador.setDispositivo(dto.dispositivo());
        atuador.setUpdated_at(LocalDateTime.now());

        return atuadorRepository.save(atuador);
    }
    public void delete(long id) throws NotFoundException {
        var res = atuadorRepository.findById(id);

        if(res.isEmpty()) {
            throw new NotFoundException("Atuador " + id + " não existe.");
        }

        atuadorRepository.delete(res.get());
    }
}

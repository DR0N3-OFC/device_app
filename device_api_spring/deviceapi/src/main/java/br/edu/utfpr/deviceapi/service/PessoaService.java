package br.edu.utfpr.deviceapi.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.utfpr.deviceapi.dto.PessoaDTO;
import br.edu.utfpr.deviceapi.exception.NotFoundException;
import br.edu.utfpr.deviceapi.model.Pessoa;
import br.edu.utfpr.deviceapi.repository.PessoaRepository;

@Service
public class PessoaService {
    @Autowired
    private PessoaRepository pessoaRepository;

    /**
     * Inserir uma pessoa no DB.
     * @return
     */
    public Pessoa create(PessoaDTO dto) {
        var pessoa = new Pessoa();
        BeanUtils.copyProperties(dto, pessoa);

        pessoa.setCreated_at(LocalDateTime.now());
        pessoa.setUpdated_at(LocalDateTime.now());
        // Persistir no Banco de dados
        return pessoaRepository.save(pessoa);
    }

    /**
     * Buscar no banco de dados todas.
     * @return
     */
    public List<Pessoa> getAll() {
        return pessoaRepository.findAll();
    }

    /**
     * Buscar uma pessoa pelo ID.
     * @param id
     * @return
     */
    public Optional<Pessoa> getById(long id) {
        return pessoaRepository.findById(id);
    }


    public Pessoa update(long id, PessoaDTO dto) throws NotFoundException {
        var res = pessoaRepository.findById(id);

        if(res.isEmpty()) {
            throw new NotFoundException("Pessoa " + id + " não existe.");
        }

        var pessoa = res.get();
        pessoa.setNome(dto.nome());
        pessoa.setEmail(dto.email());
        pessoa.setUpdated_at(LocalDateTime.now());

        return pessoaRepository.save(pessoa);
    }

    public Optional<Pessoa> findByEmail(String email) {
        return pessoaRepository.findByEmail(email);
    }

    public void delete(long id) throws NotFoundException {
        var res = pessoaRepository.findById(id);

        if(res.isEmpty()) {
            throw new NotFoundException("Pessoa " + id + " não existe.");
        }

        pessoaRepository.delete(res.get());
    }

}

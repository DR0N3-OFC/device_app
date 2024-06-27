package br.edu.utfpr.deviceapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.deviceapi.model.Medicao;

public interface MedicaoRepository extends JpaRepository<Medicao, Long> {

}

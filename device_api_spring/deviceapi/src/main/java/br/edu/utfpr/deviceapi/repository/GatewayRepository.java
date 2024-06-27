package br.edu.utfpr.deviceapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.deviceapi.model.Gateway;

public interface GatewayRepository extends JpaRepository<Gateway, Long> {

}
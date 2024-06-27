package br.edu.utfpr.deviceapi.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity //Entidade gerenciada pelo JPA (pode ser persistida)
@Table(name = "tb_atuador") // Define o noma da tabel
@Data
public class Atuador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long atuador_id; 
    
    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false)
    private LocalDateTime created_at;

    @Column(nullable = false)
    private LocalDateTime updated_at;

    @ManyToOne
    @JoinColumn(name = "dispositivo_id")
    @JsonIgnore
    private Dispositivo dispositivo;
}

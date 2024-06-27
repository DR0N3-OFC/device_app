package br.edu.utfpr.deviceapi.dto;

import java.util.Date;
import java.util.List;

import br.edu.utfpr.deviceapi.model.Dispositivo;
import br.edu.utfpr.deviceapi.model.Medicao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SensorDTO(
    @NotBlank(message = "O nome do sensor não pode estar vazio.")
    @Size(min = 3, max = 50, message = "O nome do sensor deve ter entre 3 e 50 caracteres.")
    String nome,
    @NotBlank(message = "O tipo do sensor não pode estar vazio.")
    @Size(min = 3, max = 30, message = "O tipo do sensor deve ter entre 3 e 30 caracteres.")
    String tipo,
    Dispositivo dispositivo,
    Date created_at,
    Date updated_at,
    List<Medicao> medicoes) {
}

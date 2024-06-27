package br.edu.utfpr.deviceapi.dto;

import java.util.Date;

import br.edu.utfpr.deviceapi.model.Dispositivo;
import jakarta.validation.constraints.NotBlank;

public record AtuadorDTO(
    @NotBlank(message = "O nome do atuador não pode estar vazio.")
    String nome,
    Date created_at,
    Date updated_at,
    Dispositivo dispositivo) {

}

package br.edu.utfpr.deviceapi.dto;

import java.util.Date;
import java.util.List;

import br.edu.utfpr.deviceapi.model.Dispositivo;
import br.edu.utfpr.deviceapi.model.Pessoa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GatewayDTO(
    @NotBlank(message = "O nome do gateway não pode estar vazio.")
    @Size(min = 3, max = 200, message = "O nome do gateway deve ter entre 3 e 200 caracteres.")
    String nome, 
    String descricao,
    @NotBlank(message = "O endereço do gateway não pode estar vazio.")
    @Size(min = 7, max = 15, message = "O endereço do gateway deve ter entre 3 e 200 caracteres.") 
    String endereco,
    Date created_at,
    Date updated_at,
    Pessoa pessoa,
    List<Dispositivo> dispositivos) {

}

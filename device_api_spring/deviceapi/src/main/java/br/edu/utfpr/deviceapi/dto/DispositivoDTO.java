package br.edu.utfpr.deviceapi.dto;

import java.util.Date;
import java.util.List;

import br.edu.utfpr.deviceapi.model.Atuador;
import br.edu.utfpr.deviceapi.model.Gateway;
import br.edu.utfpr.deviceapi.model.Sensor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DispositivoDTO(
    @NotBlank(message = "O nome do dispositivo não pode estar vazio.")
    @Size(min = 3, max = 100, message = "O nome do dispositivo deve ter entre 3 e 100 caracteres.")
    String nome,
    String descricao,
    @NotBlank(message = "A localização do dispositivo não pode estar vazia.")
    @Size(min = 7, max = 15, message = "A localização do dispositivo deve ter entre 3 e 200 caracteres.")
    String localizacao,
    @NotBlank(message = "O endereço do dispositivo não pode estar vazio.")
    @Size(min = 7, max = 15, message = "O endereço do dispositivo deve ter entre 3 e 200 caracteres.")
    String endereco,
    Gateway gateway,
    Date created_at,
    Date updated_at,
    List<Atuador> atuadores,
    List<Sensor> sensores) {

}

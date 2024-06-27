package br.edu.utfpr.deviceapi.dto;

import java.util.List;

import br.edu.utfpr.deviceapi.model.Gateway;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PessoaDTO(
    @NotBlank(message = "O nome não pode estar vazio.")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres.")
    String nome,
    @NotBlank(message = "O e-mail não pode estar vazio.")
    @Email(message = "Formato de e-mail inválido.")
    String email,
    @NotBlank(message = "A senha não pode estar vazia.")
    @Size(min = 8, max = 30, message = "A senha deve ter entre 8 e 30 caracteres.")
    String senha,
    List<Gateway> gateways) {
}

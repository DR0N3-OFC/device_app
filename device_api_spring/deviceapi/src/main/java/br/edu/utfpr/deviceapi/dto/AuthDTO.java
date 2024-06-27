package br.edu.utfpr.deviceapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuthDTO {
    @NotBlank(message = "O nome de usuário não pode estar vazio.")
    public String username;

    @NotBlank(message = "A senha não pode estar vazia.")
    public String password;
}
package br.edu.utfpr.deviceapi.dto;

import java.util.Date;

import br.edu.utfpr.deviceapi.model.Sensor;
import jakarta.validation.constraints.NotNull;

public record MedicaoDTO(
    @NotNull(message = "A data não pode ser nula!")
    Date data,
    double valor,
    Date created_at,
    Date updated_at,
    Sensor sensor) {

}

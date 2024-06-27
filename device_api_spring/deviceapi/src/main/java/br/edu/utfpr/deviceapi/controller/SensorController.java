package br.edu.utfpr.deviceapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.deviceapi.dto.SensorDTO;
import br.edu.utfpr.deviceapi.exception.NotFoundException;
import br.edu.utfpr.deviceapi.model.Medicao;
import br.edu.utfpr.deviceapi.model.Sensor;
import br.edu.utfpr.deviceapi.producer.DeviceProducer;
import br.edu.utfpr.deviceapi.service.SensorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/sensor")
@Tag(name = "Sensor", description = "Endpoint para operações relacionadas a sensores")
public class SensorController {
    @Autowired private SensorService sensorService;
    @Autowired private DeviceProducer producer;

    @PostMapping
    @Operation(summary = "Criar um novo sensor", description = "Registra um novo objeto de sensor com base no DTO recebido.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sucesso, retorna o sensor", content = @Content(schema = @Schema(implementation = Sensor.class))),
        @ApiResponse(responseCode = "404", description = "Não encontrado, nenhum sensor com o ID fornecido")
    })
    public ResponseEntity<Object> create(@Valid @RequestBody SensorDTO dto) {
        try {
            var res = sensorService.create(dto);

            // Seta o status para 201 (CREATED) e devolve
            // o objeto sensor em JSON.
            producer.sendMessage(String.format("Sensor criado: %s", dto.nome()));

            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        } catch(Exception ex) {
            // Seta o status para 400 (Bad request) e devolve
            // a mensagem da exceção lançada.
            producer.sendMessage(String.format("Erro ao criar sensor: %s", ex.getMessage()));
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Obter todas as sensors do DB.
     */
    @GetMapping
    @Operation(summary = "Obter todos os sensores")
    public List<Sensor> getAll() {
        return sensorService.getAll();
    }

    /**
     * Obter 1 sensor pelo ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obter um sensor pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso, retorna o sensor", content = @Content(schema = @Schema(implementation = Sensor.class))),
        @ApiResponse(responseCode = "404", description = "Não encontrado, nenhum sensor com o ID fornecido")
    })
    public ResponseEntity<Object> getById(@PathVariable("id") long id) {
        var sensor = sensorService.getById(id);
        
        return sensor.isPresent()
            ? ResponseEntity.ok().body(sensor.get())
            : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/{id}/medicoes")
    @Operation(summary = "Obter medicoes por ID do gateway")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso, retorna os medicoes do gateway", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Medicao.class)))),
        @ApiResponse(responseCode = "404", description = "Não encontrado, nenhuma medicao com o ID fornecido")
    })
    public ResponseEntity<Object> getMeasurementsBySensorId(@PathVariable("id") long id) {
        var sensor = sensorService.getById(id);
        
        return sensor.isPresent()
            ? ResponseEntity.ok().body(sensor.get().getMedicoes())
            : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um sensor com base no seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso, retorna o sensor atualizado", content = @Content(schema = @Schema(implementation = Sensor.class))),
        @ApiResponse(responseCode = "404", description = "Não encontrado, nenhum sensor com o ID fornecido"),
        @ApiResponse(responseCode = "400", description = "ERRO, ocorreu algum erro na requisição")
    })
    public ResponseEntity<Object> update(@PathVariable long id,
        @RequestBody SensorDTO dto) {
            try {
                var sensor = sensorService.update(id, dto);

                producer.sendMessage(String.format("Sensor com ID %d atualizado: %s", id, dto.nome()));
                return ResponseEntity.ok().body(sensor);
            } catch(NotFoundException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
            } catch(Exception ex) {
                producer.sendMessage(String.format("Erro ao atualizar sensor: %s", ex.getMessage()));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
            }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um sensor com base no seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso, sensor deletado"),
        @ApiResponse(responseCode = "404", description = "Não encontrado, nenhum sensor com o ID fornecido"),
        @ApiResponse(responseCode = "400", description = "ERRO, ocorreu algum erro na requisição")
    })
    public ResponseEntity<Object> delete(@PathVariable("id") long id){
        try {
            sensorService.delete(id);

            producer.sendMessage(String.format("Sensor com ID %d removido.", id));
            return ResponseEntity.ok().build();
        } catch(NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch(Exception ex) {
            producer.sendMessage(String.format("Erro ao remover sensor com ID %d.", id));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

}

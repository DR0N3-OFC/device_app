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

import br.edu.utfpr.deviceapi.dto.DispositivoDTO;
import br.edu.utfpr.deviceapi.exception.NotFoundException;
import br.edu.utfpr.deviceapi.model.Atuador;
import br.edu.utfpr.deviceapi.model.Dispositivo;
import br.edu.utfpr.deviceapi.model.Sensor;
import br.edu.utfpr.deviceapi.producer.DeviceProducer;
import br.edu.utfpr.deviceapi.service.DispositivoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/dispositivo")
@Tag(name = "Dispositivo", description = "Endpoint para operações relacionadas a dispositivos")
public class DispositivoController {
    @Autowired
    private DispositivoService dispositivoService;
    @Autowired private DeviceProducer producer;

    @PostMapping
    @Operation(summary = "Criar um novo dispositivo", description = "Registra um novo objeto de dispositivo com base no DTO recebido.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sucesso, retorna o dispositivo", content = @Content(schema = @Schema(implementation = Dispositivo.class))),
        @ApiResponse(responseCode = "404", description = "Não encontrado, nenhum dispositivo com o ID fornecido")
    })
    public ResponseEntity<Object> create(@Valid @RequestBody DispositivoDTO dto) {
        try {
            var res = dispositivoService.create(dto);
            producer.sendMessage(String.format("Dispositivo criado: %s (%s)", dto.nome(), dto.endereco()));
            // Seta o status para 201 (CREATED) e devolve
            // o objeto dispositivo em JSON.
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        } catch(Exception ex) {
            // Seta o status para 400 (Bad request) e devolve
            // a mensagem da exceção lançada.
            producer.sendMessage(String.format("Erro ao criar dispositivo: %s", ex.getMessage()));
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Obter todas as dispositivos do DB.
     */
    @GetMapping
    @Operation(summary = "Obter todos os dispositivos")
    public List<Dispositivo> getAll() {
        return dispositivoService.getAll();
    }

    /**
     * Obter 1 dispositivo pelo ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obter um dispositivo pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso, retorna o dispositivo", content = @Content(schema = @Schema(implementation = Dispositivo.class))),
        @ApiResponse(responseCode = "404", description = "Não encontrado, nenhum dispositivo com o ID fornecido")
    })
    public ResponseEntity<Object> getById(@PathVariable("id") long id) {
        var dispositivo = dispositivoService.getById(id);
        
        return dispositivo.isPresent()
            ? ResponseEntity.ok().body(dispositivo.get())
            : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/{id}/atuadores")
    @Operation(summary = "Obter atuadores por ID do dispositivo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso, retorna os atuadores do dispositivo", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Atuador.class)))),
        @ApiResponse(responseCode = "404", description = "Não encontrado, nenhum dispositivo com o ID fornecido")
    })
    public ResponseEntity<Object> getActuatorsByDeviceId(@PathVariable("id") long id) {
        var dispositivo = dispositivoService.getById(id);
        
        return dispositivo.isPresent()
            ? ResponseEntity.ok().body(dispositivo.get().getAtuadores())
            : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/sensores")
    @Operation(summary = "Obter sensores por ID do dispositivo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso, retorna os sensores do dispositivo", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Sensor.class)))),
        @ApiResponse(responseCode = "404", description = "Não encontrado, nenhum sensor com o ID fornecido")
    })
    public ResponseEntity<Object> getSensorsByDeviceId(@PathVariable("id") long id) {
        var dispositivo = dispositivoService.getById(id);
        
        return dispositivo.isPresent()
            ? ResponseEntity.ok().body(dispositivo.get().getSensores())
            : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um dispositivo com base no seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso, retorna o dispositivo atualizado", content = @Content(schema = @Schema(implementation = Dispositivo.class))),
        @ApiResponse(responseCode = "404", description = "Não encontrado, nenhum dispositivo com o ID fornecido"),
        @ApiResponse(responseCode = "400", description = "ERRO, ocorreu algum erro na requisição")
    })
    public ResponseEntity<Object> update(@PathVariable long id,
        @RequestBody DispositivoDTO dto) {
            try {
                var dispositivo = dispositivoService.update(id, dto);

                producer.sendMessage(String.format("Dispositivo com ID %d atualizado: %s (%s)", id, dto.nome(), dto.endereco()));
                return ResponseEntity.ok().body(dispositivo);
            } catch(NotFoundException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
            } catch(Exception ex) {
                producer.sendMessage(String.format("Erro ao atualizar dispositivo: %s", ex.getMessage()));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
            }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um dispositivo com base no seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso, dispositivo deletado"),
        @ApiResponse(responseCode = "404", description = "Não encontrado, nenhum dispositivo com o ID fornecido"),
        @ApiResponse(responseCode = "400", description = "ERRO, ocorreu algum erro na requisição")
    })
    public ResponseEntity<Object> delete(@PathVariable("id") long id){
        try {
            dispositivoService.delete(id);

            producer.sendMessage(String.format("Dispositivo com ID %d removido.", id));
            return ResponseEntity.ok().build();
        } catch(NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch(Exception ex) {
            producer.sendMessage(String.format("Erro ao remover dispositivo com ID %d.", id));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

}

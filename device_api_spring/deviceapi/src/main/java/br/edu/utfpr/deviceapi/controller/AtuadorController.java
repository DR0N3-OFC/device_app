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

import br.edu.utfpr.deviceapi.dto.AtuadorDTO;
import br.edu.utfpr.deviceapi.exception.NotFoundException;
import br.edu.utfpr.deviceapi.model.Atuador;
import br.edu.utfpr.deviceapi.producer.DeviceProducer;
import br.edu.utfpr.deviceapi.service.AtuadorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/atuador")
@Tag(name = "Atuador", description = "Endpoint para operações relacionadas a atuadores")
public class AtuadorController {
    @Autowired private AtuadorService atuadorService;
    @Autowired private DeviceProducer producer;

    @PostMapping
    @Operation(summary = "Criar um novo atuador", description = "Registra um novo objeto de atuador com base no DTO recebido.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sucesso, retorna o atuador", content = @Content(schema = @Schema(implementation = Atuador.class))),
        @ApiResponse(responseCode = "404", description = "Não encontrado, nenhum atuador com o ID fornecido")
    })
    public ResponseEntity<Object> create(@Valid @RequestBody AtuadorDTO dto) {
        try {
            var res = atuadorService.create(dto);
            // Seta o status para 201 (CREATED) e devolve
            // o objeto atuador em JSON.
            producer.sendMessage(String.format("Atuador criado: %s", dto.nome()));

            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        } catch(Exception ex) {
            // Seta o status para 400 (Bad request) e devolve
            // a mensagem da exceção lançada.
            producer.sendMessage(String.format("Erro ao criar atuador: %s", ex.getMessage()));
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Obter todas as atuadores do DB.
     */
    @GetMapping
    @Operation(summary = "Obter todos os atuadores")
    public List<Atuador> getAll() {
        return atuadorService.getAll();
    }

    /**
     * Obter 1 atuador pelo ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obter um atuador pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso, retorna o atuador", content = @Content(schema = @Schema(implementation = Atuador.class))),
        @ApiResponse(responseCode = "404", description = "Não encontrado, nenhum atuador com o ID fornecido")
    })
    public ResponseEntity<Object> getById(@PathVariable("id") long id) {
        var atuador = atuadorService.getById(id);
        
        return atuador.isPresent()
            ? ResponseEntity.ok().body(atuador.get())
            : ResponseEntity.notFound().build();
    }
    

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um atuador com base no seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso, retorna o atuador atualizado", content = @Content(schema = @Schema(implementation = Atuador.class))),
        @ApiResponse(responseCode = "404", description = "Não encontrado, nenhum atuador com o ID fornecido"),
        @ApiResponse(responseCode = "400", description = "ERRO, ocorreu algum erro na requisição")
    })
    public ResponseEntity<Object> update(@PathVariable long id,
        @RequestBody AtuadorDTO dto) {
            try {
                var atuador = atuadorService.update(id, dto);

                producer.sendMessage(String.format("Atuador com ID %d atualizado: %s", id, dto.nome()));
                return ResponseEntity.ok().body(atuador);
            } catch(NotFoundException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
            } catch(Exception ex) {
                producer.sendMessage(String.format("Erro ao atualizar atuador: %s", ex.getMessage()));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
            }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um atuador com base no seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso, atuador deletado"),
        @ApiResponse(responseCode = "404", description = "Não encontrado, nenhum atuador com o ID fornecido"),
        @ApiResponse(responseCode = "400", description = "ERRO, ocorreu algum erro na requisição")
    })
    public ResponseEntity<Object> delete(@PathVariable("id") long id){
        try {
            atuadorService.delete(id);

            producer.sendMessage(String.format("Atuador com ID %d removido.", id));
            return ResponseEntity.ok().build();
        } catch(NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch(Exception ex) {
            producer.sendMessage(String.format("Erro ao remover atuador com ID %d.", id));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

}

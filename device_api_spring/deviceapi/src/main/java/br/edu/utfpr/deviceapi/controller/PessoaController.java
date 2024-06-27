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

import br.edu.utfpr.deviceapi.dto.PessoaDTO;
import br.edu.utfpr.deviceapi.exception.NotFoundException;
import br.edu.utfpr.deviceapi.model.Gateway;
import br.edu.utfpr.deviceapi.model.Pessoa;
import br.edu.utfpr.deviceapi.producer.DeviceProducer;
import br.edu.utfpr.deviceapi.service.PessoaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/pessoa")
@Tag(name = "Pessoa", description = "Endpoint para operações relacionadas a pessoas")
public class PessoaController {
    @Autowired private PessoaService pessoaService;
    @Autowired private DeviceProducer producer;

    @PostMapping
    @Operation(summary = "Criar uma nova pessoa", description = "Registra um novo objeto de pessoa com base no DTO recebido.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sucesso, retorna a pessoa", content = @Content(schema = @Schema(implementation = Pessoa.class))),
        @ApiResponse(responseCode = "404", description = "Não encontrado, nenhuma pessoa com o ID fornecido")
    })
    public ResponseEntity<Object> create(@Valid @RequestBody PessoaDTO dto) {
        try {
            var res = pessoaService.create(dto);
            producer.sendMessage(String.format("Pessoa criada: %s (%s)", dto.nome(), dto.email()));
            // Seta o status para 201 (CREATED) e devolve
            // o objeto Pessoa em JSON.
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        } catch(Exception ex) {
            // Seta o status para 400 (Bad request) e devolve
            // a mensagem da exceção lançada.
            producer.sendMessage(String.format("Erro ao criar pessoa: %s", ex.getMessage()));
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Obter todas as pessoas do DB.
     */
    @GetMapping
    @Operation(summary = "Obter todas as pessoas")
    public List<Pessoa> getAll() {
        return pessoaService.getAll();
    }

    /**
     * Obter 1 pessoa pelo ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obter uma pessoa pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso, retorna a pessoa", content = @Content(schema = @Schema(implementation = Pessoa.class))),
        @ApiResponse(responseCode = "404", description = "Não encontrado, nenhuma pessoa com o ID fornecido")
    })
    public ResponseEntity<Object> getById(@PathVariable("id") long id) {
        var pessoa = pessoaService.getById(id);
        
        return pessoa.isPresent()
            ? ResponseEntity.ok().body(pessoa.get())
            : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/{id}/gateways")
    @Operation(summary = "Obter gateways por ID da pessoa")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso, retorna os gateways da pessoa", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Gateway.class)))),
        @ApiResponse(responseCode = "404", description = "Não encontrado, nenhum gateway com o ID fornecido")
    })
    public ResponseEntity<Object> getGatewaysByPersonId(@PathVariable("id") long id) {
        var pessoa = pessoaService.getById(id);
        
        return pessoa.isPresent()
            ? ResponseEntity.ok().body(pessoa.get().getGateways())
            : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma pessoa com base no seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso, retorna a pessoa atualizada", content = @Content(schema = @Schema(implementation = Pessoa.class))),
        @ApiResponse(responseCode = "404", description = "Não encontrado, nenhuma pessoa com o ID fornecido"),
        @ApiResponse(responseCode = "400", description = "ERRO, ocorreu algum erro na requisição")
    })
    public ResponseEntity<Object> update(@PathVariable long id,
        @RequestBody PessoaDTO dto) {
            try {
                var pessoa = pessoaService.update(id, dto);

                producer.sendMessage(String.format("Pessoa com ID %d atualizada: %s (%s)", id, dto.nome(), dto.email()));
                return ResponseEntity.ok().body(pessoa);
            } catch(NotFoundException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
            } catch(Exception ex) {
                producer.sendMessage(String.format("Erro ao atualizar pessoa: %s", ex.getMessage()));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
            }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta uma pessoa com base no seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso, pessoa deletada"),
        @ApiResponse(responseCode = "404", description = "Não encontrado, nenhuma pessoa com o ID fornecido"),
        @ApiResponse(responseCode = "400", description = "ERRO, ocorreu algum erro na requisição")
    })
    public ResponseEntity<Object> delete(@PathVariable("id") long id){
        try {
            pessoaService.delete(id);

            producer.sendMessage(String.format("Pessoa com ID %d removida.", id));
            return ResponseEntity.ok().build();
        } catch(NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch(Exception ex) {
            producer.sendMessage(String.format("Erro ao remover pessoa com ID %d.", id));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

}

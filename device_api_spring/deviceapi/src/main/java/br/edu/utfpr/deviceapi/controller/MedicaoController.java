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

import br.edu.utfpr.deviceapi.dto.MedicaoDTO;
import br.edu.utfpr.deviceapi.exception.NotFoundException;
import br.edu.utfpr.deviceapi.model.Medicao;
import br.edu.utfpr.deviceapi.producer.DeviceProducer;
import br.edu.utfpr.deviceapi.service.MedicaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/medicao")
@Tag(name = "Medicao", description = "Endpoint para operações relacionadas a medicoes")
public class MedicaoController {
    @Autowired
    private MedicaoService medicaoService;
    @Autowired private DeviceProducer producer;

    @PostMapping
    @Operation(summary = "Criar uma novo medicao", description = "Registra uma novo objeto de medicao com base no DTO recebido.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sucesso, retorna o medicao", content = @Content(schema = @Schema(implementation = Medicao.class))),
        @ApiResponse(responseCode = "404", description = "Não encontrado, nenhuma medicao com o ID fornecida")
    })
    public ResponseEntity<Object> create(@Valid @RequestBody MedicaoDTO dto) {
        try {
            var res = medicaoService.create(dto);

            // Seta o status para 201 (CREATED) e devolve
            // o objeto medicao em JSON.
            producer.sendMessage(String.format("Medicao criada em %s -> Valor(%s)", dto.data(), dto.valor()));
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        } catch(Exception ex) {
            // Seta o status para 400 (Bad request) e devolve
            // a mensagem da exceção lançada.
            producer.sendMessage(String.format("Erro ao criar medicao: %s", ex.getMessage()));
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Obter todas as medicaos do DB.
     */
    @GetMapping
    @Operation(summary = "Obter todas os medicoes")
    public List<Medicao> getAll() {
        return medicaoService.getAll();
    }

    /**
     * Obter 1 medicao pelo ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obter uma medicao pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso, retorna o medicao", content = @Content(schema = @Schema(implementation = Medicao.class))),
        @ApiResponse(responseCode = "404", description = "Não encontrado, nenhuma medicao com o ID fornecida")
    })
    public ResponseEntity<Object> getById(@PathVariable("id") long id) {
        var medicao = medicaoService.getById(id);
        
        return medicao.isPresent()
            ? ResponseEntity.ok().body(medicao.get())
            : ResponseEntity.notFound().build();
    }
    

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma medicao com base no seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso, retorna a medicao atualizada", content = @Content(schema = @Schema(implementation = Medicao.class))),
        @ApiResponse(responseCode = "404", description = "Não encontrado, nenhuma medicao com o ID fornecida"),
        @ApiResponse(responseCode = "400", description = "ERRO, ocorreu algum erro na requisição")
    })
    public ResponseEntity<Object> update(@PathVariable long id,
        @RequestBody MedicaoDTO dto) {
            try {
                var medicao = medicaoService.update(id, dto);

                producer.sendMessage(String.format("Medicao com ID %d atualizada: %s", id, dto.valor()));
                return ResponseEntity.ok().body(medicao);
            } catch(NotFoundException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
            } catch(Exception ex) {
                producer.sendMessage(String.format("Erro ao atualizar medicao: %s", ex.getMessage()));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
            }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta uma medicao com base no seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso, medicao deletada"),
        @ApiResponse(responseCode = "404", description = "Não encontrado, nenhuma medicao com o ID fornecida"),
        @ApiResponse(responseCode = "400", description = "ERRO, ocorreu algum erro na requisição")
    })
    public ResponseEntity<Object> delete(@PathVariable("id") long id){
        try {
            medicaoService.delete(id);

            producer.sendMessage(String.format("Medicao com ID %d removida.", id));
            return ResponseEntity.ok().build();
        } catch(NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch(Exception ex) {
            producer.sendMessage(String.format("Erro ao remover medicao com ID %d.", id));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

}

package br.edu.utfpr.deviceapi.controller;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.deviceapi.dto.AuthDTO;
import br.edu.utfpr.deviceapi.exception.NotFoundException;
import br.edu.utfpr.deviceapi.producer.DeviceProducer;
import br.edu.utfpr.deviceapi.security.JwtUtil;
import br.edu.utfpr.deviceapi.service.PessoaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired private DeviceProducer producer;

    @Autowired private PessoaService pessoaService;

    @Value("${jwt_secret}")
    private String jwtSecret;

    @PostMapping
    public ResponseEntity<Object> auth(@Valid @RequestBody AuthDTO authDTO) {
        try {
            var payload = new HashMap<String, Object>();
            payload.put("username", authDTO.username);

            var now = Instant.now();

            var jwt = jwtUtil.generateToken(payload, jwtSecret, 36000);

            var pessoa = pessoaService.findByEmail(authDTO.username);

            if (!pessoa.isPresent())
                throw new NotFoundException("Usuário não encontrado ou senha incorreta");
            
            var res = new HashMap<String, Object>();
            res.put("token", jwt);
            res.put("issuedIn", now);
            res.put("expiresIn", now.plus(36000, ChronoUnit.SECONDS));

            producer.sendMessage(String.format("Usuário conectado: %s", authDTO.username));
            return ResponseEntity.ok().body(res);
        } catch (NotFoundException e) {
            producer.sendMessage(String.format("Erro de autenticação: %s", e.getMessage()));
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

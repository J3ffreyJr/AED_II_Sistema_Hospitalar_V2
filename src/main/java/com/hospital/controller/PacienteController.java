package com.hospital.controller;

import com.hospital.dto.EstatisticasResponse;
import com.hospital.dto.PacienteRequest;
import com.hospital.model.Paciente;
import com.hospital.service.SistemaAtendimento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pacientes")
@CrossOrigin(origins = "*")
public class PacienteController {

    @Autowired
    private SistemaAtendimento sistema;

    @GetMapping
    public ResponseEntity<List<Paciente>> listarFilaNormal() {
        return ResponseEntity.ok(sistema.listarFilaNormal());
    }

    @GetMapping("/prioritarios")
    public ResponseEntity<List<Paciente>> listarFilaPrioritaria() {
        return ResponseEntity.ok(sistema.listarFilaPrioritaria());
    }

    @GetMapping("/historico")
    public ResponseEntity<List<Paciente>> listarHistorico() {
        return ResponseEntity.ok(sistema.listarHistorico());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paciente> buscarPaciente(@PathVariable int id) {
        Paciente paciente = sistema.buscarPaciente(id);
        if (paciente == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(paciente);
    }

    @GetMapping("/proximo")
    public ResponseEntity<Paciente> atenderProximo() {
        Paciente paciente = sistema.atenderProximoPaciente();
        if (paciente == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(paciente);
    }

    @PostMapping
    public ResponseEntity<Paciente> inserirPaciente(@RequestBody PacienteRequest request) {
        Paciente paciente = sistema.criarPaciente(request);
        sistema.inserirPaciente(paciente);
        return ResponseEntity.ok(paciente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerPaciente(@PathVariable int id) {
        sistema.removerPaciente(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/desfazer")
    public ResponseEntity<Map<String, String>> desfazer() {
        boolean sucesso = sistema.desfazerUltimaOperacao();
        if (sucesso) {
            return ResponseEntity.ok(Map.of("mensagem", "Operacao desfeita com sucesso"));
        }
        return ResponseEntity.badRequest().body(Map.of("mensagem", "Nenhuma operacao para desfazer"));
    }

    @GetMapping("/estatisticas")
    public ResponseEntity<EstatisticasResponse> estatisticas() {
        return ResponseEntity.ok(sistema.estatisticas());
    }

    @GetMapping("/proximo-id")
    public ResponseEntity<Map<String, Integer>> getProximoId() {
        return ResponseEntity.ok(Map.of("proximoId", sistema.getProximoId()));
    }
}

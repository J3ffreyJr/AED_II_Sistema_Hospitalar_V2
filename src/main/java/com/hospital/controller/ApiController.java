package com.hospital.controller;

import com.hospital.dto.EstatisticasResponse;
import com.hospital.service.SistemaAtendimento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ApiController {

    @Autowired
    private SistemaAtendimento sistema;

    @GetMapping("/estatisticas")
    public ResponseEntity<EstatisticasResponse> estatisticas() {
        return ResponseEntity.ok(sistema.estatisticas());
    }

    @GetMapping("/proximo-id")
    public ResponseEntity<java.util.Map<String, Integer>> getProximoId() {
        return ResponseEntity.ok(java.util.Map.of("proximoId", sistema.getProximoId()));
    }
}

package com.hospital.service;

import com.hospital.dto.EstatisticasResponse;
import com.hospital.dto.PacienteRequest;
import com.hospital.model.Paciente;
import com.hospital.service.estruturas.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class SistemaAtendimento {
    private Fila filaNormal;
    private HeapPrioridade filaPrioritaria;
    private TabelaHash tabela;
    private ListaDuplamenteLigada historico;
    private Pilha pilhaDesfazer;
    private int contadorId;

    public SistemaAtendimento() {
        this.filaNormal = new Fila();
        this.filaPrioritaria = new HeapPrioridade();
        this.tabela = new TabelaHash();
        this.historico = new ListaDuplamenteLigada();
        this.pilhaDesfazer = new Pilha();
        this.contadorId = 1;
    }

    public Paciente criarPaciente(PacienteRequest request) {
        Paciente p = new Paciente(contadorId++, request.getNome(), request.getIdade(),
                request.getBi(), request.getTelefone(), request.getEndereco(), request.getPrioridade());
        return p;
    }

    public void inserirPaciente(Paciente p) {
        tabela.inserir(p);

        if ((p.getPrioridade() != null && p.getPrioridade().equalsIgnoreCase("prioritario")) || p.getIdade() >= 60) {
            filaPrioritaria.inserir(p);
        } else {
            filaNormal.enqueue(p);
        }
    }

    public Paciente atenderProximoPaciente() {
        Paciente paciente = null;

        if (!filaPrioritaria.isEmpty()) {
            paciente = filaPrioritaria.remover();
            historico.inserir(paciente);
            pilhaDesfazer.push(paciente);
        } else if (!filaNormal.isEmpty()) {
            paciente = filaNormal.dequeue();
            historico.inserir(paciente);
            pilhaDesfazer.push(paciente);
        }

        if (paciente != null) {
            tabela.remover(paciente.getId());
        }

        return paciente;
    }

    public Paciente buscarPaciente(int id) {
        return tabela.buscar(id);
    }

    public void removerPaciente(int id) {
        tabela.remover(id);
    }

    public boolean desfazerUltimaOperacao() {
        if (pilhaDesfazer.isEmpty()) {
            return false;
        }

        Paciente ultimo = pilhaDesfazer.pop();
        historico.remover(ultimo.getId());

        if ((ultimo.getPrioridade() != null && ultimo.getPrioridade().equalsIgnoreCase("prioritario")) || ultimo.getIdade() >= 60) {
            filaPrioritaria.inserir(ultimo);
        } else {
            filaNormal.enqueue(ultimo);
        }

        tabela.inserir(ultimo);
        return true;
    }

    public java.util.List<Paciente> listarFilaNormal() {
        return filaNormal.listarTodos();
    }

    public java.util.List<Paciente> listarFilaPrioritaria() {
        return filaPrioritaria.listarTodos();
    }

    public java.util.List<Paciente> listarHistorico() {
        return historico.listarTodos();
    }

    public EstatisticasResponse estatisticas() {
        return new EstatisticasResponse(
                filaNormal.getTamanho(),
                filaPrioritaria.getTamanho(),
                historico.getTamanho(),
                pilhaDesfazer.getTamanho()
        );
    }

    public int getProximoId() {
        return contadorId;
    }
}

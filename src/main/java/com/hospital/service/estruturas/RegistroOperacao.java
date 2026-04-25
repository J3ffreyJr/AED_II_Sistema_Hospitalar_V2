package com.hospital.service.estruturas;

import com.hospital.model.Paciente;
import java.time.LocalDateTime;

public class RegistroOperacao {
    private Paciente paciente;
    private String tipo; // "atendido" ou "removido"
    private String filaOrigem; // "normal" ou "prioritaria"
    private LocalDateTime timestamp;
    private int posicaoOriginal; // Posição original na fila antes de sair
    private String criterioOrdenacao; // Critério de ordenação usado

    public RegistroOperacao(Paciente paciente, String tipo, String filaOrigem) {
        this.paciente = paciente;
        this.tipo = tipo;
        this.filaOrigem = filaOrigem;
        this.timestamp = LocalDateTime.now();
        this.posicaoOriginal = -1;
        this.criterioOrdenacao = "entrada";
    }

    public Paciente getPaciente() { return paciente; }
    public String getTipo() { return tipo; }
    public String getFilaOrigem() { return filaOrigem; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public int getPosicaoOriginal() { return posicaoOriginal; }
    public String getCriterioOrdenacao() { return criterioOrdenacao; }

    public void setPosicaoOriginal(int posicao) { this.posicaoOriginal = posicao; }
    public void setCriterioOrdenacao(String criterio) { this.criterioOrdenacao = criterio; }

    public boolean isRemovido() { return "removido".equals(tipo); }
    public boolean isAtendido() { return "atendido".equals(tipo); }
}
package com.hospital.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Atendimento {
    private Paciente paciente;
    private LocalDateTime dataHora;
    private String tipo;
    private String observacao;

    public Atendimento(Paciente paciente, String tipo) {
        this.paciente = paciente;
        this.dataHora = LocalDateTime.now();
        this.tipo = tipo;
        this.observacao = "";
    }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return "Atendimento{paciente=" + paciente.getNome() + ", dataHora=" + dataHora.format(formatter) + ", tipo=" + tipo + '}';
    }
}

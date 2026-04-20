package com.hospital.dto;

import com.hospital.model.Paciente;

public class AtendimentoResponse {
    private Paciente paciente;
    private String dataHora;
    private String tipo;

    public AtendimentoResponse(Paciente paciente, String dataHora, String tipo) {
        this.paciente = paciente;
        this.dataHora = dataHora;
        this.tipo = tipo;
    }

    public Paciente getPaciente() { return paciente; }
    public String getDataHora() { return dataHora; }
    public String getTipo() { return tipo; }
}

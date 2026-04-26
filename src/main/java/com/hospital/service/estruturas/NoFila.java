package com.hospital.service.estruturas;

import com.hospital.model.Paciente;

public class NoFila {
    private Paciente paciente;
    private NoFila proximo;
    private NoFila anterior;

    public NoFila(Paciente paciente) {
        this.paciente = paciente;
        this.proximo = null;
        this.anterior = null;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public NoFila getProximo() {
        return proximo;
    }

    public void setProximo(NoFila proximo) {
        this.proximo = proximo;
    }

    public NoFila getAnterior() {
        return anterior;
    }

    public void setAnterior(NoFila anterior) {
        this.anterior = anterior;
    }
}

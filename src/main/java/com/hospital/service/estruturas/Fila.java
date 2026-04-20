package com.hospital.service.estruturas;

import com.hospital.model.Paciente;

public class Fila {
    private NoFila inicio;
    private NoFila fim;
    private int tamanho;

    public Fila() {
        this.inicio = null;
        this.fim = null;
        this.tamanho = 0;
    }

    public void enqueue(Paciente paciente) {
        NoFila novo = new NoFila(paciente);
        if (isEmpty()) {
            inicio = novo;
            fim = novo;
        } else {
            fim.setProximo(novo);
            fim = novo;
        }
        tamanho++;
    }

    public Paciente dequeue() {
        if (isEmpty()) {
            return null;
        }
        Paciente paciente = inicio.getPaciente();
        inicio = inicio.getProximo();
        if (inicio == null) {
            fim = null;
        }
        tamanho--;
        return paciente;
    }

    public Paciente peek() {
        if (isEmpty()) {
            return null;
        }
        return inicio.getPaciente();
    }

    public boolean isEmpty() {
        return inicio == null;
    }

    public int getTamanho() {
        return tamanho;
    }

    public java.util.List<Paciente> listarTodos() {
        java.util.List<Paciente> lista = new java.util.ArrayList<>();
        if (isEmpty()) {
            return lista;
        }
        NoFila atual = inicio;
        while (atual != null) {
            lista.add(atual.getPaciente());
            atual = atual.getProximo();
        }
        return lista;
    }
}

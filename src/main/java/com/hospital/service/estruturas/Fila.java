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

    public void inserirNoInicio(Paciente paciente) {
        NoFila novo = new NoFila(paciente);
        if (isEmpty()) {
            inicio = novo;
            fim = novo;
        } else {
            novo.setProximo(inicio);
            inicio.setAnterior(novo);
            inicio = novo;
        }
        tamanho++;
    }

    public Paciente removerPorId(int id) {
        if (isEmpty()) return null;

        if (inicio.getPaciente().getId() == id) {
            return dequeue();
        }

        NoFila atual = inicio;
        while (atual.getProximo() != null) {
            if (atual.getProximo().getPaciente().getId() == id) {
                NoFila removido = atual.getProximo();
                atual.setProximo(removido.getProximo());
                if (removido == fim) {
                    fim = atual;
                }
                tamanho--;
                return removido.getPaciente();
            }
            atual = atual.getProximo();
        }
        return null;
    }

    public boolean isEmpty() {
        return inicio == null;
    }

    public int getTamanho() {
        return tamanho;
    }

    public void inserirNaPosicao(int posicao, Paciente paciente) {
        if (posicao < 0 || posicao > tamanho) {
            // Se posição inválida, insere no final
            enqueue(paciente);
            return;
        }

        if (posicao == 0) {
            // Insere no início
            inserirNoInicio(paciente);
            return;
        }

        if (posicao >= tamanho) {
            // Insere no final
            enqueue(paciente);
            return;
        }

        // Insere na posição específica
        NoFila novo = new NoFila(paciente);
        NoFila atual = inicio;
        
        for (int i = 0; i < posicao - 1; i++) {
            atual = atual.getProximo();
        }

        novo.setProximo(atual.getProximo());
        atual.setProximo(novo);
        tamanho++;
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

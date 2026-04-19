package Estrutura_d_Dados;

import Entidades.Paciente;

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

    public void listarTodos() {
        if (isEmpty()) {
            System.out.println("Fila vazia.");
            return;
        }
        NoFila atual = inicio;
        int pos = 1;
        while (atual != null) {
            System.out.println(pos++ + ". " + atual.getPaciente());
            atual = atual.getProximo();
        }
    }
}

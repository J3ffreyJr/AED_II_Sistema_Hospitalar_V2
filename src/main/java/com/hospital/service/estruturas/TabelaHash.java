package com.hospital.service.estruturas;

import com.hospital.model.Paciente;

public class TabelaHash {
    private static final int TAMANHO = 100;
    private EntradaHash[] tabela;
    private int tamanho;

    public TabelaHash() {
        this.tabela = new EntradaHash[TAMANHO];
        this.tamanho = 0;
    }

    private int funcaoHash(int chave) {
        return chave % TAMANHO;
    }

    public void inserir(Paciente paciente) {
        int indice = funcaoHash(paciente.getId());
        EntradaHash nova = new EntradaHash(paciente.getId(), paciente);
        if (tabela[indice] == null) {
            tabela[indice] = nova;
        } else {
            EntradaHash atual = tabela[indice];
            while (atual.getProximo() != null) {
                atual = atual.getProximo();
            }
            atual.setProximo(nova);
        }
        tamanho++;
    }

    public Paciente buscar(int id) {
        int indice = funcaoHash(id);
        EntradaHash atual = tabela[indice];
        while (atual != null) {
            if (atual.getChave() == id) return atual.getValor();
            atual = atual.getProximo();
        }
        return null;
    }

    public void remover(int id) {
        int indice = funcaoHash(id);
        EntradaHash atual = tabela[indice];
        EntradaHash anterior = null;
        while (atual != null) {
            if (atual.getChave() == id) {
                if (anterior == null) {
                    tabela[indice] = atual.getProximo();
                } else {
                    anterior.setProximo(atual.getProximo());
                }
                tamanho--;
                return;
            }
            anterior = atual;
            atual = atual.getProximo();
        }
    }

    public boolean contem(int id) { return buscar(id) != null; }
    public int getTamanho() { return tamanho; }
}

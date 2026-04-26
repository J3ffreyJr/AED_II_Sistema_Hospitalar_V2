package com.hospital.service.estruturas;

import java.util.ArrayList;
import java.util.List;

public class Pilha<T> {
    private List<T> elementos;

    public Pilha() {
        this.elementos = new ArrayList<>();
    }

    public void push(T elemento) {
        elementos.add(elemento);
    }

    public T pop() {
        return isEmpty() ? null : elementos.remove(elementos.size() - 1);
    }

    public T peek() {
        return isEmpty() ? null : elementos.get(elementos.size() - 1);
    }

    public boolean isEmpty() {
        return elementos.isEmpty();
    }

    public int getTamanho() {
        return elementos.size();
    }

    public void limpar() {
        elementos.clear();
    }

    public List<T> getElementos() {
        return new ArrayList<>(elementos);
    }
}
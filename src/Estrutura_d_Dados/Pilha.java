package Estrutura_d_Dados;

import Entidades.Paciente;

import java.util.ArrayList;
import java.util.List;

public class Pilha {
    private List<Paciente> elementos;

    public Pilha() {
        this.elementos = new ArrayList<>();
    }

    public void push(Paciente paciente) {
        elementos.add(paciente);
    }

    public Paciente pop() {
        return isEmpty() ? null : elementos.remove(elementos.size() - 1);
    }

    public Paciente peek() {
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
}

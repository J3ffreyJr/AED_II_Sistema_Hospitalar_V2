import java.util.ArrayList;
import java.util.List;

public class HeapPrioridade {
    private List<Paciente> heap;

    public HeapPrioridade() {
        this.heap = new ArrayList<>();
    }

    private int getPai(int index) { return (index - 1) / 2; }
    private int getFilhoEsquerdo(int index) { return 2 * index + 1; }
    private int getFilhoDireito(int index) { return 2 * index + 2; }

    private boolean temMaiorPrioridade(Paciente p1, Paciente p2) {
        boolean p1Idoso = p1.getIdade() >= 60;
        boolean p2Idoso = p2.getIdade() >= 60;
        if (p1Idoso && !p2Idoso) return true;
        if (!p1Idoso && p2Idoso) return false;
        return p1.getIdade() > p2.getIdade();
    }

    public void inserir(Paciente paciente) {
        heap.add(paciente);
        heapifyUp(heap.size() - 1);
    }

    private void heapifyUp(int index) {
        while (index > 0) {
            int pai = getPai(index);
            if (temMaiorPrioridade(heap.get(index), heap.get(pai))) {
                Paciente temp = heap.get(pai);
                heap.set(pai, heap.get(index));
                heap.set(index, temp);
                index = pai;
            } else {
                break;
            }
        }
    }

    public Paciente remover() {
        if (isEmpty()) return null;
        Paciente maisPrioritario = heap.get(0);
        Paciente ultimo = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {
            heap.set(0, ultimo);
            heapifyDown(0);
        }
        return maisPrioritario;
    }

    private void heapifyDown(int index) {
        int maior = index;
        int esquerda = getFilhoEsquerdo(index);
        int direita = getFilhoDireito(index);

        if (esquerda < heap.size() && temMaiorPrioridade(heap.get(esquerda), heap.get(maior))) {
            maior = esquerda;
        }
        if (direita < heap.size() && temMaiorPrioridade(heap.get(direita), heap.get(maior))) {
            maior = direita;
        }
        if (maior != index) {
            Paciente temp = heap.get(index);
            heap.set(index, heap.get(maior));
            heap.set(maior, temp);
            heapifyDown(maior);
        }
    }

    public Paciente peek() {
        return isEmpty() ? null : heap.get(0);
    }

    public boolean isEmpty() { return heap.isEmpty(); }
    public int getTamanho() { return heap.size(); }

    public void listarTodos() {
        if (isEmpty()) {
            System.out.println("Fila prioritaria vazia.");
            return;
        }
        for (int i = 0; i < heap.size(); i++) {
            System.out.println((i + 1) + ". " + heap.get(i));
        }
    }
}

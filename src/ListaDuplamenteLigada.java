public class ListaDuplamenteLigada {
    private NoListaDuplamenteLigada inicio;
    private NoListaDuplamenteLigada fim;
    private int tamanho;

    public ListaDuplamenteLigada() {
        this.inicio = null;
        this.fim = null;
        this.tamanho = 0;
    }

    public void inserir(Paciente paciente) {
        NoListaDuplamenteLigada novo = new NoListaDuplamenteLigada();
        novo.setDado(paciente);
        novo.setProximo(null);
        novo.setAnterior(fim);
        if (isEmpty()) {
            inicio = novo;
            fim = novo;
        } else {
            fim.setProximo(novo);
            fim = novo;
        }
        tamanho++;
    }

    public Paciente remover(int id) {
        NoListaDuplamenteLigada atual = inicio;
        while (atual != null) {
            if (atual.getDado().getId() == id) {
                if (atual == inicio) {
                    inicio = atual.getProximo();
                    if (inicio != null) inicio.setAnterior(null);
                } else if (atual == fim) {
                    fim = atual.getAnterior();
                    if (fim != null) fim.setProximo(null);
                } else {
                    atual.getAnterior().setProximo(atual.getProximo());
                    atual.getProximo().setAnterior(atual.getAnterior());
                }
                tamanho--;
                return atual.getDado();
            }
            atual = atual.getProximo();
        }
        return null;
    }

    public Paciente buscar(int id) {
        NoListaDuplamenteLigada atual = inicio;
        while (atual != null) {
            if (atual.getDado().getId() == id) return atual.getDado();
            atual = atual.getProximo();
        }
        return null;
    }

    public boolean isEmpty() { return inicio == null; }
    public int getTamanho() { return tamanho; }

    public void listarTodos() {
        if (isEmpty()) {
            System.out.println("Lista vazia.");
            return;
        }
        NoListaDuplamenteLigada atual = inicio;
        int pos = 1;
        while (atual != null) {
            System.out.println(pos++ + ". " + atual.getDado());
            atual = atual.getProximo();
        }
    }
}

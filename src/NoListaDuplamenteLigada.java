public class NoListaDuplamenteLigada {
    private Paciente dado;
    private NoListaDuplamenteLigada proximo;
    private NoListaDuplamenteLigada anterior;

    public Paciente getDado() { return dado; }
    public void setDado(Paciente dado) { this.dado = dado; }
    public NoListaDuplamenteLigada getProximo() { return proximo; }
    public void setProximo(NoListaDuplamenteLigada proximo) { this.proximo = proximo; }
    public NoListaDuplamenteLigada getAnterior() { return anterior; }
    public void setAnterior(NoListaDuplamenteLigada anterior) { this.anterior = anterior; }
}

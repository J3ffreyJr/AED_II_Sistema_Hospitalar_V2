public class EntradaHash {
    private int chave;
    private Paciente valor;
    private EntradaHash proximo;

    public EntradaHash(int chave, Paciente valor) {
        this.chave = chave;
        this.valor = valor;
        this.proximo = null;
    }

    public int getChave() { return chave; }
    public void setChave(int chave) { this.chave = chave; }
    public Paciente getValor() { return valor; }
    public void setValor(Paciente valor) { this.valor = valor; }
    public EntradaHash getProximo() { return proximo; }
    public void setProximo(EntradaHash proximo) { this.proximo = proximo; }
}

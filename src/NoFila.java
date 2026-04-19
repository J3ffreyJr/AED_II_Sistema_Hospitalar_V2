public class NoFila {
    private Paciente paciente;
    private NoFila proximo;

    public NoFila(Paciente paciente) {
        this.paciente = paciente;
        this.proximo = null;
    }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    public NoFila getProximo() { return proximo; }
    public void setProximo(NoFila proximo) { this.proximo = proximo; }
}

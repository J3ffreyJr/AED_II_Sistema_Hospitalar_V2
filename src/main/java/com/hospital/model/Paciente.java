package com.hospital.model;

public class Paciente {
    private int id;
    private String nome;
    private int idade;
    private String bi;
    private String telefone;
    private String endereco;
    private String prioridade;
    private boolean removido;

    public boolean isRemovido() {
        return removido;
    }

    public void setRemovido(boolean removido) {
        this.removido = removido;
    }

    public Paciente() {
    }

    public Paciente(int id, String nome, int idade, String bi, String telefone, String endereco, String prioridade) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.bi = bi;
        this.telefone = telefone;
        this.endereco = endereco;
        this.prioridade = prioridade;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getIdade() {
        return idade;
    }

    public String getBi() {
        return bi;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getPrioridade() {
        return prioridade;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public void setBi(String bi) {
        this.bi = bi;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }

    @Override
    public String toString() {
        return "Paciente{id=" + id + ", nome='" + nome + '\'' + ", idade=" + idade + ", bi='" + bi + '\''
                + ", telefone='" + telefone + '\'' + ", endereco='" + endereco + '\'' + ", prioridade='" + prioridade
                + '\'' + '}';
    }
}

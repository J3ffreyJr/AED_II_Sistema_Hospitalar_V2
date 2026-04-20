package com.hospital.dto;

public class EstatisticasResponse {
    private int filaNormalSize;
    private int filaPrioritariaSize;
    private int totalAtendidos;
    private int operacoesUndo;

    public EstatisticasResponse(int filaNormalSize, int filaPrioritariaSize, int totalAtendidos, int operacoesUndo) {
        this.filaNormalSize = filaNormalSize;
        this.filaPrioritariaSize = filaPrioritariaSize;
        this.totalAtendidos = totalAtendidos;
        this.operacoesUndo = operacoesUndo;
    }

    public int getFilaNormalSize() { return filaNormalSize; }
    public int getFilaPrioritariaSize() { return filaPrioritariaSize; }
    public int getTotalAtendidos() { return totalAtendidos; }
    public int getOperacoesUndo() { return operacoesUndo; }
}

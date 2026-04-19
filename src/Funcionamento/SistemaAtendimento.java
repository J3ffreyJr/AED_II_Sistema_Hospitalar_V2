package Funcionamento;

import Entidades.Paciente;
import Estrutura_d_Dados.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SistemaAtendimento {
    private Fila filaNormal;
    private HeapPrioridade filaPrioritaria;
    private TabelaHash tabela;
    private ListaDuplamenteLigada historico;
    private Pilha pilhaDesfazer;
    private int contadorId;

    public SistemaAtendimento() {
        this.filaNormal = new Fila();
        this.filaPrioritaria = new HeapPrioridade();
        this.tabela = new TabelaHash();
        this.historico = new ListaDuplamenteLigada();
        this.pilhaDesfazer = new Pilha();
        this.contadorId = 1;
    }

    public Paciente criarPaciente(String nome, int idade, String bi, String telefone, String endereco, String prioridade) {
        return new Paciente(contadorId++, nome, idade, bi, telefone, endereco, prioridade);

    }

    public void inserirPaciente(Paciente p) {
        tabela.inserir(p);

        if (p.getPrioridade().equalsIgnoreCase("prioritario") || p.getIdade() >= 60) {
            filaPrioritaria.inserir(p);
            System.out.println("Paciente " + p.getNome() + " inserido na fila prioritaria.");
        } else {
            filaNormal.enqueue(p);
            System.out.println("Paciente " + p.getNome() + " inserido na fila normal.");
        }
    }

    public Paciente atenderProximoPaciente() {
        Paciente paciente = null;

        if (!filaPrioritaria.isEmpty()) {
            paciente = filaPrioritaria.remover();
            historico.inserir(paciente);
            pilhaDesfazer.push(paciente);
            System.out.println("ATENDIDO (Prioritario): " + paciente);
            System.out.println("Data/Hora: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        } else if (!filaNormal.isEmpty()) {
            paciente = filaNormal.dequeue();
            historico.inserir(paciente);
            pilhaDesfazer.push(paciente);
            System.out.println("ATENDIDO (Normal): " + paciente);
            System.out.println("Data/Hora: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        } else {
            System.out.println("Nao ha pacientes na fila.");
            return null;
        }

        if (paciente != null) {
            tabela.remover(paciente.getId());
        }

        return paciente;
    }

    public Paciente buscarPaciente(int id) {
        Paciente p = tabela.buscar(id);
        if (p != null) {
            System.out.println("Paciente encontrado: " + p);
        } else {
            System.out.println("Paciente com ID " + id + " nao encontrado.");
        }
        return p;
    }

    public void removerPaciente(int id) {
        Paciente p = tabela.buscar(id);
        if (p != null) {
            tabela.remover(id);
            System.out.println("Paciente " + p.getNome() + " removido do sistema.");
        } else {
            System.out.println("Paciente com ID " + id + " nao encontrado.");
        }
    }

    public void desfazerUltimaOperacao() {
        if (pilhaDesfazer.isEmpty()) {
            System.out.println("Nenhuma operacao para desfazer.");
            return;
        }

        Paciente ultimo = pilhaDesfazer.pop();
        historico.remover(ultimo.getId());

        System.out.println("DESFEITO: Entidades.Atendimento de " + ultimo.getNome() + " foi desfeito.");

        if (ultimo.getPrioridade().equalsIgnoreCase("prioritario") || ultimo.getIdade() >= 60) {
            filaPrioritaria.inserir(ultimo);
        } else {
            filaNormal.enqueue(ultimo);
        }

        tabela.inserir(ultimo);
    }

    public void listarFilaNormal() {
        System.out.println("\n=== FILA NORMAL ===");
        filaNormal.listarTodos();
    }

    public void listarFilaPrioritaria() {
        System.out.println("\n=== FILA PRIORITARIA ===");
        filaPrioritaria.listarTodos();
    }

    public void listarHistorico() {
        System.out.println("\n=== HISTORICO DE ATENDIMENTOS ===");
        historico.listarTodos();
    }

    public void estatisticas() {
        System.out.println("\n=== ESTATISTICAS ===");
        System.out.println("Pacientes na fila normal: " + filaNormal.getTamanho());
        System.out.println("Pacientes na fila prioritaria: " + filaPrioritaria.getTamanho());
        System.out.println("Total de pacientes atendidos: " + historico.getTamanho());
        System.out.println("Operacoes pendentes de undo: " + pilhaDesfazer.getTamanho());
    }

    public int getProximoId() {
        return contadorId;
    }
}

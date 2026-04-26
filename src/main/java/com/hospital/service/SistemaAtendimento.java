package com.hospital.service;

import com.hospital.dto.EstatisticasResponse;
import com.hospital.dto.PacienteRequest;
import com.hospital.model.Paciente;
import com.hospital.service.estruturas.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

@Service
public class SistemaAtendimento {
    private Fila filaNormal;
    private HeapPrioridade filaPrioritaria;
    private TabelaHash tabela;
    private ListaDuplamenteLigada historico;
    private Pilha<RegistroOperacao> pilhaDesfazer;
    private Pilha<RegistroOperacao> pilhaRefazer; // Pilha auxiliar para refazer operações
    private java.util.Queue<RegistroOperacao> filaOperacoesDesfeitas; // Fila para registrar operações desfeitas
    private int contadorId;

    @Autowired
    private PersistenciaService persistenciaService;

    public SistemaAtendimento() {
        this.filaNormal = new Fila();
        this.filaPrioritaria = new HeapPrioridade();
        this.tabela = new TabelaHash();
        this.historico = new ListaDuplamenteLigada();
        this.pilhaDesfazer = new Pilha<RegistroOperacao>();
        this.pilhaRefazer = new Pilha<>(); // Inicializa pilha auxiliar
        this.filaOperacoesDesfeitas = new java.util.LinkedList<>(); // Inicializa fila de operações desfeitas
        this.contadorId = 1;
    }

    /**
     * Carrega os dados persistidos ao inicializar o serviço
     */
    @PostConstruct
    public void carregarDadosPersistidos() {
        if (persistenciaService == null) {
            System.out.println("PersistenciaService não foi injetado");
            return;
        }

        PersistenciaService.PacientesCarregados dados = persistenciaService.carregarPacientes();

        // Carrega pacientes da fila normal
        if (dados.filaNormal != null) {
            for (Paciente p : dados.filaNormal) {
                filaNormal.enqueue(p);
                tabela.inserir(p);
            }
        }

        // Carrega pacientes da fila prioritária
        if (dados.filaPrioritaria != null) {
            for (Paciente p : dados.filaPrioritaria) {
                filaPrioritaria.inserir(p);
                tabela.inserir(p);
            }
        }

        // Carrega histórico
        if (dados.historico != null) {
            for (Paciente p : dados.historico) {
                historico.inserir(p);
            }
        }

        this.contadorId = dados.proximoId;
        System.out.println("Dados persistidos carregados com sucesso!");
    }

    /**
     * Salva os dados persistidos
     */
    private void salvarDados() {
        if (persistenciaService != null) {
            persistenciaService.salvarPacientes(
                    filaNormal.listarTodos(),
                    filaPrioritaria.listarTodos(),
                    historico.listarTodos(),
                    contadorId
            );
        }
    }

    public Paciente criarPaciente(PacienteRequest request) {
        return new Paciente(contadorId++, request.getNome(), request.getIdade(),
                request.getBi(), request.getTelefone(), request.getEndereco(), request.getPrioridade());
    }

    public void inserirPaciente(Paciente p) {
        tabela.inserir(p);

        if ((p.getPrioridade() != null && p.getPrioridade().equalsIgnoreCase("prioritario")) || p.getIdade() >= 60) {
            filaPrioritaria.inserir(p);
        } else {
            filaNormal.enqueue(p);
        }

        salvarDados();
    }

    private boolean isPrioritario(Paciente p) {
        return (p.getPrioridade() != null && p.getPrioridade().equalsIgnoreCase("prioritario")) || p.getIdade() >= 60;
    }

    public Paciente atenderProximoPaciente() {
        Paciente paciente = null;
        String filaOrigem = null;
        int posicaoOriginal = -1;

        if (!filaPrioritaria.isEmpty()) {
            paciente = filaPrioritaria.remover();
            filaOrigem = "prioritaria";
            posicaoOriginal = 0; // Heap prioritário
        } else if (!filaNormal.isEmpty()) {
            posicaoOriginal = filaNormal.listarTodos().indexOf(filaNormal.peek());
            paciente = filaNormal.dequeue();
            filaOrigem = "normal";
        }

        if (paciente != null) {
            historico.inserir(paciente);
            RegistroOperacao registro = new RegistroOperacao(paciente, "atendido", filaOrigem);
            registro.setPosicaoOriginal(posicaoOriginal);
            registro.setCriterioOrdenacao("entrada");
            pilhaDesfazer.push(registro);
            tabela.remover(paciente.getId());
            salvarDados();
        }

        return paciente;
    }

    public Paciente buscarPaciente(int id) {
        return tabela.buscar(id);
    }

    public boolean removerPaciente(int id) {
        Paciente paciente = null;
        int posicaoOriginal = -1;
        String filaPosicao = "";

        // Tenta remover da fila normal
        List<Paciente> filaNormalLista = filaNormal.listarTodos();
        for (int i = 0; i < filaNormalLista.size(); i++) {
            if (filaNormalLista.get(i).getId() == id) {
                posicaoOriginal = i;
                filaPosicao = "normal";
                break;
            }
        }

        paciente = filaNormal.removerPorId(id);

        // Se não encontrou na fila normal, tenta na prioritária
        if (paciente == null) {
            java.util.List<Paciente> filaPrioritariaLista = filaPrioritaria.listarTodos();
            for (int i = 0; i < filaPrioritariaLista.size(); i++) {
                if (filaPrioritariaLista.get(i).getId() == id) {
                    posicaoOriginal = i;
                    filaPosicao = "prioritaria";
                    break;
                }
            }
            paciente = filaPrioritaria.removerPorId(id);
        }

        if (paciente != null) {
            String filaOrigem = isPrioritario(paciente) ? "prioritaria" : "normal";
            paciente.setRemovido(true);
            historico.inserir(paciente);
            RegistroOperacao registro = new RegistroOperacao(paciente, "removido", filaOrigem);
            registro.setPosicaoOriginal(posicaoOriginal);
            registro.setCriterioOrdenacao("entrada");
            pilhaDesfazer.push(registro);
            tabela.remover(id);
            salvarDados();
            return true;
        }
        return false;
    }

    public boolean desfazerUltimaOperacao() {
        if (pilhaDesfazer.isEmpty()) {
            return false;
        }

        RegistroOperacao registro = pilhaDesfazer.pop();

        // Inserir na pilha auxiliar (refazer)
        pilhaRefazer.push(registro);

        // Inserir na fila de operações desfeitas
        filaOperacoesDesfeitas.offer(registro);

        Paciente paciente = registro.getPaciente();

        // Remove do histórico
        historico.remover(paciente.getId());

        // Volta para a fila na mesma posição com ordenação
        if (isPrioritario(paciente)) {
            filaPrioritaria.inserirMantendoPosicao(paciente, registro.getPosicaoOriginal());
        } else {
            filaNormal.inserirNaPosicao(registro.getPosicaoOriginal(), paciente);
        }

        paciente.setRemovido(false);
        tabela.inserir(paciente);
        salvarDados();
        return true;
    }

    public boolean refazerUltimaOperacao() {
        if (pilhaRefazer.isEmpty()) {
            return false;
        }

        RegistroOperacao registro = pilhaRefazer.pop();

        // Reinsere na pilha de desfazer
        pilhaDesfazer.push(registro);

        Paciente paciente = registro.getPaciente();

        // Remove da fila
        if (isPrioritario(paciente)) {
            filaPrioritaria.removerPorId(paciente.getId());
        } else {
            filaNormal.removerPorId(paciente.getId());
        }

        // Volta para o histórico
        historico.inserir(paciente);

        paciente.setRemovido(registro.isRemovido());
        tabela.remover(paciente.getId());
        salvarDados();
        return true;
    }

    public List<Paciente> listarFilaNormal() {
        return filaNormal.listarTodos();
    }

    public List<Paciente> listarFilaPrioritaria() {
        return filaPrioritaria.listarTodos();
    }

    public List<Paciente> listarHistorico() {
        return historico.listarTodos();
    }

    public void limparHistorico() {
        historico.limpar();
        salvarDados();
    }

    public List<Paciente> listarHistoricoOrdenado(String criterio) {
        List<Paciente> lista = historico.listarTodos();
        switch (criterio) {
            case "nome":
                lista.sort((a, b) -> a.getNome().compareToIgnoreCase(b.getNome()));
                break;
            case "id":
                lista.sort((a, b) -> Integer.compare(a.getId(), b.getId()));
                break;
            case "idade":
                lista.sort((a, b) -> Integer.compare(a.getIdade(), b.getIdade()));
                break;
            case "entrada":
            default:
                break;
        }
        return lista;
    }

    public EstatisticasResponse estatisticas() {
        return new EstatisticasResponse(
                filaNormal.getTamanho(),
                filaPrioritaria.getTamanho(),
                historico.getTamanho(),
                pilhaDesfazer.getTamanho()
        );
    }

    public int getProximoId() {
        return contadorId;
    }

    // Métodos para acessar pilha de refazer e fila de operações desfeitas
    public Pilha<RegistroOperacao> getPilhaRefazer() {
        return pilhaRefazer;
    }

    public Queue<RegistroOperacao> getFilaOperacoesDesfeitas() {
        return filaOperacoesDesfeitas;
    }

    public int getTamanhoFilaOperacoesDesfeitas() {
        return filaOperacoesDesfeitas.size();
    }

    public List<RegistroOperacao> listarOperacoesDesfeitas() {
        return new ArrayList<>(filaOperacoesDesfeitas);
    }
}

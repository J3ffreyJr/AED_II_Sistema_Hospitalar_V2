package com.hospital.service;

import com.hospital.model.Paciente;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class PersistenciaService {
    private static final String DADOS_DIR = "dados";
    private static final String ARQUIVO_PACIENTES = "dados/pacientes.txt";

    public PersistenciaService() {
        // Cria o diretório de dados se não existir
        try {
            Files.createDirectories(Paths.get(DADOS_DIR));
        } catch (IOException e) {
            System.err.println("Erro ao criar diretório de dados: " + e.getMessage());
        }
    }

    /**
     * Salva a lista de pacientes em um arquivo TXT
     */
    public void salvarPacientes(List<Paciente> filaNormal, List<Paciente> filaPrioritaria,
            List<Paciente> historico, int proximoId) {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(ARQUIVO_PACIENTES), StandardCharsets.UTF_8))) {

            // Cabeçalho com metadados
            writer.write("=== SISTEMA HOSPITALAR - DADOS PERSISTIDOS ===\n");
            writer.write("PROXIMO_ID=" + proximoId + "\n");
            writer.write("\n");

            // Salva fila normal
            writer.write("--- FILA NORMAL ---\n");
            if (filaNormal != null && !filaNormal.isEmpty()) {
                for (Paciente p : filaNormal) {
                    writer.write(formatarPaciente(p));
                }
            } else {
                writer.write("[Vazia]\n");
            }
            writer.write("\n");

            // Salva fila prioritária
            writer.write("--- FILA PRIORITARIA ---\n");
            if (filaPrioritaria != null && !filaPrioritaria.isEmpty()) {
                for (Paciente p : filaPrioritaria) {
                    writer.write(formatarPaciente(p));
                }
            } else {
                writer.write("[Vazia]\n");
            }
            writer.write("\n");

            // Salva histórico
            writer.write("--- HISTORICO ---\n");
            if (historico != null && !historico.isEmpty()) {
                for (Paciente p : historico) {
                    writer.write(formatarPaciente(p));
                }
            } else {
                writer.write("[Vazio]\n");
            }

            System.out.println("Dados salvos com sucesso em: " + ARQUIVO_PACIENTES);
        } catch (IOException e) {
            System.err.println("Erro ao salvar pacientes: " + e.getMessage());
        }
    }

    /**
     * Formata um paciente para salvar em arquivo
     */
    private String formatarPaciente(Paciente p) {
        return String.format("ID=%d|NOME=%s|IDADE=%d|BI=%s|TELEFONE=%s|ENDERECO=%s|PRIORIDADE=%s|REMOVIDO=%s\n",
                p.getId(), p.getNome(), p.getIdade(), p.getBi(), p.getTelefone(),
                p.getEndereco(), p.getPrioridade(), p.isRemovido());
    }

    /**
     * Carrega os pacientes do arquivo TXT
     */
    public PacientesCarregados carregarPacientes() {
        List<Paciente> filaNormal = new ArrayList<>();
        List<Paciente> filaPrioritaria = new ArrayList<>();
        List<Paciente> historico = new ArrayList<>();
        int proximoId = 1;

        Path arquivoPath = Paths.get(ARQUIVO_PACIENTES);
        if (!Files.exists(arquivoPath)) {
            System.out.println("Arquivo de dados não encontrado. Iniciando com dados vazios.");
            return new PacientesCarregados(filaNormal, filaPrioritaria, historico, proximoId);
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(ARQUIVO_PACIENTES), StandardCharsets.UTF_8))) {

            String linha;
            String secaoAtual = null;

            while ((linha = reader.readLine()) != null) {
                linha = linha.trim();

                // Ignora linhas vazias
                if (linha.isEmpty()) {
                    continue;
                }

                // Verifica linhas de metadados
                if (linha.startsWith("PROXIMO_ID=")) {
                    proximoId = Integer.parseInt(linha.substring("PROXIMO_ID=".length()));
                    continue;
                }

                // Detecta seções
                if (linha.equals("--- FILA NORMAL ---")) {
                    secaoAtual = "NORMAL";
                    continue;
                } else if (linha.equals("--- FILA PRIORITARIA ---")) {
                    secaoAtual = "PRIORITARIA";
                    continue;
                } else if (linha.equals("--- HISTORICO ---")) {
                    secaoAtual = "HISTORICO";
                    continue;
                }

                // Ignora cabeçalho e linhas vazias
                if (linha.startsWith("===") || linha.startsWith("---") || linha.equals("[Vazia]") ||
                        linha.equals("[Vazio]")) {
                    continue;
                }

                // Parse do paciente
                Paciente p = parsearPaciente(linha);
                if (p != null && secaoAtual != null) {
                    switch (secaoAtual) {
                        case "NORMAL":
                            filaNormal.add(p);
                            break;
                        case "PRIORITARIA":
                            filaPrioritaria.add(p);
                            break;
                        case "HISTORICO":
                            historico.add(p);
                            break;
                    }
                }
            }

            System.out.println("Dados carregados com sucesso: " +
                    (filaNormal.size() + filaPrioritaria.size()) + " pacientes em filas, " +
                    historico.size() + " no histórico");

        } catch (IOException e) {
            System.err.println("Erro ao carregar pacientes: " + e.getMessage());
        }

        return new PacientesCarregados(filaNormal, filaPrioritaria, historico, proximoId);
    }

    /**
     * Parse de uma linha de paciente
     */
    private Paciente parsearPaciente(String linha) {
        try {
            String[] partes = linha.split("\\|");
            int id = -1;
            String nome = "";
            int idade = 0;
            String bi = "";
            String telefone = "";
            String endereco = "";
            String prioridade = "";
            boolean removido = false;

            for (String parte : partes) {
                String[] keyValue = parte.split("=", 2);
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim();
                    String value = keyValue[1].trim();

                    switch (key) {
                        case "ID":
                            id = Integer.parseInt(value);
                            break;
                        case "NOME":
                            nome = value;
                            break;
                        case "IDADE":
                            idade = Integer.parseInt(value);
                            break;
                        case "BI":
                            bi = value;
                            break;
                        case "TELEFONE":
                            telefone = value;
                            break;
                        case "ENDERECO":
                            endereco = value;
                            break;
                        case "PRIORIDADE":
                            prioridade = value;
                            break;
                        case "REMOVIDO":
                            removido = Boolean.parseBoolean(value);
                            break;
                    }
                }
            }

            if (id != -1) {
                Paciente p = new Paciente(id, nome, idade, bi, telefone, endereco, prioridade);
                p.setRemovido(removido);
                return p;
            }
        } catch (Exception e) {
            System.err.println("Erro ao parsear paciente: " + linha + " - " + e.getMessage());
        }
        return null;
    }

    /**
     * Classe para retornar os dados carregados
     */
    public static class PacientesCarregados {
        public List<Paciente> filaNormal;
        public List<Paciente> filaPrioritaria;
        public List<Paciente> historico;
        public int proximoId;

        public PacientesCarregados(List<Paciente> filaNormal, List<Paciente> filaPrioritaria,
                List<Paciente> historico, int proximoId) {
            this.filaNormal = filaNormal;
            this.filaPrioritaria = filaPrioritaria;
            this.historico = historico;
            this.proximoId = proximoId;
        }
    }
}

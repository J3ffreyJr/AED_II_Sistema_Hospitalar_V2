package Funcionamento;

import Entidades.Paciente;

import java.util.Scanner;

public class Menu {
    private Scanner scanner;
    private SistemaAtendimento sistema;

    public Menu() {
        this.scanner = new Scanner(System.in);
        this.sistema = new SistemaAtendimento();
    }

    public void iniciar() {
        int opcao;
        do {
            System.out.println("\n========== SISTEMA HOSPITALAR ==========");
            System.out.println("1. Inserir paciente");
            System.out.println("2. Atender proximo paciente");
            System.out.println("3. Listar fila normal");
            System.out.println("4. Listar fila prioritaria");
            System.out.println("5. Buscar paciente por ID");
            System.out.println("6. Remover paciente por ID");
            System.out.println("7. Ver historico de atendimentos");
            System.out.println("8. Desfazer ultima operacao");
            System.out.println("9. Ver estatisticas");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opcao: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    inserirPaciente();
                    break;
                case 2:
                    atenderPaciente();
                    break;
                case 3:
                    sistema.listarFilaNormal();
                    break;
                case 4:
                    sistema.listarFilaPrioritaria();
                    break;
                case 5:
                    buscarPaciente();
                    break;
                case 6:
                    removerPaciente();
                    break;
                case 7:
                    sistema.listarHistorico();
                    break;
                case 8:
                    sistema.desfazerUltimaOperacao();
                    break;
                case 9:
                    sistema.estatisticas();
                    break;
                case 0:
                    System.out.println("Ate breve!");
                    break;
                default:
                    System.out.println("Opcao invalida.");
            }
        } while (opcao != 0);
    }

    private void inserirPaciente() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Idade: ");
        int idade = scanner.nextInt();
        scanner.nextLine();

        System.out.print("BI: ");
        String bi = scanner.nextLine();

        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();

        System.out.print("Endereco: ");
        String endereco = scanner.nextLine();

        System.out.print("Prioridade (normal/prioritario): ");
        String prioridade = scanner.nextLine();

        Paciente p = sistema.criarPaciente(nome, idade, bi, telefone, endereco, prioridade);
        sistema.inserirPaciente(p);
    }

    private void atenderPaciente() {
        sistema.atenderProximoPaciente();
    }

    private void buscarPaciente() {
        System.out.print("Digite o ID do paciente: ");
        int id = scanner.nextInt();
        sistema.buscarPaciente(id);
    }

    private void removerPaciente() {
        System.out.print("Digite o ID do paciente: ");
        int id = scanner.nextInt();
        sistema.removerPaciente(id);
    }
}

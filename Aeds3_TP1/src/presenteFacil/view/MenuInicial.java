package src.presenteFacil.view;

import src.presenteFacil.controller.ControladorUsuario;
import src.presenteFacil.model.Usuario;
import src.presenteFacil.utils.ClearConsole;

import java.util.Scanner;

public class MenuInicial {

    private ControladorUsuario controlador;
    private Usuario usuarioLogado;

    public MenuInicial(ControladorUsuario controlador) {
        this.controlador = controlador;
    }


    public void exibir(Scanner scanner) throws Exception{

        while (true) {

            System.out.println("\n");
            System.out.println("-------- PresenteFácil 2.0 --------");
            System.out.println("-----------------------------------");
            System.out.println("\n(1) Login");
            System.out.println("(2) Novo usuário");
            System.out.println("(3) Reativar usuário");

            System.out.println("\n(S) Sair");

            System.out.print("\nOpção: ");
                       
            String entrada = scanner.nextLine().toUpperCase();
            ClearConsole.clearScreen();

            if (entrada.equals("S")) {
                System.out.println("\n-- Você foi desconectado. --\n");
                return;
            }

            try {
                int opcao = Integer.parseInt(entrada);

                switch (opcao) {

                    case 1:
                        usuarioLogado = controlador.loginUsuario(scanner);

                        ClearConsole.clearScreen();

                        if (usuarioLogado != null) {
                            System.out.println("\n-- Login efetuado com sucesso! Bem-vindo(a), " 
                                                + usuarioLogado.getNome() + ". --\n");

                            // Encaminha para o menu principal
                            MenuUsuario menuPrincipal = new MenuUsuario(controlador, usuarioLogado);
                            menuPrincipal.exibir(scanner);

                        } else {
                            System.out.println("\n-- E-mail ou senha inválidos, ou conta desativada. Tente novamente. --\n");
                        }
                        break;

                    case 2:
                        controlador.criarNovoUsuario(scanner);
                        break;
                    case 3:
                        controlador.reativarUsuario(scanner);
                        break;
                    default:
                        System.out.println("\nOpção inválida. Tente novamente.\n");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("\nEntrada inválida. Por favor, digite um número ou 'S' para sair.\n");
            }
        }
    }
}

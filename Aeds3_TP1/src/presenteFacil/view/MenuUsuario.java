package src.presenteFacil.view;

import src.presenteFacil.controller.ControladorListaDePresentes;
import src.presenteFacil.controller.ControladorProduto;
import src.presenteFacil.controller.ControladorUsuario;
import src.presenteFacil.model.Usuario;
import src.presenteFacil.utils.ClearConsole;

import java.util.Scanner;

public class MenuUsuario {

    private ControladorUsuario userController;
    private ControladorListaDePresentes giftListController;
    private Usuario usuarioLogado;

    public MenuUsuario(ControladorUsuario userController, Usuario usuarioLogado) throws Exception {
        this.userController = userController;
        this.usuarioLogado = usuarioLogado;
        this.giftListController = new ControladorListaDePresentes();
    }

    public void exibir(Scanner scanner) throws Exception {

        String entrada;

        while (true) {
            System.out.println("------- Menu Principal -------");
            System.out.println("------------------------------");
            System.out.println("\n> Início\n");
            System.out.println("(1) Meus dados................");
            System.out.println("(2) Minhas listas.............");
            System.out.println("(3) Produtos..................");
            System.out.println("(4) Buscar lista..............");
            System.out.println("(5) Desativar minha conta.....");
            System.out.println("(6) Excluir minha conta.......");

            System.out.println("\n(S) Sair......................");

            System.out.print("\nOpção: ");

            entrada = scanner.nextLine().toUpperCase();
            ClearConsole.clearScreen();

            if (entrada.equals("S")) {
                userController.logout();
                System.out.println("\n-- Você foi desconectado. --\n");
                return;
            }

            try {
                int opcao = Integer.parseInt(entrada);

                switch (opcao) {
                    case 1:
                        userController.exibirMeusDados(scanner);
                        break;

                    case 2:
                        MenuMinhasListas menuListas = new MenuMinhasListas(giftListController, usuarioLogado);
                        menuListas.exibirMenu(scanner);
                        break;

                    case 3:
                        try {
                            ControladorProduto produtoController = new ControladorProduto();
                            MenuProdutos menuProdutos = new MenuProdutos(produtoController, usuarioLogado);
                            menuProdutos.exibirMenu(scanner);
                        } catch (Exception e) {
                            System.err.println("\nErro ao inicializar o módulo de produtos: " + e.getMessage() + "\n");
                        }
                        break;

                    case 4:
                        giftListController.buscarListaPorCodigoComProdutos(scanner, usuarioLogado);
                        break;

                    case 5:
                        boolean foiDesativado = userController.desativarPropriaConta(scanner);
                        if (foiDesativado) return; // Se a conta foi desativada, sai do menu
                        break;

                    case 6:
                        boolean foiExcluido = userController.excluirPropriaConta(scanner);
                        if (foiExcluido) return; // Se a conta foi excluída, sai do menu
                        break;

                    default:
                        System.out.println("\nOpção inválida. Tente novamente.\n");
                        break;
                }

            } catch (NumberFormatException e) {
                System.out.println("\nEntrada inválida. Digite um número ou 'S'.\n");
            }
        }
    }
}

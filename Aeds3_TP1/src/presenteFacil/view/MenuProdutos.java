package src.presenteFacil.view;

import src.presenteFacil.controller.ControladorProduto;
import src.presenteFacil.utils.ClearConsole;
import src.presenteFacil.model.*;

import java.util.Scanner;

public class MenuProdutos {

    private ControladorProduto produtoController;
    private Usuario usuarioLogado;

    public MenuProdutos(ControladorProduto produtoController, Usuario usuarioLogado) {
        this.produtoController = produtoController;
        this.usuarioLogado = usuarioLogado;
    }

    public void exibirMenu(Scanner scanner) {
        String opcao;
        boolean continua = true;

        while (continua) {
            System.out.println("-------- PresenteFácil 3.0 --------");
            System.out.println("-----------------------------------");
            System.out.println("> Início > Produtos\n");
            System.out.println("(1) Buscar produtos por GTIN");
            System.out.println("(2) Buscar produtos por nome");
            System.out.println("(3) Listar todos os produtos");
            System.out.println("(4) Cadastrar um novo produto");
            System.out.println("(5) Reativar produto");
            System.out.println("\n(R) Retornar ao menu anterior");
            System.out.print("\nOpção: ");

            opcao = scanner.nextLine().trim().toUpperCase();
            ClearConsole.clearScreen();

            switch (opcao) {
                case "1":
                    produtoController.buscarProdutoPorGtin(scanner, usuarioLogado);
                    break;
                case "2":
                    produtoController.buscarProdutoPorNome(scanner, usuarioLogado);
                    break;
                case "3":
                    produtoController.listarTodosOsProdutos(scanner, usuarioLogado);
                    break;
                case "4":
                    produtoController.cadastrarNovoProduto(scanner);
                    break;
                case "5":
                    produtoController.reativarProdutoPorGtin(scanner);
                    break;
                case "R":
                    continua = false;
                    break;
                default:
                    System.out.println("\n-- Opção inválida. Tente novamente. --\n");
                    break;
            }
        }
    }
}

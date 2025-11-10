package src.presenteFacil.view;

import java.util.Scanner;
import src.presenteFacil.controller.ControladorListaDePresentes;
import src.presenteFacil.model.*;
import src.presenteFacil.utils.ClearConsole;



public class MenuMinhasListas {

    private ControladorListaDePresentes giftListController;
    private Usuario usuarioLogado;

    public MenuMinhasListas(ControladorListaDePresentes giftListController, Usuario usuarioLogado) {
        this.giftListController = giftListController;
        this.usuarioLogado = usuarioLogado;
    }

    public void exibirMenu(Scanner scanner) throws Exception {
        String opcao;
        boolean continua = true;

        while (continua) {

            Lista[] listas = giftListController.mostrarMinhasListas(usuarioLogado);
             
            System.out.println("(N) Nova Lista"); 
            System.out.println("(R) Retornar ao menu anterior"); 
            System.out.println("(D) Mostrar Listas Desativadas");
            System.out.println("(A) Reativar Lista");
            System.out.println();
            System.out.print("\nOpção: "); 

            opcao = scanner.nextLine().trim().toUpperCase(); 
            
            ClearConsole.clearScreen();

            switch (opcao) {
                case "N":
                    giftListController.criarNovaLista(scanner, usuarioLogado);
                    break;
                case "R":
                    System.out.println("\n-- Retornando ao menu anterior. --\n");
                    continua = false;
                    break;
                case "A":
                    giftListController.reativarLista(scanner);
                    break;
                case "D":
                    menuExibirListaDesativadas(scanner);
                    break;
                default:
                    if(IsNumber.isNumber(opcao)){ 
                        int indice = Integer.parseInt(opcao); 

                        ClearConsole.clearScreen();

                        System.out.println("\n[Selecionou a lista " + indice + "]\n"); 

                        if(listas != null && indice > 0 && indice <= listas.length){ 
                            giftListController.mostrarLista(scanner, listas[indice - 1], usuarioLogado); 
                        } else { 
                            System.out.println("\nOpção Inválida. Tente novamente.\n");
                        } 
                    }else{
                        System.out.println("\nOpção Inválida. Tente novamente.\n");
                    }
            }
        }
    }

    public void menuExibirListaDesativadas(Scanner scanner) throws Exception{
        String opcao;
        boolean continua = true;

        while (continua) {
            System.out.println("-------- PresenteFácil 3.0 --------"); 
            System.out.println("-----------------------------------"); 
            System.out.println("> Início > Minhas Listas\n"); 

            giftListController.mostrarListasDesativadas(usuarioLogado);
             
            System.out.println("(R) Retornar ao menu anterior");
            System.out.println("(A) Reativar Lista");
            System.out.println();
            System.out.print("\nOpção: "); 

            opcao = scanner.nextLine().trim().toUpperCase(); 

            switch (opcao) {
                case "R":
                    System.out.println("\n-- Retornando ao menu anterior. --\n");
                    continua = false;
                    break;

                case "A":
                    giftListController.reativarLista(scanner);
                    break;
                default:
                    System.out.println("\nOpição Invalida. Tente novamente.\n");
            }
        }
    }
}


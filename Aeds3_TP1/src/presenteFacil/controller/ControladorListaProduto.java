package src.presenteFacil.controller;

import src.presenteFacil.model.*;
import src.presenteFacil.utils.ClearConsole;
import src.presenteFacil.controller.*;
import src.presenteFacil.view.MenuMinhasListas;
import src.presenteFacil.aeds3.*;

import java.util.*;

public class ControladorListaProduto {
    private ArquivoListaProduto arqListaProduto; 
    private ArquivoProduto arqProduto;
    private ControladorProduto controladorProduto;
    private Usuario usuarioLogado;

    public ControladorListaProduto() throws Exception{
        this.arqListaProduto = new ArquivoListaProduto();
        this.arqProduto = new ArquivoProduto();
        this.controladorProduto = new ControladorProduto();
        
    }

    public void setUsuario(Usuario usuarioLogado){
        this.usuarioLogado = usuarioLogado;
    }

    public void gerenciarProdutoLista(Scanner scanner, Lista lista, Usuario usuarioLogado) throws Exception{
        setUsuario(usuarioLogado);
        String opcao;
        boolean continua = true;

        try{
            while(continua) {
                System.out.println("-------- PresenteFácil 3.0 --------"); 
                System.out.println("-----------------------------------"); 
                System.out.println("> Início > Minhas Listas > " + lista.getNome() + " > Produtos \n");

                Produto[] produtos = mostrarProdutosPorListaProduto(lista);

                System.out.println();
                System.out.println("(A) Acrescentar produto");
                System.out.println("(R) Retornar ao menu anterior");
                System.out.println();
                System.out.print("\nOpção: ");

                opcao = scanner.nextLine().trim().toUpperCase();

                switch (opcao) {
                    case "R":
                        System.out.println("\n-- Retornando ao menu anterior. --\n");
                        continua = false;
                        break;
                    case "A":
                        acresentarProdutoLista(scanner, lista);
                        continua = false;
                        break;
                    default:
                        if(IsNumber.isNumber(opcao)){ 
                            int indice = Integer.parseInt(opcao); 
                            System.out.println("\n[Selecionou a lista " + indice + "]\n"); 

                            if(produtos != null && indice > 0 && indice <= produtos.length){ 
                                mostrarDetalhesListaProduto(scanner, lista, produtos[indice - 1]); 
                            } else { 
                                System.out.println("\nOpição Invalida. Tente novamente.\n");
                            } 
                        }else{
                            System.out.println("\nOpição Invalida. Tente novamente.\n");
                        }
                }
            }
        } catch (Exception e) {
            System.err.println("\nOcorreu um erro ao Gerenciar os produtos da lista: " + e.getMessage() + "\n");
        }
    }

    public void mostrarDetalhesListaProduto(Scanner scanner, Lista lista, Produto produto) throws Exception{
        try{
            ListaProduto listaProduto = arqListaProduto.readByListaIdAndProdutoId(lista.getId(), produto.getId());
            boolean continua = true;

            while (continua) {
                System.out.println("-------- PresenteFácil 3.0 --------"); 
                System.out.println("-----------------------------------"); 
                System.out.println("> Início > Minhas Listas > " + lista.getNome() + " > Produtos > " + produto.getNome() +"\n");

                System.out.println("\n-------- Detalhes do Produto --------");
                System.out.println(produto.toString());
                System.out.println(listaProduto.toString());
                System.out.println("-------------------------------------\n");
                System.out.println("(1) Alterar a quantidade");
                System.out.println("(2) Alterar as observações");
                System.out.println("(3) Remover o produto desta lista");
                System.out.println("(R) Retornar ao menu anterior");
                System.out.print("\nOpcao: ");

                String opcao = scanner.nextLine().trim().toUpperCase();
                switch (opcao) {
                    case "1":
                        mudarQuantidade(scanner, listaProduto);
                        break;
                    case "2":
                        mudarObservacoes(scanner, listaProduto);
                        break;
                    case "3":
                        boolean result = removerProdutoDaLista(scanner, listaProduto);
                        if(result){
                            continua = false;
                        }
                        break;
                    case "R":
                        return;
                    default:
                        System.out.println("\n-- Opcao invalida. --\n");
                }
            }
        } catch (Exception e) {
            System.err.println("\nErro ao buscar mostrar Detalhes ListaProduto: " + e.getMessage() + "\n");
        }
    }

    public boolean removerProdutoDaLista(Scanner scanner, ListaProduto listaProduto) throws Exception{
        try{
            String opcao = "";
            boolean continua = true;

            while (continua) {
                System.out.print("Deseja deletar esse produto da lista? (S/N): ");
                opcao = scanner.nextLine().trim().toUpperCase();

                if (opcao.equals("S")) {
                    boolean result = arqListaProduto.delete(listaProduto.getId());
                    if(result){
                        System.out.println("\n--- Produto deletado com sucesso! ---\n");
                    }else{
                        System.out.println("\n--- Erro ao deletar o produto! ---\n");
                    }
                    continua = false;
                    return result;
                } else if (opcao.equals("N")) {
                    System.out.println("\n--- Produto não foi deletado da lista. ---\n");
                    return false;
                } else {
                    System.out.println("Entrada inválida. Por favor, digite 'S' para sim ou 'N' para não.");
                }
            }
        } catch (Exception e) {
            System.err.println("\nErro ao remover produto da lista: " + e.getMessage() + "\n");
        }
        return false;
    }

    public void buscarProdutorPorGtin(Scanner scanner, Lista lista) throws Exception {
        try {
            System.out.println("-------- PresenteFácil 3.0 --------"); 
            System.out.println("-----------------------------------"); 
            System.out.println("> Início > Minhas Listas > " + lista.getNome() + " > Produtos > Acrescentar produto > Buscar por GTIN \n");

            System.out.print("Digite o GTIN-13 do produto: ");
            String gtin13 = scanner.nextLine();
            boolean estaNaLista = false;
            Produto[] produtos = arqListaProduto.getProdutosByListaId(lista.getId());

            for(int i = 0; i < produtos.length; i++){
                if(gtin13.equals(produtos[i].getGtin13())){
                    estaNaLista = true;
                    break;
                }
            }

            if(estaNaLista){
                System.out.println("\n--- Projduto já está cadastrado na lista! ---\n");
                return;
            }

            Produto produto = arqProduto.read(gtin13);

            if (produto == null) {
                System.out.println("\n-- Nenhum produto encontrado com este GTIN-13. --\n");
            } else {
                if (!produto.isAtivo()) {
                    System.out.println("\n-- Produto inativo. Nao pode ser adicionado a lista. --\n");
                    return;
                }
                System.out.println("\n-------- Detalhes do Produto --------");
                System.out.println(produto);
                System.out.println("-------------------------------------\n");

                boolean continua = true;
                int quantidade = 1;

                while(continua){
                    System.out.print("Digite a quantidade: ");
                    quantidade = scanner.nextInt();

                    if(quantidade < 1){
                        System.out.println("--- A quantidade não pode ser menor ou igual a zero! ---");
                    }
                    continua = false;
                }
                scanner.nextLine();

                System.out.println();
                System.out.print("Observações (Opcional): ");
                String observacoes = scanner.nextLine();
                System.out.println();

                String opcao = "";
                continua = true;

                while (continua) {
                    System.out.print("Deseja acrescentar esse produto à lista? (S/N): ");
                    opcao = scanner.nextLine().trim().toUpperCase();

                    if (opcao.equals("S")) {
                        System.out.println("Produto adicionado à lista com sucesso!");
                        continua = false;
                    } else if (opcao.equals("N")) {
                        System.out.println("Produto não foi adicionado à lista.");
                        return;
                    } else {
                        System.out.println("Entrada inválida. Por favor, digite 'S' para sim ou 'N' para não.");
                    }
                }

                ListaProduto novaListaProduto = new ListaProduto(lista.getId(), produto.getId(), quantidade, observacoes);
                arqListaProduto.create(novaListaProduto);
            }
        } catch (Exception e) {
            System.err.println("\nErro ao buscar produto: " + e.getMessage() + "\n");
        }
    }  

    public void acresentarProdutoLista(Scanner scanner, Lista lista){
        System.out.println("-------- PresenteFácil 1.0 --------"); 
        System.out.println("-----------------------------------"); 
        System.out.println("> Início > Minhas Listas > " + lista.getNome() + " > Protudos > Acrescentar produto \n");

        String opcao;
        boolean continua = true;

        try{
            while(continua) {
                System.out.println("(1) Buscar produtos por GTIN");
                System.out.println("(2) Buscar produtos por nome");
                System.out.println("(3) Listar todos os produtos");
                System.out.println();
                System.out.println("(R) Retornar ao menu anterior");
                System.out.println();
                System.out.print("\nOpção: ");

                opcao = scanner.nextLine().trim().toUpperCase();

                switch (opcao) {
                    case "1": //busca por GTIN
                        buscarProdutorPorGtin(scanner, lista);
                        continua = false;
                        break;
                    case "2": // busca por nome 
                        buscarProdutoPorNome(scanner, lista);
                        continua = false;
                        break;
                    case "3": // listagem 
                        listagemDeProdutos(scanner, lista);
                        continua = false;
                        break;
                    case "R":
                        System.out.println("\n-- Retornando ao menu anterior. --\n");
                        continua = false;
                        break;
                    default:
                        System.out.println("\nOpição Invalida. Tente novamente.\n");
                }
            }
        } catch (Exception e) {
            System.err.println("\nOcorreu um erro ao mostrar acresentar produto: " + e.getMessage() + "\n");
        }
    }

    public void buscarProdutoPorNome(Scanner scanner, Lista lista){ 
    
    // 1. Definição do cabeçalho (Mantido)
    System.out.println("-------- PresenteFácil 3.0 --------"); 
    System.out.println("-----------------------------------"); 
    System.out.println("> Início > Minhas Listas > " + lista.getNome() + " > Produtos > Acrescentar produto > Buscar por Nome \n");

    try {
        // O ControladorProduto.buscarProdutoPorNome retorna o ArrayList<Produto> JÁ ORDENADO
        // Ele também lida com o input do nome e a exibição inicial dos resultados.
        ArrayList<Produto> produtosOrdenados = controladorProduto.buscarProdutoPorNome(scanner, usuarioLogado);

        if (produtosOrdenados == null || produtosOrdenados.isEmpty()) {
            // A mensagem de "não encontrado" já é dada pelo controladorProduto, aqui apenas retorna
            return; 
        }

        // --- LÓGICA DE INTERAÇÃO APÓS A BUSCA ---
        // A lista 'produtosOrdenados' JÁ ESTÁ ORDENADA CORRETAMENTE POR TFxIDF.
        
        // Exibe os produtos (já ordenados)
        for (int i = 0; i < produtosOrdenados.size(); i++) {
             System.out.println("(" + (i + 1) + ") " + produtosOrdenados.get(i).getNome());
        }

        boolean continua = true;
        String opcao;
        while (continua) {
            System.out.println("\n(R) Retornar ao menu anterior");
            System.out.println();
            System.out.print("\nOpção: ");

            opcao = scanner.nextLine().trim().toUpperCase();

            switch (opcao) {
                case "R":
                    System.out.println("\n-- Retornando ao menu anterior. --\n");
                    continua = false;
                    break;
                default:
                    if (IsNumber.isNumber(opcao)) {
                        int indice = Integer.parseInt(opcao);
                        Produto produtoSelecionado = (indice > 0 && indice <= produtosOrdenados.size()) ? 
                                                     produtosOrdenados.get(indice - 1) : null;

                        if (produtoSelecionado != null) {
                            // Verifica se o produto já está na lista (Lógica movida para uma função)
                            if (produtoJaExisteNaLista(lista, produtoSelecionado)) {
                                System.out.println("\n--- Produto já está cadastrado na lista! ---\n");
                                return; // Finaliza
                            } else {
                                // Chama o método que lida com a quantidade/observações e adiciona
                                acrescentarPorBuscaPorNome(scanner, lista, produtoSelecionado);
                                return; // Finaliza após adicionar ou cancelar
                            }
                        } else {
                            System.out.println("\nOpção Inválida. Tente novamente.\n");
                        }
                    } else {
                        System.out.println("\nOpção Inválida. Tente novamente.\n");
                    }
            }
        }
    } catch (Exception e) {
        System.err.println("\nErro ao buscar produto: " + e.getMessage() + "\n");
    }
}

// --- NOVO MÉTODO DE VERIFICAÇÃO ---
public boolean produtoJaExisteNaLista(Lista lista, Produto produto) throws Exception {
    Produto[] produtosNaLista = arqListaProduto.getProdutosByListaId(lista.getId());
    for(Produto p : produtosNaLista){
        if(p.getId() == produto.getId()){
            return true;
        }
    }
    return false;
}

    public void acrescentarPorBuscaPorNome(Scanner scanner, Lista lista, Produto produto){
        try {
            if (produto == null) {
                System.out.println("\n-- Nenhum produto encontrado! --\n");
            } else {
                if (!produto.isAtivo()) {
                    System.out.println("\n-- Produto inativo. Nao pode ser adicionado a lista. --\n");
                    return;
                }
                System.out.println("\n-------- Detalhes do Produto --------");
                System.out.println(produto);
                System.out.println("-------------------------------------\n");

                boolean continua = true;
                int quantidade = 1;

                while(continua){
                    System.out.print("Digite a quantidade: ");
                    quantidade = scanner.nextInt();

                    if(quantidade < 1){
                        System.out.println("--- A quantidade não pode ser menor ou igual a zero! ---");
                    }
                    continua = false;
                }
                scanner.nextLine();

                System.out.println();
                System.out.print("Observações (Opcional): ");
                String observacoes = scanner.nextLine();
                System.out.println();

                String opcao = "";
                continua = true;

                while (continua) {
                    System.out.print("Deseja acrescentar esse produto à lista? (S/N): ");
                    opcao = scanner.nextLine().trim().toUpperCase();

                    if (opcao.equals("S")) {
                        System.out.println("Produto adicionado à lista com sucesso!");
                        continua = false;
                    } else if (opcao.equals("N")) {
                        System.out.println("Produto não foi adicionado à lista.");
                        return;
                    } else {
                        System.out.println("Entrada inválida. Por favor, digite 'S' para sim ou 'N' para não.");
                    }
                }

                ListaProduto novaListaProduto = new ListaProduto(lista.getId(), produto.getId(), quantidade, observacoes);
                arqListaProduto.create(novaListaProduto);
            }
        } catch (Exception e) {
            System.err.println("\nErro ao acrescentar por busca por nome: " + e.getMessage() + "\n");
        }
    }

    public Produto[] mostrarProdutosPorListaProduto(Lista lista) throws Exception {
        try {
            Produto[] produtos = arqListaProduto.getProdutosByListaId(lista.getId());
            ListaProduto[] listaProdutos = arqListaProduto.readByListaId(lista.getId());

            if (produtos == null || produtos.length == 0) {
                System.out.println("\n-- Nenhum produto cadastrado. --\n");
                return null;
            }

            Arrays.sort(produtos, Comparator.comparing(Produto::getNome, String.CASE_INSENSITIVE_ORDER));

            for (int i = 0; i < produtos.length; i++) {
                for (int j = 0; j < listaProdutos.length; j++) {
                    if (produtos[i].getId() == listaProdutos[j].getIdProduto()) {
                        String status = produtos[i].isAtivo() ? "" : " (Inativo)";
                        System.out.println("(" + (i + 1) + ") " + produtos[i].getNome() + status + " " +
                            "(x" + listaProdutos[j].getQuantidade() + ")");
                    }
                }
            }

            return produtos;
        } catch (Exception e) {
            System.err.println("\nErro ao mostrar produtos por ListaProduto: " + e.getMessage() + "\n");
            return null;  
        }
    }

    public void listagemDeProdutos(Scanner scanner, Lista lista){ //coisas aqui
        try {
            List<Produto> produtos = arqProduto.listarTodos();
            if (produtos.isEmpty()) {
                System.out.println("\n-- Nenhum produto cadastrado. --\n");
                return;
            }

            int paginaAtual = 0;
            final int ITENS_POR_PAGINA = 10;
            boolean sair = false;

            produtos.sort(Comparator.comparing(Produto::getNome, String.CASE_INSENSITIVE_ORDER));

            while (!sair){
                System.out.println("-------- PresenteFácil 3.0 --------"); 
                System.out.println("-----------------------------------"); 
                System.out.println("> Início > Minhas Listas > " + lista.getNome() + " > Protudos > Acrescentar produto > Listagem \n");

                int totalPaginas = (int) Math.ceil((double) produtos.size() / ITENS_POR_PAGINA);
                System.out.println("Pagina " + (paginaAtual + 1) + " de " + totalPaginas);
                System.out.println("----------------------------------\n");

                int inicio = paginaAtual * ITENS_POR_PAGINA;
                int fim = Math.min(inicio + ITENS_POR_PAGINA, produtos.size());

                for (int i = inicio; i < fim; i++) {
                    Produto p = produtos.get(i);
                    String status = p.isAtivo() ? "" : " (Inativo)";
                    System.out.println("(" + (i + 1) + ") " + p.getNome() + status);
                }

                System.out.println("\nDigite o numero para ver detalhes");
                System.out.println("(P) Proxima pagina");
                System.out.println("(A) Pagina anterior");
                System.out.println("(R) Retornar ao menu anterior");
                System.out.print("\nOpcao: ");
                String opcao = scanner.nextLine().trim().toUpperCase();

                switch (opcao) {
                    case "P":
                        if (paginaAtual < totalPaginas - 1) paginaAtual++;
                        break;
                    case "A":
                        if (paginaAtual > 0) paginaAtual--;
                        break;
                    case "R":
                        sair = true;
                        break;
                    default:
                        try {
                            int escolha = Integer.parseInt(opcao);
                            int indiceEscolhido = escolha - 1;
                            if (indiceEscolhido >= inicio && indiceEscolhido < fim) {
                                Produto selecionado = produtos.get(indiceEscolhido); 
                                acrescentarPorListagemDeProdutos(scanner, lista, selecionado);
                            } else {
                                System.out.println("\n-- Numero fora da pagina atual. --\n");
                            }
                        } catch (NumberFormatException nf) {
                            System.out.println("\n-- Opcao invalida. --\n");
                        }
                        break;
                }
            }
        }  catch (Exception e) {
            System.err.println("\nErro ao listar produtos: " + e.getMessage() + "\n");
        }
    }

    public void acrescentarPorListagemDeProdutos(Scanner scanner, Lista lista, Produto produto){
        try {
            boolean estaNaLista = false;
            Produto[] produtos = arqListaProduto.getProdutosByListaId(lista.getId());

            for(int i = 0; i < produtos.length; i++){
                if(produto.getGtin13().equals(produtos[i].getGtin13())){
                    estaNaLista = true;
                    break;
                }
            }

            if(estaNaLista){
                System.out.println("\n--- Projduto já está cadastrado na lista! ---\n");
                return;
            }

            if (produto == null) {
                System.out.println("\n-- Nenhum produto encontrado com este GTIN-13. --\n");
            } else {
                if (!produto.isAtivo()) {
                    System.out.println("\n-- Produto inativo. Nao pode ser adicionado a lista. --\n");
                    return;
                }
                System.out.println("\n-------- Detalhes do Produto --------");
                System.out.println(produto);
                System.out.println("-------------------------------------\n");

                boolean continua = true;
                int quantidade = 1;

                while(continua){
                    System.out.print("Digite a quantidade: ");
                    quantidade = scanner.nextInt();

                    if(quantidade < 1){
                        System.out.println("--- A quantidade não pode ser menor ou igual a zero! ---");
                    }
                    continua = false;
                }
                scanner.nextLine();

                System.out.println();
                System.out.print("Observações (Opcional): ");
                String observacoes = scanner.nextLine();
                System.out.println();

                String opcao = "";
                continua = true;

                while (continua) {
                    System.out.print("Deseja acrescentar esse produto à lista? (S/N): ");
                    opcao = scanner.nextLine().trim().toUpperCase();

                    if (opcao.equals("S")) {
                        System.out.println("Produto adicionado à lista com sucesso!");
                        continua = false;
                    } else if (opcao.equals("N")) {
                        System.out.println("Produto não foi adicionado à lista.");
                        return;
                    } else {
                        System.out.println("Entrada inválida. Por favor, digite 'S' para sim ou 'N' para não.");
                    }
                }

                ListaProduto novaListaProduto = new ListaProduto(lista.getId(), produto.getId(), quantidade, observacoes);
                arqListaProduto.create(novaListaProduto);
            }
        } catch (Exception e) {
            System.err.println("\nErro ao acrescentar por listagem de produto: " + e.getMessage() + "\n");
        }
    }

    public void mudarQuantidade(Scanner scanner, ListaProduto listaProduto){
        boolean continua = true;

        while(continua){
            System.out.print("Digite a nova quantidade: ");
            int quantidade = scanner.nextInt();
            if(quantidade < 1){
                System.out.println("--- A quantidade não pode ser menor ou igual a zero! ---");
            }
            listaProduto.setQuantidade(quantidade);
            try {
                arqListaProduto.update(listaProduto);
                System.out.println("\n-- Quantidade atualizada com sucesso! --\n");
            } catch (Exception e) {
                System.err.println("\nErro ao salvar a nova quantidade: " + e.getMessage() + "\n");
            }
            try { scanner.nextLine(); } catch (Exception ignore) {}
            continua = false;
        }
    }

    public void mudarObservacoes(Scanner scanner, ListaProduto listaProduto){
        System.out.print("Digite aas novas observações: ");
        String observacoes = scanner.nextLine();
        if(!observacoes.trim().isEmpty()){
            listaProduto.setObservacoes(observacoes);
            try {
                arqListaProduto.update(listaProduto);
                System.out.println("\n-- Observacoes atualizadas com sucesso! --\n");
            } catch (Exception e) {
                System.err.println("\nErro ao salvar as observacoes: " + e.getMessage() + "\n");
            }
        } else {
            System.out.println("\n-- Nenhuma alteracao realizada. --\n");
        }
    }
}

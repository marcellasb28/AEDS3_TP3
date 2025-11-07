package src.presenteFacil.controller;

import src.presenteFacil.model.*;
import src.presenteFacil.utils.ClearConsole;
import src.presenteFacil.controller.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class ControladorListaDePresentes {

    private ArquivoLista arqListas;
    private ControladorListaProduto controladorListaProduto = new ControladorListaProduto();
    private Usuario usuarioLogado;
    private DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public ControladorListaDePresentes() throws Exception {
        this.arqListas = new ArquivoLista();
        this.controladorListaProduto = new ControladorListaProduto();
    }

    public void setUsuario(Usuario usuarioLogado){
        this.usuarioLogado = usuarioLogado;
    }

    public void criarNovaLista(Scanner scanner, Usuario usuario) {   

        System.out.println("-------- PresenteFácil 1.0 --------"); 
        System.out.println("-----------------------------------"); 
        System.out.println("> Início > Minhas Listas > Nova Lista\n");
        try{
            System.out.print("Nome da Lista: ");
            String nome = scanner.nextLine();

            System.out.print("\nDescrição: ");
            String descricao = scanner.nextLine();

            LocalDate dataLimite = null;

            while(dataLimite == null) {

                System.out.print("\nData limite (dd/MM/yyyy): ");
                String dataLimiteStr = scanner.nextLine();
                
                try {
                    dataLimite = LocalDate.parse(dataLimiteStr, formato);
                } 
                catch(DateTimeParseException e) {
                    System.out.println("\n-- Data em formato inválido. Tente novamente. --\n");
                }
            }
            
            String codigo = NanoID.nanoId();
            
            int idUsuario = usuario.getID();
            LocalDate dataCriacao = LocalDate.now();
            Lista novaLista = new Lista(codigo, nome, descricao, dataCriacao, dataLimite, idUsuario);
            arqListas.create(novaLista);

            ClearConsole.clearScreen();

            System.out.println("\n-- Lista criada com sucesso! (Código compartilhável: " + codigo + ") --\n");
        }
        
        catch(Exception e){
            System.err.println("\nOcorreu um erro ao criar a Lista: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    public Lista[] mostrarMinhasListas(Usuario usuario) throws Exception{
        try{
            System.out.println("-------- PresenteFácil 1.0 --------"); 
            System.out.println("-----------------------------------"); 
            System.out.println("> Início > Minhas Listas\n");
            System.out.println("\nLISTAS");
            boolean temListasAtivas = false;
            Lista[] listas = arqListas.readByUsuario(usuario.getId());

            if (listas == null || listas.length == 0) {
                System.out.println("\n-- Nenhuma lista cadastrada. --\n");
                return null;
            }

            Arrays.sort(listas, Comparator.comparing(Lista::getNome, String.CASE_INSENSITIVE_ORDER));

            for (int i = 0; i < listas.length; i++) {
                if(listas[i].isAtiva()){
                    temListasAtivas = true;
                    System.out.println("\n(" + (i + 1) + ") " + listas[i].getNome() + " - "
                        + listas[i].getDataCriacao().format(formato));
                }
            }
            if(!temListasAtivas){
                System.out.println("\n-- Nenhuma lista ativa. --\n");
                return null;
            }
            System.out.println();
            return listas;
        } catch (Exception e) {
            System.err.println("\nErro ao buscar lista: " + e.getMessage() + "\n");
        }
        return null;
    }

    public void mostrarListasDesativadas(Usuario usuario) {

        System.out.println("-------- PresenteFácil 1.0 --------"); 
        System.out.println("-----------------------------------"); 
        System.out.println("> Início > Minhas Listas > Listas Desativadas\n");
        
        try{
            boolean temListasDesativadas = false;
            Lista[] listas = arqListas.readByUsuarioDisableLists(usuario.getId());

            if (listas == null || listas.length == 0) {
                System.out.println("\n-- Nenhuma lista desativada. --\n");
                return;
            }

            Arrays.sort(listas, Comparator.comparing(Lista::getNome, String.CASE_INSENSITIVE_ORDER));

            for (int i = 0; i < listas.length; i++) {
                if(!listas[i].isAtiva()){
                    temListasDesativadas = true;
                    System.out.println("(" + (i + 1) + ") " + listas[i].getNome() + " - "
                        + listas[i].getDataCriacao().format(formato) + " - (Código: " + listas[i].getCodigo() + ")");
                }
            }
            if(!temListasDesativadas){
                System.out.println("\n-- Nenhuma lista desativada. --\n");
                return;
            }
            System.out.println();
        } catch (Exception e) {
            System.err.println("\nErro ao buscar lista: " + e.getMessage() + "\n");
        }
    }

    public void buscarListaPorCodigo(Scanner scanner, Usuario usuarioLogado) {

        if (usuarioLogado == null) {
            System.err.println("\n-- Usuário não autenticado. Faça login antes de buscar listas. --\n");
            return;
        }
        this.usuarioLogado = usuarioLogado; // garante o campo preenchido

        System.out.println("-------- PresenteFácil 1.0 --------"); 
        System.out.println("-----------------------------------"); 
        System.out.println("> Início > Buscar Lista\n");

        try {
            System.out.print("\nDigite o código da lista: ");
            String codigo = scanner.nextLine();
            Lista lista = arqListas.readByCodigo(codigo); // usa o campo, não o parâmetro

            if (lista == null || !lista.isAtiva()) {
                System.out.println("\n-- Nenhuma lista encontrada com esse código. --\n");
            } else {
                System.out.println("\n-- Lista encontrada! --");
                ClearConsole.clearScreen();
                System.out.println("-------- PresenteFácil 1.0 --------"); 
                System.out.println("-----------------------------------"); 
                System.out.println("> Início > " + lista.getNome() + "\n");
                System.out.println("Proprietário(a) da lista: " + usuarioLogado.getNome());
                System.out.println("Nome: " + lista.getNome());
                System.out.println("Descrição: " + lista.getDescricao());
                System.out.println("Data de criação: " + lista.getDataCriacao().format(formato));
                System.out.println("Data limite: " + lista.getDataLimite().format(formato));
                System.out.println("Código compartilhável: " + lista.getCodigo());
                System.out.println("Ativa: " + (lista.isAtiva() ? "Sim" : "Não"));
                System.out.println("------------------------------\n");
            }
        } catch (Exception e) {
            System.err.println("\nErro ao buscar lista: " + e.getMessage() + "\n");
        }
    }


    public void mostrarLista(Scanner scanner, Lista lista, Usuario usuarioLogado) throws Exception{
        try {
            if (lista == null) {
                System.out.println("\n-- Nenhuma lista encontrada com esse código. --\n");
                return;
            }
            setUsuario(usuarioLogado);
            
            ClearConsole.clearScreen();
            System.out.println("-------- PresenteFácil 1.0 --------"); 
            System.out.println("-----------------------------------"); 
            System.out.println("> Início > Minhas Listas > " + lista.getNome() + "\n");
            System.out.println("Nome: " + lista.getNome());
            System.out.println("Descrição: " + lista.getDescricao());
            System.out.println("Data de criação: " + lista.getDataCriacao().format(formato));
            System.out.println("Data limite: " + lista.getDataLimite().format(formato));
            //System.out.println("ID do Usuário: " + lista.getIdUsuario());
            System.out.println("Código compartilhável: " + lista.getCodigo());
            System.out.println();

            String opcao;
            boolean continua = true;

            while(continua) {
                System.out.println("(1) Gerenciar produtos da lista");
                System.out.println("(2) Alterar dados da lista");
                System.out.println("(3) Desativar lista");
                System.out.println("(4) Excluir lista");
                System.out.println();
                System.out.println("(R) Retornar ao menu anterior");
                System.out.println();
                System.out.print("\nOpção: ");

                opcao = scanner.nextLine().trim().toUpperCase();

                switch (opcao) {
                    case "1":
                        controladorListaProduto.gerenciarProdutoLista(scanner, lista, usuarioLogado);
                        continua = false;
                        break;
                    case "2":
                        alterarDadosLista(scanner, lista);
                        continua = false; 
                        break;
                    case "3":
                        boolean resp = desativarLista(scanner, lista.getId(), lista.getNome());
                        if(resp){
                            System.out.println("\n-- Lista desativada com sucesso! --\n");
                        } else {
                            System.out.println("\n-- Erro ao desativar a lista. --\n");
                        }
                        continua = false;
                        break;
                    case "4":
                        boolean foiDeletado = deletarLista(scanner, lista.getId(), lista.getNome());
                        if (foiDeletado) {
                            continua = false;
                        }
                        break;
                    case "R":
                        System.out.println("\n-- Retornando ao menu anterior. --\n");
                        continua = false;
                        break;
                    default:
                        System.out.println("\nOpção Inválida. Tente novamente.\n");
                }
            }
        } catch (Exception e) {
            System.err.println("\nErro ao mostrar lista: " + e.getMessage() + "\n");
        }
    }

    public void alterarDadosLista(Scanner scanner, Lista lista) {
        System.out.println("-------- PresenteFácil 1.0 --------"); 
        System.out.println("-----------------------------------"); 
        System.out.println("> Início > Minhas Listas > " + lista.getNome() + " > Alterar Dados da Lista\n");
        System.out.println("\n----- Alterar Dados da Lista ------");
        System.out.println("\n[Deixe o campo em branco para manter a informação atual.]\n");

        try {
            System.out.println("Nome atual: " + lista.getNome());
            System.out.print("\nNovo nome: ");
            String novoNome = scanner.nextLine();
            if (!novoNome.trim().isEmpty()) {
                lista.setNome(novoNome);
            }

            System.out.println("\nDescrição atual: " + lista.getDescricao());
            System.out.print("\nNova descrição: ");
            String novaDescricao = scanner.nextLine();
            if (!novaDescricao.trim().isEmpty()) {
                lista.setDescricao(novaDescricao);
            }

            boolean dataValida = false;
            while(!dataValida) {
                System.out.println("\nData limite atual: " + lista.getDataLimite().format(formato));
                System.out.print("\nNova data limite (dd/MM/yyyy): ");
                String novaDataStr = scanner.nextLine();
                if (novaDataStr.trim().isEmpty()) {
                    dataValida = true;
                } else {
                    try {
                        LocalDate novaData = LocalDate.parse(novaDataStr, formato);
                        lista.setDataLimite(novaData);
                        dataValida = true;
                    } catch (DateTimeParseException e) {
                        System.out.println("\n-- Formato de data inválido. Tente novamente. --\n");
                    }
                }
            }
            
            if (arqListas.update(lista)) {
                System.out.println("\n-- Lista alterada com sucesso! --\n");
            } else {
                System.out.println("\n-- Erro ao alterar a lista. --\n");
            }

        } catch (Exception e) {
            System.err.println("\nOcorreu um erro ao alterar a lista: " + e.getMessage() + "\n");
        }
    }

    public ArquivoLista getArquivoLista() {
        return this.arqListas;
    }

    public boolean deletarLista(Scanner scanner, int idLista, String nome) throws Exception {
        System.out.println("-------- PresenteFácil 1.0 --------"); 
        System.out.println("-----------------------------------"); 
        System.out.println("> Início > Minhas Listas > " + nome + " > Deletar Lista\n");
        System.out.println("\n-------- Deletar Lista ----------");
        System.out.print("Você tem certeza que deseja deletar esta lista? (S/N): ");
        String confirmacao = scanner.nextLine().toUpperCase();

        if (!confirmacao.equals("S")) {
            System.out.println("\n-- Operação cancelada. --\n");
            return false;
        }
        
        try {
            boolean sucesso = arqListas.delete(idLista);
            if (sucesso) {
                System.out.println("\n-- Lista deletada com sucesso! --\n");
            } else {
                System.out.println("\n-- Nenhuma lista encontrada com esse ID. --\n");
            }
            return sucesso;
        } catch (Exception e) {
            System.err.println("\nErro ao deletar lista: " + e.getMessage() + "\n");
            return false;
        }
    }

    public boolean desativarLista(Scanner scanner, int idLista, String nome) throws Exception {
        System.out.println("-------- PresenteFácil 1.0 --------"); 
        System.out.println("-----------------------------------"); 
        System.out.println("> Início > Minhas Listas > " + nome + " > Desativar Lista\n");
        System.out.println("\n-------- Desativar Lista ----------");
        System.out.print("\nVocê tem certeza que deseja deletar esta lista? (S/N): ");
        String confirmacao = scanner.nextLine().toUpperCase();

        if (!confirmacao.equals("S")) {
            System.out.println("\n-- Operação cancelada. --\n");
            return false;
        }
        
        try {
            boolean sucesso = arqListas.disableList(idLista);
            if (sucesso) {
                System.out.println("\n-- Lista desativada com sucesso! --\n");
            } else {
                System.out.println("\n-- Nenhuma lista encontrada com esse ID. --\n");
            }
            return sucesso;
        } catch (Exception e) {
            System.err.println("\nErro ao desativar lista: " + e.getMessage() + "\n");
            return false;
        }
    }

    public void reativarLista(Scanner scanner) {
        System.out.println("-------- PresenteFácil 1.0 --------"); 
        System.out.println("-----------------------------------"); 
        System.out.println("> Início > Minhas Listas > Reativar Lista\n");
        try {
            System.out.print("Digite o código da lista a ser reativada: ");
            String codigo = scanner.nextLine();
            Lista lista = arqListas.readByCodigo(codigo);
            
            if (lista == null) {
                System.out.println("\n-- Nenhuma lista encontrada com esse código. --\n");
                return;
            }
            
            if (lista.isAtiva()) {
                System.out.println("\n-- A lista já está ativa. --\n");
                return;
            }

            System.out.print("Você tem certeza que deseja reativar esta lista? (S/N): ");
            String confirmacao = scanner.nextLine().toUpperCase();

            if (!confirmacao.equals("S")) {
                System.out.println("\n-- Operação cancelada. --\n");
                return;
            }

            lista.setAtiva(true);
            if (arqListas.update(lista)) {
                System.out.println("\n-- Lista reativada com sucesso! --\n");
            } else {
                System.out.println("\n-- Erro ao reativar a lista. --\n");
            }

        } catch (Exception e) {
            System.err.println("\nErro ao reativar lista: " + e.getMessage() + "\n");
        }
    }
    // Versao que exibe tambem os produtos de uma lista compartilhada
    public void buscarListaPorCodigoComProdutos(Scanner scanner, Usuario usuarioLogado) {
        if (usuarioLogado == null) {
            System.err.println("\n-- Usuario nao autenticado. Faca login antes de buscar listas. --\n");
            return;
        }

        System.out.println("-------- PresenteFacil 1.0 --------");
        System.out.println("-----------------------------------");
        System.out.println("> Inicio > Buscar Lista\n");

        try {
            System.out.print("\nDigite o codigo da lista: ");
            String codigo = scanner.nextLine();
            Lista lista = arqListas.readByCodigo(codigo);

            if (lista == null || !lista.isAtiva()) {
                System.out.println("\n-- Nenhuma lista ativa encontrada com esse codigo. --\n");
                return;
            }

            ClearConsole.clearScreen();
            System.out.println("-------- PresenteFacil 1.0 --------");
            System.out.println("-----------------------------------");
            System.out.println("> Inicio > " + lista.getNome() + "\n");

            // Dono da lista
            String nomeDono = "desconhecido";
            try {
                ArquivoUsuario arqUsuarios = new ArquivoUsuario();
                Usuario dono = arqUsuarios.read(lista.getIdUsuario());
                if (dono != null) nomeDono = dono.getNome();
            } catch (Exception ignore) { }
            System.out.println("Proprietario(a) da lista: " + nomeDono);

            System.out.println("Nome: " + lista.getNome());
            System.out.println("Descricao: " + lista.getDescricao());
            System.out.println("Data de criacao: " + lista.getDataCriacao().format(formato));
            System.out.println("Data limite: " + lista.getDataLimite().format(formato));
            System.out.println("Codigo compartilhavel: " + lista.getCodigo());
            System.out.println("Ativa: " + (lista.isAtiva() ? "Sim" : "Nao"));
            System.out.println("------------------------------\n");

            // Produtos contidos na lista
            try {
                ArquivoListaProduto arqLP = new ArquivoListaProduto();
                ListaProduto[] itens = arqLP.readByListaId(lista.getId());
                ArquivoProduto arqProd = new ArquivoProduto();
                if (itens != null && itens.length > 0) {
                    System.out.println("Produtos desta lista:");
                    for (ListaProduto lp : itens) {
                        Produto p = arqProd.read(lp.getIdProduto());
                        if (p != null) {
                            String st = p.isAtivo() ? "" : " (Inativo)";
                            System.out.println("- " + p.getNome() + st + " (x" + lp.getQuantidade() + ")");
                        }
                    }
                    System.out.println();
                } else {
                    System.out.println("Produtos desta lista: Nenhum produto cadastrado.\n");
                }
            } catch (Exception e) {
                System.err.println("\nErro ao listar produtos da lista compartilhada: " + e.getMessage() + "\n");
            }
        } catch (Exception e) {
            System.err.println("\nErro ao buscar lista: " + e.getMessage() + "\n");
        }
    }
}

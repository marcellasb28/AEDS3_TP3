package src.presenteFacil.controller;

import java.util.Scanner;
import src.presenteFacil.model.*;
import src.presenteFacil.utils.ClearConsole;

public class ControladorUsuario {

    private ArquivoUsuario arqUsuarios;
    private ArquivoLista arqListas;
    public Usuario usuarioLogado;

    public ControladorUsuario() throws Exception {
        this.arqUsuarios = new ArquivoUsuario();
        this.arqListas = new ArquivoLista();
        this.usuarioLogado = null;
    }

    public Usuario getUsuarioLogado() {
        return this.usuarioLogado;
    }

    public void criarNovoUsuario(Scanner scanner) {
        System.out.println("-------- PresenteFácil 3.0 --------");
        System.out.println("-----------------------------------");
        System.out.println("\n---------- Novo Usuário -----------");
        try {
            System.out.print("\nNome completo: ");
            String nome = scanner.nextLine();
            System.out.print("\nE-mail: ");
            String email = scanner.nextLine();
            if (arqUsuarios.read(email) != null) {
                System.out.println("\n-- ERRO: O e-mail informado já está em uso! --\n");
                return;
            }
            System.out.print("\nSenha: ");
            String senha = scanner.nextLine();
            int hashSenha = senha.hashCode();
            System.out.print("\nPergunta secreta: ");
            String pergunta = scanner.nextLine();
            System.out.print("\nResposta secreta: ");
            String resposta = scanner.nextLine();
            int hashResposta = resposta.hashCode();
            Usuario novoUsuario = new Usuario(nome, email, hashSenha, pergunta, resposta);
            arqUsuarios.create(novoUsuario);

            ClearConsole.clearScreen();

            System.out.println("\n-- Usuário criado com sucesso! --\n");

        } catch (Exception e) {
            System.err.println("\nOcorreu um erro ao criar o usuário: " + e.getMessage() + "\n");
        }
    }

    public Usuario loginUsuario(Scanner scanner) {
        System.out.println("-------- PresenteFácil 3.0 --------");
        System.out.println("-----------------------------------");
        System.out.println("\n------------- Login ---------------");
        try {
            System.out.print("\nE-mail: ");
            String email = scanner.nextLine();
            System.out.print("\nSenha: ");
            String senha = scanner.nextLine();
            System.out.println("\n");
            Usuario usuario = arqUsuarios.read(email);
            if (usuario != null) {
                int hashSenhaDigitada = senha.hashCode();
                if (hashSenhaDigitada == usuario.getHashSenha()) {
                    if (usuario.isAtivo()) {
                        this.usuarioLogado = usuario;
                        return this.usuarioLogado;
                    } else {
                        System.out.println("\n-- ERRO: Esta conta foi desativada. --\n");
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("\nErro no login: " + e.getMessage() + "\n");
        }
        System.out.println("\n-- E-mail ou senha inválidos. --\n");
        return null;
    }

    public void logout() { this.usuarioLogado = null; }

    public boolean desativarPropriaConta(Scanner scanner) throws Exception {
        if (this.usuarioLogado == null) {
            System.out.println("\n-- Nao ha usuario logado para desativar. --\n");
            return false;
        }
        System.out.println("-------- PresenteFácil 3.0 --------");
        System.out.println("-----------------------------------");
        System.out.println("\n------ Desativar Minha Conta ------");
        System.out.print("Tem certeza que deseja desativar sua conta? (S/N): ");
        String confirmacao = scanner.nextLine().toUpperCase();
        
        if (!confirmacao.equals("S")) {
            System.out.println("\n-- Operacao cancelada. --\n");
            return false;
        }

        this.usuarioLogado.setAtivo(false);

        boolean sucesso = arqUsuarios.update(this.usuarioLogado);
        if (sucesso) {
            Lista[] listas = arqListas.readByUsuario(usuarioLogado.getId());
            for (Lista l : listas) arqListas.disableList(l.getId());
            System.out.println("\n-- Conta desativada com sucesso! Voce sera desconectado. --\n");
            logout();
        }
        return sucesso;
    }

    public boolean excluirPropriaConta(Scanner scanner) throws Exception {
        if (this.usuarioLogado == null) {
            System.out.println("\n-- Nao ha usuario logado para excluir. --\n");
            return false;
        }
        System.out.println("ATENCAO! Esta acao e PERMANENTE e IRREVERSIVEL.");
        System.out.println(" > Nome: " + this.usuarioLogado.getNome());
        System.out.println(" > E-mail: " + this.usuarioLogado.getEmail());
        System.out.print("\nDigite 'EXCLUIR' para confirmar a exclusao permanente de sua conta: ");
        String confirmacao = scanner.nextLine();
        if (!confirmacao.equals("EXCLUIR")) {
            System.out.println("\n-- Operacao cancelada. --\n");
            return false;
        }
        Lista[] listas = arqListas.readByUsuario(usuarioLogado.getId());
        for (Lista l : listas) arqListas.delete(l.getId());
        boolean sucesso = arqUsuarios.delete(this.usuarioLogado.getId());
        if (sucesso) {
            System.out.println("\n-- Conta excluida permanentemente! --\n");
            logout();
        }
        return sucesso;
    }

    public void reativarUsuario(Scanner scanner) throws Exception {
        System.out.println("-------- PresenteFácil 3.0 --------");
        System.out.println("-----------------------------------");
        System.out.println("\n--------- Reativar Usuario --------");
        System.out.print("\nE-mail: ");
        String email = scanner.nextLine();
        System.out.print("\nSenha: ");
        String senha = scanner.nextLine();
        Usuario usuario = arqUsuarios.read(email);
        if (usuario == null) { System.out.println("\n-- ERRO: Conta nao encontrada. --\n"); return; }
        if (senha.hashCode() != usuario.getHashSenha()) { System.out.println("\n-- Senha incorreta. --\n"); return; }
        if (usuario.isAtivo()) { System.out.println("\n-- Conta ja ativa. --\n"); return; }
        System.out.print("\nPergunta secreta: " + usuario.getPerguntaSecreta() + "\n");
        System.out.print("\nResposta secreta: ");
        String resp = scanner.nextLine();
        int hashRespostaFornecida = resp.hashCode();
        if (hashRespostaFornecida != usuario.getHashRespostaSecreta()) { System.out.println("\n-- Resposta secreta incorreta. --\n"); return; }
        usuario.setAtivo(true);
        if (arqUsuarios.update(usuario)) {
            Lista[] listas = arqListas.readByUsuario(usuario.getId());
            for (Lista l : listas) arqListas.activeList(l.getId());
            System.out.println("\n-- Conta reativada com sucesso! --\n");
        } else {
            System.out.println("\n-- Falha ao reativar a conta. --\n");
        }
    }

    // Meus dados + edição
    public void exibirDadosDoUsuarioLogado() {
        System.out.println("-------- PresenteFácil 3.0 --------");
        System.out.println("-----------------------------------");
        System.out.println("> Início > Meus Dados\n");
        if (this.usuarioLogado != null) {
            System.out.println(this.usuarioLogado.toString());
        } else {
            System.out.println("Nenhum usuario esta logado no momento.\n");
        }
    }

    public void exibirMeusDados(Scanner scanner) {
        if (this.usuarioLogado == null) {
            System.out.println("\n-- Nenhum usuario logado. --\n");
            return;
        }
        boolean sair = false;
        while (!sair) {
            System.out.println("-------- PresenteFácil 3.0 --------");
            System.out.println("-----------------------------------");
            System.out.println("> Início > Meus Dados\n");
            System.out.println(this.usuarioLogado.toString());
            System.out.println("(1) Alterar meus dados");
            System.out.println("(R) Retornar");
            System.out.print("\nOpcao: ");
            String op = scanner.nextLine().trim().toUpperCase();

            ClearConsole.clearScreen();

            switch (op) {
                case "1":
                    alterarDadosUsuario(scanner);
                    break;
                case "R":
                    sair = true;
                    break;
                default:
                    System.out.println("\n-- Opcao invalida. --\n");
            }
        }
    }

    private void alterarDadosUsuario(Scanner scanner) {

        if (this.usuarioLogado == null) return;
        boolean sair = false;
        while (!sair) {
            System.out.println("\n-------- Alterar Meus Dados --------");
            System.out.println("\nDeseja alterar:\n");
            System.out.println("(1) Nome");
            System.out.println("(2) E-mail");
            System.out.println("(3) Senha");
            System.out.println("(4) Pergunta Secreta");
            System.out.println("(5) Resposta Secreta");
            System.out.println("(R) Retornar");
            System.out.print("\nOpcao: ");
            String op = scanner.nextLine().trim().toUpperCase();

            ClearConsole.clearScreen();

            try {
                switch (op) {
                    case "1": {
                        System.out.println("Nome atual: " + usuarioLogado.getNome());
                        System.out.print("\nNovo nome: ");
                        String novo = scanner.nextLine();

                        ClearConsole.clearScreen();

                        if (!novo.trim().isEmpty()) {
                            usuarioLogado.setNome(novo);
                            salvarUsuarioLogado();
                            System.out.println("\n-- Nome atualizado! --\n");
                        } 
                        
                        else {
                            System.out.println("\n-- Nenhuma alteração realizada. --\n");
                        }
                        break;
                    }

                    case "2": {
                        System.out.println("Email atual: " + usuarioLogado.getEmail());
                        System.out.print("\nNovo e-mail: ");
                        String novo = scanner.nextLine().trim();
                        
                        ClearConsole.clearScreen();

                        if (novo.isEmpty()) { System.out.println("\n-- Nenhuma alteração realizada. --\n"); break; }
                        Usuario existente = arqUsuarios.read(novo);

                        if (existente != null && existente.getId() != usuarioLogado.getId()) {
                            System.out.println("\n-- Já existe uma conta com este e-mail. --\n");
                            break;
                        }

                        usuarioLogado.setEmail(novo);
                        salvarUsuarioLogado();
                        System.out.println("\n-- E-mail atualizado! --\n");
                        break;
                    }
                    case "3": {
                        System.out.print("Nova senha: ");
                        String s = scanner.nextLine();

                        ClearConsole.clearScreen();

                        if (s.trim().isEmpty()) { System.out.println("\n-- Nenhuma alteração realizada. --\n"); break; }
                        usuarioLogado.setHashSenha(s.hashCode());
                        salvarUsuarioLogado();
                        System.out.println("\n-- Senha atualizada! --\n");
                        break;
                    }
                    case "4": {
                        System.out.println("Pergunta atual: " + usuarioLogado.getPerguntaSecreta());
                        System.out.print("\nNova pergunta: ");
                        String p = scanner.nextLine();

                        ClearConsole.clearScreen();

                        if (p.trim().isEmpty()) { System.out.println("\n-- Nenhuma alteração realizada. --\n"); break; }
                        usuarioLogado.setPerguntaSecreta(p);
                        salvarUsuarioLogado();
                        System.out.println("\n-- Pergunta secreta atualizada! --\n");
                        break;
                    }
                    case "5": {
                        System.out.print("\nNova resposta secreta: ");
                        String r = scanner.nextLine();

                        ClearConsole.clearScreen();

                        if (r.trim().isEmpty()) { System.out.println("\n-- Nenhuma alteração realizada. --\n"); break; }
                        usuarioLogado.setHashRespostaSecreta(r.hashCode());
                        salvarUsuarioLogado();
                        System.out.println("\n-- Resposta secreta atualizada! --\n");
                        break;
                    }
                    case "R":
                        sair = true;
                        break;
                    default:
                        System.out.println("\n-- Opção inválida. --\n");
                }
            } catch (Exception e) {
                System.err.println("\nErro ao alterar dados do usuário: " + e.getMessage() + "\n");
                return;
            }
        }
    }

    private void salvarUsuarioLogado() throws Exception {
        arqUsuarios.update(usuarioLogado);
    }
}


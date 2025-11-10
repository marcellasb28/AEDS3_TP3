import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.Charset;
import src.presenteFacil.controller.ControladorUsuario;
import src.presenteFacil.view.MenuInicial;

/**
 * Classe principal do sistema PresenteFácil 2.0.
 * 
 * Responsável por iniciar a aplicação, instanciar os controladores
 * e chamar a primeira tela de interação (MenuInicial).
 * 
 * Exemplo de execução:
 * <pre>
 * java src.presenteFacil.Main
 * </pre>
 * 
 * @author Yasmin Torres Moreira dos Santos
 * @version 3.0
 */

public class Main {
    
    /**
     * Método principal que inicia a aplicação.
     * 
     * @param args argumentos de linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        // Ajusta E/S para a codificação real do console (Windows/Unix)
        Charset consoleCs = null;
        try {
            if (System.console() != null) {
                consoleCs = System.console().charset();
            }
        } catch (Throwable ignored) { }

        if (consoleCs != null) {
            try {
                System.setOut(new PrintStream(System.out, true, consoleCs));
                System.setErr(new PrintStream(System.err, true, consoleCs));
            } catch (Exception ignored) { }
        }

        Scanner scanner = (consoleCs != null)
                ? new Scanner(new InputStreamReader(System.in, consoleCs))
                : new Scanner(System.in);

        try {
            ControladorUsuario userController = new ControladorUsuario();

            MenuInicial menuInicial = new MenuInicial(userController);
            menuInicial.exibir(scanner);

        }
        
        catch (Exception e) {
            System.err.println("ERRO FATAL: Não foi possível iniciar o sistema.");
            e.printStackTrace();
        }
        
        finally {
            scanner.close();
        }
    }
}

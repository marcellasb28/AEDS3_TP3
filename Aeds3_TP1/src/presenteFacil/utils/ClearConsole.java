package src.presenteFacil.utils;

public class ClearConsole {

    public static void clearScreen() {

        try {

            final String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } 
            
            else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } 
        
        catch (final Exception e) {
            System.out.println("Error clearing console: " + e.getMessage());
        }
    }

    public static void delayAndClear() {
        
    try {
        // Aguarda 1 segundo (1000 milissegundo)
        Thread.sleep(1000);

        // Limpa o terminal (Windows, Linux e macOS)
        System.out.print("\033[H\033[2J");
        System.out.flush();

    } catch (InterruptedException e) {
        // Caso o delay seja interrompido
        System.err.println("Delay interrompido: " + e.getMessage());
    }
}


}
package please.help;

/**
 * Запускает работу {@link ClientManager}.
 */

public class Main {

    public static void main(String[] args) {
        ClientManager manager = ClientManager.createManager();
        manager.start();
    }
}



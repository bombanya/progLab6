package please.help;
/**
 * Запускает работу {@link CollectionManager}.
 */

public class Main {

    /**
     * Принимает аргумент коммандной строки, запускает работу {@link CollectionManager}.
     * @param args путь к файлу с сохраненной коллекцией в формате json
     */
    public static void main(String[] args){
        CollectionManager manager = CollectionManager.createManager(args);
        if (manager != null) manager.start();
    }
}

package please.help.commands;

import please.help.CollectionManager;
import please.help.Mode;
import please.help.ServerPackage;

import java.util.LinkedList;

/**
 * Класс для комманды exit.
 * Формат комманды: exit
 */
public class Exit extends Command{

    private static final long serialVersionUID = 20200916L;

    public Exit(){
        commandName = "exit";
    }

    @Override
    public ServerPackage execute(CollectionManager manager) {
        manager.setManagerMode(Mode.EXIT);
        return new ServerPackage(manager.collection.hashCode(),
                "Работа сервера завершается", null);
    }

    @Override
    public Exit validateCommand(LinkedList<String[]> data, CollectionManager manager) {
        if (data.size() == 0 || data.poll().length > 1) {
            System.out.println("Неверно введена комманда.");
            return null;
        }
        Exit exit = new Exit();
        exit.makeValid();
        return exit;
    }
}

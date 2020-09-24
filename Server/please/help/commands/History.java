package please.help.commands;

import please.help.*;

import java.util.LinkedList;

/**
 * Класс для комманды history.
 * Формат комманды: history
 */
public class History extends Command{

    private static final long serialVersionUID = 20200916L;

    public History(){
        commandName = "history";
    }

    @Override
    public ServerPackage execute(CollectionManager manager) {
        return new ServerPackage(manager.collection.hashCode(),
                manager.printHistory(), null);
    }

    @Override
    public History validateCommand(LinkedList<String[]> data, CollectionManager manager) {
        if (data.size() == 0 || data.poll().length > 1) {
            System.out.println("Неверно введена комманда.");
            return null;
        }
        History history = new History();
        history.makeValid();
        return history;
    }
}

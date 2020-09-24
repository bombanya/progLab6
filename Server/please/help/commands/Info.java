package please.help.commands;

import please.help.*;

import java.util.LinkedList;


/**
 * Класс для комманды info.
 * Формат комманды: info
 */
public class Info extends Command{

    private static final long serialVersionUID = 20200916L;

    public Info(){
        commandName = "info";
    }

    @Override
    public ServerPackage execute(CollectionManager manager) {
        String feedback = "Информация о коллекции:\n" +
                "Тип: LinkedList<Organization>\n" +
                "Дата инициализации: " + manager.getCreationDate() +
                "\nКоличество элементов: " + manager.collection.size();
        return new ServerPackage(manager.collection.hashCode(), feedback, null);
    }

    @Override
    public Info validateCommand(LinkedList<String[]> data, CollectionManager manager) {
        if (data.size() == 0 || data.poll().length > 1) {
            System.out.println("Неверно введена комманда.");
            return null;
        }
        Info info = new Info();
        info.makeValid();
        return info;
    }
}

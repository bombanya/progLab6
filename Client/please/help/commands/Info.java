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
    public Info validateCommand(LinkedList<String[]> data, ClientManager manager) {
        if (data.size() == 0 || data.poll().length > 1) {
            System.out.println("Неверно введена комманда.");
            return null;
        }
        Info info = new Info();
        info.makeValid();
        return info;
    }
}

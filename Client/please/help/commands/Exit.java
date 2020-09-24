package please.help.commands;

import please.help.ClientManager;
import please.help.Mode;

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
    public Exit validateCommand(LinkedList<String[]> data, ClientManager manager) {
        if (data.size() == 0 || data.poll().length > 1) {
            System.out.println("Неверно введена комманда.");
            return null;
        }
        manager.setManagerMode(Mode.EXIT);
        return new Exit();
    }
}

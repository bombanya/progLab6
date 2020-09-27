package please.help;

import please.help.commands.Command;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Класс, хранящий и обрабатывающий историю комманд.
 * Хранит последние пять успешно выполненные команды без аргументов.
 */

public class CommandsHistory{

    private final ArrayList<String> history = new ArrayList<>();

    /**
     * Добавляет очередную комманду в список комманд.
     * Если в списке становится больше пяти комманд, удаляет ту, которая была раньше всех введена.
     * @param c комманда ({@link please.help.commands.Command}).
     */
    public void addCommand(Command c){
        history.add(c.getCommandName());
        if (history.size() > 5) history.remove(0);
    }

    /**
     * Выводит комманды по одной в строке, последнюю добавленную - последней.
     */
    public String printCommands(){
        StringBuilder res = new StringBuilder();
        history.forEach(p -> res.append(p).append("\n"));
        return res.toString();
    }
}

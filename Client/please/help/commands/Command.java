package please.help.commands;

import please.help.*;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Абстрактный класс для всех комманд.
 */

public abstract class Command implements Serializable {

    private static final long serialVersionUID = 20200916L;
    protected String commandName;
    private boolean valid = false;

    /**
     * Возвращает название комманды.
     * @return название комманды
     */
    public String getCommandName() {
        return commandName;
    }

    protected void makeValid(){
        valid = true;
    }

    public boolean isValid() {
        return valid;
    }

    public abstract Command validateCommand(LinkedList<String[]> data, ClientManager manager);
}

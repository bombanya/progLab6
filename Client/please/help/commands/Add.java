package please.help.commands;

import please.help.*;
import please.help.organizationBuilding.Organization;

import java.util.LinkedList;

import please.help.organizationBuilding.*;


/**
 * Класс для комманды add.
 * Формат комманды: add
 */
public class Add extends Command{

    private static final long serialVersionUID = 20200916L;
    private Organization org;

    public Add(){
        commandName = "add";
    }

    private Add(Organization org){
        commandName = "add";
        this.org = org;
    }


    @Override
    public Add validateCommand(LinkedList<String[]> data, ClientManager manager) {
        if (data.size() == 0 || data.poll().length > 1) {
            System.out.println("Неверно введена комманда.");
            return null;
        }
        if (manager.getManagerMode() == Mode.CONSOLE) org = ConsoleValidator.buildFromConsole(null);
        else org = ScriptValidator.buildFromScript(null, data, manager);
        Add add = new Add(org);
        if (org != null) add.makeValid();
        return add;
    }
}

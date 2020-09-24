package please.help.commands;

import please.help.*;
import please.help.organizationBuilding.*;

import java.time.LocalDateTime;
import java.util.LinkedList;

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
    public ServerPackage execute(CollectionManager manager) {
        if (org.getCreationDate() == null){
            org.setId(OrganizationBuilder.generateId());
            org.setCreationDate(LocalDateTime.now());
        }
        manager.collection.add(org);
        return new ServerPackage(manager.collection.hashCode(),
                "Элемент добавлен", null);
    }

    @Override
    public Add validateCommand(LinkedList<String[]> data, CollectionManager manager) {
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

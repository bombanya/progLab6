package please.help.commands;

import please.help.*;
import please.help.organizationBuilding.ConsoleValidator;
import please.help.organizationBuilding.Organization;
import please.help.organizationBuilding.OrganizationBuilder;
import please.help.organizationBuilding.ScriptValidator;

import java.util.LinkedList;

/**
 * Класс для комманды remove_greater.
 * Формат комманды: remove_greater
 */
public class Remove_greater extends Command{

    private static final long serialVersionUID = 20200916L;
    private Organization org;

    public Remove_greater(){
        commandName = "remove_greater";
    }

    private Remove_greater(Organization org){
        commandName = "remove_greater";
        this.org = org;
    }

    @Override
    public ServerPackage execute(CollectionManager manager) {
        Organization[] toDelete = manager.collection.stream().filter(p -> p.compareTo(org) > 0)
                .toArray(Organization[]::new);
        for (Organization o : toDelete){
            OrganizationBuilder.deleteId(o.getId());
            manager.collection.remove(o);
        }
        return new ServerPackage(manager.collection.hashCode(),
                "Элементы удалены", null);
    }

    @Override
    public Remove_greater validateCommand(LinkedList<String[]> data, CollectionManager manager) {
        if (data.size() == 0 || data.poll().length > 1) {
            System.out.println("Неверно введена комманда.");
            return null;
        }
        if (manager.getManagerMode() == Mode.CONSOLE) org = ConsoleValidator.buildFromConsole(null);
        else org = ScriptValidator.buildFromScript(null, data, manager);
        Remove_greater remove_greater = new Remove_greater(org);
        if (org != null) remove_greater.makeValid();
        return remove_greater;
    }
}

package please.help.commands;

import please.help.*;
import please.help.organizationBuilding.Organization;

import java.util.LinkedList;

/**
 * Класс для комманды print_field_ascending_type.
 * Формат комманды: print_field_ascending_type
 */
public class Print_field_ascending_type extends Command{

    private static final long serialVersionUID = 20200916L;

    public Print_field_ascending_type(){
        commandName = "print_field_ascending_type";
    }

    @Override
    public ServerPackage execute(CollectionManager manager) {
        StringBuilder feedback = new StringBuilder();
        manager.collection.stream().map(Organization::getType).sorted().forEach(p -> feedback.append(p).append("\n"));
        return new ServerPackage(manager.collection.hashCode(), feedback.toString(), null);
    }

    @Override
    public Print_field_ascending_type validateCommand(LinkedList<String[]> data, CollectionManager manager) {
        if (data.size() == 0 || data.poll().length > 1) {
            System.out.println("Неверно введена комманда.");
            return null;
        }
        Print_field_ascending_type print_field_ascending_type = new Print_field_ascending_type();
        print_field_ascending_type.makeValid();
        return print_field_ascending_type;
    }
}

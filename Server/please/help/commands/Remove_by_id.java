package please.help.commands;

import please.help.*;
import please.help.organizationBuilding.Organization;
import please.help.organizationBuilding.OrganizationBuilder;

import java.util.LinkedList;

/**
 * Класс для комманды remove_by_id.
 * Формат комманды: remove_by_id id
 */
public class Remove_by_id extends Command{

    private static final long serialVersionUID = 20200916L;
    private Long id;

    public Remove_by_id(){
        commandName = "remove_by_id";
    }

    private Remove_by_id(Long id){
        commandName = "remove_by_id";
        this.id = id;
    }

    @Override
    public ServerPackage execute(CollectionManager manager) {
        for (Organization o : manager.collection){
            if (o.getId().equals(id)){
                manager.collection.remove(o);
                OrganizationBuilder.deleteId(id);
                return new ServerPackage(manager.collection.hashCode(),
                        "Элемент удален", null);
            }
        }
        return new ServerPackage(manager.collection.hashCode(),
                "Нет элемента с таким id", null);
    }

    @Override
    public Remove_by_id validateCommand(LinkedList<String[]> data, CollectionManager manager) {
        if (data.size() == 0 || data.peek().length != 2) {
            System.out.println("Неверно введена комманда.");
            data.poll();
            return null;
        }

        String[] polledCommand = data.poll();
        try {
            Long id = Long.parseLong(polledCommand[1]);
            Remove_by_id remove_by_id = new Remove_by_id(id);
            remove_by_id.makeValid();
            return remove_by_id;
        }
        catch (NumberFormatException e){
            System.out.println("Комманда должна вводиться вместе со значением типа long.");
            return null;
        }
    }
}

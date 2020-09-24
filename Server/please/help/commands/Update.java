package please.help.commands;

import please.help.*;
import please.help.organizationBuilding.*;

import java.util.LinkedList;

/**
 * Класс для комманды update.
 * Формат комманды: update id
 */
public class Update extends Command{

    private static final long serialVersionUID = 20200916L;
    private Long id;
    private Organization org;

    public Update(){
        commandName = "update";
    }

    private Update(Long id, Organization org){
        commandName = "update";
        this.id = id;
        this.org = org;
    }

    @Override
    public ServerPackage execute(CollectionManager manager) {
        for (Organization o : manager.collection){
            if (o.getId().equals(id)){
                manager.collection.remove(o);
                manager.collection.add(org);
                return new ServerPackage(manager.collection.hashCode(),
                        "Элемент обновлен.", null);
            }
        }
        return new ServerPackage(manager.collection.hashCode(),
                "Нет элемента с таким id.", null);
    }

    @Override
    public Update validateCommand(LinkedList<String[]> data, CollectionManager manager) {
        if (data.size() == 0 || data.peek().length != 2) {
            System.out.println("Неверно введена комманда.");
            data.poll();
            return null;
        }

        String[] polledCommand = data.poll();

        try {
            id = Long.parseLong(polledCommand[1]);
            ServerPackage packageWithOldOrg = (new GetOrgById(id)).execute(manager);
            if (packageWithOldOrg.getOrgForUpdate() != null){
                OrganizationBuilder.deleteId(packageWithOldOrg.getOrgForUpdate().getId());
                if (manager.getManagerMode() == Mode.CONSOLE) org = ConsoleValidator
                        .buildFromConsole(packageWithOldOrg.getOrgForUpdate());
                else org = ScriptValidator.buildFromScript(packageWithOldOrg.getOrgForUpdate(), data, manager);
                Update update = new Update(id, org);
                if (org != null) update.makeValid();
                else {
                    System.out.println("Элемент не был изменен.");
                    OrganizationBuilder.addId(packageWithOldOrg.getOrgForUpdate().getId());
                }
                return update;
            }
            else{
                System.out.println(packageWithOldOrg.getServerFeedback());
                return new Update();
            }
        }
        catch (NumberFormatException e){
            System.out.println("Комманда должна вводиться вместе со значением типа long.");
            return null;
        }
    }
}

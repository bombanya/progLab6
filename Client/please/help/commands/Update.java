package please.help.commands;

import please.help.*;
import please.help.organizationBuilding.ConsoleValidator;
import please.help.organizationBuilding.Organization;
import please.help.organizationBuilding.ScriptValidator;

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
    public Update validateCommand(LinkedList<String[]> data, ClientManager manager) {
        if (data.size() == 0 || data.peek().length != 2) {
            System.out.println("Неверно введена комманда.");
            data.poll();
            return null;
        }

        String[] polledCommand = data.poll();

        try {
            id = Long.parseLong(polledCommand[1]);
            String information = "Если прервать соединение на данном этапе, комманда не будет сохранена.";
            ServerPackage packageWithOldOrg = manager.getClient().sendCommand(new GetOrgById(id), information);
            if (packageWithOldOrg != null) {
                if (packageWithOldOrg.getOrgForUpdate() != null) {
                    if (manager.getManagerMode() == Mode.CONSOLE) org = ConsoleValidator
                            .buildFromConsole(packageWithOldOrg.getOrgForUpdate());
                    else org = ScriptValidator.buildFromScript(packageWithOldOrg.getOrgForUpdate(), data, manager);
                    Update update = new Update(id, org);
                    if (org != null) update.makeValid();
                    else System.out.println("Элемент не был изменен.");
                    return update;
                } else {
                    System.out.println(packageWithOldOrg.getServerFeedback());
                    return new Update();
                }
            }
            else return new Update();
        }
        catch (NumberFormatException e){
            System.out.println("Комманда должна вводиться вместе со значением типа long.");
            return null;
        }
    }
}

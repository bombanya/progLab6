package please.help.commands;

import please.help.CollectionManager;
import please.help.ServerPackage;
import please.help.organizationBuilding.Organization;

import java.util.LinkedList;

public class GetOrgById extends Command{

    private static final long serialVersionUID = 20200916L;
    private final Long id;

    public GetOrgById(Long id){
        commandName = "getOrgById";
        this.id = id;
    }

    @Override
    public ServerPackage execute(CollectionManager manager) {
        for (Organization org : manager.collection){
            if (org.getId().equals(id)) return new ServerPackage(manager.collection.hashCode(),
                    null, org);
        }
        return new ServerPackage(manager.collection.hashCode(),
                "Нет элемента с таким id", null);
    }

    @Override
    public GetOrgById validateCommand(LinkedList<String[]> data, CollectionManager manager){
        return null;
    }
}

package please.help.commands;

import please.help.ClientManager;

import java.util.LinkedList;

public class GetOrgById extends Command{

    private static final long serialVersionUID = 20200916L;
    private final Long id;

    public GetOrgById(Long id){
        commandName = "getOrgById";
        this.id = id;
    }

    @Override
    public GetOrgById validateCommand(LinkedList<String[]> data, ClientManager manager){
        return null;
    }
}

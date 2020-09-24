package please.help.commands;

import please.help.ClientManager;

import java.util.LinkedList;

public class Send_commands extends Command{

    private static final long serialVersionUID = 20200916L;

    public Send_commands(){
        commandName = "send_commands";
    }

    @Override
    public Send_commands validateCommand(LinkedList<String[]> data, ClientManager manager) {
        if (data.size() == 0 || data.poll().length > 1) {
            System.out.println("Неверно введена комманда.");
            return null;
        }
        manager.getClient().sendCommands();
        return new Send_commands();
    }
}

package please.help.commands;

import please.help.ClientManager;

import java.util.LinkedList;

public class Saved_commands extends Command{

    private static final long serialVersionUID = 20200916L;

    public Saved_commands(){
        commandName = "saved_commands";
    }

    @Override
    public Saved_commands validateCommand(LinkedList<String[]> data, ClientManager manager) {
        if (data.size() == 0 || data.poll().length > 1) {
            System.out.println("Неверно введена комманда.");
            return null;
        }
        manager.getClient().printSavedCommands();
        return new Saved_commands();
    }
}

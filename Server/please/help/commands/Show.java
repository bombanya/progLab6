package please.help.commands;

import please.help.*;

import java.util.LinkedList;

/**
 * Класс для комманды show.
 * Формат комманды: show
 */
public class Show extends Command{

    private static final long serialVersionUID = 20200916L;

    public Show(){
        commandName = "show";
    }

    @Override
    public ServerPackage execute(CollectionManager manager) {
        StringBuilder feedback = new StringBuilder();
        manager.collection.forEach(p -> feedback.append(p.toString()).append("\n"));
        return new ServerPackage(manager.collection.hashCode(), feedback.toString(), null);
    }

    @Override
    public Show validateCommand(LinkedList<String[]> data, CollectionManager manager) {
        if (data.size() == 0 || data.poll().length > 1) {
            System.out.println("Неверно введена комманда.");
            return null;
        }
        Show show = new Show();
        show.makeValid();
        return show;
    }
}

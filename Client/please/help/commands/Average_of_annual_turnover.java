package please.help.commands;

import please.help.*;

import java.util.LinkedList;

/**
 * Класс для комманды average_of_annual_turnover.
 * Формат комманды: average_of_annual_turnover
 */
public class Average_of_annual_turnover extends Command{

    private static final long serialVersionUID = 20200916L;

    public Average_of_annual_turnover(){
        commandName = "average_of_annual_turnover";
    }

    @Override
    public Average_of_annual_turnover validateCommand(LinkedList<String[]> data, ClientManager manager) {
        if (data.size() == 0 || data.poll().length > 1) {
            System.out.println("Неверно введена комманда.");
            return null;
        }
        Average_of_annual_turnover average_of_annual_turnover = new Average_of_annual_turnover();
        average_of_annual_turnover.makeValid();
        return average_of_annual_turnover;
    }
}

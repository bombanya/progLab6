package please.help.commands;

import please.help.*;

import java.util.LinkedList;

/**
 * Класс для комманды count_by_annual_turnover.
 * Формат комманды: count_by_annual_turnover annualTurnover
 */
public class Count_by_annual_turnover extends Command{

    private double annual_turnover;
    private static final long serialVersionUID = 20200916L;

    public Count_by_annual_turnover(){
        commandName = "count_by_annual_turnover";
    }

    private Count_by_annual_turnover(double annual_turnover){
        commandName = "count_by_annual_turnover";
        this.annual_turnover = annual_turnover;
    }

    @Override
    public Count_by_annual_turnover validateCommand(LinkedList<String[]> data, ClientManager manager) {
        if (data.size() == 0 || data.peek().length != 2) {
            System.out.println("Неверно введена комманда.");
            data.poll();
            return null;
        }
        String[] polledCommand = data.poll();

        try{
            double value = Double.parseDouble(polledCommand[1]);
            Count_by_annual_turnover count_by_annual_turnover = new Count_by_annual_turnover(value);
            count_by_annual_turnover.makeValid();
            return count_by_annual_turnover;
        }
        catch (NumberFormatException e){
            System.out.println("Комманда должна вводиться вместе со значением типа double.");
            return null;
        }

    }
}

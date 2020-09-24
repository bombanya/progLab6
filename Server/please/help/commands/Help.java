package please.help.commands;

import please.help.*;

import java.util.LinkedList;

/**
 * Класс для комманды help.
 * Формат комманды: help
 */
public class Help extends Command{

    private static final long serialVersionUID = 20200916L;

    public Help(){
        commandName = "help";
    }

    @Override
    public ServerPackage execute(CollectionManager manager) {
        String feedback = "Список доступных команд:\n" +
                "help : вывести справку по доступным командам\n" +
                "info : вывести в стандартный поток вывода информацию о " + "" +
                "коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
                "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                "add {element} : добавить новый элемент в коллекцию\n" +
                "update id {element} : обновить значение элемента коллекции, id которого равен заданному\n" +
                "remove_by_id id : удалить элемент из коллекции по его id\n" +
                "clear : очистить коллекцию\n" +
                "save : сохранить коллекцию в файл\n" +
                "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся " +
                "команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" +
                "exit : завершить программу (без сохранения в файл)\n" +
                "add_if_max {element} : добавить новый элемент в коллекцию, если " +
                "его значение превышает значение наибольшего элемента " +
                "этой коллекции (сортировка по полю annualTurnover)\n" +
                "remove_greater {element} : удалить из коллекции все элементы, " +
                "превышающие заданный (сортировка по полю annualTurnover)\n" +
                "history : вывести последние 5 команд (без их аргументов)\n" +
                "average_of_annual_turnover : вывести среднее значение поля annualTurnover" +
                " для всех элементов коллекции\n" +
                "count_by_annual_turnover annualTurnover : вывести количество элементов, " +
                "значение поля annualTurnover которых равно заданному\n" +
                "print_field_ascending_type : вывести значения поля type всех элементов в порядке возрастания";
        return new ServerPackage(manager.collection.hashCode(), feedback, null);
    }

    @Override
    public Help validateCommand(LinkedList<String[]> data, CollectionManager manager) {
        if (data.size() == 0 || data.poll().length > 1) {
            System.out.println("Неверно введена комманда.");
            return null;
        }
        Help help = new Help();
        help.makeValid();
        return help;
    }
}

package please.help;

import please.help.commands.*;
import please.help.organizationBuilding.Organization;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Класс для управления коллекцией объектов типа {@link Organization}.
 * Хранит все необходимые для коллекции служебные данные и список доступных комманд.
 */

public class ClientManager{

    private Mode managerMode = Mode.CONSOLE;
    private int currentScriptSkip = 0;
    private String currentScript;
    private final Client client;

    private final ArrayList<Command> listOfCommands = new ArrayList<>();

    {
        listOfCommands.add(new Add());
        listOfCommands.add(new Help());
        listOfCommands.add(new Info());
        listOfCommands.add(new Show());
        listOfCommands.add(new Update());
        listOfCommands.add(new Remove_by_id());
        listOfCommands.add(new Clear());
        listOfCommands.add(new Execute_script());
        listOfCommands.add(new History());
        listOfCommands.add(new Add_if_max());
        listOfCommands.add(new Remove_greater());
        listOfCommands.add(new Average_of_annual_turnover());
        listOfCommands.add(new Count_by_annual_turnover());
        listOfCommands.add(new Print_field_ascending_type());
        listOfCommands.add(new Exit());
        listOfCommands.add(new Saved_commands());
        listOfCommands.add(new Send_commands());
    }

    private ClientManager(String ip, int port) {
        this.client = new Client(ip, port);
    }

    public static ClientManager createManager(){
        System.out.println("Введите ip и номер порта сервера в одну строку через пробел:");
        Scanner scan = new Scanner(System.in);
        while (true) {
            try {
                String[] input = scan.nextLine().trim().split("\\s+");
                if (input.length == 2) {
                    return new ClientManager(input[0], Integer.parseInt(input[1]));
                }
                System.out.println("Некорректный ввод. Повторите попытку.");
            }
            catch (NumberFormatException e){
                System.out.println("Некорректный ввод. Повторите попытку.");
            }
        }
    }

    public void start(){
        System.out.println("Вывести справку по доступным командам: 'help'.\n-----");
        LinkedList<String[]> inputList = new LinkedList<>();
        Scanner scan = new Scanner(System.in);

        while (managerMode != Mode.EXIT){
            inputList.add(scan.nextLine().trim().split("\\s+"));
            executeCommand(inputList);
        }
    }

    public boolean executeCommand(LinkedList<String[]> inputList){
        String information = "Вы можете прервать процесс установления соединения с сервером и " +
                "продолжить вводить команды.\n"
                + "Неотправленные команды сохраняются.\n" +
                "Вывести сохраненые неотправленные команды: 'saved_commands'\n" +
                "Отправить все сохраненные команды: 'send_commands'";

        for (Command c : listOfCommands) {
            if (inputList.peek()[0].equals(c.getCommandName())) {
                Command newCommand = c.validateCommand(inputList, this);
                if (newCommand != null) {
                    if (newCommand.isValid()) {
                        ServerPackage feedback = client.sendCommand(newCommand, information);
                        if (feedback != null) System.out.println(feedback.getServerFeedback());
                        else client.addCommand(newCommand);
                    }
                    if (!(c.getCommandName().equals("execute_script"))) System.out.println("-----");
                    return true;
                } else {
                    System.out.println("-----");
                    return false;
                }
            }
        }
        System.out.println("Некорректный ввод. Повторите попытку.\n-----");
        inputList.poll();
        return false;

    }

    /**
     * Устанавливает значение поля managerMode.
     * @param managerMode режим работы {@link ClientManager}
     * @see Mode
     */
    public void setManagerMode(Mode managerMode) {
        this.managerMode = managerMode;
    }

    /**
     * Возвращает значение поля managerMode.
     * @return режим работы {@link ClientManager}
     * @see Mode
     */
    public Mode getManagerMode() {
        return managerMode;
    }

    /**
     * Устанавливает значение поля currentScriptSkip.
     * @param currentScriptSkip количество строк, которые будут пропущены при загрузке скрипта.
     *                          Нужно для возможности изменять файл скрипта, не прерывая его выполнение.
     */
    public void setCurrentScriptSkip(int currentScriptSkip) {
        this.currentScriptSkip = currentScriptSkip;
    }

    /**
     * Возвращает значение поля currentScriptSkip.
     * @return количество строк, которые будут пропущены при загрузке скрипта.
     */
    public int getCurrentScriptSkip() {
        return currentScriptSkip;
    }

    /**
     * Увеличение значения поля currentScriptSkip на 1.
     */
    public void incrementScriptSkip(){
        currentScriptSkip++;
    }

    /**
     * Устанавливает значение поля currentScript.
     * @param currentScript путь к файлу, из которого загружен скрипт.
     *                      Нужно для возможности изменять файл скрипта, не прерывая его выполнение.
     */
    public void setCurrentScript(String currentScript) {
        this.currentScript = currentScript;
    }

    /**
     * Возвращает значение поля currentScript.
     * @return путь к файлу, из которого загружен скрипт.
     */
    public String getCurrentScript() {
        return currentScript;
    }

    public Client getClient() {
        return client;
    }
}

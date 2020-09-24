package please.help;

import please.help.commands.*;
import please.help.organizationBuilding.Organization;
import please.help.organizationBuilding.OrganizationBuilder;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.time.LocalDateTime;

/**
 * Класс для управления коллекцией объектов типа {@link Organization}.
 * Хранит все необходимые для коллекции служебные данные и список доступных комманд.
 */

public class CollectionManager implements Serializable {

    /** Коллекция объектов типа {@link Organization}*/
    public LinkedList<Organization> collection = new LinkedList<>();
    private LocalDateTime creationDate = LocalDateTime.now();
    private final CommandsHistory history = new CommandsHistory();
    private Mode managerMode = Mode.CONSOLE;
    private String saveFile = null;
    private int currentScriptSkip = 0;
    private String currentScript;
    private Server server;

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
        listOfCommands.add(new Save());
    }

    private CollectionManager(int port) {
        this.server = new Server(port);
    }

    /**
     * Создает новый объект типа {@link OrganizationBuilder}.
     * При вызове пробует загрузить коллекцию из файла. В случае успеха возвращает новый объект.
     * В случае неудачи предлагает пользователю создать новую пустую коллекцию.
     * @param args путь к файлу с сохраненной коллекцией
     * @return {@link CollectionManager} - если коллекция успешно загрузилась из файла
     * или если пользователь согласился создать новую, null - иначе
     */
    public static CollectionManager createManager(String[] args){
        CollectionManager manager = new CollectionManager(-1);
        boolean success = JSONParser.parse(args, manager);
        if (success){
            System.out.println("Коллекция успешно загружена.");
        }
        else{
            System.out.println("Коллекция не была загружена. Создать новую? (y/n)");
            Scanner scan = new Scanner(System.in);
            while (true){
                String[] input = scan.nextLine().trim().split("\\s+");
                if (input.length == 1){
                    if (input[0].equals("y")) {
                        manager = new CollectionManager(-1);
                        break;
                    }
                    if (input[0].equals("n")) return null;
                }
                System.out.println("Некорректный ввод. Повторите попытку.");
            }
        }

        System.out.println("Введите номер порта сервера:");
        Scanner scan = new Scanner(System.in);
        while (true) {
            try {
                String[] input = scan.nextLine().trim().split("\\s+");
                if (input.length == 1) {
                    manager.setServer(new Server(Integer.parseInt(input[0])));
                    return manager;
                }
                System.out.println("Некорректный ввод. Повторите попытку.");
            }
            catch (NumberFormatException e){
                System.out.println("Некорректный ввод. Повторите попытку.");
            }
        }
    }


    public void start(){
        try {
            server.startReceiving(this);
        } catch (Exception e) {
            System.out.println("Произошла ошибка при инициализации сервера.");
        }
    }

    public boolean executeCommand(LinkedList<String[]> inputList){
        for (Command c : listOfCommands) {
            if (inputList.peek()[0].equals(c.getCommandName())) {
                Command newCommand = c.validateCommand(inputList, this);
                if (newCommand != null){
                    addCommandToHistory(newCommand);
                    if (newCommand.isValid()) {
                        System.out.println(newCommand.execute(this).getServerFeedback());
                    }
                    if (!(c.getCommandName().equals("execute_script"))) System.out.println("-----");
                    Server.logger.info("Выполнена команда {}", newCommand.getCommandName());
                    return true;
                }
                else{
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
     * Устанавливает значение поля creationDate.
     * @param date новое дата и время создания коллекции
     */
    public void setCreationDate(LocalDateTime date){
        creationDate = date;
    }

    /**
     * Возвращает значение поля creationDate
     * @return дата и время создания коллекции
     */
    public LocalDateTime getCreationDate(){
        return creationDate;
    }

    /**
     * Устанавливает значение поля saveFile.
     * @param saveFile путь к файлу, из которого загружена коллекция
     */
    public void setSaveFile(String saveFile) {
        this.saveFile = saveFile;
    }

    /**
     * Возвращает значение поля saveFile.
     * @return путь к файлу, из которого загружена коллекция
     */
    public String getSaveFile() {
        return saveFile;
    }

    /**
     * Устанавливает значение поля managerMode.
     * @param managerMode режим работы {@link CollectionManager}
     * @see Mode
     */
    public void setManagerMode(Mode managerMode) {
        this.managerMode = managerMode;
    }

    /**
     * Возвращает значение поля managerMode.
     * @return режим работы {@link CollectionManager}
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

    public void setServer(Server server) {
        this.server = server;
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

    /**
     * Выводит последние пять успешно выполненных комманд без аргументов.
     */
    public String printHistory(){
        return history.printCommands();
    }

    public void addCommandToHistory(Command com){
        history.addCommand(com);
    }
}

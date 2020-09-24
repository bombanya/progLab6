package please.help.commands;

import please.help.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Класс для комманды execute_script.
 * Формат комманды: execute_script file_name
 */
public class Execute_script extends Command{

    public Execute_script(){
        commandName = "execute_script";
    }

    /**
     * Добавляет в список данные из файла.
     * Читает файл, добавляет в список комманд строки из файла. Может пропускать какое-то
     * количество строк, что нужно для возможности изменять файл скрипта, не прерывая его выполнение.
     * @param path путь к файлу скрипта
     * @param skip число строк, которые нужно пропустить
     * @param parsedCommands список, в которые будут добавлены строки из файла
     * @return true - если файл существует и во время его чтения не было ошибок,
     * false - в ином случае
     */
    public static boolean openScript(String path, int skip, LinkedList<String[]> parsedCommands){
        File scriptFile = new File(path);
        if (!scriptFile.isFile()){
            System.out.println("Файл скрипта не обнаружен.");
            return false;
        }

        String[] parsedLines;
        try(FileReader reader = new FileReader(scriptFile)){
            char[] buff = new char[500];
            StringBuilder builder = new StringBuilder();
            int c;
            while((c = reader.read(buff)) > 0){
                if (c < 500) buff = Arrays.copyOf(buff, c);
                builder.append(String.valueOf(buff));
            }
            parsedLines = builder.toString().split("\\n");
        }
        catch (IOException e) {
            System.out.println("Ошибка во время чтения файла.");
            return false;
        }

        if (parsedLines.length < skip){
            System.out.println("Некорректные изменения в файле скрипта. Исполнение прервано.");
            return false;
        }
        else {
            parsedCommands.clear();
            for (String str : Arrays.copyOfRange(parsedLines, skip, parsedLines.length)){
                parsedCommands.add(str.trim().split("\\s+"));
            }
            return true;
        }
    }

    @Override
    public ServerPackage execute(CollectionManager manager) {
        return new ServerPackage(0, null, null);
    }

    @Override
    public Command validateCommand(LinkedList<String[]> data, CollectionManager manager){
        if (data.size() == 0 || data.peek().length != 2) {
            System.out.println("Неверно введена комманда.");
            data.poll();
            return null;
        }

        String[] polledCommand = data.poll();

        LinkedList<String[]> parsedCommands = new LinkedList<>();
        if (!(openScript(polledCommand[1], 0, parsedCommands))) return null;

        int skip = 0;
        File scriptPath = new File(polledCommand[1]);

        while (parsedCommands.size() != 0 && manager.getManagerMode() != Mode.EXIT){
            manager.setManagerMode(Mode.SCRIPT);
            manager.setCurrentScript(polledCommand[1]);
            String[] command = parsedCommands.peek();
            System.out.println(Arrays.toString(command));
            if (command.length == 2 && command[0].equals("execute_script")
                    && (command[1].equals(scriptPath.getAbsolutePath())
                    || command[1].equals(scriptPath.getName()))){
                System.out.println("Попытка запустить рекурсию. Продолжить выполнение скрипта? (y/n)");
                Scanner scan = new Scanner(System.in);
                while (true){
                    String[] input = scan.nextLine().trim().split("\\s+");
                    if (input.length == 1){
                        if (input[0].equals("y")) break;
                        if (input[0].equals("n")) {
                            manager.setManagerMode(Mode.CONSOLE);
                            return new Execute_script();
                        }
                    }
                    System.out.println("Некорректный ввод. Повторите попытку.");
                }
            }

            skip++;
            manager.setCurrentScriptSkip(skip);
            if (!(manager.executeCommand(parsedCommands))){
                System.out.println("Ошибка во время исполнения скрипта. Вы можете:\n" +
                        "- исправить некорректную строку в файле и продолжить выполнение" +
                        "(все предыдущие строки должны остаться без изменений): 'update'\n" +
                        "- пропустить некорректную строку и продолжить выполение: 'skip'\n" +
                        "- прервать исполнение скрипта (изменения в коллекции будут сохранены): 'stop'");
                Scanner scan = new Scanner(System.in);
                while (true){
                    String[] input = scan.nextLine().trim().split("\\s+");
                    if (input.length == 1){
                        if (input[0].equals("update")){
                            skip--;
                            if (!(openScript(polledCommand[1], skip, parsedCommands))) return null;
                            else break;
                        }
                        else if (input[0].equals("skip")) break;

                        else if (input[0].equals("stop")){
                            manager.setManagerMode(Mode.CONSOLE);
                            return new Execute_script();
                        }
                    }
                    System.out.println("Некорректный ввод. Повторите попытку.");
                }
            }
            else if (!command[0].equals("execute_script")) skip = manager.getCurrentScriptSkip();
        }
        if (manager.getManagerMode() != Mode.EXIT) manager.setManagerMode(Mode.CONSOLE);
        return new Execute_script();
    }
}

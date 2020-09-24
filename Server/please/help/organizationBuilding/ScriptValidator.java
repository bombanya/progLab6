package please.help.organizationBuilding;

import please.help.CollectionManager;
import please.help.commands.Execute_script;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class ScriptValidator {

    public static Organization buildFromScript(Organization org, LinkedList<String[]> data, CollectionManager manager){
        int iterator = 0;

        Long id = null;
        LocalDateTime creationDate = null;

        if (org != null) {
            id = org.getId();
            creationDate = org.getCreationDate();
        }

        OrganizationBuilder builder = OrganizationBuilder.createBuilder(id, creationDate);
        if (builder == null){
            System.out.println("Произошло что-то очень плохое.");
            return null;
        }

        while (iterator != 7){
            if (data.size() == 0){
                System.out.println("В файле больше нет данных, но очередной объект создан не полностью.");
            }
            else {
                String[] input = data.peek();
                System.out.println(Arrays.toString(input));

                if (input.length != 1) {
                    System.out.println("Ошибка: поля нужно вводить по одному значению в строку.");
                } else {
                    if (builder.addField(input[0])){
                        iterator++;
                        manager.incrementScriptSkip();
                        data.poll();
                        continue;
                    }
                }
            }

            System.out.println("Ошибка во время добавления нового объекта. Вы можете:\n" +
                    "- исправить некорректную строку в файле и продолжить выполнение" +
                    "(все предыдущие строки должны остаться без изменений): 'update'\n" +
                    "- пропустить некорректную строку и продолжить выполение: 'skip'\n" +
                    "- прервать добавление нового объекта: 'stopInit'");
            Scanner scan = new Scanner(System.in);
            while (true){
                String[] input = scan.nextLine().trim().split("\\s+");
                if (input.length == 1){
                    if (input[0].equals("update")){
                        if (!(Execute_script.openScript(manager.getCurrentScript(),
                                manager.getCurrentScriptSkip(), data))) return null;
                        else break;
                    }
                    else if (input[0].equals("skip")) {
                        if (data.size() != 0) {
                            data.poll();
                            manager.incrementScriptSkip();
                        }
                        break;
                    }

                    else if (input[0].equals("stopInit")){
                        OrganizationBuilder.deleteId(builder.getId());
                        return null;
                    }
                }
                System.out.println("Некорректный ввод. Повторите попытку.");
            }
        }
        return builder.getOrganization();
    }
}

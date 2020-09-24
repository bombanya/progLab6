package please.help;

import org.json.JSONException;
import org.json.JSONObject;
import please.help.commands.Command;
import please.help.organizationBuilding.OrganizationBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Класс, обеспечивающий загрузку коллекции из json файла.
 */

public class JSONParser {

    private static LocalDateTime parseDate(JSONObject objForDate) throws JSONException, DateTimeException{
        int year = objForDate.getInt("year");
        int month = objForDate.getInt("month");
        int day = objForDate.getInt("day");
        int hour = objForDate.getInt("hour");
        int minute = objForDate.getInt("minute");
        int second = objForDate.getInt("second");
        return LocalDateTime.of(year, month, day, hour, minute, second);
    }

    /**
     * Парсит json файл, в котором коллекция записана так же,
     * как ее сохраняет метод {@link Command#execute(CollectionManager)}.
     *
     * Обрабатывает следующие возможные ошибки:
     * <p>- передана пустая строка вместо пути к файлу;</p>
     * <p>- передано несколько отдельных строк;</p>
     * <p>- по переданному пути не существует файла или во время его чтения происходит ошибка;</p>
     * <p>- всевозможные ошибки при разборе полей объектов.</p>
     * <p>При обработке любой ошибки выводится сообщение, поясняющее, что произошло.</p>
     * @param args путь к файлу с сохраненной коллекцией
     * @param manager {@link CollectionManager}, в который нужно передать все объекты
     * @return false - во время парсинга произошла какая-либо ошибка, true - коллекция успешно загружена
     */
    public static boolean parse(String[] args, CollectionManager manager){
        if (args.length == 0){
            System.out.println("Имя файла для загрузки коллекции можно передать с помощью аргумента коммандной строки.");
            return false;
        }
        if (args.length > 1){
            System.out.println("Программа может принять только один аргумент коммандной строки.");
            return false;
        }

        File file = new File(args[0]);
        if (!file.isFile()){
            System.out.println("Ошибка: файл не существует.");
            return false;
        }

        String toParse;

        try(FileReader reader = new FileReader(file)){
            char[] buff = new char[500];
            StringBuilder builder = new StringBuilder();
            int c;
            while((c = reader.read(buff)) > 0){
                if (c < 500){
                    buff = Arrays.copyOf(buff, c);
                }
                builder.append(String.valueOf(buff));
            }
            toParse = builder.toString();
        }
        catch (IOException e) {
            System.out.println("Ошибка во время чтения файла.");
            return false;
        }

        manager.setSaveFile(args[0]);

        try{
            JSONObject json = new JSONObject(toParse);
            manager.setCreationDate(parseDate(json.getJSONObject("creationDate")));

            for (String key : JSONObject.getNames(json)){
                if (!key.equals("creationDate")) {
                    Long id = Long.parseLong(key);

                    JSONObject anotherOrg = json.getJSONObject(key);

                    LocalDateTime date = parseDate(anotherOrg.getJSONObject("creationDate"));
                    OrganizationBuilder builder = OrganizationBuilder.createBuilder(id, date);

                    if (builder == null) return false;
                    else{
                        boolean flag;
                        JSONObject objForCoord = anotherOrg.getJSONObject("coordinates");
                        JSONObject objForAddress = anotherOrg.getJSONObject("officialAddress");

                        flag = builder.addField(anotherOrg.getString("name"));
                        if (flag) flag = builder.addField(objForCoord.get("x").toString());
                        if (flag) flag = builder.addField(objForCoord.get("y").toString());
                        if (flag) flag = builder.addField(anotherOrg.get("annualTurnover").toString());
                        if (flag) flag = builder.addField(anotherOrg.getString("type"));
                        if (flag) {
                            if (objForAddress.get("street") == JSONObject.NULL){
                                flag = builder.addField("");
                            }
                            else flag = builder.addField(objForAddress.getString("street"));
                        }
                        if (flag) {
                            if (objForAddress.get("zipCode") == JSONObject.NULL){
                                flag = builder.addField("");
                            }
                            else flag = builder.addField(objForAddress.getString("zipCode"));
                        }

                        if (!flag) return false;
                    }

                    manager.collection.add(builder.getOrganization());
                }
            }
            return true;
        }
        catch (DateTimeException e){
            System.out.println("Ошибка при чтении даты.");
            return false;
        }
        catch (JSONException e){
            System.out.println("Ошибка: в файле не хватает необходимых полей или они содержат некорректные данные.");
            return false;
        }
    }
}

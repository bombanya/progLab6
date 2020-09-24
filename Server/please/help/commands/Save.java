package please.help.commands;

import org.json.JSONObject;
import please.help.*;
import please.help.organizationBuilding.Organization;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;

/**
 * Класс для комманды save.
 * Формат комманды: save
 */
public class Save extends Command{

    private static final long serialVersionUID = 20200916L;

    public Save(){
        commandName = "save";
    }

    private JSONObject objForLocalDateTime(LocalDateTime date){
        return new JSONObject().put("year", date.getYear()).put("month", date.getMonthValue())
                .put("day", date.getDayOfMonth()).put("hour", date.getHour())
                .put("minute", date.getMinute()).put("second", date.getSecond());
    }

    /**
     * Метод для основной функциоальности комманды.
     * Метод создает json файл следующего формата:
     * <p>{"creationDate": {"year": int, "month": int, "day": int, "hour": int, "minute": int, "second": int},</p>
     * <p>"id": {"name": String, "annualTurnover": double, "type": OrganizationType, </p>
     * <p>"coordinates": {"x": int, "y": int},</p>
     * <p>"creationDate": {"year": int, "month": int, "day": int, "hour": int, "minute": int, "second": int},</p>
     * <p>"officialAddress": {"street": String, "zipCode": String},}</p><p>...}</p>
     * Если была создана новая коллекция, метод создаст новый файл под сохранение.
     *
     * @param manager@return true - комманда успешно выполнена, false - в ином случае
     */
    @Override
    public ServerPackage execute(CollectionManager manager) {
        JSONObject json = new JSONObject();

        LocalDateTime creationDateOfCollection = manager.getCreationDate();
        json.put("creationDate", objForLocalDateTime(creationDateOfCollection));

        for (Organization org : manager.collection){
            JSONObject objForElement = new JSONObject();
            objForElement.put("name", org.getName());

            JSONObject objForCoord = new JSONObject();
            objForCoord.put("x", org.getCoordinates().getX()).put("y", org.getCoordinates().getY());
            objForElement.put("coordinates", objForCoord);

            LocalDateTime date = org.getCreationDate();
            objForElement.put("creationDate", objForLocalDateTime(date));

            objForElement.put("annualTurnover", org.getAnnualTurnover());
            objForElement.put("type", org.getType().toString());


            JSONObject objForAddress = new JSONObject();
            if (org.getOfficialAddress().getStreet() == null) objForAddress.put("street", JSONObject.NULL);
            else objForAddress.put("street", org.getOfficialAddress().getStreet());
            if (org.getOfficialAddress().getZipCode() == null) objForAddress.put("zipCode", JSONObject.NULL);
            else objForAddress.put("zipCode", org.getOfficialAddress().getZipCode());
            objForElement.put("officialAddress", objForAddress);

            json.put(org.getId().toString(), objForElement);
        }

        File saveFile;
        if (manager.getSaveFile() != null) {
            saveFile = new File(manager.getSaveFile());
            if (!saveFile.isFile()){
                return new ServerPackage(manager.collection.hashCode(),
                        "Файл сохранения не обнаружен.", null);
            }
        }
        else{
            int i = 0;
            saveFile = new File("organizations.json");
            while (saveFile.isFile()){
                i++;
                saveFile = new File("organizations" + i + ".json");
            }
            System.out.println("Будет создан новый файл сохранения " + saveFile.getName());
        }

        try(FileOutputStream writer = new FileOutputStream(saveFile)){
            byte[] buffer = json.toString().getBytes();
            writer.write(buffer, 0, buffer.length);
            return new ServerPackage(manager.collection.hashCode(),
                    "Коллекция сохранена.", null);
        }
        catch (IOException e) {
            return new ServerPackage(manager.collection.hashCode(),
                    "Во время сохранения произошла ошибка.", null);
        }
    }

    @Override
    public Save validateCommand(LinkedList<String[]> data, CollectionManager manager) {
        if (data.size() == 0 || data.poll().length > 1) {
            System.out.println("Неверно введена комманда.");
            return null;
        }
        Save save = new Save();
        save.makeValid();
        return save;
    }
}

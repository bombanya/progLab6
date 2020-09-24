package please.help.organizationBuilding;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
/**
 * Класс, конструирующий объекты типа {@link Organization}.
 * Хранит значения всех полей, которые необходимы для инициализации объекта, обеспечивает их корректность.
 * Также хранит список всех занятых id, который используется для генерации новых id и проверки их
 * уникальности при считывании коллекции из файла.
 */

public class OrganizationBuilder {

    private final Long id;
    private final LocalDateTime creationDate;
    private final static ArrayList<Long> idList = new ArrayList<>(Arrays.asList(0L));

    private String name;
    private int x;
    private int y;
    private double annualTurnover;
    private OrganizationType type;
    private String street;
    private String zipCode;
    private int iterator = 0;

    private OrganizationBuilder(Long id, LocalDateTime creationDate){
        this.id = id;
        this.creationDate = creationDate;
    }

    /**
     * Удаляет id из списка занятых id. Используется при удалении объекта из коллекции.
     * @param id, который нужно удалить
     */
    public static void deleteId(Long id){
        idList.remove(id);
    }

    /**
     * Добавляет id в список занятых.
     * @param id, который нужно добавить
     */
    public static void addId(Long id){
        idList.add(id);
    }

    /**
     * Возвращает id конструируемого объекта.
     * @return id
     */
    public Long getId() {
        return id;
    }

    public static Long generateId(){
        Long res = Collections.min(idList) + 1;
        while (idList.contains(res)){
            if (res.equals(Long.MAX_VALUE)){
                System.out.println("Ошибка: коллекция полностью заполнена.");
                return null;
            }
            else res++;
        }
        idList.add(res);
        return res;
    }

    /**
     * Возвращает новый объект {@link OrganizationBuilder}.
     * Принимает значения id и creationDate, которые будут использованы для конструирования новой
     * {@link Organization} (может быть использовано для обновления объекта). Если эти значения равны null,
     * то автоматически генерирует корректные значения. Если принимает уже занятый id,
     * то вернет null и выведет сообщение об ошибке.
     * @param id для {@link Organization}
     * @param creationDate для {@link Organization}
     * @return {@link OrganizationBuilder}, если объект успешно создан, null - при ошибке
     */
    public static OrganizationBuilder createBuilder(Long id, LocalDateTime creationDate){
        if (id != null){
            if (id <= 0 || idList.contains(id)){
                System.out.println("Ошибка: элемент с таким id уже существует.");
                return null;
            }
            else{
                idList.add(id);
                return new OrganizationBuilder(id, creationDate);
            }
        }
        else{
            id = Collections.min(idList) + 1;
            while (idList.contains(id)){
                if (id.equals(Long.MAX_VALUE)){
                    System.out.println("Ошибка: коллекция полностью заполнена.");
                    return null;
                }
                else id++;
            }
            idList.add(id);
            return new OrganizationBuilder(id, LocalDateTime.now());
        }
    }

    /**
     * Заполняет поля объекта.
     * Вызов этого метода с корректной строкой инициализирует очередное поле конструируемого
     * объекта. То есть для успешного создания объекта нужно вызвать этот метод как минимум 7 раз.
     * Контролирует корректность вводимых данных в соответствии с требованиями ({@link Organization},
     * {@link Address}, {@link Coordinates}). В случае ошибки выводит сообщение, поясняющее, что произошло.
     * В соответствии с условием, поля нужно вводить по одному значению в строку.
     * @param unparsedField строковое представление значения для очередного поля. Метод способен
     *                      обрабатывать всевозможные ошибки ввода
     * @return true - очередное поле успешно инициализировано, false - при ошибке
     */
    public boolean addField(String unparsedField){
        try {
            if (unparsedField.trim().split("\\s+").length != 1) {
                System.out.println("Ошибка: поля нужно вводить по одному значению в строку.");
                return false;
            } else {
                String parsedField = unparsedField.trim().split("\\s+")[0];
                if (parsedField.equals("")) parsedField = null;

                if (parsedField == null && iterator < 5) {
                    System.out.println("Ошибка: поле не может быть пусто.");
                    return false;
                }

                if (iterator == 0) name = parsedField;

                else if (iterator == 1) {
                    int x = Integer.parseInt(parsedField);
                    if (x > 765) {
                        System.out.println("Ошибка: максимальное значение поля: 765");
                        return false;
                    }
                    else this.x = x;
                }

                else if (iterator == 2){
                    int y = Integer.parseInt(parsedField);
                    if (y > 450) {
                        System.out.println("Ошибка: максимальное значение поля: 450");
                        return false;
                    }
                    else this.y = y;
                }

                else if (iterator == 3){
                    double annualTurnover = Double.parseDouble(parsedField);
                    if (annualTurnover <= 0){
                        System.out.println("Ошибка: значение поля должно быть больше 0");
                        return false;
                    }
                    else this.annualTurnover = annualTurnover;
                }

                else if (iterator == 4) type = OrganizationType.valueOf(parsedField);
                else if (iterator == 5) street = parsedField;
                else if (iterator == 6){
                    if (parsedField != null && parsedField.length() < 4){
                        System.out.println("Ошибка: длина строки должна быть не меньше 4");
                        return false;
                    }
                    else zipCode = parsedField;
                }

                else{
                    System.out.println("Ошибка: значения всех полей уже введены");
                    return false;
                }

                iterator++;
                return true;
            }
        }
        catch (IllegalArgumentException e){
            System.out.println("Ошибка: значение не соответствует типу поля.");
            return false;
        }
    }

    /**
     * Возвращает новый объект типа {@link Organization}.
     * @return {@link Organization}, если все необходимые поля инициализированы, null - иначе
     */
    public Organization getOrganization(){
        if (iterator < 6){
            return null;
        }
        else{
            Address officialAddress = new Address(street, zipCode);
            Coordinates coordinates = new Coordinates(x, y);
            Organization org = new Organization(name, coordinates, annualTurnover, type, officialAddress);
            org.setId(id);
            org.setCreationDate(creationDate);
            return org;
        }
    }
}

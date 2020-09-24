package please.help.organizationBuilding;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Класс, экземпляры которого хранятся в коллекции.
 */

public class Organization implements Comparable<Organization>, Serializable {

    private static final long serialVersionUID = 20200916L;

    private Long id;
    private LocalDateTime creationDate;

    private final String name;
    private final Coordinates coordinates;
    private final double annualTurnover;
    private final OrganizationType type;
    private final Address officialAddress;

    /**
     * @param name название компании. Поле не может быть null. Строка не может быть пустой
     * @param coordinates координаты компании ({@link Coordinates}). Поле не может быть null
     * @param annualTurnover значение годового оборота компании. Поле не может быть null.
     *                      Значение поля должно быть больше 0
     * @param type тип компании ({@link OrganizationType}). Поле не может быть null
     * @param officialAddress адрес компании ({@link Address}. Поле не может быть null
     */

    public Organization(String name, Coordinates coordinates,
                        double annualTurnover, OrganizationType type, Address officialAddress ){
        this.name = name;
        this.coordinates = coordinates;
        this.annualTurnover = annualTurnover;
        this.type = type;
        this.officialAddress = officialAddress;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Возвращает значение поля name
     * @return имя
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает значение поля coordinates
     * @return координаты компании ({@link Coordinates})
     */
    public Coordinates getCoordinates(){
        return coordinates;
    }

    /**
     * Возвращает значение поля annualTurnover
     * @return годовой оборот компании
     */
    public double getAnnualTurnover() {
        return annualTurnover;
    }

    /**
     * Возвращает значение поля type
     * @return тип компании ({@link OrganizationType})
     */
    public OrganizationType getType() {
        return type;
    }

    /**
     * Возвращает значение поля officialAddress
     * @return адрес компании ({@link Address})
     */
    public Address getOfficialAddress() {
        return officialAddress;
    }

    /**
     * Возвращает значение поля creationDate
     * @return дата и время создания объекта
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Возвращает значение поля id
     * @return id компании
     */
    public Long getId() {
        return id;
    }

    /**
     * Возвращает массив из строковых представлений каждого поля объекта
     * @return массив строковых представлений каждого поля объекта
     * (координаты x и y добавляются отдельно, аналогично с названием улицы и индексом)
     */
    public String[] getAllFields(){
        return new String[]{
                getName(), String.valueOf(getCoordinates().getX()),
                String.valueOf(getCoordinates().getY()), String.valueOf(getAnnualTurnover()),
                getType().toString(), getOfficialAddress().getStreet(), getOfficialAddress().getZipCode()
        };
    }

    /**
     * Сравнивает две организации по полю annualTurnover
     * @param other другая организация
     * @return целое число больше 0, если this больше other;
     * <p>целое число меньше 0, если this меньше other;</p>
     * <p>0, если они равны.</p>
     */
    @Override
    public int compareTo(Organization other) {
        return Double.compare(annualTurnover, other.getAnnualTurnover());
    }

    /**
     * Сравнивает объект типа Organization с другим объектом.
     * @param other сравниваемый объект.
     * @return true - если два Organization равны. false - в ином случае.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null) return false;
        if (getClass() != other.getClass()) return false;
        return ((Organization) other).getId().equals(this.id);
    }

    /**
     * Возвращает хэш объекта.
     * @return хэш объекта.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, creationDate);
    }

    /**
     * Возвращает строковое представление адреса.
     * @return строка формата
     * <p>"Organization{ id = Long; creationDate = LocalDateTime, name = String,</p>
     * <p>Coordinates = Coordinates, annualTurnover = double, type = OrganizationType,</p>
     * <p>officialAddress = Address }"</p>
     */
    @Override
    public String toString() {
        return "Organization{ " + "id = " + id + "; creationDate = " + creationDate
                + "; name = " + name + "; coordinates = " + coordinates
                + "; annualTurnover = " + annualTurnover + "; type = " + type
                + "; officialAddress = " + officialAddress + " }";
    }
}

package please.help.organizationBuilding;

import java.io.Serializable;
import java.util.Objects;

/**
 * Класс адреса с полями street и zipCode.
 * Используется в классе {@link Organization}.
 */

public class Address implements Serializable {

    private static final long serialVersionUID = 20200916L;
    private final String street;
    private final String zipCode;

    /**
     * @param street улица. По условию может быть null.
     * @param zipCode индекс. По условию длина строки должна быть не меньше 4, может быть null.
     */
    public Address(String street, String zipCode){
        this.street = street;
        this.zipCode = zipCode;
    }

    /**
     * Возвращает значение поля street.
     * @return название улицы.
     */
    public String getStreet(){
        return street;
    }

    /**
     * Возвращает значение поля zipCode.
     * @return индекс.
     */
    public String getZipCode(){
        return zipCode;
    }

    /**
     * Сравнивает объект типа Address с другим объектом.
     * @param other сравниваемый объект.
     * @return true - если два Address равны. false - в ином случае.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null) return false;
        if (getClass() != other.getClass()) return false;
        return ((Address) other).getStreet().equals(this.street) &&
                ((Address) other).getZipCode().equals(this.zipCode);
    }

    /**
     * Возвращает хэш объекта.
     * @return хэш объекта.
     */
    @Override
    public int hashCode() {
        return Objects.hash(street, zipCode);
    }

    /**
     * Возвращает строковое представление адреса.
     * @return строка формата "Address{ street = String; zipCode = String}"
     */
    @Override
    public String toString() {
        return "Address{ " + "street = " + street + "; zipCode = " + zipCode + " }";
    }
}

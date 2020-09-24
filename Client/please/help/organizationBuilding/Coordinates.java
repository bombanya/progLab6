package please.help.organizationBuilding;

import java.io.Serializable;
import java.util.Objects;

/**
 * Класс, хранящий координаты x и y.
 * Используется в классе {@link Organization}.
 */

public class Coordinates implements Serializable {

    private static final long serialVersionUID = 20200916L;
    private final int x;
    private final int y;

    /**
     * @param x координата x. Максимальное значение: 765.
     * @param y кооридната y. Максимальное значение: 450.
     */
    public Coordinates(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * Возвращает координату x.
     * @return координата x.
     */
    public int getX(){
        return x;
    }

    /**
     * Возвращает координату y.
     * @return координата y.
     */
    public int getY(){
        return y;
    }

    /**
     * Сравнивает объект типа Coordinates с другим объектом.
     * @param other сравниваемый объект.
     * @return true - если два Coordinates равны. false - в ином случае.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null) return false;
        if (getClass() != other.getClass()) return false;
        return ((Coordinates) other).getX() == this.x &&
                ((Coordinates) other).getY() == this.y;
    }

    /**
     * Возвращает хэш объекта.
     * @return хэш объекта.
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Возвращает строковое представление координат.
     * @return строка формата "Coordinates{ x = int; y = int}"
     */
    @Override
    public String toString() {
        return "Coordinates{ " + "x = " + x + "; y = " + y + " }";
    }
}

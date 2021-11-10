package lib.types;

import lib.io.CheckedReader;
import lib.io.xml.UnmarshalCheckable;

import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.util.Objects;

/**
 * Класс координат
 */
public class Coordinates implements UnmarshalCheckable {
    private Long x;     // Поле не может быть null
    private double y;

    private Coordinates() {}

    /**
     * Создание координат.
     * @param   x координата x
     * @param   y координата y
     * @throws  ValueException если поле равно {@code null}
     */
    @SuppressWarnings("unused")
    public Coordinates(Long x, double y) {
        setX(x);
        setY(y);
    }

    /**
     * Ввод и создание новых координат
     * @param reader {@link CheckedReader}, с помощью которого производится ввод
     * @return новые координаты
     * @throws IOException при неудачной или прерванной операции ввода
     * @throws ValueException при вводе некорректных данных
     */
    public static Coordinates fromReader(CheckedReader reader) throws IOException {
        reader.printPrompt("  Координаты:\n");
        Coordinates result = new Coordinates();
        reader.readValue(Long.class, result::setX, "    x: ");
        reader.readValue(double.class, result::setY, "    y: ");
        return result;
    }

    /**
     * @return x
     */
    @SuppressWarnings("unused")
    public Long getX() {
        return x;
    }

    /**
     * @return y
     */
    @SuppressWarnings("unused")
    public double getY() {
        return y;
    }

    /**
     *
     * @param x координата x
     */
    public void setX(Long x) {
        if (x == null)
            throw new ValueException("Координата х не может быть null");
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unused")
    @Override
    public void afterUnmarshal(Unmarshaller u, Object parent) {
        if (x == null)
            throw new IllegalArgumentException();
    }

    /**
     * Сравнение на равенство объектов координат.
     * @param object сравниваемый объект
     * @return {@code true}, если равно; иначе {@code false}
     */
    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object instanceof Coordinates) {
            Coordinates coord = (Coordinates) object;
            return x.equals(coord.x) && y == coord.y;
        }
        return false;
    }

    /**
     * @return хэш-код
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Представление в виде {x, y}
     * @return Строка-представление объекта класса
     */
    @Override
    public String toString() {
        return "{" + x + ", " + y + '}';
    }
}

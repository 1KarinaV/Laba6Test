package lib.io.xml;

import javax.xml.bind.MarshalException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Класс-адаптер для преобразования даты в строку и обратно.
 */
public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
    /**
     * Преобразовывать объект класса LocalDate в строку.
     * Используется JAXB-ом, при сохранении коллекции в файл.
     */
    public String marshal(LocalDate v) throws MarshalException {
        try {
            return v.toString();
        } catch (NullPointerException e) {
            throw new MarshalException(e.getMessage());
        }
    }

    /**
     * Преобразовывать строку в объект класса LocalDate.
     * Используется JAXB-ом, при восстановлении коллекции из файла.
     */
    public LocalDate unmarshal(String v) throws UnmarshalException {
        try {
            return LocalDate.parse(v);
        } catch (DateTimeParseException e) {
            throw new UnmarshalException(v);
        }
    }
}

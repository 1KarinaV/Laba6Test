package lib.io.xml;

import javax.xml.bind.*;
import javax.xml.namespace.QName;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Path;

/**
 * Класс предоставляет методы сохранения в файл и загрузки из файла
 * данных с помощью JAXB.
 */
public class XmlData {
    /**
     * Сохранение свойств указанного объекта в файл.
     * @param obj объект сериализации
     * @param path путь к файлу
     * @param <T> тип объекта
     * @throws JAXBException если классы данных с неверными JAXB аннотациями
     *          или другие неожиданные ошибки при работе JAXB
     */
    public static <T> void marshall(T obj, Path path) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(obj.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(obj, path.toFile());
    }

    public static <T> String marshall(Class<T> tClass, T obj, String elemName) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(tClass);
        Marshaller marshaller = context.createMarshaller();

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

        QName qName = new QName("", elemName);
        JAXBElement<T> elem = new JAXBElement<>(qName, tClass, obj);

        StringWriter stringWriter = new StringWriter();
        marshaller.marshal(elem, stringWriter);
        return stringWriter.toString();
    }

    /**
     * Загрузка свойств из указанного файла в объект.
     * @param tClass объект класса возвращаемого типа
     * @param path путь к файлу
     * @param <T> тип возвращаемого объекта
     * @return загруженный объект
     * @throws JAXBException если классы данных с неверными JAXB аннотациями
     *          или другие неожиданные ошибки при работе JAXB
     */
    @SuppressWarnings("unchecked")
    public static <T> T unmarshall(Class<T> tClass, Path path) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(tClass);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (T) unmarshaller.unmarshal(path.toFile());
    }

    @SuppressWarnings("unchecked")
    public static <T> T unmarshall(Class<T> tClass, String str) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(tClass);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (T) unmarshaller.unmarshal(new StringReader(str));
    }

}

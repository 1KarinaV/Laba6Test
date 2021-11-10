package lib.io.xml;

import javax.xml.bind.Unmarshaller;

/**
 * Применяется к классам, десериализуемым из XML с помощью JAXB.
 */
public interface UnmarshalCheckable {
    /**
     * Метод обратного вызова, вызываемый после анмаршалинга
     * всех свойств, но до их присваивания данному объекту.
     * @param u объект Unmarshaller, с помощью которого производится десериализация
     * @param parent объект, в который будут записаны полученный свойства
     */
    @SuppressWarnings("unused")
    void afterUnmarshal(Unmarshaller u, Object parent);
}

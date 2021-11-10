package lib.types;

import lib.io.xml.LocalDateAdapter;
import lib.io.xml.UnmarshalCheckable;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Класс адаптер коллекции музыкальных групп.
 */
@XmlRootElement(name = "collection")
public class MusicBandCollection implements UnmarshalCheckable {
    @XmlElement(name = "band")
    private ArrayDeque<MusicBand> collection;
    @XmlAttribute
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private final LocalDate init_date;

    /**
     * Создание новой коллекции музыкальных групп.
     */
    public MusicBandCollection() {
        collection = new ArrayDeque<>();
        init_date = LocalDate.now();
    }

    /**
     * Сортировка коллекции по умолчанию.
     */
    public void sort() {
        sort(Comparator.comparing(MusicBand::getId));
    }

    /**
     * Сортировка коллекции.
     * @param cmp объект компаратор
     */
    public void sort(Comparator<? super MusicBand> cmp) {
        collection = stream()
                .sorted(cmp)
                .collect(Collectors.toCollection(ArrayDeque::new));
    }

    /**
     * Удаление всех элементов, удовлетворяющих данному предикату.
     * @param filter предикат
     * @return {@code true}, если удаление произведено; иначе {@code false}
     */
    public boolean removeIf(Predicate<? super MusicBand> filter) {
        return collection.removeIf(filter);
    }

    public void clear() {
        collection.clear();
    }

    /**
     * Добавление группы в коллекцию.
     * @param band добавляемая группа
     * @return {@code true}
     */
    public boolean add(MusicBand band) {
        band.setId();
        boolean result = collection.add(band);
        sort();
        return result;
    }

    /**
     * @return дата создания коллекции.
     */
    public LocalDate initDate() {
        return init_date;
    }

    /**
     * @return количество элементов в коллекции
     */
    public int size() {
        return collection.size();
    }

    /**
     * @return {@code true}, если коллекция пуста; иначе {@code false}
     */
    public boolean isEmpty() {
        return collection.isEmpty();
    }

    /**
     * @return последовательный {@code Stream} с данной коллекцией в роли источника
     */
    public Stream<MusicBand> stream() {
        return collection.stream();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unused")
    @Override
    public void afterUnmarshal(Unmarshaller u, Object parent) {
        if (stream().map(MusicBand::getId).distinct().count() != size()
                || !collection.isEmpty() && init_date == null)
            throw new IllegalStateException();
        sort();
    }

    /**
     * Возвращает объект класса коллекции - внутреннего представления.
     * @return объект класса коллекции
     */
    public static Class<?> getUnderlyingType() {
        return ArrayDeque.class;
    }
}

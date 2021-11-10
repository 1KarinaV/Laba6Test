package lib.db;

import lib.io.xml.XmlData;
import lib.types.MusicBand;
import lib.types.MusicBandCollection;

import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;
import javax.xml.bind.UnmarshalException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * База данных музыкальных групп.
 * Класс инкапсулирует работу с коллекцией и соответствующим ей XML файлом.
 * (Де)сериализация данных коллекции происходит с помощью JAXB.
 */
public class Database {
    private final Path path;
    private final MusicBandCollection collection;

    /**
     * Создание объекта с элементами данных, расположенных в файле.
     * @param strPath путь к файлу с данными коллекции
     * @throws DatabaseException если происходит ошибка работы с файлом
     * @throws JAXBException если классы данных с неверными JAXB аннотациями
     *          или другие неожиданные ошибки при работе JAXB
     */
    public Database(String strPath) throws DatabaseException, JAXBException {
        path = Paths.get(strPath).normalize().toAbsolutePath();
        if (Files.exists(path)) {
            try {
                collection = XmlData.unmarshall(MusicBandCollection.class, path);
            } catch (UnmarshalException e) {
                throw new DatabaseException("Файл поврежден или имеет неверный формат: " + path);
            }
        }
        else {
            collection = new MusicBandCollection();
            save();
        }
    }

    /**
     * Запись данных в файл.
     * @throws DatabaseException если происходит ошибка работы с файлом
     * @throws JAXBException если классы данных с неверными JAXB аннотациями
     *          или другие неожиданные ошибки при работе JAXB
     */
    public void save() throws DatabaseException, JAXBException {
        if (Files.notExists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                throw new DatabaseException("Невозможно создать файл: " + path);
            }
        }

        if (!Files.isWritable(path))
            throw new DatabaseException("Запись в файл невозможна: " + path);

        try {
            XmlData.marshall(collection, path);
        } catch (MarshalException e) {
            throw new DatabaseException("Ошибка сохранения в файл: " + path + ": " + e.getMessage());
        }
    }

    /**
     * Вывод на экран всех групп.
     */
    public String show() {
        if (collection.isEmpty())
            return "Коллекция пуста.";
        return collection.stream()
                .map(MusicBand::toString)
                .collect(Collectors.joining("\n"));
    }

    /**
     * Поиск группы MusicBand по {@code id}.
     * @param id искомый id
     * @return найденный элемент, либо null
     */
    public MusicBand find(Integer id) {
        return collection.stream()
                .filter(x -> Objects.equals(x.getId(), id))
                .findAny()
                .orElse(null);
    }

    /**
     * Вывести значения дат основания всех групп в порядке убывания.
     */
    public String showEstablishmentDates() {
        return collection.stream()
                .map(MusicBand::getEstablishmentDate)
                .sorted(Comparator.reverseOrder())
                .map(LocalDate::toString)
                .collect(Collectors.joining("\n"));
    }

    /**
     * Вывести сумму участников всех групп.
     * @return сумма участников групп
     */
    public long getParticipantsSum() {
        return collection.stream()
                .map(MusicBand::getNumberOfParticipants)
                .reduce(0L, Long::sum);
    }

    /**
     * Вывести среднее число участников во всех группах.
     * @return среднее число участников
     */
    public long getParticipantsAvg() {
        if (collection.isEmpty())
            return 0;
        return getParticipantsSum() / collection.size();
    }

    /**
     * Добавление группы в коллекцию.
     * @param band добавляемая группа
     * @return {@code true}
     */
    public boolean add(MusicBand band) {
        return collection.add(band);
    }

    /**
     * Добавление группы в коллекцию, если ее значение превышает
     * значение наибольшего элемента этой коллекции.
     * @param band добавляемая группа
     * @return {@code true}, если добавлена; иначе, {@code false}
     */
    public boolean addIfMax(MusicBand band) {
        MusicBand max = collection.stream().max(Comparator.naturalOrder()).orElse(null);
        if (max == null || band.compareTo(max) > 0)
            return add(band);
        return false;
    }

    /**
     * Удаление группы по id.
     * @param id удаляемый id
     * @return {@code true}, если удалена; иначе, {@code false}
     */
    public boolean remove(Integer id) {
        return collection.removeIf(x -> Objects.equals(x.getId(), id));
    }

    /**
     * Удалить из коллекции все группы, превышающие заданную.
     * @param mb заданная группа
     * @return количество удаленных групп
     */
    public int removeGreater(MusicBand mb) {
        int initial = collection.size();
        collection.removeIf(x -> x.compareTo(mb) > 0);
        return initial - collection.size();
    }

    /**
     * Очистить коллекцию
     */
    public void clear() {
        collection.clear();
    }

    /**
     * Строковое представление в виде информации о коллекции и данных.
     * @return строковое представление
     */
    @Override
    public String toString() {
        return "Информация о коллекции:" +
                "\n  файл данных: " + path +
                "\n  дата инициализации: " + collection.initDate() +
                "\n  тип коллекции: " + collection.getClass().getSimpleName() +
                "\n  тип внутреннего представления коллекции: " + MusicBandCollection.getUnderlyingType().getSimpleName() +
                "\n  тип элементов: " + MusicBand.class.getSimpleName() +
                "\n  количество элементов: " + collection.size();
    }
}

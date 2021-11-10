package lib.types;

import lib.io.CheckedReader;
import lib.io.xml.UnmarshalCheckable;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;
import java.io.IOException;
import java.util.Objects;

/**
 * Представляет лучший альбом группы (типа {@link MusicBand}).
 */
public class Album implements Comparable<Album>, UnmarshalCheckable {
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Long tracks; //Поле не может быть null, Значение поля должно быть больше 0

    private Album() {}

    /**
     * Создает новый лучший альбом
     * @param   name название альбома
     * @param   tracks количество треков
     * @throws  ValueException если name пустая строка или {@code null};
     *          если tracks меньше или равно {@code 0} или {@code null}
     */
    @SuppressWarnings("unused")
    public Album(String name, Long tracks) {
        setName(name);
        setTracks(tracks);
    }

    /**
     * Ввод и создание нового альбома
     * @param reader {@link CheckedReader}, с помощью которого производится ввод
     * @return новый альбом
     * @throws IOException при неудачной или прерванной операции ввода
     * @throws ValueException при вводе некорректных данных
     */
    public static Album fromReader(CheckedReader reader) throws IOException {
        reader.printPrompt("  Лучший альбом:\n");
        Album result = new Album();
        reader.readString(result::setName, "    Название альбома: ");
        reader.readValue(Long.class, result::setTracks, "    Количество треков: ");
        return result;
    }

    /**
     * @return название альбома
     */
    @XmlValue
    public String getName() {
        return name;
    }

    /**
     * @return количество треков в альбоме
     */
    @XmlAttribute
    public Long getTracks() {
        return tracks;
    }

    /**
     * Задает название альбома.
     * @param   name название альбома
     * @throws  ValueException если поле равно {@code null} или пустая строка
     */
    public void setName(String name) {
        if (name == null || name.isEmpty())
            throw new ValueException("Имя альбома не может быть пустым");
        this.name = name;
    }

    /**
     * Задает количество треков в альбоме.
     * @param   tracks количество треков в альбоме
     * @throws  ValueException если поле равно {@code null}
     *          или значение поля меньше или равно {@code 0}
     */
    public void setTracks(Long tracks) {
        if (tracks == null || tracks <= 0)
            throw new ValueException("Количество треков в альбоме должно быть больше нуля");
        this.tracks = tracks;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unused")
    @Override
    public void afterUnmarshal(Unmarshaller u, Object parent) {
        setName(name);
        setTracks(tracks);
    }

    /**
     * @return Строка-представление объекта класса
     */
    @Override
    public String toString() {
        return "{name='" + name + '\'' + ", tracks=" + tracks + '}';
    }

    /**
     * @return хэш объекта
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, tracks);
    }

    /**
     * Проверка на равенство альбомов.
     * @param   object объект, с которым происходит сравнение
     * @return  {@code true}, если объекты равны; иначе, {@code false}.
     */
    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object instanceof Album) {
            Album album = (Album) object;
            return name.equals(album.getName()) && tracks.equals(album.getTracks());
        }
        return false;
    }

    /**
     * Сравнение альбомов.
     * @param   album объект, с которым происходит сравнение
     * @return  отрицательное, {@code 0} или положительное целое, если данный объект меньше,
     *          равен или больше указанного объекта.
     */
    @Override
    public int compareTo(Album album) {
        int result = name.compareTo(album.getName());
        if (result == 0)
            return tracks.compareTo(album.getTracks());
        return result;
    }
}

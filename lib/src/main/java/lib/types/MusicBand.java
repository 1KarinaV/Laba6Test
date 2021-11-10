package lib.types;

import lib.io.CheckedReader;
import lib.io.xml.LocalDateAdapter;
import lib.io.xml.UnmarshalCheckable;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Музыкальная группа
 */
public class MusicBand implements Comparable<MusicBand>, UnmarshalCheckable {
    private static Integer next_id = 1;
    private boolean standalone;
    private Integer id;
    private String name;
    private Coordinates coordinates;
    private LocalDate creationDate;
    private long numberOfParticipants;
    private LocalDate establishmentDate;
    private MusicGenre genre;
    private Album bestAlbum;

    private MusicBand() {
        standalone = true;
        id = 0;
    }

    /**
     * Ввод и создание новой группы
     * @param reader {@link CheckedReader}, с помощью которого производится ввод
     * @return новая группа
     * @throws IOException при неудачной или прерванной операции ввода
     * @throws ValueException при вводе некорректных данных
     */
    public static MusicBand fromReader(CheckedReader reader) throws IOException {
        reader.printPrompt("Введите данные музыкальной группы:\n");
        MusicBand result = new MusicBand();
        result.setCreationDate(LocalDate.now());
        reader.readString(result::setName, "  Название: ");
        result.setCoordinates(Coordinates.fromReader(reader));
        reader.readValue(Long.class, result::setNumberOfParticipants, "  Количество участников: ");
        reader.readValue(LocalDate.class, result::setEstablishmentDate, "  Дата основания: ");
        result.setBestAlbum(Album.fromReader(reader));
        reader.readValue(MusicGenre.class, result::setGenre, "Жанр " + MusicGenre.getValues() + ": ");
        return result;
    }

    /**
     * @return id
     */
    @XmlAttribute
    public Integer getId() {
        return id;
    }

    /**
     * @return название группы
     */
    public String getName() {
        return name;
    }

    /**
     * @return {@link Coordinates координаты}
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * @return дата создания записи
     */
    @SuppressWarnings("unused")
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    public LocalDate getCreationDate() {
        return creationDate;
    }

    /**
     * @return количество участников группы
     */
    public long getNumberOfParticipants() {
        return numberOfParticipants;
    }

    /**
     * @return дата основания группы
     */
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    public LocalDate getEstablishmentDate() {
        return establishmentDate;
    }

    /**
     * @return музыкальный {@link MusicGenre жанр}
     */
    public MusicGenre getGenre() {
        return genre;
    }

    /**
     * @return лучший альбом
     */
    public Album getBestAlbum() {
        return bestAlbum;
    }

    void setId() {
        standalone = false;
        setId(nextId());
    }

    // Устанавливает id
    private void setId(Integer newId) {
        if (this.id != 0)
            throw new IllegalStateException("id уже установлен");

        if (newId == null || newId < 1)
            throw new IllegalArgumentException("id должно быть больше нуля : " + newId);
        this.id = newId;
        next_id = (newId >= next_id ? newId + 1 : next_id);
    }

    /**
     * Устанавливает название группы
     * @param name название
     */
    public void setName(String name) {
        if (name == null || name.isEmpty())
            throw new ValueException("Название группы не может быть пустым");
        this.name = name;
    }

    /**
     * Устанавливает координаты {@link Coordinates}.
     * @param coord координаты
     */
    public void setCoordinates(Coordinates coord) {
        if (coord == null)
            throw new ValueException("Координаты не могут быть пустыми");
        this.coordinates = coord;
    }

    // Устанавливает дату создания записи
    private void setCreationDate(LocalDate date) {
        if (date == null)
            throw new IllegalArgumentException("Дата создания записи не может быть пустой");
        this.creationDate = date;
    }

    /**
     *  Устанавливает количество участников группы.
     * @param n количество участников
     */
    public void setNumberOfParticipants(long n) {
        if (n <= 0)
            throw new ValueException("Количество членов группы должно быть больше нуля");
        this.numberOfParticipants = n;
    }

    /**
     * Устанавливает дату основания группы.
     * @param date дата
     */
    public void setEstablishmentDate(LocalDate date) {
        if (date == null)
            throw new ValueException("Дата основания группы не может быть пустой");
        this.establishmentDate = date;
    }

    /**
     * Устанавливает жанр: один из {@link MusicGenre}.
     * @param genre жанр
     */
    public void setGenre(MusicGenre genre) {
        if (genre == null)
            throw new ValueException("Жанр не может быть пустыми");
        this.genre = genre;
    }

    /**
     * Устанавливает лучший альбом.
     * @param album альбом
     */
    public void setBestAlbum(Album album) {
        if (album == null)
            throw new ValueException("Альбом не может быть пустыми");
        this.bestAlbum = album;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unused")
    @Override
    public void afterUnmarshal(Unmarshaller u, Object parent) {
        if (id == null || id == 0 && !standalone || id < 1
                || name == null || name.isEmpty()
                || coordinates == null || creationDate == null
                || numberOfParticipants <= 0 || establishmentDate == null
                || genre == null || bestAlbum == null)
            throw new IllegalArgumentException();
    }

    /**
     * @return хэш объекта
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, numberOfParticipants, establishmentDate, genre, bestAlbum);
    }

    /**
     * @return Строка-представление объекта класса
     */
    @Override
    public String toString() {
        return "MusicBand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", numberOfParticipants=" + numberOfParticipants +
                ", establishmentDate=" + establishmentDate +
                ", genre=" + genre +
                ", bestAlbum=" + bestAlbum +
                '}';
    }

    /**
     * Сравнение на равенство объектов.
     * @param object сравниваемый объект
     * @return {@code true}, если равно; иначе {@code false}
     */
    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object instanceof MusicBand) {
            MusicBand band = (MusicBand) object;
            return compareTo(band) == 0
                    && coordinates.equals(band.coordinates)
                    && genre.equals(band.genre)
                    && bestAlbum.equals(band.bestAlbum);
        }
        return false;
    }

    /**
     * Сравнение текущего объекта с заданным.
     * Сравнение производится по имени. Если имена одинаковы,
     * сравнивается количество участников. Если количество одинаково,
     * сравниваются даты основания групп.
     * @param band сравниваемый объект
     * @return -1, 0 или 1 если этот объект меньше, равен или больше заданного
     */
    @Override
    public int compareTo(MusicBand band) {
        int result = name.compareTo(band.name);
        if (result == 0) {
            long r = numberOfParticipants - band.numberOfParticipants;
            result = r < 0 ? -1 : r > 0 ? 1 : 0;
            if (result == 0) {
                result = establishmentDate.compareTo(band.establishmentDate);
            }
        }
        return result;
    }

    private static Integer nextId()
    {
        return next_id++;
    }
}

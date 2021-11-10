package lib.types;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;
import java.util.Arrays;

/**
 * Перечисление жанра музыки
 */
@XmlType(name = "genre")
@XmlEnum
public enum MusicGenre {
    /**
     * Строковое значение 'soul'
     */
    @XmlEnumValue("soul")
    SOUL("soul"),
    /**
     * Строковое значение 'punk rock'
     */
    @XmlEnumValue("punk rock")
    PUNK_ROCK("punk rock"),
    /**
     * Строковое значение 'post punk'
     */
    @XmlEnumValue("post punk")
    POST_PUNK("post punk");

    private final String value;

    MusicGenre(String str) {
        value = str;
    }

    public String getValue() {
        return value;
    }

    /**
     * Возвращает строку-подсказку с возможными значениями жанров.
     * @return строка-подсказка
     */
    public static String getValues() {
        return Arrays.toString(values());
    }

    /**
     * Строковое представление перечисления.
     * Строковое значения в одинарных кавычках.
     * @return строковое представление
     */
    @Override
    public String toString() {
        return "'" + getValue() + "'";
    }

    /**
     * Преобразует строку к типу {@code MusicGenre}.
     * @param str строка
     * @return объект {@code MusicGenre}
     */
    public static MusicGenre parse(String str) {
        return Arrays.stream(MusicGenre.values())
                .filter(c -> str.equals(c.toString().substring(1, c.toString().length() - 1)))
                .findAny()
                .orElseGet(() -> MusicGenre.valueOf(str.toUpperCase()));
    }
}

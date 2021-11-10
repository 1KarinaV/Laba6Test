package lib.io;

import lib.types.MusicGenre;
import lib.types.ValueException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * <p>Читает текст из символьного потока ввода.</p>
 * <p>Предоставляет функции ввода строки {@link #readString(Consumer, String) readString}
 * и ввода значений {@link #readValue(Class, Consumer, String) readValue}
 * в интерактивном режиме с проверкой введенных данных внешним методом.</p>
 * <p>Интерактивный режим поддерживается только с потоком {@code System.in},
 * при этом, приглашения к вводу выводятся в {@code System.out} функцией
 * {@link #printPrompt(String) printPrompt}. В случае, когда потоком ввода
 * является иной источник (например, файл), для проверяемых функций выполняются
 * только проверки корректности ввода, без вывода приглашений.</p>
 * <p>Сообщения о некорректном вводе всегда выводятся в {@code System.out}.
 * Кроме того, в интерактивном режиме в случае некорректного ввода запрос на
 * ввод повторяется.</p>
 */
public class CheckedReader extends BufferedReader {
    private final boolean interactive;

    private static final HashMap<Class<?>, Function<String,?>> parser = new HashMap<>();
    static {
        parser.put(int.class       , Integer::parseInt);
        parser.put(long.class      , Long::parseLong);
        parser.put(double.class    , Double::parseDouble);
        parser.put(Integer.class   , Integer::valueOf);
        parser.put(Long.class      , Long::valueOf);
        parser.put(Double.class    , Double::valueOf);
        parser.put(LocalDate.class , LocalDate::parse);
        parser.put(MusicGenre.class , MusicGenre::parse);
    }

    /**
     * Создает {@code CheckedReader}
     * @param in поток ввода
     */
    public CheckedReader(InputStream in) {
        super(new InputStreamReader(in));
        interactive = in.equals(System.in);
    }

    /**
     * Вывод подсказки или строки приглашения в интерактивном режиме
     * @param prompt стока-приглашение
     */
    public void printPrompt(String prompt) {
        if (interactive)
            System.out.printf("%s", prompt);
    }

    private String readString(String prompt) throws IOException {
        printPrompt(prompt);
        String result = readLine();
        if (result == null)
            throw new IOException("Неожиданный конец ввода");
        result = result.trim();
        if (result.isEmpty())
            return null;
        return result;
    }

    /**
     * Ввод строки.
     * Проверка выполняется в передаваемом в качестве параметра
     * методе-потребителе строки.
     * @param fn метод-потребитель вводимых данных
     * @param prompt стока-приглашение
     * @throws IOException при неудачной или прерванной операции ввода
     * @throws ValueException при вводе некорректной строки
     */
    public void readString(Consumer<String> fn, String prompt) throws IOException {
        while (true) {
            try {
                fn.accept(readString(prompt));
                return;
            } catch (ValueException e) {
                System.out.println(e.getMessage());
                if (!interactive)
                    throw e;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private  <T> T readValue(Class<T> tClass, String prompt) throws IOException {
        String line = readString(prompt);
        Function<String, ?> func = parser.get(tClass);
        if (func != null) {
            try {
                return (T) func.apply(line);
            }
            catch (DateTimeParseException | IllegalArgumentException | NullPointerException e) {
                throw new ValueException("Недопустимое значение: " + line);
            }
        }
        throw new UnsupportedOperationException("Нет преобразования строки в " + tClass.getName());
    }

    /**
     * Ввод значения.
     * Проверка выполняется в передаваемом в качестве параметра
     * методе-потребителе значения.
     * @param <T> тип значения
     * @param tClass объект класса типа значения
     * @param fn метод-потребитель вводимых данных
     * @param prompt стока-приглашение
     * @throws IOException при неудачной или прерванной операции ввода
     * @throws ValueException при вводе некорректного значения
     */
    public <T> void readValue(Class<T> tClass, Consumer<T> fn, String prompt) throws IOException {
        while (true) {
            try {
                fn.accept(readValue(tClass, prompt));
                return;
            } catch (ValueException e) {
                System.out.println("Ошибка: " + e.getMessage());
                if (!interactive)
                    throw e;
            }
        }
    }
}

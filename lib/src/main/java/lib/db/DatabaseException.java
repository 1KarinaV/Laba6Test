package lib.db;

import java.io.IOException;

/**
 * Класс исключений при работе с файлом базы данных.
 */
public class DatabaseException extends IOException {
    public DatabaseException(String what) {
        super(what);
    }
}

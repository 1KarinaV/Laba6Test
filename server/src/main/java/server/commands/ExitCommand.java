package server.commands;

import lib.commands.CommandData;
import lib.db.Database;
import lib.db.DatabaseException;
import lib.io.CheckedReader;

import javax.xml.bind.JAXBException;

/**
 * Команда "exit".
 * Не имеет аргументов.
 * Команда выхода из приложения без сохранения данных.
 */
public class ExitCommand extends lib.commands.ExitCommand {
    private final Database database;

    public ExitCommand(Database db) {
        database = db;
    }
    /**
     * {@inheritDoc}
     * @return
     * @param args
     */
    @Override
    public CommandData execute(CheckedReader reader, String... args) throws JAXBException, DatabaseException {
        database.save();
        return super.execute(reader, args);
    }
}

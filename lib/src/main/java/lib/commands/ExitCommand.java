package lib.commands;

import lib.db.DatabaseException;
import lib.io.CheckedReader;

import javax.xml.bind.JAXBException;

/**
 * Команда "exit".
 * Не имеет аргументов.
 * Команда выхода из приложения без сохранения данных.
 */
public class ExitCommand extends AbstractCommand {
    /**
     * Создание команды "exit"
     */
    public ExitCommand() {
        super("exit", "exit - завершить программу (без сохранения в файл)");
    }

    /**
     * {@inheritDoc}
     * @return
     * @param args
     */
    @Override
    public CommandData execute(CheckedReader reader, String... args) throws JAXBException, DatabaseException {
        System.exit(0);
        return null;
    }
}

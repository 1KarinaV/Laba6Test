package lib.commands;

import lib.io.CheckedReader;

import javax.xml.bind.JAXBException;
import java.io.IOException;

/**
 * Команда "info".
 * Не имеет аргументов.
 * Команда вывода информации о коллекции и данных.
 */
public class InfoCommand extends AbstractCommand {
    /**
     * Создание команды "info"
     */
    public InfoCommand() {
        super("info", "info - вывести в стандартный поток вывода информацию о коллекции");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandData execute(CheckedReader reader, String... args) throws IOException, JAXBException {
        if (args.length > 0)
            throw new CommandException("Ошибка: команда '" + name() + "' не имеет аргументов");
        return super.execute(reader, args);
    }
}

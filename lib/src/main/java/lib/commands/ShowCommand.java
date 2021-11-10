package lib.commands;


import lib.io.CheckedReader;

import javax.xml.bind.JAXBException;
import java.io.IOException;

/**
 * Команда "show".
 * Не имеет аргументов.
 * Вывод на экран всех групп.
 */
public class ShowCommand extends AbstractCommand {
    /**
     * Создание команды "show"
     */
    public ShowCommand() {
        super("show", "show - вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
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

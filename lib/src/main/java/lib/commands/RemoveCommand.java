package lib.commands;

import lib.io.CheckedReader;

import java.io.IOException;

/**
 * Команда "remove_by_id id".
 * Имеет один аргумент - id группы.
 * Удаление группы с заданным id.
 */
public class RemoveCommand extends AbstractCommand {
    /**
     * Создание команды "remove_by_id"
     */
    public RemoveCommand() {
        super("remove_by_id", "remove_by_id id - удалить элемент из коллекции по его id");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandData execute(CheckedReader reader, String... args) throws IOException {
        if (args.length != 1)
            throw new CommandException("Ошибка: команда '" + name() + "' должна иметь один аргумент.");
        CommandData result = new CommandData();
        result.setCommand(name());
        result.setArgument(args[0]);
        return result;
    }
}

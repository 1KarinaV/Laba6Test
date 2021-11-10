package lib.commands;


import lib.io.CheckedReader;

import javax.xml.bind.JAXBException;
import java.io.IOException;

/**
 * Команда "sum_of_number_of_participants".
 * Не имеет аргументов.
 * Вывести сумму участников всех групп.
 */
public class ParticipantsSumCommand extends AbstractCommand {
    /**
     * Создание команды "sum_of_number_of_participants"
     */
    public ParticipantsSumCommand() {
        super("sum_of_number_of_participants",
                "sum_of_number_of_participants - " +
                        "вывести сумму значений поля numberOfParticipants для всех элементов коллекции");
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

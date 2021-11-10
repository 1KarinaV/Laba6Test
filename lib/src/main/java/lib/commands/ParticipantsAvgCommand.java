package lib.commands;

import lib.io.CheckedReader;

import javax.xml.bind.JAXBException;
import java.io.IOException;

/**
 * Команда "average_of_number_of_participants".
 * Не имеет аргументов.
 * Выводит среднее число участников во всех группах.
 */
public class ParticipantsAvgCommand extends AbstractCommand {
    /**
     * Создание команды "average_of_number_of_participants"
     */
    public ParticipantsAvgCommand() {
        super("average_of_number_of_participants",
                "average_of_number_of_participants - " +
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

package lib.commands;

import lib.io.CheckedReader;
import lib.types.MusicBand;

import java.io.IOException;

/**
 * Команда "add_if_max {element}".
 * Не имеет аргументов.
 * Добавляет музыкальную группу в базу данных групп, если
 * введенное значение превышает значение наибольшего элемента.
 */
public class AddIfMaxCommand extends AbstractCommand {
    /**
     * Создание команды "add_if_max"
     */
    public AddIfMaxCommand() {
        super("add_if_max",
                "add_if_max - добавить новый элемент в коллекцию, если его значение\n" +
                        "             превышает значение наибольшего элемента этой коллекции");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandData execute(CheckedReader reader, String... args) throws IOException {
        if (args.length > 0)
            throw new CommandException("Ошибка: команда '" + name() + "' не имеет аргументов");

        MusicBand mb = MusicBand.fromReader(reader);
        CommandData result = new CommandData();
        result.setCommand(name());
        String str = mb.getName() + '\n' +
                mb.getCoordinates().getX() + '\n' +
                mb.getCoordinates().getY() + '\n' +
                mb.getNumberOfParticipants() + '\n' +
                mb.getEstablishmentDate() + '\n' +
                mb.getBestAlbum().getName() + '\n' +
                mb.getBestAlbum().getTracks() + '\n' +
                mb.getGenre().getValue();
        result.setContent(str);
        return result;
    }
}

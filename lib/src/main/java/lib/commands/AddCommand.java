package lib.commands;

import lib.io.CheckedReader;
import lib.types.MusicBand;

import java.io.IOException;

/**
 * Команда "add".
 * Не имеет аргументов.
 * Добавляет музыкальную группу в базу данных групп.
 */
public class AddCommand extends AbstractCommand {
    /**
     * Создание команды "add"
     */
    public AddCommand() {
        super("add", "add - добавить новый элемент в коллекцию");
    }

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

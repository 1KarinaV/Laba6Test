package lib.commands;


import lib.io.CheckedReader;
import lib.types.MusicBand;

import java.io.IOException;

/**
 * Команда "remove_greater".
 * Не имеет аргументов.
 * Удалить из коллекции все группы, превышающие заданную.
 */
public class RemoveGreaterCommand extends AbstractCommand {
    /**
     * Создание команды "remove_greater"
     */
    public RemoveGreaterCommand() {
        super("remove_greater",
                "remove_greater - удалить из коллекции все элементы, превышающие заданный");
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

package lib.commands;

import lib.io.CheckedReader;
import lib.types.MusicBand;

import java.io.IOException;

/**
 * Команда "update id".
 * Имеет один аргумент - id группы.
 * Обновление группы с заданным id.
 */
public class UpdateCommand extends AbstractCommand {
    /**
     * Создание команды "update"
     */
    public UpdateCommand() {
        super("update", "update id - обновить значение элемента коллекции по заданному id");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandData execute(CheckedReader reader, String... args) throws IOException {
        if (args.length != 1)
            throw new CommandException("Ошибка: команда '" + name() + "' должна иметь один аргумент.");

        MusicBand mb = MusicBand.fromReader(reader);
        String str = mb.getName() + '\n' +
                mb.getCoordinates().getX() + '\n' +
                mb.getCoordinates().getY() + '\n' +
                mb.getNumberOfParticipants() + '\n' +
                mb.getEstablishmentDate() + '\n' +
                mb.getBestAlbum().getName() + '\n' +
                mb.getBestAlbum().getTracks() + '\n' +
                mb.getGenre().getValue();
        CommandData result = new CommandData();
        result.setCommand(name());
        result.setArgument(args[0]);
        result.setContent(str);
        return result;
    }
}

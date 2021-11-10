package server.commands;

import lib.commands.CommandData;
import lib.db.Database;
import lib.io.CheckedReader;
import lib.types.MusicBand;

import java.io.IOException;

public class UpdateCommand extends lib.commands.UpdateCommand {
    private final Database database;

    public UpdateCommand(Database db) {
        database = db;
    }

    @Override
    public CommandData execute(CheckedReader reader, String... args) throws IOException {
        CommandData result = new CommandData();
        result.setCommand(name());
        Integer id = Integer.valueOf(args[0]);
        MusicBand found = database.find(id);
        if (found != null) {
            MusicBand tmp = MusicBand.fromReader(reader);
            found.setBestAlbum(tmp.getBestAlbum());
            found.setCoordinates(tmp.getCoordinates());
            found.setEstablishmentDate(tmp.getEstablishmentDate());
            found.setGenre(tmp.getGenre());
            found.setName(tmp.getName());
            found.setNumberOfParticipants(tmp.getNumberOfParticipants());
            result.setContent("Обновлен элемент:\n" + found);
        }
        else {
            result.setContent("Элемент с id=" + id + " отсутствует в коллекции.");
        }
        return result;
    }
}

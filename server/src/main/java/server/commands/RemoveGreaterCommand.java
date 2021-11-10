package server.commands;

import lib.commands.CommandData;
import lib.db.Database;
import lib.io.CheckedReader;
import lib.types.MusicBand;

import java.io.IOException;

public class RemoveGreaterCommand extends lib.commands.RemoveGreaterCommand {
    private final Database database;

    public RemoveGreaterCommand(Database db) {
        database = db;
    }

    @Override
    public CommandData execute(CheckedReader reader, String... args) throws IOException {
        CommandData result = new CommandData();
        result.setCommand(name());
        MusicBand mb = MusicBand.fromReader(reader);
        result.setContent("Удалено элементов: " + database.removeGreater(mb));
        return result;
    }
}

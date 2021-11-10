package server.commands;

import lib.commands.CommandData;
import lib.db.Database;
import lib.io.CheckedReader;
import lib.types.MusicBand;

import java.io.IOException;

public class AddCommand extends lib.commands.AddCommand {
    private final Database database;

    public AddCommand(Database db) {
        database = db;
    }

    @Override
    public CommandData execute(CheckedReader reader, String... args) throws IOException {
        CommandData result = new CommandData();
        result.setCommand(name());
        MusicBand mb = MusicBand.fromReader(reader);
        database.add(mb);
        result.setContent("Добавлен элемент");
        return result;
    }
}

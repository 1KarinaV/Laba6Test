package server.commands;

import lib.commands.CommandData;
import lib.db.Database;
import lib.io.CheckedReader;
import lib.types.MusicBand;

import java.io.IOException;

public class AddIfMaxCommand extends lib.commands.AddIfMaxCommand {
    private final Database database;

    public AddIfMaxCommand(Database db) {
        database = db;
    }

    @Override
    public CommandData execute(CheckedReader reader, String... args) throws IOException {
        CommandData result = new CommandData();
        result.setCommand(name());
        MusicBand mb = MusicBand.fromReader(reader);
        if (database.addIfMax(mb))
            result.setContent("Добавлен элемент");
        else
            result.setContent("Ничего не добавлено");
        return result;
    }
}

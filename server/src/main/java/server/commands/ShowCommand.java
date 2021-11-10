package server.commands;

import lib.commands.CommandData;
import lib.db.Database;
import lib.io.CheckedReader;

public class ShowCommand extends lib.commands.ShowCommand {
    private final Database database;

    public ShowCommand(Database db) {
        database = db;
    }

    @Override
    public CommandData execute(CheckedReader reader, String... args) {
        CommandData result = new CommandData();
        result.setCommand(name());
        result.setContent(database.show());
        return result;
    }
}

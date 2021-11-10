package server.commands;

import lib.commands.CommandData;
import lib.db.Database;
import lib.io.CheckedReader;

public class InfoCommand extends lib.commands.InfoCommand {
    private final Database database;

    public InfoCommand(Database db) {
        database = db;
    }

    @Override
    public CommandData execute(CheckedReader reader, String... args) {
        CommandData result = new CommandData();
        result.setCommand(name());
        result.setContent(database.toString());
        return result;
    }
}

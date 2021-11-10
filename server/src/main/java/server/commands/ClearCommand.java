package server.commands;

import lib.commands.CommandData;
import lib.db.Database;
import lib.io.CheckedReader;


public class ClearCommand extends lib.commands.ClearCommand {
    private final Database database;

    public ClearCommand(Database db) {
        database = db;
    }

    @Override
    public CommandData execute(CheckedReader reader, String... args) {
        CommandData result = new CommandData();
        result.setCommand(name());
        database.clear();
        result.setContent("OK");
        return result;
    }
}

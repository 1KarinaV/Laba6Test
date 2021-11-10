package server.commands;

import lib.commands.CommandData;
import lib.db.Database;
import lib.io.CheckedReader;

public class EstablishmentDatesCommand extends lib.commands.EstablishmentDatesCommand {
    private final Database database;

    public EstablishmentDatesCommand(Database db) {
        database = db;
    }

    @Override
    public CommandData execute(CheckedReader reader, String... args) {
        CommandData result = new CommandData();
        result.setCommand(name());
        result.setContent(database.showEstablishmentDates());
        return result;
    }
}

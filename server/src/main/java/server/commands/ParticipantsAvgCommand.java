package server.commands;

import lib.commands.CommandData;
import lib.db.Database;
import lib.io.CheckedReader;

public class ParticipantsAvgCommand extends lib.commands.ParticipantsAvgCommand {
    private final Database database;

    public ParticipantsAvgCommand(Database db) {
        database = db;
    }

    @Override
    public CommandData execute(CheckedReader reader, String... args) {
        CommandData result = new CommandData();
        result.setCommand(name());
        result.setContent(Long.toString(database.getParticipantsAvg()));
        return result;
    }
}

package server.commands;

import lib.commands.CommandData;
import lib.db.Database;
import lib.io.CheckedReader;

public class ParticipantsSumCommand extends lib.commands.ParticipantsSumCommand {
    private final Database database;

    public ParticipantsSumCommand(Database db) {
        database = db;
    }

    @Override
    public CommandData execute(CheckedReader reader, String... args) {
        CommandData result = new CommandData();
        result.setCommand(name());
        result.setContent(Long.toString(database.getParticipantsSum()));
        return result;
    }
}

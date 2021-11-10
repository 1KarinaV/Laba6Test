package server.commands;

import lib.commands.CommandData;
import lib.db.Database;
import lib.io.CheckedReader;

public class RemoveCommand extends lib.commands.RemoveCommand {
    private final Database database;

    public RemoveCommand(Database db) {
        database = db;
    }

    @Override
    public CommandData execute(CheckedReader reader, String... args) {
        CommandData result = new CommandData();
        result.setCommand(name());
        Integer id = Integer.valueOf(args[0]);
        if (database.remove(id))
            result.setContent("Элемент успешно удален.");
        else
            result.setContent("Элемент с id=" + id + " отсутствует в коллекции.");
        return result;
    }
}

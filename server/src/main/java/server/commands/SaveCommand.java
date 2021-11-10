package server.commands;

import lib.commands.AbstractCommand;
import lib.commands.CommandData;
import lib.db.Database;
import lib.db.DatabaseException;
import lib.io.CheckedReader;

import javax.xml.bind.JAXBException;

public class SaveCommand extends AbstractCommand {
    private final Database database;

    public SaveCommand(Database db) {
        super("save", "save - сохранить коллекцию в файл");
        database = db;
    }

    @Override
    public CommandData execute(CheckedReader reader, String... args) throws JAXBException, DatabaseException {
        database.save();
        return null;
    }
}

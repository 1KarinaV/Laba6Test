package lib.commands;

import lib.io.CheckedReader;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public class ClearCommand extends AbstractCommand {
    public ClearCommand() {
        super("clear", "clear - очистить коллекцию");
    }

    @Override
    public CommandData execute(CheckedReader reader, String... args) throws IOException, JAXBException {
        if (args.length > 0)
            throw new CommandException("Ошибка: команда '" + name() + "' не имеет аргументов");
        return super.execute(reader, args);
    }
}

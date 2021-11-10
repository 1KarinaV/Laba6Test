package lib.commands;

import lib.io.CheckedReader;

public class HelpCommand extends AbstractCommand {
    private final CommandList commands;

    public HelpCommand(CommandList cmdList) {
        super("help", "help - вывести справку по доступным командам");
        commands = cmdList;
    }

    @Override
    public CommandData execute(CheckedReader reader, String... args) {
        commands.forEach((k, v) -> System.out.println(v.description()));
        return null;
    }
}

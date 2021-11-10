package lib.commands;

import java.util.TreeMap;
import java.util.function.BiConsumer;

public class CommandList {
    private final TreeMap<String, Command> commands;

    public CommandList() {
        commands = new TreeMap<>();
    }

    public void add(Command cmd) {
        commands.putIfAbsent(cmd.name(), cmd);
    }

    public Command get(String name) {
        return commands.get(name);
    }

    public void clear() {
        commands.clear();
    }

    public void forEach(BiConsumer<String, Command> action) {
        commands.forEach(action);
    }
}

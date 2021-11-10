package lib.commands;

import java.io.IOException;

/**
 * Класс исключений при выполнении команд
 */
public class CommandException extends IOException {
    public CommandException(String what) {
        super(what);
    }
}

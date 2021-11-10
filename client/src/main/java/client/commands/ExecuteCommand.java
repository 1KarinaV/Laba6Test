package client.commands;

import lib.commands.AbstractCommand;
import lib.commands.CommandData;
import lib.commands.CommandException;
import lib.io.CheckedReader;
import lib.types.ValueException;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Команда "execute_script file_name".
 * Имеет один аргумент file_name - имя файла со скриптом.
 * Считывает и исполняет скрипт из указанного файла.
 */
public class ExecuteCommand extends AbstractCommand {
    /**
     * Создание команды "execute_script"
     */
    public ExecuteCommand() {
        super("execute_script", "execute_script file_name - считать и исполнить скрипт из указанного файла");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandData execute(CheckedReader reader, String... args) throws IOException, JAXBException {
        if (args.length != 1)
            throw new CommandException("Ошибка: Команда '" + name() + "' должна иметь один аргумент.");

        Path path = Paths.get(args[0]);
        if (Files.notExists(path)) {
            System.out.println("Ошибка: Файл не существует: " + args[0]);
        }
        else if (Files.isReadable(path)) {
            try (ClientCommandInvoker ci = ClientCommandInvoker.getGlobal().createFromThis(path)) {
                ci.run();
            } catch (ValueException e) {
                System.out.println("Ошибка: Выполнение скрипта прервано: " + path);
            }
        }
        else {
            System.out.println("Ошибка: Невозможно прочитать файл: " + args[0]);
        }
        return null;
    }
}

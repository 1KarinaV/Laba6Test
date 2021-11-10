package lib.commands;

import lib.io.CheckedReader;

import javax.xml.bind.JAXBException;
import java.io.IOException;

/**
 * Интерфейс команд
 */
public interface Command {
    /**
     * Название команды
     * @return название команды
     */
    String name();

    /**
     * Имя команды и ее использование
     * @return описание команды
     */
    String description();

    /**
     * Выполняет команду.
     * @param args аргумент команды
     * @throws IOException при ошибках ввода команды
     */
    default CommandData execute(CheckedReader reader, String... args) throws IOException, JAXBException {
        CommandData result = new CommandData();
        result.setCommand(name());
        return result;
    }
}

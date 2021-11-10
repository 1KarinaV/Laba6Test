package lib.commands;

import lib.io.CheckedReader;

import javax.xml.bind.JAXBException;
import java.io.IOException;

/**
 * Команда "print_field_descending_establishment_date".
 * Не имеет аргументов.
 * Выводит значения дат основания всех групп в порядке убывания.
 */
public class EstablishmentDatesCommand extends AbstractCommand {
    /**
     * Создание команды "print_field_descending_establishment_date"
     */
    public EstablishmentDatesCommand() {
        super("print_field_descending_establishment_date",
                "print_field_descending_establishment_date - " +
                        "вывести значения поля establishmentDate всех элементов в порядке убывания");
    }

    @Override
    public CommandData execute(CheckedReader reader, String... args) throws IOException, JAXBException {
        if (args.length > 0)
            throw new CommandException("Ошибка: команда '" + name() + "' не имеет аргументов");
        return super.execute(reader, args);
    }
}

package lib.commands;

/**
 * Абстрактный класс команд, выполняемых над базой данных.
 */
public abstract class AbstractCommand implements Command {
    private final String name;
    private final String description;

    /**
     * Создание новой команды
     * @param name имя команды
     * @param description описание команды
     */
    public AbstractCommand(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String name() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String description() {
        return description;
    }
}

package server;

import lib.commands.CommandList;
import lib.db.Database;
import server.commands.*;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public class Server {
    public static void main(String[] args) {
        if (args.length != 1)
            printUsage();

        try {
            Database db = new Database(args[0]);
            ServerCommandInvoker invoker = ServerCommandInvoker.getGlobal(db);
            invoker.makeBound();

            CommandList cmdList = new CommandList();
            cmdList.add(new SaveCommand(db));
            invoker.register(cmdList);

            cmdList.clear();
            cmdList.add(new AddCommand(db));
            cmdList.add(new AddIfMaxCommand(db));
            cmdList.add(new ClearCommand(db));
            cmdList.add(new EstablishmentDatesCommand(db));
            cmdList.add(new InfoCommand(db));
            cmdList.add(new ParticipantsAvgCommand(db));
            cmdList.add(new ParticipantsSumCommand(db));
            cmdList.add(new RemoveCommand(db));
            cmdList.add(new RemoveGreaterCommand(db));
            cmdList.add(new ShowCommand(db));
            cmdList.add(new UpdateCommand(db));
            invoker.registerRemote(cmdList);

            invoker.run();
        } catch (IOException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (JAXBException e) {
            System.out.println("Внутренняя ошибка работы с JAXB");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void printUsage()
    {
        System.out.println("Программа принимает на вход ровно один аргумент - путь до файла.");
        System.out.println("Пожалуйста, проверьте верность аргументов и повторите запуск.");
        System.exit(1);
    }
}

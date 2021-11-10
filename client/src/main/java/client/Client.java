package client;

import client.commands.ClientCommandInvoker;
import client.commands.ExecuteCommand;
import lib.commands.*;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public class Client {
    public static void main(String[] args) {
        if (args.length != 1)
            printUsage();

        try (ClientCommandInvoker invoker = ClientCommandInvoker.getGlobal()) {
            CommandList cmdList = new CommandList();
            cmdList.add(new AddCommand());
            cmdList.add(new AddIfMaxCommand());
            cmdList.add(new ClearCommand());
            cmdList.add(new EstablishmentDatesCommand());
            cmdList.add(new ExecuteCommand());
            cmdList.add(new InfoCommand());
            cmdList.add(new ParticipantsAvgCommand());
            cmdList.add(new ParticipantsSumCommand());
            cmdList.add(new RemoveCommand());
            cmdList.add(new RemoveGreaterCommand());
            cmdList.add(new ShowCommand());
            cmdList.add(new UpdateCommand());

            invoker.register(cmdList);
            invoker.makeConnected(args[0]);
            invoker.run();
        } catch (IOException e) {
            System.out.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        } catch (JAXBException e) {
            System.out.println("Внутренняя ошибка работы с JAXB");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void printUsage()
    {
        System.out.println("Программа принимает на вход ровно один аргумент - адрес сервера.");
        System.out.println("Пожалуйста, проверьте верность аргументов и повторите запуск.");
        System.exit(1);
    }
}

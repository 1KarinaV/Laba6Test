package lib.commands;

import lib.io.xml.XmlData;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.FIELD)
public class CommandData {
    private String command = "";
    private String argument = "";
    private String content = "";

    public static CommandData unmarshall(String str) throws JAXBException {
        return XmlData.unmarshall(CommandData.class, str);
    }

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String marshall() throws JAXBException {
        return XmlData.marshall(CommandData.class, this, "data");
    }
}

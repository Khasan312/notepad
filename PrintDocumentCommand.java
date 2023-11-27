public class PrintDocumentCommand implements Command {

    private final Commands commands;

    public PrintDocumentCommand(Commands commands) {
        this.commands = commands;
    }

    @Override
    public void executeCommand() {
        commands.printDocument();
    }
}

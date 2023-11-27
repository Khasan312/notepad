public class OpenDocumentCommand implements Command{

    private final Commands commands;

    public OpenDocumentCommand(Commands commands) {
        this.commands = commands;
    }

    @Override
    public void executeCommand() {
        commands.openDocument();
    }
}

public class NewDocumentCommand implements Command{

    private final Commands commands;

    public NewDocumentCommand(Commands commands) {
        this.commands = commands;
    }

    @Override
    public void executeCommand() {
        commands.createNewDocument();
    }
}

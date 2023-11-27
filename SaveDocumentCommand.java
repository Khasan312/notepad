public class SaveDocumentCommand implements Command{
    private final Commands commands;

    public SaveDocumentCommand(Commands commands) {
        this.commands = commands;
    }

    public void executeCommand() {
        commands.saveDocument();
    }
}

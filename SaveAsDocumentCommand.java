public class SaveAsDocumentCommand implements Command{

    private final Commands commands;

    public SaveAsDocumentCommand(Commands commands) {
        this.commands = commands;
    }

    public void executeCommand() {
        commands.saveAsDocument();
    }
}

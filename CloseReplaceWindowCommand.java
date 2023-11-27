public class CloseReplaceWindowCommand implements Command {

    private final Commands commands;

    public CloseReplaceWindowCommand(Commands commands) {
        this.commands = commands;
    }

    @Override
    public void executeCommand() {
        commands.closeReplacePanelWindow();
    }
}

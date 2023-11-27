public class RedoCommand implements Command {

    private final Commands commands;

    public RedoCommand(Commands commands) {
        this.commands = commands;
    }

    @Override
    public void executeCommand() {
        commands.redoAction();
    }
}

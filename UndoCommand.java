public class UndoCommand implements Command {

    private final Commands commands;

    public UndoCommand(Commands commands) {
        this.commands = commands;
    }

    @Override
    public void executeCommand() {
        commands.undoAction();
    }
}

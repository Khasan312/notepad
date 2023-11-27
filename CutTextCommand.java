public class CutTextCommand implements Command {

    private final Commands commands;

    public CutTextCommand(Commands commands) {
        this.commands = commands;
    }

    @Override
    public void executeCommand() {
        commands.cutText();
    }
}

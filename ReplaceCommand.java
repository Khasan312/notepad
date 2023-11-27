public class ReplaceCommand implements Command {

    private final Commands commands;

    public ReplaceCommand(Commands commands) {
        this.commands = commands;
    }
    @Override
    public void executeCommand() {
        commands.doReplace();
    }
}

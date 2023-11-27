public class ReplaceSearchCommand implements Command {

    private final Commands commands;

    public ReplaceSearchCommand(Commands commands) {
        this.commands = commands;
    }
    @Override
    public void executeCommand() {
        commands.doReplaceSearch();
    }
}

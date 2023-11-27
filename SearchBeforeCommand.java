public class SearchBeforeCommand implements Command {

    private final Commands commands;

    public SearchBeforeCommand(Commands commands) {
        this.commands = commands;
    }

    @Override
    public void executeCommand() {
        commands.searchBefore();
    }
}

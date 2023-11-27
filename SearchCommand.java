public class SearchCommand implements Command {

    private final Commands commands;

    public SearchCommand(Commands commands) {
        this.commands = commands;
    }
    @Override
    public void executeCommand() {
        commands.doSearch();
    }
}

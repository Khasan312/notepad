public class SearchAfterCommand implements Command {

    private final Commands commands;

    public SearchAfterCommand(Commands commands) {
        this.commands = commands;
    }


    @Override
    public void executeCommand() {
        commands.searchAfter();
    }
}

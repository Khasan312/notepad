public class InitSearchCommand implements Command {

    private final Commands commands;

    public InitSearchCommand(Commands commands) {
        this.commands = commands;
    }

    @Override
    public void executeCommand() {
        commands.initSearch();
    }
}

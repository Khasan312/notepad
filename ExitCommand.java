public class ExitCommand implements Command {

    private final Commands commands;

    public ExitCommand(Commands commands) {
        this.commands = commands;
    }

    @Override
    public void executeCommand() {
        commands.exit();
    }
}

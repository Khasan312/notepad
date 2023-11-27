public class SelectAllCommand implements Command {

    private final Commands commands;

    public SelectAllCommand(Commands commands) {
        this.commands = commands;
    }

    @Override
    public void executeCommand() {
        commands.selectAll();
    }
}

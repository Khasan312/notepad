public class CloseSearchWindowCommand implements Command {

    private final Commands commands;

    public CloseSearchWindowCommand(Commands commands) {
        this.commands = commands;
    }

    @Override
    public void executeCommand() {
        commands.closeSearchPanelWindow();
    }
}

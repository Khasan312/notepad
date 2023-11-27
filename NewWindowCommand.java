public class NewWindowCommand implements Command{

    private final Commands commands;

    public NewWindowCommand(Commands commands) {
        this.commands = commands;
    }

    @Override
    public void executeCommand() {
        commands.createNewWindow();
    }
}

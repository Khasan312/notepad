public class ChangeFontCommand implements Command {

    private final Commands commands;

    public ChangeFontCommand(Commands commands) {
        this.commands = commands;
    }

    @Override
    public void executeCommand() {
        commands.changeFont();
    }
}

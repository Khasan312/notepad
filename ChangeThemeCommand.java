public class ChangeThemeCommand implements Command {

    private final Commands commands;

    public ChangeThemeCommand(Commands commands) {
        this.commands = commands;
    }

    @Override
    public void executeCommand() {
        commands.updateTextFieldColors();
    }
}

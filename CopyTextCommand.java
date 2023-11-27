public class CopyTextCommand implements Command {

    private final Commands commands;

    public CopyTextCommand(Commands commands) {
        this.commands = commands;
    }

    @Override
    public void executeCommand() {
        commands.copyText();
    }
}

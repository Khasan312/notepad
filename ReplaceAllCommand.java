public class ReplaceAllCommand implements Command {

    private final Commands commands;

    public ReplaceAllCommand(Commands commands) {
        this.commands = commands;
    }
    @Override
    public void executeCommand() {
        commands.doReplaceAll();
    }
}

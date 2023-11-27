public class InitReplaceCommand implements Command {

    private final Commands commands;

    public InitReplaceCommand(Commands commands) {
        this.commands = commands;
    }

    @Override
    public void executeCommand() {
        commands.initReplace();
    }
}

public class DeleteTextCommand implements Command {

    private final Commands commands;

    public DeleteTextCommand(Commands commands) {
        this.commands = commands;
    }
    @Override
    public void executeCommand() {
        commands.deleteText();
    }
}

public class PasteTextCommand implements Command {
    private final Commands commands;

    public PasteTextCommand(Commands commands) {
        this.commands = commands;
    }

    @Override
    public void executeCommand() {
        commands.pasteText();
    }
}

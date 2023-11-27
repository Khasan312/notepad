public class PrintFileCommand implements Command {

    private final Commands commands;

    public PrintFileCommand(Commands commands) {
        this.commands = commands;
    }

    @Override
    public void executeCommand() {
        commands.printFile();
    }
}

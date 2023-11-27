public class ZoomInCommand implements Command {

    private final Commands commands;

    public ZoomInCommand(Commands commands) {
        this.commands = commands;
    }

    @Override
    public void executeCommand() {
        commands.zoomIn();
    }
}

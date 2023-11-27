public class ZoomOutCommand implements Command {

    private final Commands commands;

    public ZoomOutCommand(Commands commands) {
        this.commands = commands;
    }

    @Override
    public void executeCommand() {
        commands.zoomOut();
    }
}

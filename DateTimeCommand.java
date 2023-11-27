public class DateTimeCommand implements Command {

    private final Commands commands;

    public DateTimeCommand(Commands commands) {
        this.commands = commands;
    }

    @Override
    public void executeCommand() {
        commands.dateTime();
    }
}

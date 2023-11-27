public class AboutProgramCommand implements Command {

    private final Commands commands;

    public AboutProgramCommand(Commands commands) {
        this.commands = commands;
    }

    @Override
    public void executeCommand() {
        commands.aboutProgram();
    }
}

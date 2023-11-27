import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class Controller implements ActionListener {

    private Viewer viewer;
    private Commands commands;
    private final Map<String, Command> actions = new HashMap<>();

    public Controller(Viewer viewer) {
        this.viewer = viewer;
        commands = new Commands(viewer);
        setupCommands();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        Command action = actions.get(command);

        if (action != null) {
            action.executeCommand();
        }
    }

    private void setupCommands() {
        actions.put("Open_Document", new OpenDocumentCommand(commands));
        actions.put("Save_Document", new SaveDocumentCommand(commands));
        actions.put("Save_As_Document", new SaveAsDocumentCommand(commands));
        actions.put("Print", new PrintDocumentCommand(commands));
        actions.put("New_Window", new NewWindowCommand(commands));
        actions.put("New_Document", new NewDocumentCommand(commands));
        actions.put("Print_Document", new PrintFileCommand(commands));
        actions.put("Exit", new ExitCommand(commands));
        actions.put("Search_Action", new InitSearchCommand(commands));
        actions.put("Search_Button_Clicked", new SearchCommand(commands));
        actions.put("Cancel_Button_Clicked", new CloseSearchWindowCommand(commands));
        actions.put("Search_After_Action", new SearchAfterCommand(commands));
        actions.put("Search_Before_Action", new SearchBeforeCommand(commands));
        actions.put("Replace_Action", new InitReplaceCommand(commands));
        actions.put("Replace_Search_Button_Clicked", new ReplaceSearchCommand(commands));
        actions.put("Replace_Button_Clicked", new ReplaceCommand(commands));
        actions.put("ReplaceAll_Button_Clicked",new ReplaceAllCommand(commands));
        actions.put("Cancel_Replace_Button_Clicked", new CloseReplaceWindowCommand(commands));
        actions.put("Zoom_In", new ZoomInCommand(commands));
        actions.put("Zoom_Out", new ZoomOutCommand(commands));
        actions.put("Date_Time", new DateTimeCommand(commands));
        actions.put("Font",new ChangeFontCommand(commands));
        actions.put("Change_Theme", new ChangeThemeCommand(commands));
        actions.put("Undo_Action", new UndoCommand(commands));
        actions.put("Redo_Action", new RedoCommand(commands));
        actions.put("Copy_Action", new CopyTextCommand(commands));
        actions.put("Paste_Action", new PasteTextCommand(commands));
        actions.put("Cut_Action", new CutTextCommand(commands));
        actions.put("Delete_Action", new DeleteTextCommand(commands));
        actions.put("Select_All_Action", new SelectAllCommand(commands));
        actions.put("About", new AboutProgramCommand(commands));
    }
}

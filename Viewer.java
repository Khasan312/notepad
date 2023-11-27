import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.Utilities;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Viewer implements ActionListener {

    private JTextArea textArea;
    private JLabel labelScale;
    private JPanel panel;
    private JFrame frame;
    private UndoManager undoManager;
    private JFileChooser fileChooser;
    private Path currentFilePath;
    private JMenuBar menuBar;
    private int saveCounter = 0;
    private int MAX_FRAMES = 50;
    private int frameCount = 0;
    private boolean darkModeBoolean = false;
    private Controller controller;

    private SearchWindowController searchWindowController;
    private ReplaceWindowController replaceWindowController;

    private SearchPanel searchPanel;
    private ReplacePanel replacePanel;
    private FontDialog fontDialog;
    private UIStyler styler;

    private boolean isUpward = true;
    private boolean textWrap;
    private boolean caseSensitive;
    private String lastSearchText = "";
    private String lastReplaceText = "";

    Font font = new Font("Arial", Font.BOLD, 14);
    EmptyBorder border = new EmptyBorder(5, 5, 5, 5);

    public Viewer() {
        createNewDocument();
    }

    public void createNewDocument() {
        if (frame != null) {
            closeApp();
        }
        initializeControllers();
        panel = new JPanel();
        initializeUI();
    }

    private void initializeControllers() {
        searchWindowController = new SearchWindowController(this);
        replaceWindowController = new ReplaceWindowController(this);
        controller = new Controller(this);
        new WindowController(new Commands(this));
        fontDialog = new FontDialog(this);
        styler = new UIStyler(this);
    }

    private JFrame initializeUI() {
        textArea = new JTextArea();
        textArea.setFont(font);
        textArea.setMargin(new Insets(0, 5, 0, 0));

        initializeLabelLnCol();
        initializeFrameTitle();

        undoManager = new UndoManager();
        textArea.getDocument().addUndoableEditListener(undoManager);

        JScrollPane scrollPane = new JScrollPane(textArea);
        JMenuBar jMenuBar = getMenuBar(controller);

        textArea.setBackground(new Color(255, 255, 255));
        menuBar.setBackground(new Color(245, 245, 245));

        frame = new JFrame("Nameless - Notepad");
        frame.setIconImage(new ImageIcon("images/frameIcon.png").getImage());
        frame.setSize(1200, 850);
        frame.setJMenuBar(jMenuBar);
        frame.add("Center", scrollPane);
        frame.add(panel, BorderLayout.SOUTH);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowController(new Commands(this)));
        frame.setVisible(true);
        return frame;
    }

    JFrame[] frames = new JFrame[MAX_FRAMES];

    public void createNewFrame() {
        
        if (frameCount < MAX_FRAMES) {
            JFrame newFrame = initializeUI();
            newFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frames[frameCount] = newFrame;
            frameCount++;
        } else {
            JOptionPane.showMessageDialog(null, "The maximum number of windows has been exceeded. Close some of them");
        }

    }

    private void initializeLabelLnCol() {
        JLabel labelLnCol = new JLabel(" Line : 1, Column : 1 ");
        labelLnCol.setFont(font);

        panel.setLayout(new BorderLayout());

        textArea.addCaretListener(e -> {
            int position = e.getDot();
            int line = 1;
            int col = position + 1;

            try {
                int lineStart = Utilities.getRowStart(textArea, position);
                line = textArea.getLineOfOffset(lineStart) + 1;
                col = position - lineStart + 1;
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            labelLnCol.setText(" Line : " + line + ", Column : " + col + " ");
        });

        panel.add(labelLnCol, BorderLayout.WEST);
    }

    private void initializeFrameTitle() {
        textArea.getDocument().addDocumentListener(new DocumentListener() {


            @Override
            public void insertUpdate(DocumentEvent e) {
                updateTitle();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateTitle();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateTitle();
            }

            

            private void updateTitle() {
                String currentTitle = frame.getTitle();
                String newTitle;

                if (textArea.getText().isEmpty()) {
                    newTitle = currentTitle.replace("*", ""); 
                } else if (!currentTitle.startsWith("*")) {
                    newTitle = "*" + currentTitle; 
                } else {
                    newTitle = currentTitle;
                }

                frame.setTitle(newTitle);
                    
            }
        });
    
    }

    public JMenuBar getMenuBar(Controller controller) {
        JMenu[] menus = {
                getFileMenu(controller),
                getEditMenu(controller),
                getFormatMenu(controller),
                getViewMenu(controller),
                getHelpMenu(controller)
        };
        menuBar = new JMenuBar();

        for (JMenu menu : menus) {
            menu.setBorder(border);
            menu.setFont(font);
            menuBar.add(menu);
        }
        UIManager.put("PopupMenu.border", new LineBorder(new Color(166, 166, 166)));
        menuBar.setBorder(BorderFactory.createLineBorder(new Color(166, 166, 166)));
        UIManager.getLookAndFeelDefaults().put("MenuItem:MenuItemAccelerator[MouseOver].textForeground",Color.black);
        UIManager.getLookAndFeelDefaults().put("MenuItem[MouseOver].backgroundPainter",
                new mypainter(Color.RED));
        return menuBar;
    }

    private JMenu getFileMenu(Controller controller) {

        JMenu fileMenu = new JMenu("File");
        fileMenu.setBorder(new LineBorder(Color.WHITE));

        ImageIcon newImageIcon = new ImageIcon("images/new.png");
        JMenuItem newDocument = createMenuItem(newImageIcon, "New", "New_Document", KeyEvent.VK_N,
                ActionEvent.CTRL_MASK);

        ImageIcon newWindowImageIcon = new ImageIcon("images/new_window.png");
        JMenuItem newWindow = createMenuItem(newWindowImageIcon, "New Window", "New_Window", KeyEvent.VK_N, -2);

        ImageIcon openImageIcon = new ImageIcon("images/open.png");
        JMenuItem openDocument = createMenuItem(openImageIcon, "Open", "Open_Document", KeyEvent.VK_O,
                ActionEvent.CTRL_MASK);

        ImageIcon saveImageIcon = new ImageIcon("images/save.png");
        JMenuItem saveDocument = createMenuItem(saveImageIcon, "Save", "Save_Document", KeyEvent.VK_S,
                ActionEvent.CTRL_MASK);

        ImageIcon saveAsImageIcon = new ImageIcon("images/save_as.png");
        JMenuItem saveAsDocument = createMenuItem(saveAsImageIcon, "Save as", "Save_As_Document", KeyEvent.VK_S, -2);

        ImageIcon printImageIcon = new ImageIcon("images/print.png");
        JMenuItem print = createMenuItem(printImageIcon, "Print", "Print_Document", KeyEvent.VK_P,
                ActionEvent.CTRL_MASK);

        ImageIcon closeImageIcon = new ImageIcon("images/close.png");
        JMenuItem closeProgram = createMenuItem(closeImageIcon, "Exit", "Exit", -1, -1);

        fileMenu.setMnemonic('F');
        fileMenu.add(newDocument);
        fileMenu.add(newWindow);
        fileMenu.add(openDocument);
        fileMenu.add(saveDocument);
        fileMenu.add(saveAsDocument);
        fileMenu.add(new JSeparator());
        fileMenu.add(print);
        fileMenu.add(new JSeparator());
        fileMenu.add(closeProgram);

        return fileMenu;
    }

    private JMenu getEditMenu(Controller controller) {
        JMenu editMenu = new JMenu("Edit");

        ImageIcon undoImageIcon = new ImageIcon("images/undo.png");
        JMenuItem undoCommand = createMenuItem(undoImageIcon, "Undo", "Undo_Action", KeyEvent.VK_Z,
                ActionEvent.CTRL_MASK);

        ImageIcon redoImageIcon = new ImageIcon("images/redo.png");
        JMenuItem redoCommand = createMenuItem(redoImageIcon, "Redo", "Redo_Action", KeyEvent.VK_Y,
                ActionEvent.CTRL_MASK);

        ImageIcon cutImageIcon = new ImageIcon("images/cut.png");
        JMenuItem cutCommand = createMenuItem(cutImageIcon, "Cut", "Cut_Action", KeyEvent.VK_X,
                ActionEvent.CTRL_MASK);

        ImageIcon copyImageIcon = new ImageIcon("images/copy.png");
        JMenuItem copyCommand = createMenuItem(copyImageIcon, "Copy", "Copy_Action", KeyEvent.VK_C,
                ActionEvent.CTRL_MASK);

        ImageIcon pasteImageIcon = new ImageIcon("images/paste.png");
        JMenuItem pasteCommand = createMenuItem(pasteImageIcon, "Paste", "Paste_Action", KeyEvent.VK_V,
                ActionEvent.CTRL_MASK);

        ImageIcon deleteImageIcon = new ImageIcon("images/delete.png");
        JMenuItem deleteCommand = createMenuItem(deleteImageIcon, "Delete", "Delete_Action",
                KeyEvent.VK_DELETE, ActionEvent.CTRL_MASK);

        ImageIcon searchImageIcon = new ImageIcon("images/search.png");
        JMenuItem searchCommand = createMenuItem(searchImageIcon, "Search...", "Search_Action",
                KeyEvent.VK_F, ActionEvent.CTRL_MASK);

        ImageIcon searchAfterImageIcon = new ImageIcon("images/.png");
        JMenuItem searchAfterCommand = createMenuItem(searchAfterImageIcon, "Search After",
                "Search_After_Action", KeyEvent.VK_F3, InputEvent.SHIFT_DOWN_MASK);

        ImageIcon searchBeforeImageIcon = new ImageIcon("images/.png");
        JMenuItem searchBeforeCommand = createMenuItem(searchBeforeImageIcon, "Search Before",
                "Search_Before_Action", KeyEvent.VK_F3, InputEvent.SHIFT_DOWN_MASK);

        ImageIcon searchReplaceImageIcon = new ImageIcon("images/search_replace.png");
        JMenuItem replaceCommand = createMenuItem(searchReplaceImageIcon, "Replace", "Replace_Action",
                KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK);

        ImageIcon selectAllImageIcon = new ImageIcon("images/select_all.png");
        JMenuItem selectAllCommand = createMenuItem(selectAllImageIcon, "Select All", "Select_All_Action",
                KeyEvent.VK_A, ActionEvent.CTRL_MASK);

        ImageIcon dateTimeImageIcon = new ImageIcon("images/date_time.png");
        JMenuItem dateTime = createMenuItem(dateTimeImageIcon, "Date and Time", "Date_Time",
                KeyEvent.VK_F5, 0);

        editMenu.add(undoCommand);
        editMenu.add(redoCommand);
        editMenu.add(new JSeparator());
        editMenu.add(cutCommand);
        editMenu.add(copyCommand);
        editMenu.add(pasteCommand);
        editMenu.add(deleteCommand);
        editMenu.add(new JSeparator());
        editMenu.add(searchCommand);
        editMenu.add(searchAfterCommand);
        editMenu.add(searchBeforeCommand);
        editMenu.add(replaceCommand);
        editMenu.add(new JSeparator());
        editMenu.add(selectAllCommand);
        editMenu.add(dateTime);

        return editMenu;
    }

    private JMenu getViewMenu(Controller controller) {
        JMenu viewMenu = new JMenu("View");
        ImageIcon scaleImageIcon = new ImageIcon("images/scale.png");
        JMenu scaleMenu = new JMenu("Scale");
        scaleMenu.setIcon(scaleImageIcon);
        scaleMenu.setFont(font);
        scaleMenu.setBorder(border);

        ImageIcon zoomInImageIcon = new ImageIcon("images/zoom_in.png");
        JMenuItem zoomIn = createMenuItem(zoomInImageIcon, "Zoom In", "Zoom_In", KeyEvent.VK_ADD,
                ActionEvent.CTRL_MASK);

        ImageIcon zoomOutImageIcon = new ImageIcon("images/zoom_out.png");
        JMenuItem zoomOut = createMenuItem(zoomOutImageIcon, "Zoom Out", "Zoom_Out", KeyEvent.VK_SUBTRACT,
                ActionEvent.CTRL_MASK);

        ImageIcon darkModeImageIcon = new ImageIcon("images/theme.png");
        JMenuItem darkMode = new JMenuItem("Dark Mode", darkModeImageIcon);
        darkMode.setFont(font);
        darkMode.setBorder(border);
        darkMode.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0, ActionEvent.CTRL_MASK));
        darkMode.setActionCommand("Change_Theme");
        darkMode.addActionListener(controller);

        scaleMenu.add(zoomIn);
        scaleMenu.add(zoomOut);

        viewMenu.add(scaleMenu);
        viewMenu.add(darkMode);

        return viewMenu;
    }

    private JMenu getHelpMenu(Controller controller) {
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setFont(font);
        helpMenu.setBorder(border);
        ImageIcon aboutImageIcon = new ImageIcon("images/about.png");
        JMenuItem aboutProgram = new JMenuItem("About", aboutImageIcon);
        aboutProgram.setFont(font);
        aboutProgram.setBorder(border);
        aboutProgram.addActionListener(e -> showAboutDialog());
        aboutProgram.setActionCommand("About");

        helpMenu.add(aboutProgram);
        return helpMenu;

    }

    public void showAboutDialog() {
        AboutDialog.showAboutDialog();
    }

    public void undoAction() {
        if (undoManager.canUndo()) {
            undoManager.undo();
        }
    }

    public void redoAction() {
        if (undoManager.canRedo()) {
            undoManager.redo();
        }
    }

    public void selectAll() {
        textArea.selectAll();
    }

    public void getFontDialig() {
        fontDialog.showFontDialog();
    }

    private JMenu getFormatMenu(Controller controller) {
        ImageIcon imageIcon1 = new ImageIcon("images/font.png");
        JMenuItem fontSet = new JMenuItem("Font...", imageIcon1);
        fontSet.setFont(font);
        fontSet.setBorder(border);
        fontSet.addActionListener(e -> getFontDialig());
        fontSet.setActionCommand("Font");
        JMenu formatMenu = new JMenu("Format");
        formatMenu.add(fontSet);
        return formatMenu;
    }

    public Path getPath(String command) {
        if (fileChooser == null) {
            fileChooser = new JFileChooser();
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
        }
        if (command.equals("Save_Document")) {
            if (currentFilePath != null) {
                saveCounter++;
                return currentFilePath;
            } else {
                fileChooser.setSelectedFile(new File("*.txt"));
                fileChooser.setApproveButtonText("Save");
                saveCounter++;
            }
        }
        if (command.equals("Save_As_Document")) {
            currentFilePath = null;
            fileChooser.setSelectedFile(new File("*.txt"));
            fileChooser.setApproveButtonText("Save");
            saveCounter++;
        }
        int returnValue = fileChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.CANCEL_OPTION) {
            saveCounter--;
        }
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            currentFilePath = fileChooser.getSelectedFile().toPath();
            return currentFilePath;
        }
        return null;
    }

    public int showSaveChangesDialog() {
        int jOptionPaneCancelAnswer = 3;
        if (saveCounter < 1 && !textArea.getText().isEmpty()) {
            return JOptionPane.showConfirmDialog(frame, "Do you want to save changes?", "Save Changes",
                    JOptionPane.YES_NO_CANCEL_OPTION);
        }
        return jOptionPaneCancelAnswer;
    }

    public void showReadingErrMessage() {
        JOptionPane.showMessageDialog(frame, "We have a problem with reading the file!", "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public void showSavingErrMessage() {
        JOptionPane.showMessageDialog(frame, "We have a problem saving the file!", "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void initSearchPanel() {
        searchPanel = new SearchPanel("Search");
        searchPanel.setIconImage(new ImageIcon("images/search.png").getImage());
        searchPanel.addWindowListener(searchWindowController);
        setLastSearchText();
        SetSelectedProperties();

        searchPanel.getSearchButton().addActionListener(controller);
        searchPanel.getSearchButton().setActionCommand("Search_Button_Clicked");

        searchPanel.getCancelButton().addActionListener(controller);
        searchPanel.getCancelButton().setActionCommand("Cancel_Button_Clicked");
    }

    public void setSearchPanelNull() {
        lastSearchText = searchPanel.getSearchFieldText();
        isUpward = searchPanel.getUpSearch().isSelected();
        caseSensitive = isCaseSensitive();
        textWrap = searchPanel.getTextWrap().isSelected();
        searchPanel = null;
    }

    public int getDefaultCloseOperation() {
        return searchPanel.getDefaultCloseOperation();
    }

    public SearchPanel getSearchPanel() {
        return searchPanel;
    }

    public int getCaretPosition() {
        return textArea.getCaretPosition();
    }

    public String getSearchPanelText() {
        return searchPanel.getSearchFieldText();
    }

    public boolean isCaseSensitive() {
        return searchPanel.getCaseSensitive().isSelected();
    }

    public boolean isUpwardSearch() {
        return searchPanel.getUpSearch().isSelected();
    }

    public void closeSearchPanelWindow() {
        if (searchPanel != null) {
            searchPanel.dispose();
            setSearchPanelNull();
        }
    }

    public String getLastSearchText() {
        return lastSearchText;
    }

    public void setUpward(boolean upward) {
        isUpward = upward;
    }

    public void setLastSearchText() {
        if (!lastSearchText.isEmpty()) {
            searchPanel.setSearchFieldText(lastSearchText);

            int startIndex = 0;
            int endIndex = lastSearchText.length();

            searchPanel.getSearchField().setSelectionStart(startIndex);
            searchPanel.getSearchField().setSelectionEnd(endIndex);
        }
    }

    public void SetSelectedProperties() {
        if (!(searchPanel.getUpSearch().isSelected() && searchPanel.getDownSearch().isSelected())) {
            searchPanel.getUpSearch().setSelected(true);
        }
        if (caseSensitive) {
            searchPanel.setCaseSensitive(true);
        }
        if (textWrap) {
            searchPanel.setTextWrap(true);
        }
        if (!isUpward) {
            searchPanel.setDownSearch(true);
            searchPanel.setUpSearch(false);
        }
    }

    // Методы Замены

    public void initReplacePanel() {
        replacePanel = new ReplacePanel("Replace");
        replacePanel.addWindowListener(replaceWindowController);
        setLastReplaceSearchText();
        SetSelectedPropertiesReplace();



        replacePanel.getReplaceSearchButton().addActionListener(controller);
        replacePanel.getReplaceSearchButton().setActionCommand("Replace_Search_Button_Clicked");

        replacePanel.getReplaceButton().addActionListener(controller);
        replacePanel.getReplaceButton().setActionCommand("Replace_Button_Clicked");

        replacePanel.getReplaceAllButton().addActionListener(controller);
        replacePanel.getReplaceAllButton().setActionCommand("ReplaceAll_Button_Clicked");

        replacePanel.getCancelButton().addActionListener(controller);
        replacePanel.getCancelButton().setActionCommand("Cancel_Replace_Button_Clicked");
    }


    public void setReplacePanelNull() {
        lastSearchText = replacePanel.getReplaceSearchFieldText();
        lastReplaceText = replacePanel.getReplaceFieldText();
        caseSensitive = isCaseSensitiveReplace();
        textWrap = isTextWrapReplace();
        replacePanel = null;
    }

    public int getReplacePanelDefaultCloseOperation() {
        return replacePanel.getDefaultCloseOperation();
    }

    public ReplacePanel getReplacePanel() {
        return replacePanel;
    }

    public void closeReplacePanelWindow() {
        if (replacePanel != null) {
            replacePanel.dispose();
            setReplacePanelNull();
        }
    }

    public String getReplaceSearchPanelText() {
        return replacePanel.getReplaceSearchFieldText();
    }

    public String getReplacePanelText() {
        return replacePanel.getReplaceFieldText();
    }

    public boolean isCaseSensitiveReplace() {
        return replacePanel.getCaseSensitive().isSelected();
    }

    public boolean isTextWrapReplace() {
        return replacePanel.getTextWrap().isSelected();
    }



    public void setLastReplaceSearchText() {
        if (!lastSearchText.isEmpty()) {
            replacePanel.setReplaceSearchFieldText(lastSearchText);
            replacePanel.setReplaceFieldText(lastReplaceText);

            int startIndex = 0;
            int endIndex = lastSearchText.length();

            replacePanel.getReplaceSearchField().setSelectionStart(startIndex);
            replacePanel.getReplaceSearchField().setSelectionEnd(endIndex);
        }
    }
    public void SetSelectedPropertiesReplace() {
        if (caseSensitive) {
            replacePanel.setCaseSensitive(true);
        }
        if (textWrap) {
            replacePanel.setTextWrap(true);
        }
    }

    public void selectReplaceText(int start, int end) {
        textArea.setSelectionStart(start);
        textArea.setSelectionEnd(end);
    }

    public void showReplaceSearchNotFoundMessage() {
        JOptionPane.showMessageDialog(frame, "Can't find \"" + replacePanel.getReplaceSearchFieldText() + "\"", "Search",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public int getSelectionEnd(){
        return textArea.getSelectionEnd();
    }
    public int getSelectionStart(){
        return textArea.getSelectionStart();
    }

    public void replaceSelectedText(String replaceText) {
        textArea.replaceSelection(replaceText);
    }


    public void selectText(int start, int end) {
        textArea.setSelectionStart(start);
        textArea.setSelectionEnd(end);
    }

    public void showSearchNotFoundMessage() {
        JOptionPane.showMessageDialog(frame, "Can't find \"" + searchPanel.getSearchFieldText() + "\"", "Search",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void closeApp() {
        frame.dispose();
    }

    public boolean hasUnsavedChanges() {
        return !textArea.getText().isEmpty();
    }

    public void setTextArea(String text) {
        textArea.setText(text);
    }

    public String getContentTextArea() {
        return textArea.getText();
    }

    public Font getFontTextArea() {
        return textArea.getFont();
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public void setFontTextArea(Font font) {
        textArea.setFont(font);
    }

    public JFrame getFrame() {
        return frame;
    }

    public JMenuBar getBar() {
        return menuBar;
    }

    private JMenuItem createMenuItem(ImageIcon imageIcon, String text, String command, int keyEvent, int modifier) {
        JMenuItem menuItem = new JMenuItem(text, imageIcon);
        menuItem.setFont(font);
        EmptyBorder border = new EmptyBorder(1, 5, 1, 0);
        menuItem.setBorder(border);
        if (keyEvent != -1) {
            menuItem.setAccelerator(KeyStroke.getKeyStroke(keyEvent, modifier));
        }
        if (modifier == -2) {
            menuItem.setAccelerator(
                    KeyStroke.getKeyStroke(keyEvent, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        }
        menuItem.setActionCommand(command);
        menuItem.addActionListener(controller);
        return menuItem;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // String font = fontDialog.getFontList();
        // int style = fontDialog.getStyleList();
        // int size = fontDialog.getSizeList();
        // Font selectedFont = new Font(font, style, size);
        // fontDialog.setPreviewText(selectedFont);
    }


    class mypainter implements Painter<JComponent> {

        private Color color=null;

        public mypainter(Color c) {
            color = c;
        }

        @Override
        public void paint(Graphics2D g, JComponent object, int width, int height) {
            g.setColor(color);
            g.fillRect(0, 0, width, height);
        }

    }
}

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;

public class Commands {
    private Viewer viewer;
    private Print printDocument;
    private String fileContent;
    private JFileChooser fileChooser;
    private boolean darkModeBoolean = false;

    private int start;
    private int openWindowCounter = 0;
    private boolean replaceQeue = false;

    public Commands(Viewer viewer) {
        this.viewer = viewer;
    }

    public void aboutProgram() {
        viewer.showAboutDialog();
    }

    public void redoAction() {
        viewer.redoAction();
    }

    public void undoAction() {
        viewer.undoAction();
    }

    public void pasteText() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable clipboardData = clipboard.getContents(null);

        if (clipboardData != null && clipboardData.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                String clipboardText = (String) clipboardData.getTransferData(DataFlavor.stringFlavor);
                int selectionStart = viewer.getTextArea().getSelectionStart();
                int selectionEnd = viewer.getTextArea().getSelectionEnd();
                if (selectionStart != selectionEnd) {
                    viewer.getTextArea().replaceRange("", selectionStart, selectionEnd);
                }
                viewer.getTextArea().insert(clipboardText, viewer.getCaretPosition());
            } catch (UnsupportedFlavorException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void cutText() {
        String selectedText = viewer.getTextArea().getSelectedText();
        if (selectedText != null) {
            copyText();
            deleteText();
        }
    }

    public void copyText() {
        String selectedText = viewer.getTextArea().getSelectedText();
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable selection = new StringSelection(selectedText);
        clipboard.setContents(selection, null);
    }

    public void deleteText() {
        String selectedText = viewer.getTextArea().getSelectedText();
        if (selectedText != null) {
            viewer.getTextArea().replaceSelection("");
        }
    }

    public void selectAll() {
        viewer.selectAll();
    }

    public void changeFont() {
        viewer.getFontDialig();
    }

    public void dateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        String formattedDate = now.format(formatter);

        String currentText = viewer.getTextArea().getText();
        viewer.setTextArea(currentText + formattedDate);
    }

    public void zoomOut() {
        Font currentFont = viewer.getFontTextArea();
        int size = Math.max(10, currentFont.getSize() - 2);
        Font newFont = new Font(currentFont.getName(), currentFont.getStyle(), size);
        viewer.setFontTextArea(newFont);
    }

    public void zoomIn() {
        Font curreFont = viewer.getFontTextArea();
        int size = Math.max(10, curreFont.getSize() + 2);
        Font newFont = new Font(curreFont.getName(), curreFont.getStyle(), size);
        viewer.setFontTextArea(newFont);
    }

    public void updateTextFieldColors() {

        fileChooser = new JFileChooser();
        Color colorMenu = new Color(245, 245, 245);
        Color colorMenuDark = new Color(60,63,65);
        Color textAreaColor = new Color(43, 43, 43);
        Color optionPaneColor = new Color(255, 255, 255);

        if (darkModeBoolean()) {
            viewer.getTextArea().setBackground(textAreaColor);
            viewer.getTextArea().setForeground(Color.WHITE);
            viewer.getBar().setBackground(colorMenuDark);
            viewer.getTextArea().setCaretColor(Color.WHITE);

            setMenuBarColors(viewer.getBar(), colorMenuDark, Color.WHITE);

            UIManager.put("TextArea.background", new Color(255, 255, 255));
            UIManager.put("OptionPane.background", colorMenuDark);
            UIManager.put("Panel.background", colorMenuDark);
            UIManager.put("FileChooser.background", colorMenuDark);
            UIManager.put("FileChooser.approveButtonBackground", colorMenuDark);
            UIManager.put("ComboBox.background", colorMenuDark);
            UIManager.put("ComboBox.foreground", Color.white);
            UIManager.put("CheckBox.background", colorMenuDark);
            UIManager.put("CheckBox.foreground", Color.white);
            UIManager.put("TextField.background", colorMenuDark);
            UIManager.put("TextField.foreground", Color.white);
            UIManager.put("RadioButton.background", colorMenuDark);
            UIManager.put("Button.background", colorMenuDark);
            UIManager.put("Button.foreground", Color.white);
            UIManager.put("List.background", colorMenuDark);
            UIManager.put("List.foreground", Color.white);
            UIManager.put("Label.foreground", Color.white);
            fileChooser.updateUI();

        } else {

            viewer.getTextArea().setBackground(Color.WHITE);
            viewer.getTextArea().setForeground(Color.BLACK);
            viewer.getBar().setBackground(colorMenu);
            viewer.getTextArea().setCaretColor(Color.BLACK);

            setMenuBarColors(viewer.getBar(), Color.WHITE, Color.BLACK);

            UIManager.put("TextArea.background", Color.WHITE);
            UIManager.put("OptionPane.background", optionPaneColor);
            UIManager.put("Panel.background", optionPaneColor);
            UIManager.put("FileChooser.background", optionPaneColor);
            UIManager.put("FileChooser.approveButtonBackground", optionPaneColor);
            UIManager.put("ComboBox.background", optionPaneColor);
            UIManager.put("ComboBox.foreground", Color.BLACK);
            UIManager.put("CheckBox.background", Color.WHITE);
            UIManager.put("CheckBox.foreground", Color.BLACK);
            UIManager.put("TextField.background", Color.WHITE);
            UIManager.put("TextField.foreground", Color.BLACK);
            UIManager.put("RadioButton.background", Color.WHITE);
            UIManager.put("Button.background", optionPaneColor);
            UIManager.put("Button.foreground", Color.black);
            UIManager.put("List.background", optionPaneColor);
            UIManager.put("List.foreground", Color.BLACK);
            UIManager.put("Label.foreground", Color.black);
            fileChooser.updateUI();

        }
    }

    public void setMenuBarColors(JMenuBar menuBar, Color backgroundColor, Color foregroundColor) {
        for (Component c : menuBar.getComponents()) {
            if (c instanceof JMenu) {
                setMenuColors((JMenu) c, backgroundColor, foregroundColor);
            }
        }
    }

    public void setMenuColors(JMenu menu, Color backgroundColor, Color foregroundColor) {
        menu.setBackground(backgroundColor);
        menu.setForeground(foregroundColor);

        for (Component c : menu.getMenuComponents()) {
            if (c instanceof JMenu) {

                setMenuColors((JMenu) c, backgroundColor, foregroundColor);
            } else if (c instanceof JMenuItem) {
                c.setBackground(backgroundColor);
                c.setForeground(foregroundColor);
            }
        }
    }

    public boolean darkModeBoolean() {
        return darkModeBoolean = !darkModeBoolean;
    }

    public boolean exit() {
        if (viewer.hasUnsavedChanges()) {
            int response = viewer.showSaveChangesDialog();
            if (response == 0) {
                saveDataToFile(viewer.getPath("Save_Document"), viewer.getContentTextArea());
                viewer.closeApp();
            } else if (response == 2) {
                return false;
            }
        }
        viewer.closeApp();
        return true;
    }

    public void saveAsDocument() {
        Path file = viewer.getPath("Save_As_Document");
        Path fileName = file.getFileName();
        if (file != null) {
            boolean flag = saveDataToFile(file, viewer.getContentTextArea());
         
            viewer.getFrame().setTitle(fileName + " - Notepad");
            if (!flag) {
                viewer.showSavingErrMessage();
            }
        }
    }

    public void printDocument() {
        String data = viewer.getContentTextArea();
        Font font = viewer.getFontTextArea();
        Print document = new Print(data, font);
        document.printDocument();
    }

    public void createNewDocument() {
      if (viewer.hasUnsavedChanges()) {
        int response = viewer.showSaveChangesDialog();
     
        if (response == 0 || response == 3) {
            saveDataToFile(viewer.getPath("Save"), viewer.getContentTextArea());
        } else if (response == 2) {
          return;
        }
      }
      viewer.setTextArea("");   
    }

    public void createNewWindow() {
          viewer.createNewFrame();
        
      }

    public void saveDocument() {
        Path file = viewer.getPath("Save_Document");
        Path fileName = file.getFileName();
        if (file != null) {
            boolean flag = saveDataToFile(file, viewer.getContentTextArea());
            viewer.getFrame().setTitle(fileName + " - Notepad");

            if (!flag) {
                viewer.showSavingErrMessage();
            }
        }
    }

    public void openDocument() {
        if (viewer.hasUnsavedChanges()) {
            int response = viewer.showSaveChangesDialog();
            if (response == 0) {
                saveDataToFile(viewer.getPath("Save"), viewer.getContentTextArea());
            } else if (response == 2) {
                return;
            }
        }
        Path path = viewer.getPath("Open");
        if (path != null) {
            fileContent = readFile(path);
            if (fileContent != null) {
                viewer.setTextArea(fileContent);
            } else {
                viewer.showReadingErrMessage();
            }
        }
    }

    public void printFile() {
        String text = viewer.getContentTextArea();
        Font font = viewer.getFontTextArea();
        printDocument = new Print(text, font);
        printDocument.printDocument();
    }

    public boolean saveDataToFile(Path file, String data) {
        try {
            Files.write(file, data.getBytes());
            return true;
        } catch (IOException e) {
            System.out.println("Error saving file" + e);
            return false;
        }
    }

    public String readFile(Path file) {
        try {
            List<String> linesList = Files.readAllLines(file);
            fileContent = String.join("\n", linesList);
            linesList.clear();
            return fileContent;
        } catch (IOException e) {
            System.out.println("Error reading file " + e);
            return null;
        }
    }



    public void initSearch() {
        if (viewer.getSearchPanel() == null) {
            viewer.initSearchPanel();
            viewer.getSearchPanel().setVisible(true);
            openWindowCounter++;
        }
    }

    public void searchAfter() {
        if (viewer.getLastSearchText().isEmpty()) {
            viewer.initSearchPanel();
            viewer.getSearchPanel().setVisible(true);
        } else {
            viewer.setUpward(false);
            viewer.initSearchPanel();
            viewer.getSearchPanel().setVisible(false);
            doSearch();
        }
    }

    public void searchBefore() {
        if (viewer.getLastSearchText().isEmpty()) {
            viewer.initSearchPanel();
            viewer.getSearchPanel().setVisible(true);
        } else {
            viewer.setUpward(true);
            viewer.initSearchPanel();
            viewer.getSearchPanel().setVisible(false);
            doSearch();
        }
    }

    public void doSearch() {
        String searchText = viewer.getSearchPanelText();
        String content = viewer.getContentTextArea();
        boolean isUpward = viewer.isUpwardSearch();
        boolean isCaseSensitive = viewer.isCaseSensitive();

        if (start == 0) {
            start = viewer.getCaretPosition();
        }

        if (isUpward) {
            doUpWardSearch(searchText, content, isCaseSensitive);
        } else {
            doDownwardSearch(searchText, content, isCaseSensitive);
        }
    }

    public void doDownwardSearch(String searchText, String content, boolean isCaseSensitive) {
        int index = -1;

        if (isCaseSensitive) {
            index = content.indexOf(searchText, start);
        } else {
            index = content.toLowerCase().indexOf(searchText.toLowerCase(), start);
        }

        if (index != -1) {
            viewer.selectText(index, index + searchText.length());
            start = index + 1;
        } else {
            viewer.showSearchNotFoundMessage();
        }
    }

    public void doUpWardSearch(String searchText, String content, boolean isCaseSensitive) {
        int index = -1;

        if (start == content.length()) {
            start = viewer.getCaretPosition();
        }

        String reversedContent;
        String reversedSearchText;
        if (isCaseSensitive) {

            reversedContent = new StringBuilder(content).reverse().toString();
            reversedSearchText = new StringBuilder(searchText).reverse().toString();

        } else {
            reversedContent = new StringBuilder(content.toLowerCase()).reverse().toString();
            reversedSearchText = new StringBuilder(searchText.toLowerCase()).reverse().toString();
        }
        index = reversedContent.indexOf(reversedSearchText, content.length() - start);
        if (index != -1) {
            index = content.length() - index - reversedSearchText.length();
        }

        if (index != -1) {
            viewer.selectText(index, index + searchText.length());
            start = index;
        } else {
            viewer.showSearchNotFoundMessage();
        }
    }

    public void closeSearchPanelWindow() {
        viewer.closeSearchPanelWindow();
        start = 0;
    }

   

    public void initReplace() {
        if (viewer.getReplacePanel() == null) {
            viewer.initReplacePanel();
            viewer.getReplacePanel().setVisible(true);
            openWindowCounter++;
        }
    }

    public void doReplaceSearch() {
        String searchText = viewer.getReplaceSearchPanelText();
        String content = viewer.getContentTextArea();
        boolean isCaseSensitive = viewer.isCaseSensitiveReplace();
        boolean isTextWrap = viewer.isTextWrapReplace();
        start = viewer.getCaretPosition();
        doReplaceSearch(searchText, content, isCaseSensitive, isTextWrap);

    }
    public void doReplace() {
        String searchText = viewer.getReplaceSearchPanelText();
        String content = viewer.getContentTextArea();
        String replaceText = viewer.getReplacePanelText();
        boolean isCaseSensitive = viewer.isCaseSensitiveReplace();
        boolean isTextWrap = viewer.isTextWrapReplace();
        start = viewer.getCaretPosition();
        doReplace(replaceText, searchText, content, isCaseSensitive, isTextWrap);
    }



    public void doReplaceAll() {
        String searchText = viewer.getReplaceSearchPanelText();
        String content = viewer.getContentTextArea();
        String replaceText = viewer.getReplacePanelText();
        boolean isCaseSensitive = viewer.isCaseSensitiveReplace();
        start = viewer.getCaretPosition();
        doReplaceAll(replaceText, searchText, content, isCaseSensitive);
    }


    public void doReplaceSearch(String searchText, String content, boolean isCaseSensitive, boolean isTextWrap) {
        int index;
        int hasSearchText;

        if (isCaseSensitive) {
            hasSearchText = content.indexOf(searchText);
            index = content.indexOf(searchText, start);
        } else {
            hasSearchText = content.toLowerCase().indexOf(searchText.toLowerCase());
            index = content.toLowerCase().indexOf(searchText.toLowerCase(), start);
        }
        if (hasSearchText != -1) {
            if (index != -1) {
                viewer.selectReplaceText(index, index + searchText.length());
                start = index + 1;
                replaceQeue = true;
            } else {
                if (isTextWrap) {
                    viewer.selectReplaceText(hasSearchText, hasSearchText + searchText.length());
                    start = hasSearchText + 1;
                    replaceQeue = true;
                } else {
                    viewer.showReplaceSearchNotFoundMessage();
                }
            }
        } else {
            viewer.showReplaceSearchNotFoundMessage();
        }
    }


    public void doReplace(String replaceText, String searchText, String content, boolean isCaseSensitive, boolean isTextWrap) {
        if (replaceQeue) {
            int index = viewer.getSelectionStart();
            viewer.replaceSelectedText(replaceText);
            viewer.selectReplaceText(index, index + replaceText.length());
            start = index + 1;
            replaceQeue = false;
        }
        else {
            doReplaceSearch(searchText, content, isCaseSensitive, isTextWrap);
        }
    }
    public void doReplaceAll(String replaceText, String searchText, String content, boolean isCaseSensitive) {
        int index;

        if (isCaseSensitive) {
            index = content.indexOf(searchText);
        }
        else {
            index = content.toLowerCase().indexOf(searchText.toLowerCase());
        }

        while(index != -1) {

            viewer.selectReplaceText(index, index + searchText.length());
            viewer.replaceSelectedText(replaceText);
            start = index + 1;

            if (isCaseSensitive) {
                index = viewer.getContentTextArea().indexOf(searchText, start);
            } else {
                index = viewer.getContentTextArea().toLowerCase().indexOf(searchText.toLowerCase(), start);
            }
        }
    }

    public void closeReplacePanelWindow() {
        viewer.closeReplacePanelWindow();
        start = 0;
    }
}

import java.awt.Color;
import java.awt.Component;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;

public class UIStyler {
    private JFileChooser fileChooser;
    private boolean darkModeBoolean = false;
    private Viewer viewer;
    private Controller controller;
    private JMenuBar menuBar;

    public UIStyler(Viewer viewer) {
        controller = new Controller(viewer);
        this.viewer = viewer;
        menuBar = viewer.getMenuBar(controller);
    }

    public void updateTextFieldColors() {

        fileChooser = new JFileChooser();
        Color colorMenu = new Color(245, 245, 245);
        Color colorMenuDark = new Color(60,63,65);
        Color textAreaColor = new Color(43, 43, 43);
        Color optionPaneColor = new Color(255, 255, 255);

        if (darkModeBoolean) {
            viewer.getTextArea().setBackground(textAreaColor);
            viewer.getTextArea().setForeground(Color.WHITE);
            menuBar.setBackground(colorMenuDark);
            viewer.getTextArea().setCaretColor(Color.WHITE);

            setMenuBarColors(menuBar, colorMenuDark, Color.WHITE);

            UIManager.put("TextArea.background", new Color(255, 255, 255));
            UIManager.put("OptionPane.background", colorMenuDark);
            UIManager.put("Panel.background", colorMenuDark);
            UIManager.put("FileChooser.background", colorMenuDark);
            UIManager.put("FileChooser.approveButtonBackground", colorMenuDark);
            UIManager.put("ComboBox.background", colorMenuDark);
            UIManager.put("CheckBox.background", colorMenuDark);
            UIManager.put("TextField.background", colorMenuDark);
            UIManager.put("RadioButton.background", colorMenuDark);
            fileChooser.updateUI();

        } else {
            System.out.println("Light Mode");
            viewer.getTextArea().setBackground(Color.WHITE);
            viewer.getTextArea().setForeground(Color.BLACK);
            menuBar.setBackground(colorMenu);
            viewer.getTextArea().setCaretColor(Color.BLACK);

            setMenuBarColors(menuBar, Color.WHITE, Color.BLACK);

            UIManager.put("TextArea.background", Color.WHITE);
            UIManager.put("OptionPane.background", optionPaneColor);
            UIManager.put("Panel.background", optionPaneColor);
            UIManager.put("FileChooser.background", optionPaneColor);
            UIManager.put("FileChooser.approveButtonBackground", optionPaneColor);
            UIManager.put("ComboBox.background", optionPaneColor);
            UIManager.put("CheckBox.background", Color.WHITE);
            UIManager.put("TextField.background", Color.WHITE);
            UIManager.put("TextField.foreground", Color.BLACK);
            UIManager.put("RadioButton.background", Color.WHITE);
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

    public boolean getDarkMode() {
        return darkModeBoolean = !darkModeBoolean;
    }

}

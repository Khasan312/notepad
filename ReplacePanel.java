import javax.swing.*;
import java.awt.*;
import javax.swing.border.LineBorder;

public class ReplacePanel extends JFrame {
    private JPanel panel = new JPanel();
    private JTextField searchField = new JTextField(25);
    private JButton searchButton = new JButton("Search");
    private JButton cancelButton = new JButton("Cancel");
    private JLabel what = new JLabel("What:");
    private JCheckBox caseSensitive = new JCheckBox("Case Sensitive");
    private JCheckBox textWrap = new JCheckBox("Text Wrap");
    private JTextField replaceField = new JTextField(35);
    private JButton replaceButton = new JButton("Replace");
    private JButton replaceAllButton = new JButton("Replace all");
    private JLabel byWhat = new JLabel("By what:");

    public ReplacePanel(String title) throws HeadlessException {
        super(title);
        setSize(490, 230);
        setResizable(false);
        setLocationRelativeTo(null);
        initReplaceComponents();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(false);
        
    }

    private void initReplaceComponents() {
        panel.setLayout(null);

        setButtonsProperties();

        searchField.setBounds(60, 15, 290, 25);
        replaceField.setBounds(60, 50, 290, 25);
        searchButton.setBounds(365, 10, 100, 25);
        replaceButton.setBounds(365, 45, 100, 25);
        replaceAllButton.setBounds(365, 80, 100, 25);
        cancelButton.setBounds(365, 115, 100, 25);
        what.setBounds(15, 14, 100, 20);
        byWhat.setBounds(10, 49, 100, 20);
        caseSensitive.setBounds(10, 140, 120, 25);
        textWrap.setBounds(10, 110, 120, 25);
        caseSensitive.setFocusable(false);
        textWrap.setFocusable(false);

        panel.add(searchField);
        panel.add(replaceField);
        panel.add(searchButton);
        panel.add(cancelButton);
        panel.add(replaceButton);
        panel.add(replaceAllButton);
        panel.add(what);
        panel.add(byWhat);
        panel.add(caseSensitive);
        panel.add(textWrap);
        add(panel);
    }

    private void setButtonsProperties() {
        searchButton.setBackground(Color.WHITE);
        replaceButton.setBackground(Color.WHITE);
        replaceAllButton.setBackground(Color.WHITE);
        cancelButton.setBackground(Color.WHITE);
        searchButton.setFocusable(false);
        replaceButton.setFocusable(false);
        replaceAllButton.setFocusable(false);
        cancelButton.setFocusable(false);
    }

    public JButton getReplaceSearchButton() {
        return searchButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public JButton getReplaceButton() {
        return replaceButton;
    }

    public JButton getReplaceAllButton() {
        return replaceAllButton;
    }

    public String getReplaceSearchFieldText() {
        return searchField.getText();
    }

    public String getReplaceFieldText() {
        return replaceField.getText();
    }

    public void setReplaceSearchFieldText(String text) {
        searchField.setText(text);
    }

    public JTextField getReplaceSearchField() {
        return searchField;
    }


    public void setReplaceFieldText(String text) {
        replaceField.setText(text);
    }

    public JCheckBox getCaseSensitive() {
        return caseSensitive;
    }

    public JCheckBox getTextWrap() {
        return textWrap;
    }


    public void setCaseSensitive(boolean isCaseSensitive) {
        caseSensitive.setSelected(isCaseSensitive);
    }


    public void setTextWrap(boolean isTextWrap) {
        textWrap.setSelected(isTextWrap);
    }



}

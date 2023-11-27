import javax.swing.*;
import java.awt.*;
import javax.swing.border.LineBorder;

public class SearchPanel extends JFrame {
    private JPanel panel = new JPanel();
    private JTextField searchField = new JTextField(25);
    private JButton searchButton = new JButton("Search");
    private JButton cancelButton = new JButton("Cancel");
    private JLabel what = new JLabel("What:");
    private JRadioButton upSearch = new JRadioButton("Up");
    private JRadioButton downSearch = new JRadioButton("Down");
    private JLabel direction = new JLabel("Direction");
    private JButton transperantButton = new JButton();
    private JCheckBox caseSensitive = new JCheckBox("Case Sensitive");
    private JCheckBox textWrap = new JCheckBox("Text Wrap");

    public SearchPanel(String title) throws HeadlessException {
        super(title);
        setSize(380, 157);
        setResizable(false);
        setLocationRelativeTo(null);
        initComponents();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(false);
    }

    private void initComponents() {
        panel.setLayout(null);

        setButtonsProperties();
        initRadioGroup();

        searchField.setBounds(60, 10, 210, 20);
        searchButton.setBounds(280, 10, 75, 20);
        cancelButton.setBounds(280, 35, 75, 20);
        what.setBounds(10, 9, 100, 20);
        direction.setBounds(150, 40, 60, 20);
        transperantButton.setBounds(128, 50, 131, 38);
        upSearch.setBounds(130, 60, 50, 20);
        downSearch.setBounds(200, 60, 58, 20);
        caseSensitive.setBounds(10, 70, 95, 20);
        textWrap.setBounds(10, 93, 95, 20);
        caseSensitive.setFocusable(false);
        textWrap.setFocusable(false);

        panel.add(searchField);
        panel.add(searchButton);
        panel.add(cancelButton);
        panel.add(what);
        panel.add(upSearch);
        panel.add(downSearch);
        panel.add(direction);
        panel.add(transperantButton);
        panel.add(caseSensitive);
        panel.add(textWrap);
        add(panel);
    }
    private void setButtonsProperties() {
        searchButton.setBackground(Color.WHITE);
        cancelButton.setBackground(Color.WHITE);
        searchButton.setFocusable(false);
        cancelButton.setFocusable(false);
        transperantButton.setFocusable(false);
        transperantButton.setContentAreaFilled(false);
        transperantButton.setBorder(new LineBorder(Color.lightGray));
    }

    private void initRadioGroup() {
        ButtonGroup radioGroup = new ButtonGroup();
        upSearch.setFocusable(false);
        downSearch.setFocusable(false);
        radioGroup.add(upSearch);
        radioGroup.add(downSearch);
    }

    public JTextField getSearchField() {
        return searchField;
    }

    public String getSearchFieldText() {
        return searchField.getText();
    }

    public void setSearchFieldText(String text) {
        searchField.setText(text);
    }

    public JButton getSearchButton() {
        return searchButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public JRadioButton getUpSearch() {
        return upSearch;
    }

    public JCheckBox getCaseSensitive() {
        return caseSensitive;
    }

    public JCheckBox getTextWrap() {
        return textWrap;
    }

    public void setDownSearch(boolean isDownSearch) {
        downSearch.setSelected(isDownSearch);
    }

    public void setUpSearch(boolean isUpSearch) {
        upSearch.setSelected(isUpSearch);
    }

    public void setCaseSensitive(boolean isCaseSensitive) {
       caseSensitive.setSelected(isCaseSensitive);
    }

    public void setTextWrap(boolean isTextWrap) {
        textWrap.setSelected(isTextWrap);
    }

    public JRadioButton getDownSearch() {
        return downSearch;
    }
}

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Insets;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FontDialog {
    private JList<String> fontList;
    private JList<String> styleList;
    private JList<Integer> sizeList;
    private JTextArea previewText;

    private final Viewer viewer;

    public FontDialog(Viewer viewer) {
        this.viewer = viewer;
    }

    public void showFontDialog() {
        Font currentFont = viewer.getFontTextArea();
        JPanel fontJPanel = new JPanel();
        Font fontUI = new Font("Arial", Font.PLAIN, 14);
        EmptyBorder border = new EmptyBorder(40, 0, 5, 0);

        JLabel fontLabel = new JLabel("Family");
        JTextField currentFontLabel = new JTextField("");
        fontLabel.setFont(viewer.font);
        fontLabel.setBorder(border);
        currentFontLabel.setPreferredSize(new Dimension(240, 30));
        currentFontLabel.setText(currentFont.getName());
        currentFontLabel.setFont(fontUI);
        Font[] osFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
        String[] osFontsName = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        fontList = new JList<>(osFontsName);
        fontList.setFixedCellHeight(24);
        fontList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fontList.setSelectedValue(currentFont.getName(), true);
        fontList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof String) {
                    String font = osFontsName[index];
                    int style = osFonts[index].getStyle();
                    setFont(new Font(font, style, 17));
                }
                return this;
            }
        });
        JScrollPane fontScrollPane = new JScrollPane(fontList);
        fontScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        fontScrollPane.setPreferredSize(new Dimension(240, 150));

        JLabel styleLabel = new JLabel("Style");
        JTextField currentStyleLabel = new JTextField("");
        styleLabel.setFont(viewer.font);
        styleLabel.setBorder(border);
        currentStyleLabel.setPreferredSize(new Dimension(160, 30));
        currentStyleLabel.setFont(fontUI);
        // Create a new DefaultListModel for styleList
        DefaultListModel<String> styleListModel = new DefaultListModel<>();
        String[] currentStyles = getAvailableStyles(currentFont.getFontName());
        for (String style : currentStyles) {
            styleListModel.addElement(style);
        }
        System.out.println(styleListModel);
        styleList = new JList<>(styleListModel);
        styleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        fontList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedFontFamily = fontList.getSelectedValue();
                if (selectedFontFamily != null) {
                    // String selectedFontFamily = selectedFont;
                    String[] availableStyles = getAvailableStyles(selectedFontFamily);

                    // Update styleListModel with available styles for the selected font
                    styleListModel.clear();
                    for (String style : availableStyles) {
                        styleListModel.addElement(style);
                    }
                    currentFontLabel.setText(selectedFontFamily);
                    updatePreviewText();
                }
            }
        });

        styleList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedFont = getFontList();
                int selectedStyleIndex = styleList.getSelectedIndex();
                int selectedSize = getSizeList();
                Font selectedFontFeatures = new Font(selectedFont, selectedStyleIndex, selectedSize);
                setPreviewText(selectedFontFeatures);

                // Update the styleTextField with the selected style
                if (selectedStyleIndex >= 0) {
                    currentStyleLabel.setText(styleList.getModel().getElementAt(selectedStyleIndex));
                }
                updatePreviewText();
            }
        });

        JScrollPane styleScrollPane = new JScrollPane(styleList);
        styleScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        styleScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        styleScrollPane.setPreferredSize(new Dimension(160, 150));

        JLabel sizeLabel = new JLabel("Size");
        JTextField currentSizeLabel = new JTextField("");
        sizeLabel.setFont(viewer.font);
        sizeLabel.setBorder(border);
        currentSizeLabel.setPreferredSize(new Dimension(85, 30));
        currentSizeLabel.setText(Integer.toString(currentFont.getSize()));
        currentSizeLabel.setFont(fontUI);
        Integer[] fontSizeOptions = { 12, 14, 16, 18, 20, 22, 24, 26, 28, 36, 48, 72 };
        sizeList = new JList<>(fontSizeOptions);
        sizeList.setFont(fontUI);
        sizeList.setFixedCellHeight(21);
        sizeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sizeList.setSelectedValue(currentFont.getSize(), true);

        sizeList.addListSelectionListener(e -> {
            currentSizeLabel.setText(Integer.toString(sizeList.getSelectedValue()));
            updatePreviewText();
        });
        JScrollPane sizeScrollPane = new JScrollPane(sizeList);
        sizeScrollPane.setPreferredSize(new Dimension(85, 150));

        previewText = new JTextArea("The sound of ocean waves calms my soul.");
        previewText.setLineWrap(true);
        previewText.setWrapStyleWord(true);
        previewText.setBorder(new EmptyBorder(10, 20, 10, 20));

        Font previewFont = new Font(currentFont.getName(), currentFont.getStyle(), currentFont.getSize());
        previewText.setFont(previewFont);

        JScrollPane scrollPane = new JScrollPane(fontJPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(575, 500));

        fontJPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 0, 10);

        gbc.gridy = 0;
        gbc.gridx = 0;
        fontJPanel.add(fontLabel, gbc);

        gbc.gridx = 1;
        fontJPanel.add(styleLabel, gbc);

        gbc.gridx = 2;
        fontJPanel.add(sizeLabel, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        fontJPanel.add(currentFontLabel, gbc);

        gbc.gridx = 1;
        fontJPanel.add(currentStyleLabel, gbc);

        gbc.gridx = 2;
        fontJPanel.add(currentSizeLabel, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        fontJPanel.add(fontScrollPane, gbc);

        gbc.gridx = 1;
        fontJPanel.add(styleScrollPane, gbc);

        gbc.gridx = 2;
        fontJPanel.add(sizeScrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(50, 10, 120, 10);
        gbc.anchor = GridBagConstraints.SOUTH;
        fontJPanel.add(previewText, gbc);

        int result = JOptionPane.showConfirmDialog(viewer.getFrame(), scrollPane, "Font",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String selectedFont = getFontList();
            int size = getSizeList();
            int selectedStyle = getStyleList();
            ;
            Font selectedFontFeatures = new Font(selectedFont, selectedStyle, size);
            viewer.setFontTextArea(selectedFontFeatures);
        }
    }

    public String getFontList() {
        return fontList.getSelectedValue();
    }

    public int getStyleList() {
        return styleList.getSelectedIndex();
    }

    public int getSizeList() {
        Integer selectedSize = sizeList.getSelectedValue();
        if (selectedSize != null) {
            return selectedSize;
        } else {
            return previewText.getFont().getSize();
        }
    }

    public void setPreviewText(Font font) {
        previewText.setFont(font);
    }

    public String[] getAvailableStyles(String name) {
        List<String> styles = new ArrayList<>();
        GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] fonts = e.getAllFonts();
        for (Font font : fonts) {
            if (font.getFamily().equals(name)) {
                String style = font.getFontName(Locale.US);
                String processedStyle = style.replaceFirst(name, "").trim();
                // if (fontList.getSelectedValue().equals(name)) {
                //     processedStyle = style.replaceFirst(name, "Regular").trim();
                // }
                if (!styles.contains(processedStyle)) {
                    styles.add(processedStyle);
                }
            }
        }
        // List<String> processedStyles = new ArrayList<>();
        // for (String style : styles) {
        // if (!name.equals(style)) {
        // String processedStyle = style.replaceFirst(name, "").trim().toLowerCase();
        // processedStyles.add(processedStyle);
        // }

        // }
        return styles.toArray(new String[0]);
    }

    private void updatePreviewText() {
        String selectedFontName = getFontList();
        int selectedStyleIndex = getStyleList();
        int selectedSize = getSizeList();

        // Determine the selected style, considering the styleList selection
        int selectedStyle = Font.PLAIN;
        if (selectedStyleIndex >= 0) {
            selectedStyle = selectedStyleIndex;
        }
        Font selectedFontFeatures = new Font(selectedFontName, selectedStyle, selectedSize);
        setPreviewText(selectedFontFeatures);
    }
}
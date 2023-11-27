import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class AboutDialog {
    private static JLabel aboutLabel;

    public static void showAboutDialog() {
        aboutLabel = new JLabel("<html><div style='text-align: center; font-size: 18px;'>" +
                "QuickNote — small and lightweight desktop text editor<br>" +
                "<b>COR desktop</b><br>" +

                "Copyright© 2023 – The COR Team<br><br>" +
                "Developed by 'COR TEAM' Chain Of Responsibility.<br>" +
                "For more information and updates, text Ajar " +
                "</div></html>");

        JOptionPane.showMessageDialog(null, aboutLabel, "About Program", JOptionPane.INFORMATION_MESSAGE);
    }
}

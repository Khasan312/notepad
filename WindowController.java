import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class WindowController extends JFrame implements WindowListener {

    private Commands commands;

    public WindowController(Commands commands) {
        this.commands = commands;
    }

    public void windowOpened(WindowEvent var1) {
    };

    public void windowClosing(WindowEvent var1) {
        if (commands.exit()) {
            dispose();
        } else {
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        }
    };
    
    public void windowClosed(WindowEvent var1) {
    };

    public void windowIconified(WindowEvent var1) {
    };

    public void windowDeiconified(WindowEvent var1) {
    };

    public void windowActivated(WindowEvent var1) {
    };

    public void windowDeactivated(WindowEvent var1) {
    };

}
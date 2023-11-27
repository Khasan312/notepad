import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ReplaceWindowController extends JFrame implements WindowListener {

    Viewer viewer;

    public ReplaceWindowController(Viewer viewer) {
        this.viewer = viewer;
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        viewer.setReplacePanelNull();
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}

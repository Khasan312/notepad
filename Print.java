import javax.swing.*;
import java.awt.print.PrinterJob;
import java.awt.print.PrinterException;
import java.awt.print.Printable;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.List;

public class Print implements Printable {

  private String data;
  private int[] pageBreaks;
  private List<String> textLines;
  private Font font;
  private JOptionPane progressDialog = null;

  public Print(String data, Font font) {
    this.font = font;
    this.data = data;
  }

  private void initTextLinesToPage(FontMetrics fontMetrics, int lineLength) {
    if (textLines == null) {
      String[] lines = data.split("\n");
      textLines = new ArrayList<>();
      for (String line : lines) {
        if (fontMetrics.stringWidth(line) <= lineLength) {
          textLines.add(line);
        } else {
          int currentIndex = 0;
          int endIndex = 0;
          while (currentIndex < line.length()) {
            int remainingWidth = lineLength;
            while (endIndex < line.length() && remainingWidth > 0) {
              int charWidth = fontMetrics.charWidth(line.charAt(endIndex));
              remainingWidth -= charWidth;
              endIndex++;
            }
            textLines.add(line.substring(currentIndex, endIndex));
            currentIndex = endIndex;
          }
        }
      }
    }
  }



  public void printDocument() {
    PrinterJob job = PrinterJob.getPrinterJob();
    job.setPrintable(this);
    boolean doPrint = job.printDialog();
    if (doPrint) {
      try {
        job.print();
        JOptionPane.showMessageDialog(new JFrame(), "Document is printed");
      } catch (PrinterException ex) {
        JOptionPane.showMessageDialog(new JFrame(), "Something went wrong, try again. \nError: "+ ex);
      }
      textLines.clear();
    }
  }

  public int print(Graphics g, PageFormat pf, int pageIndex) {
      g.setFont(font);
      FontMetrics metrics = g.getFontMetrics(font);
      int lineHeight = metrics.getHeight();

    if (pageBreaks == null) {
      initTextLinesToPage(metrics, (int) (pf.getImageableWidth()- 100));
      int linesPerPage = ((int)pf.getImageableHeight() - 100) / lineHeight;
      int numBreaks = (textLines.size() - 1) / linesPerPage;
      pageBreaks = new int[numBreaks];
      for (int i = 0; i < numBreaks; i++) {
        pageBreaks[i] = (i + 1) * linesPerPage;
      }
    }
    if (pageIndex > pageBreaks.length) {
      return NO_SUCH_PAGE;
    }
    Graphics2D g2d = (Graphics2D)g;
    g2d.translate(pf.getImageableX(), pf.getImageableY());
    int y = 50;
    int x = 50;
    int start = (pageIndex == 0) ? 0 : pageBreaks[pageIndex-1];
    int end   = (pageIndex == pageBreaks.length)
    ? textLines.size() : pageBreaks[pageIndex];

    for (int line = start; line < end; line++) {
      y += lineHeight;
      g.drawString(textLines.get(line), x, y);
    }

    Font fontColumnar = new Font("Serif", Font.PLAIN, 12);
    g.setFont(fontColumnar);
    String pageNum = "Page # : ";
    g.drawString(pageNum + (pageIndex + 1), (int)pf.getImageableWidth() - metrics.stringWidth(pageNum) - x, ((int)pf.getImageableHeight() - 20));

    // Show a JOptionPane with the page number being printed
    //updateProgress("Printing page " + (pageIndex + 1));

    return PAGE_EXISTS;
  }


  // Function to create or update the progress dialog
  private void updateProgress(String message) {
    if (progressDialog == null) {
      progressDialog = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
    } else {
      progressDialog.setMessage(message);
    }

    JDialog dialog = progressDialog.createDialog("Printing Progress");
    dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    dialog.setVisible(true);
  }
}

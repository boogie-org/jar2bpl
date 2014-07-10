
package terpword;
import java.awt.*;
import javax.swing.*;
import java.awt.print.*;

/**
 * @author Marty Hall
 * * 
 */
public class PrintUtilities implements Printable {
  private Component componentToBePrinted;

  /**
   * prints component sent it 
   * @param c - component to be printed
   */
public static void printComponent(Component c) {
    new PrintUtilities(c).print();
  }
  
  /**
 * @param componentToBePrinted - component to be printed
 */
public PrintUtilities(Component componentToBePrinted) {
    this.componentToBePrinted = componentToBePrinted;
  }
  
  /**
 *  print function
 */
public void print() {
    PrinterJob printJob = PrinterJob.getPrinterJob();
    printJob.setPrintable(this);
    if (printJob.printDialog())
      try {
        printJob.print();
      } catch(PrinterException pe) {
        System.out.println("Error printing: " + pe);
      }
  }

  /* (non-Javadoc)
 * @see java.awt.print.Printable#print(java.awt.Graphics, java.awt.print.PageFormat, int)
 */
public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
    if (pageIndex > 0) {
      return(NO_SUCH_PAGE);
    } else {
      Graphics2D g2d = (Graphics2D)g;
      g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
      disableDoubleBuffering(componentToBePrinted);
      componentToBePrinted.paint(g2d);
      enableDoubleBuffering(componentToBePrinted);
      return(PAGE_EXISTS);
    }
  }

  /**
 * @param c - component to be printed
 */
public static void disableDoubleBuffering(Component c) {
    RepaintManager currentManager = RepaintManager.currentManager(c);
    currentManager.setDoubleBufferingEnabled(false);
  }

  /**
 * @param c - component to be printed
 */
public static void enableDoubleBuffering(Component c) {
    RepaintManager currentManager = RepaintManager.currentManager(c);
    currentManager.setDoubleBufferingEnabled(true);
  }
}
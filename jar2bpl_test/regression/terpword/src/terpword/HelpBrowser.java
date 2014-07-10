/*
 * This extremely simple browser has a text field for typing in
 * new urls, a JEditorPane to display the HTML page, and a status
 * bar to display the contents of hyperlinks the mouse passes over.
 */
package terpword;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class HelpBrowser extends JFrame {

  /**
   * Constructor
   * @param startingUrl - url path of file to be browsed
   */
public HelpBrowser(String startingUrl) {
    // Ok, first just get a screen up and visible, with an appropriate
    // handler in place for the kill window command
 	super("TerpWord Help");
    setSize(550,650);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
	we.getWindow().setVisible(false);
      }
    });

    // Now set up our basic screen components, the editor pane, the
    // text field for URLs, and the label for status and link information
    JPanel urlPanel = new JPanel();
    urlPanel.setLayout(new BorderLayout());
    JTextField urlField = new JTextField(startingUrl);
    final JLabel statusBar = new JLabel(" ");

    // Here's the editor pane configuration.  It's important to make
    // the "setEditable(false)" call, otherwise our hyperlinks won't
    // work.  (If the text is editable, then clicking on a hyperlink
    // simply means that you want to change the text...not follow the
    // link.)
    final JEditorPane jep = new JEditorPane();
    jep.setEditable(false);

    // Here's where we force the pane to use our new editor kit
    jep.setEditorKitForContentType("text/html", new PatchedHTMLEditorKit());
    try {
      jep.setPage(startingUrl);
    }
    catch(Exception e) {
      statusBar.setText("Could not open starting page.  Using a blank.");
    }
    JScrollPane jsp = new JScrollPane(jep);

    // and get the GUI components onto our content pane
    getContentPane().add(jsp, BorderLayout.CENTER);
    getContentPane().add(urlPanel, BorderLayout.NORTH);
    getContentPane().add(statusBar, BorderLayout.SOUTH);

    // and last but not least, hook up our event handlers
    urlField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
	try {
	  jep.setPage(ae.getActionCommand());
	}
	catch(Exception e) {
	  statusBar.setText("Could not open starting page.  Using a blank.");
	}
      }
    });
    jep.addHyperlinkListener(new SimpleLinkListener(jep, urlField, statusBar));
    setVisible(true);
  }
}


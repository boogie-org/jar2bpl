package terpword;
/*
 * SimpleLinkListener.java
 * A hyperlink listener for use with JEditorPane.  This
 * listener will change the cursor over hotspots based on enter/exit
 * events and also load a new page when a valid hyperlink is clicked.
 */

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

public class SimpleLinkListener implements HyperlinkListener {

  private JEditorPane pane;       // The pane we're using to display HTML

  private JTextField  urlField;   // An optional textfield for showing
                                  // the current URL being displayed

  private JLabel statusBar;       // An option label for showing where
                                  // a link would take you

  /**
   * Constructor
   * 
   * @param jep - the editor pane
   * @param jtf - the textfield
   * @param jl - the label
   */
public SimpleLinkListener(JEditorPane jep, JTextField jtf, JLabel jl) {
    pane = jep;
    urlField = jtf;
    statusBar = jl;
  }

  /**
   * Constructor 
   * 
   * @param jep - the editor pane
   */
public SimpleLinkListener(JEditorPane jep) {
    this(jep, null, null);
  }

  /* (non-Javadoc)
 * @see javax.swing.event.HyperlinkListener#hyperlinkUpdate(javax.swing.event.HyperlinkEvent)
 */
public void hyperlinkUpdate(HyperlinkEvent he) {
    // We'll keep some basic debuggin information in here so you can
    // verify our new editor kit is working.
    //System.out.print("Hyperlink event started...");

    HyperlinkEvent.EventType type = he.getEventType();
    // Ok.  Decide which event we got...
    if (type == HyperlinkEvent.EventType.ENTERED) {
      // Enter event.  Go the the "hand" cursor and fill in the status bar
      //System.out.println("entered");
      pane.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      statusBar.setText(he.getURL().toString());
    }
    else if (type == HyperlinkEvent.EventType.EXITED) {
      // Exit event.  Go back to the default cursor and clear the status bar
      //System.out.println("exited");
      pane.setCursor(Cursor.getDefaultCursor());
      statusBar.setText(" ");
    }
    else {
      // Jump event.  Get the url, and if it's not null, switch to that
      // page in the main editor pane and update the "site url" label.
      //System.out.println("activated");
      try {
	pane.setPage(he.getURL());
	if (urlField != null) {
	  urlField.setText(he.getURL().toString());
	}
      }
      catch (Exception e) {
	e.printStackTrace();
      }
    }
  }
}




import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.Stack;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.StyleSheet;
import javax.swing.undo.UndoManager;


public class FalseNegatives01 {
	

	protected UndoManager undoMngr;
	


	/* Constants */
	// Menu Keys
	public static final String KEY_MENU_FILE = "file";
	public static final String KEY_MENU_EDIT = "edit";
	public static final String KEY_MENU_VIEW = "view";
	public static final String KEY_MENU_FONT = "font";
	public static final String KEY_MENU_FORMAT = "format";
	public static final String KEY_MENU_INSERT = "insert";
	public static final String KEY_MENU_TABLE = "table";
	public static final String KEY_MENU_SEARCH = "search";
	public static final String KEY_MENU_HELP = "help";

	// Tool Keys
	public static final String KEY_TOOL_SEP = "separator";
	public static final String KEY_TOOL_NEW = "new";
	public static final String KEY_TOOL_OPEN = "open";
	public static final String KEY_TOOL_SAVE = "save";
	public static final String KEY_TOOL_CUT = "cut";
	public static final String KEY_TOOL_COPY = "copy";
	public static final String KEY_TOOL_PASTE = "paste";
	public static final String KEY_TOOL_UNDO = "undo";
	public static final String KEY_TOOL_REDO = "redo";
	public static final String KEY_TOOL_BOLD = "bold";
	public static final String KEY_TOOL_ITALIC = "italic";
	public static final String KEY_TOOL_UNDERLINE = "underline";
	public static final String KEY_TOOL_STRIKE = "strike";
	public static final String KEY_TOOL_SUPER = "superscript";
	public static final String KEY_TOOL_SUB = "subscript";
	public static final String KEY_TOOL_ULIST = "ulist";
	public static final String KEY_TOOL_OLIST = "olist";
	public static final String KEY_TOOL_ALIGNL = "alignleft";
	public static final String KEY_TOOL_ALIGNC = "aligncenter";
	public static final String KEY_TOOL_ALIGNR = "alignright";
	public static final String KEY_TOOL_ALIGNJ = "alignjustified";
	public static final String KEY_TOOL_UNICODE = "unicodesymbol";
	public static final String KEY_TOOL_UNIMATH = "unicodesymbolmath";
	public static final String KEY_TOOL_FIND = "searchfind";
	public static final String KEY_TOOL_ANCHOR = "anchor";
	public static final String KEY_TOOL_SOURCE = "viewsource";
	public static final String KEY_TOOL_SPLITPANEL = "splitpanel";
	public static final String KEY_TOOL_STYLES = "styleselect";
	public static final String KEY_TOOL_FONTS = "fontselect";
	public static final String KEY_TOOL_FONTSIZE = "fontsize";
	public static final int TOOLBAR_MAIN = 0;
	public static final int TOOLBAR_FORMAT = 1;
	public static final int TOOLBAR_STYLES = 2;


	public void keyTyped(KeyEvent ke) {
		Element elem;
		String selectedText;
		int pos = this.getCaretPosition();
		int repos = -1;
		if (ke.getKeyChar() == KeyEvent.VK_BACK_SPACE) {

		} else if (ke.getKeyChar() == KeyEvent.VK_ENTER) {
			try {
				if (checkParentsTag(HTML.Tag.UL) == true
						| checkParentsTag(HTML.Tag.OL) == true) {
					elem = getListItemParent();
					int so = elem.getStartOffset();
					int eo = elem.getEndOffset();
					char[] temp = this.getTextPane().getText(so, eo - so)
							.toCharArray();
					boolean content = false;
					for (int i = 0; i < temp.length; i++) {
						if (!(new Character(temp[i])).isWhitespace(temp[i])) {
							content = true;
						}
					}
					if (content) {
						int end = -1;
						int j = temp.length;
						do {
							j--;
							if (new Character(temp[j]).isLetterOrDigit(temp[j])) {
								end = j;
							}
						} while (end == -1 && j >= 0);
						j = end;
						do {
							j++;
							if (!new Character(temp[j]).isSpaceChar(temp[j])) {
								repos = j - end - 1;
							}
						} while (repos == -1 && j < temp.length);
						if (repos == -1) {
							repos = 0;
						}
					}
					if (elem.getStartOffset() == elem.getEndOffset()
							|| !content) {
//						manageListElement(elem);
					} else {
						if (this.getCaretPosition() + 1 == elem.getEndOffset()) {
//							insertListStyle(elem);
							this.setCaretPosition(pos - repos);
						} else {
							int caret = this.getCaretPosition();
							String tempString = this.getTextPane().getText(
									caret, eo - caret);
							this.getTextPane().select(caret, eo - 1);
							this.getTextPane().replaceSelection("");
//							insertListElement(tempString);
//							Element newLi = getListItemParent();
//							this.setCaretPosition(newLi.getEndOffset() - 1);
						}
					}
				}
			} catch (BadLocationException ble) {
//				logException("BadLocationException in keyTyped method", ble);
//				SimpleInfoDialog sidAbout = new SimpleInfoDialog(this
//						.getFrame(), "Error", true,
//						"Bad Location Exception occurred.",
//						SimpleInfoDialog.ERROR);
			}
		}
	}

	private Element getListItemParent() {
		// TODO Auto-generated method stub
		return null;
	}

	private boolean checkParentsTag(Tag ul) {
		// TODO Auto-generated method stub
		return false;
	}

	private void setCaretPosition(int i) {
		// TODO Auto-generated method stub
		
	}

	private int getCaretPosition() {
		// TODO Auto-generated method stub
		return 0;
	}

	private JTextComponent getTextPane() {
		// TODO Auto-generated method stub
		return null;
	}

}

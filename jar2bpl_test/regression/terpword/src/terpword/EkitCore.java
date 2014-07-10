/*
 GNU Lesser General Public License

 EkitCore - Base Java Swing HTML Editor & Viewer Class (Basic Version)
 Copyright (C) 2000 Howard Kistler

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package terpword;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.ChangedCharSetException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import javax.swing.text.rtf.RTFEditorKit;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

/**
 * EkitCore Main application class for editing and saving HTML in a Java text
 * component
 * 
 * @author Howard Kistler, TerpWord Team
 * 
 * @version 4.0 REQUIREMENTS Java 2 (JDK 1.3 or 1.4) Swing Library
 */
public class EkitCore extends JPanel implements ActionListener, KeyListener,
		FocusListener, DocumentListener{
	/* Components */
	private JSplitPane jspltDisplay;
	private JTextPane jtpMain;
	private ExtendedHTMLEditorKit htmlKit;
	private ExtendedHTMLDocument htmlDoc;
	private StyleSheet styleSheet;
	private JTextPane jtpSource;
	private JScrollPane jspSource;
	private JToolBar jToolBar;
	private JToolBar jToolBarMain;
	private JToolBar jToolBarFormat;
	private JToolBar jToolBarStyles;
	private JButtonNoFocus jbtnNewHTML;
	private JButtonNoFocus jbtnOpenHTML;
	private JButtonNoFocus jbtnSaveHTML;
	private JButtonNoFocus jbtnCut;
	private JButtonNoFocus jbtnCopy;
	private JButtonNoFocus jbtnPaste;
	private JButtonNoFocus jbtnUndo;
	private JButtonNoFocus jbtnRedo;
	private JToggleButtonNoFocus jbtnBold;
	private JToggleButtonNoFocus jbtnItalic;
	private JToggleButtonNoFocus jbtnUnderline;
	private JButtonNoFocus jbtnStrike;
	private JButtonNoFocus jbtnSuperscript;
	private JButtonNoFocus jbtnSubscript;
	private JButtonNoFocus jbtnUList;
	private JButtonNoFocus jbtnOList;
	private JButtonNoFocus jbtnAlignLeft;
	private JButtonNoFocus jbtnAlignCenter;
	private JButtonNoFocus jbtnAlignRight;
	private JButtonNoFocus jbtnAlignJustified;
	private JButtonNoFocus jbtnFind;
	private JButtonNoFocus jbtnUnicode;
	private JButtonNoFocus jbtnUnicodeMath;
	private JButtonNoFocus jbtnAnchor;
	private JToggleButtonNoFocus jtbtnViewSource;
	private JToggleButtonNoFocus jtbtnSplitPanel;
	private JComboBoxNoFocus jcmbFontSelector;	
	private JComboBoxNoFocus jcmbFontSize;
	private String doc_path; //location of document when saved
	private String doc_name; //name of the document when saved
	private Stack history_stack = new Stack();
	private Frame frameHandler;
	private HTMLUtilities htmlUtilities = new HTMLUtilities(this);

	/* Actions */
	private StyledEditorKit.BoldAction actionFontBold;
	private StyledEditorKit.ItalicAction actionFontItalic;
	private StyledEditorKit.UnderlineAction actionFontUnderline;
	private FormatAction actionFontStrike;	
	private FormatAction actionFontSuperscript;
	private FormatAction actionFontSubscript;
	private ListAutomationAction actionListUnordered;
	private ListAutomationAction actionListOrdered;
	private SetFontFamilyAction actionSelectFont;
	private StyledEditorKit.AlignmentAction actionAlignLeft;
	private StyledEditorKit.AlignmentAction actionAlignCenter;
	private StyledEditorKit.AlignmentAction actionAlignRight;
	private StyledEditorKit.AlignmentAction actionAlignJustified;
	private CustomAction actionInsertAnchor;
	protected UndoManager undoMngr;
	protected UndoAction undoAction;
	protected RedoAction redoAction;

	/* Menus */
	private JMenuBar jMenuBar;
	private JMenu jMenuFile;
	private JMenu jMenuEdit;
	private JMenu jMenuView;
	private JMenu jMenuFont;
	private JMenu jMenuFormat;
	private JMenu jMenuInsert;
	private JMenu jMenuTable;
	private JMenu jMenuSearch;
	private JMenu jMenuTools;
	private JMenu jMenuHelp;
	private JMenu jMenuDebug;
	private JMenuItem hist1, hist2, hist3, hist4, hist5;
	private JMenu jMenuToolbars;
	private JCheckBoxMenuItem jcbmiViewToolbar;
	private JCheckBoxMenuItem jcbmiViewToolbarMain;
	private JCheckBoxMenuItem jcbmiViewToolbarFormat;
	private JCheckBoxMenuItem jcbmiViewToolbarStyles;
	private JCheckBoxMenuItem jcbmiViewSource;
	private JCheckBoxMenuItem jcbmiSplitPanel;

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

	// Menu & Tool Key Arrays
	private static Hashtable htMenus = new Hashtable();
	private static Hashtable htTools = new Hashtable();
	private final String appName = "TerpWord";
	private final String menuDialog = "..."; /*
											  * text to append to a MenuItem
											  * label when menu item opens a
											  * dialog
											  */

	private final boolean useFormIndicator = true; /*
												    * Creates a highlighted
												    * background on a new FORM
												    * so that it may be more
												    * easily edited
												    */

	private final String clrFormIndicator = "#cccccc";

	// System Clipboard Settings
	private java.awt.datatransfer.Clipboard sysClipboard;
	private SecurityManager secManager;

	/* Variables */
	private int iSplitPos = 0;
	private int timesNewRoman = 0;
	private boolean exclusiveEdit = true;
	private String lastSearchFindTerm = null;
	private String lastSearchReplaceTerm = null;
	private boolean lastSearchCaseSetting = false;
	private boolean lastSearchTopSetting = false;
	private File currentFile = null;
	private String imageChooserStartDir = ".";
	private int indent = 0;
	private final int indentStep = 4;
	private boolean isSplit = false;
	private boolean isSource = false;
	private JScrollPane jspViewport;

	// File extensions for MutableFilter
	private final String[] extsHTML = { "html", "htm", "shtml" };
	private final String[] extsCSS = { "css" };
	private final String[] extsIMG = { "gif", "jpg", "jpeg", "png" };
	private final String[] extsRTF = { "rtf" };
	private final String[] extsSer = { "ser" };
	private final String[] extsTXT = { "txt" };

	/* Servlet Settings */
	private String ServletURL = null;
	private String TreePilotSystemID = "";
	private String ImageDir = "";
	private static ResourceBundle TreePilotProperties;

	/**
	 * Master Constructor
	 * 
	 * @param sDocument -
	 *            [String] A text or HTML document to load in the editor upon
	 *            startup.
	 * @param sStyleSheet -
	 *            [String] A CSS stylesheet to load in the editor upon startup.
	 * @param sRawDocument -
	 *            [String] A document encoded as a String to load in the editor
	 *            upon startup.
	 * @param sdocSource -
	 *            [StyledDocument] Optional document specification, using
	 *            javax.swing.text.StyledDocument.
	 * @param urlStyleSheet -
	 *            [URL] A URL reference to the CSS style sheet.
	 * @param includeToolBar -
	 *            [boolean] Specifies whether the app should include the
	 *            toolbar(s).
	 * @param showViewSource -
	 *            [boolean] Specifies whether or not to show the View Source
	 *            window on startup.
	 * @param showMenuIcons -
	 *            [boolean] Specifies whether or not to show icon pictures in
	 *            menus.
	 * @param editModeExclusive -
	 *            [boolean] Specifies whether or not to use exclusive edit mode
	 *            (recommended on).
	 * @param sLanguage -
	 *            [String] The language portion of the Internationalization
	 *            Locale to run Ekit in.
	 * @param sCountry -
	 *            [String] The country portion of the Internationalization
	 *            Locale to run Ekit in.
	 * @param base64 -
	 *            [boolean] Specifies whether the raw document is Base64 encoded
	 *            or not.
	 * @param debugMode -
	 *            [boolean] Specifies whether to show the Debug menu or not.
	 * @param hasSpellChecker -
	 *            [boolean] Specifies whether or not this uses the SpellChecker
	 *            module
	 * @param multiBar -
	 *            [boolean] Specifies whether to use multiple toolbars or one
	 *            big toolbar.
	 */
	public EkitCore(String sDocument, String sStyleSheet, String sRawDocument,
			StyledDocument sdocSource, URL urlStyleSheet,
			boolean includeToolBar, boolean showViewSource,
			boolean showMenuIcons, boolean editModeExclusive, String sLanguage,
			String sCountry, boolean base64, boolean debugMode,
			boolean hasSpellChecker, boolean multiBar) {
		super();

		exclusiveEdit = editModeExclusive;

		frameHandler = new Frame();

		//initialize history_stack
		for (int i = 0; i < 5; i++) {
			Vector vt = new Vector();
			vt.add(new String("-"));
			vt.add(new String("-"));
			history_stack.push(vt);

		}
		// Determine if system clipboard is available
		secManager = System.getSecurityManager();
		if (secManager != null) {
			try {
				secManager.checkSystemClipboardAccess();
				sysClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			} catch (SecurityException se) {
				sysClipboard = null;
			}
		}

		/* Load TreePilot properties */
		try {
			TreePilotProperties = ResourceBundle.getBundle("source.terpword.TreePilot");
		} catch (MissingResourceException mre) {
			logException(
					"MissingResourceException while loading treepilot file",mre);
		}

		
		/* Create the editor kit, document, and stylesheet */
		jtpMain = new JTextPane();
		htmlKit = new ExtendedHTMLEditorKit();
		htmlDoc = (ExtendedHTMLDocument) (htmlKit.createDefaultDocument());
		styleSheet = htmlDoc.getStyleSheet();
		htmlKit.setDefaultCursor(new Cursor(Cursor.TEXT_CURSOR));
		jtpMain.setCursor(new Cursor(Cursor.TEXT_CURSOR));

		/* Set up the text pane */
		jtpMain.setEditorKit(htmlKit);
		jtpMain.setDocument(htmlDoc);
		jtpMain.setMargin(new Insets(4, 4, 4, 4));
		jtpMain.addKeyListener(this);
		jtpMain.addFocusListener(this);
		jtpMain.setDragEnabled(true);

		/* Create the source text area */
		if (sdocSource == null) {
			jtpSource = new JTextPane();
			jtpSource.setText(jtpMain.getText());
		} else {
			jtpSource = new JTextPane(sdocSource);
			jtpMain.setText(jtpSource.getText());
		}
		jtpSource.setBackground(new Color(212, 212, 212));
		jtpSource.setSelectionColor(new Color(255, 192, 192));
		jtpSource.setMargin(new Insets(4, 4, 4, 4));
		jtpSource.getDocument().addDocumentListener(this);
		jtpSource.addFocusListener(this);
		jtpSource.setCursor(new Cursor(Cursor.TEXT_CURSOR));

		/* Add CaretListener for tracking caret location events */
		jtpMain.addCaretListener(new CaretListener() {
			/**
			 * @see javax.swing.event.CaretListener#caretUpdate(javax.swing.event.CaretEvent)
			 */
			public void caretUpdate(CaretEvent ce) {
				handleCaretPositionChange(ce);

			}
		});
		jtpSource.addCaretListener(new CaretListener() {
			/**
			 * @see javax.swing.event.CaretListener#caretUpdate(javax.swing.event.CaretEvent)
			 */
			public void caretUpdate(CaretEvent ce) {
				handleSourceCaretPositionChange(ce);

			}
		});
		
		
		/* Set up the undo features */
		undoMngr = new UndoManager();
		undoAction = new UndoAction();
		redoAction = new RedoAction();
		jtpMain.getDocument().addUndoableEditListener(
				new CustomUndoableEditListener());

		/* Insert raw document, if exists */
		if (sRawDocument != null && sRawDocument.length() > 0) {
			jtpMain.setText(sRawDocument);
		}
		jtpMain.setCaretPosition(0);
		jtpMain.getDocument().addDocumentListener(this);

		/* Import CSS from reference, if exists */
		if (urlStyleSheet != null) {
			try {
				String currDocText = jtpMain.getText();
				htmlDoc = (ExtendedHTMLDocument) (htmlKit
						.createDefaultDocument());
				styleSheet = htmlDoc.getStyleSheet();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						urlStyleSheet.openStream()));
				styleSheet.loadRules(br, urlStyleSheet);
				br.close();
				htmlDoc = new ExtendedHTMLDocument(styleSheet);
				registerDocument(htmlDoc);
				jtpMain.setText(currDocText);
				jtpSource.setText(jtpMain.getText());
			} catch (Exception e) {
				e.printStackTrace(System.out);
			}
		}

		/* Preload the specified HTML document, if exists */
		if (sDocument != null) {
			File defHTML = new File(sDocument);
			if (defHTML.exists()) {
				try {
					openDocument(defHTML);
				} catch (Exception e) {
					logException("Exception in preloading HTML document", e);
				}
			}
		}

		/* Collect the actions that the JTextPane is naturally aware of */
		Hashtable actions = new Hashtable();
		Action[] actionsArray = jtpMain.getActions();
		for (int i = 0; i < actionsArray.length; i++) {
			Action a = actionsArray[i];
			actions.put(a.getValue(Action.NAME), a);
		}

		/* Create shared actions */
		actionFontBold = new StyledEditorKit.BoldAction();
		actionFontItalic = new StyledEditorKit.ItalicAction();
		actionFontUnderline = new StyledEditorKit.UnderlineAction();
		actionFontStrike = new FormatAction(this, "Strike-through", HTML.Tag.STRIKE);
		actionFontSuperscript = new FormatAction(this, "Superscript",HTML.Tag.SUP);
		actionFontSubscript = new FormatAction(this, "Subscript", HTML.Tag.SUB);
		actionListUnordered = new ListAutomationAction(this, "Unordered List",HTML.Tag.UL);
		actionListOrdered = new ListAutomationAction(this, "Ordered List",HTML.Tag.OL);
		actionSelectFont = new SetFontFamilyAction(this, "[MENUFONTSELECTOR]");
		actionAlignLeft = new StyledEditorKit.AlignmentAction("Align Left",StyleConstants.ALIGN_LEFT);
		actionAlignCenter = new StyledEditorKit.AlignmentAction("Align Center",StyleConstants.ALIGN_CENTER);
		actionAlignRight = new StyledEditorKit.AlignmentAction("Align Right",StyleConstants.ALIGN_RIGHT);
		actionAlignJustified = new StyledEditorKit.AlignmentAction("Align Justified", StyleConstants.ALIGN_JUSTIFIED);
		actionInsertAnchor = new CustomAction(this, "Insert Anchor"+ menuDialog, HTML.Tag.A);
		
		/* Build the menus */
		/* FILE Menu */
		jMenuFile = new JMenu("File");
		htMenus.put(KEY_MENU_FILE, jMenuFile);
		JMenuItem jmiNew = new JMenuItem("New Document"); jmiNew.setActionCommand("newdoc"); jmiNew.addActionListener(this); jmiNew.setAccelerator(KeyStroke.getKeyStroke('N',java.awt.Event.CTRL_MASK, false)); if (showMenuIcons) {jmiNew.setIcon(getEkitIcon("New"));}; jMenuFile.add(jmiNew);
		JMenuItem jmiOpenHTML = new JMenuItem("Open Document" + menuDialog); jmiOpenHTML.setActionCommand("openhtml"); jmiOpenHTML.addActionListener(this); jmiOpenHTML.setAccelerator(KeyStroke.getKeyStroke('O',java.awt.Event.CTRL_MASK, false)); if (showMenuIcons) {jmiOpenHTML.setIcon(getEkitIcon("Open"));}; jMenuFile.add(jmiOpenHTML);
		jMenuFile.addSeparator();
		JMenuItem jmiPrint = new JMenuItem("Print..."); jmiPrint.setActionCommand("print"); jmiPrint.setAccelerator(KeyStroke.getKeyStroke('P',java.awt.Event.CTRL_MASK, false)); jmiPrint.addActionListener(this); jMenuFile.add(jmiPrint);
		jMenuFile.addSeparator();
		JMenu jMenuRecentlyOpened = new JMenu("Recently Opened"); history_stack = readState();
		Vector tmp = new Vector();
		tmp = (Vector) history_stack.elementAt(history_stack.size() - 1);
		hist1 = new JMenuItem((String) tmp.get(0));
		hist1.setActionCommand("openfile1");
		hist1.addActionListener(this);
		jMenuRecentlyOpened.add(hist1);
		tmp = (Vector) history_stack.elementAt(history_stack.size() - 2);
		hist2 = new JMenuItem((String) tmp.get(0));
		hist2.setActionCommand("openfile2");
		hist2.addActionListener(this);
		jMenuRecentlyOpened.add(hist2);
		tmp = (Vector) history_stack.elementAt(history_stack.size() - 3);
		hist3 = new JMenuItem((String) tmp.get(0));
		hist3.setActionCommand("openfile3");
		hist3.addActionListener(this);
		jMenuRecentlyOpened.add(hist3);
		tmp = (Vector) history_stack.elementAt(history_stack.size() - 4);
		hist4 = new JMenuItem((String) tmp.get(0));
		hist4.setActionCommand("openfile4");
		hist4.addActionListener(this);
		jMenuRecentlyOpened.add(hist4);
		tmp = (Vector) history_stack.elementAt(history_stack.size() - 5);
		hist5 = new JMenuItem((String) tmp.get(0));
		hist5.setActionCommand("openfile5");
		hist5.addActionListener(this);
		jMenuRecentlyOpened.add(hist5);
		jMenuFile.add(jMenuRecentlyOpened);
		jMenuFile.addSeparator();
		JMenuItem jmiSave = new JMenuItem("Save");
		jmiSave.setActionCommand("save");
		jmiSave.addActionListener(this);
		jmiSave.setAccelerator(KeyStroke.getKeyStroke('S',
				java.awt.Event.CTRL_MASK, false));
		if (showMenuIcons) {
			jmiSave.setIcon(getEkitIcon("Save"));
		}
		;
		jMenuFile.add(jmiSave);
		JMenuItem jmiSaveAs = new JMenuItem("Save As" + menuDialog);
		jmiSaveAs.setActionCommand("saveas");
		jmiSaveAs.addActionListener(this);
		jMenuFile.add(jmiSaveAs);
		JMenuItem jmiSaveRTF = new JMenuItem("Save RTF" + menuDialog);
		jmiSaveRTF.setActionCommand("savertf");
		jmiSaveRTF.addActionListener(this);
		jMenuFile.add(jmiSaveRTF);
		jMenuFile.addSeparator();
		JMenuItem jmiExit = new JMenuItem("Exit");
		jmiExit.setActionCommand("exit");
		jmiExit.addActionListener(this);
		jMenuFile.add(jmiExit);

		/* EDIT Menu */
		jMenuEdit = new JMenu("Edit");
		htMenus.put(KEY_MENU_EDIT, jMenuEdit);
		JMenuItem jmiCut = new JMenuItem("Cut");
		jmiCut.setActionCommand("textcut");
		jmiCut.addActionListener(this);
		jmiCut.setAccelerator(KeyStroke.getKeyStroke('X',
				java.awt.Event.CTRL_MASK, false));
		if (showMenuIcons) {
			jmiCut.setIcon(getEkitIcon("Cut"));
		}
		jMenuEdit.add(jmiCut);
		JMenuItem jmiCopy = new JMenuItem("Copy");
		jmiCopy.setActionCommand("textcopy");
		jmiCopy.addActionListener(this);
		jmiCopy.setAccelerator(KeyStroke.getKeyStroke('C',
				java.awt.Event.CTRL_MASK, false));
		if (showMenuIcons) {
			jmiCopy.setIcon(getEkitIcon("Copy"));
		}
		jMenuEdit.add(jmiCopy);
		JMenuItem jmiPaste = new JMenuItem("Paste");
		jmiPaste.setActionCommand("textpaste");
		jmiPaste.addActionListener(this);
		jmiPaste.setAccelerator(KeyStroke.getKeyStroke('V',
				java.awt.Event.CTRL_MASK, false));
		if (showMenuIcons) {
			jmiPaste.setIcon(getEkitIcon("Paste"));
		}
		jMenuEdit.add(jmiPaste);
		jMenuEdit.addSeparator();
		JMenuItem jmiUndo = new JMenuItem(undoAction);
		jmiUndo.setAccelerator(KeyStroke.getKeyStroke('Z',
				java.awt.Event.CTRL_MASK, false));
		if (showMenuIcons) {
			jmiUndo.setIcon(getEkitIcon("Undo"));
		}
		jMenuEdit.add(jmiUndo);
		JMenuItem jmiRedo = new JMenuItem(redoAction);
		jmiRedo.setAccelerator(KeyStroke.getKeyStroke('Y',
				java.awt.Event.CTRL_MASK, false));
		if (showMenuIcons) {
			jmiRedo.setIcon(getEkitIcon("Redo"));
		}
		jMenuEdit.add(jmiRedo);
		jMenuEdit.addSeparator();
		JMenuItem jmiSelAll = new JMenuItem((Action) actions
				.get(DefaultEditorKit.selectAllAction));
		jmiSelAll.setText("Select All");
		jmiSelAll.setAccelerator(KeyStroke.getKeyStroke('A',
				java.awt.Event.CTRL_MASK, false));
		jMenuEdit.add(jmiSelAll);
		JMenuItem jmiSelLine = new JMenuItem((Action) actions
				.get(DefaultEditorKit.selectLineAction));
		jmiSelLine.setText("Select Line");
		jMenuEdit.add(jmiSelLine);
		JMenuItem jmiSelWord = new JMenuItem((Action) actions
				.get(DefaultEditorKit.selectWordAction));
		jmiSelWord.setText("Select Word");
		jMenuEdit.add(jmiSelWord);
		JMenuItem jmiWordCount = new JMenuItem("Word Count");
		jmiWordCount.setActionCommand("wordcount");
		jmiWordCount.addActionListener(this);
		jMenuEdit.add(jmiWordCount);

		/* VIEW Menu */
		jMenuView = new JMenu("View");
		htMenus.put(KEY_MENU_VIEW, jMenuView);
		if (includeToolBar) {
			jMenuToolbars = new JMenu("Toolbar");

			jcbmiViewToolbarMain = new JCheckBoxMenuItem("Main", false);
			jcbmiViewToolbarMain.setActionCommand("toggletoolbarmain");
			jcbmiViewToolbarMain.addActionListener(this);
			jMenuToolbars.add(jcbmiViewToolbarMain);

			jcbmiViewToolbarStyles = new JCheckBoxMenuItem("Style", false);
			jcbmiViewToolbarStyles.setActionCommand("toggletoolbarstyles");
			jcbmiViewToolbarStyles.addActionListener(this);
			jMenuToolbars.add(jcbmiViewToolbarStyles);

			jMenuView.add(jMenuToolbars);
		}
		jcbmiViewSource = new JCheckBoxMenuItem("View Source", false);
		jcbmiViewSource.setActionCommand("viewsource");
		jcbmiViewSource.addActionListener(this);
		jMenuView.add(jcbmiViewSource);
		jcbmiSplitPanel = new JCheckBoxMenuItem("Split Panel", false);
		jcbmiSplitPanel.setActionCommand("splitpanel");
		jcbmiSplitPanel.addActionListener(this);
		jMenuView.add(jcbmiSplitPanel);

		/* FONT Menu */
		jMenuFont = new JMenu("Font");
		htMenus.put(KEY_MENU_FONT, jMenuFont);
		JMenuItem jmiBold = new JMenuItem(actionFontBold);
		jmiBold.setText("Bold");
		jmiBold.setAccelerator(KeyStroke.getKeyStroke('B',
				java.awt.Event.CTRL_MASK, false));
		if (showMenuIcons) {
			jmiBold.setIcon(getEkitIcon("Bold"));
		}
		jMenuFont.add(jmiBold);
		JMenuItem jmiItalic = new JMenuItem(actionFontItalic);
		jmiItalic.setText("Italic");
		jmiItalic.setAccelerator(KeyStroke.getKeyStroke('I',
				java.awt.Event.CTRL_MASK, false));
		if (showMenuIcons) {
			jmiItalic.setIcon(getEkitIcon("Italic"));
		}
		jMenuFont.add(jmiItalic);
		JMenuItem jmiUnderline = new JMenuItem(actionFontUnderline);
		jmiUnderline.setText("Underline");
		jmiUnderline.setAccelerator(KeyStroke.getKeyStroke('U',
				java.awt.Event.CTRL_MASK, false));
		if (showMenuIcons) {
			jmiUnderline.setIcon(getEkitIcon("Underline"));
		}
		jMenuFont.add(jmiUnderline);
		JMenuItem jmiStrike = new JMenuItem(actionFontStrike);
		jmiStrike.setText("Strike");
		if (showMenuIcons) {
			jmiStrike.setIcon(getEkitIcon("Strike"));
		}
		jMenuFont.add(jmiStrike);
		JMenuItem jmiSupscript = new JMenuItem(actionFontSuperscript);
		if (showMenuIcons) {
			jmiSupscript.setIcon(getEkitIcon("Super"));
		}
		jMenuFont.add(jmiSupscript);
		JMenuItem jmiSubscript = new JMenuItem(actionFontSubscript);
		if (showMenuIcons) {
			jmiSubscript.setIcon(getEkitIcon("Sub"));
		}
		jMenuFont.add(jmiSubscript);
		jMenuFont.addSeparator();
		JMenu jMenuFontSize = new JMenu("Font Size");
		jMenuFontSize.add(new JMenuItem(new StyledEditorKit.FontSizeAction(
				"10", 10)));
		jMenuFontSize.add(new JMenuItem(new StyledEditorKit.FontSizeAction(
				"12", 12)));
		jMenuFontSize.add(new JMenuItem(new StyledEditorKit.FontSizeAction(
				"14", 14)));
		jMenuFontSize.add(new JMenuItem(new StyledEditorKit.FontSizeAction(
				"16", 16)));
		jMenuFontSize.add(new JMenuItem(new StyledEditorKit.FontSizeAction(
				"18", 18)));
		jMenuFontSize.add(new JMenuItem(new StyledEditorKit.FontSizeAction(
				"24", 24)));
		jMenuFontSize.add(new JMenuItem(new StyledEditorKit.FontSizeAction(
				"36", 32)));
		jMenuFontSize.add(new JMenuItem(new StyledEditorKit.FontSizeAction(
				"48", 48)));
		jMenuFont.add(jMenuFontSize);
		jMenuFont.addSeparator();
		JMenu jMenuFontSub = new JMenu("Font");
		JMenuItem jmiSelectFont = new JMenuItem(actionSelectFont);
		jmiSelectFont.setText("Select Font" + menuDialog);
		if (showMenuIcons) {
			jmiSelectFont.setIcon(getEkitIcon("FontFaces"));
		}
		jMenuFontSub.add(jmiSelectFont);
		JMenuItem jmiSerif = new JMenuItem((Action) actions
				.get("font-family-Serif"));
		jmiSerif.setText("Serif");
		jMenuFontSub.add(jmiSerif);
		JMenuItem jmiSansSerif = new JMenuItem((Action) actions
				.get("font-family-SansSerif"));
		jmiSansSerif.setText("Sans-Serif");
		jMenuFontSub.add(jmiSansSerif);
		JMenuItem jmiMonospaced = new JMenuItem((Action) actions
				.get("font-family-Monospaced"));
		jmiMonospaced.setText("Monospaced");
		jMenuFontSub.add(jmiMonospaced);
		jMenuFont.add(jMenuFontSub);
		jMenuFont.addSeparator();
		JMenu jMenuFontColor = new JMenu("Color");
		Hashtable customAttr = new Hashtable();
		customAttr.put("color", "black");
		jMenuFontColor.add(new JMenuItem(new CustomAction(this, "Custom Color"
				+ menuDialog, HTML.Tag.FONT, customAttr)));
		jMenuFontColor.add(new JMenuItem(new StyledEditorKit.ForegroundAction(
				"Aqua", new Color(0, 255, 255))));
		jMenuFontColor.add(new JMenuItem(new StyledEditorKit.ForegroundAction(
				"Black", new Color(0, 0, 0))));
		jMenuFontColor.add(new JMenuItem(new StyledEditorKit.ForegroundAction(
				"Blue", new Color(0, 0, 255))));
		jMenuFontColor.add(new JMenuItem(new StyledEditorKit.ForegroundAction(
				"Fuschia", new Color(255, 0, 255))));
		jMenuFontColor.add(new JMenuItem(new StyledEditorKit.ForegroundAction(
				"Gray", new Color(128, 128, 128))));
		jMenuFontColor.add(new JMenuItem(new StyledEditorKit.ForegroundAction(
				"Green", new Color(0, 128, 0))));
		jMenuFontColor.add(new JMenuItem(new StyledEditorKit.ForegroundAction(
				"Lime", new Color(0, 255, 0))));
		jMenuFontColor.add(new JMenuItem(new StyledEditorKit.ForegroundAction(
				"Maroon", new Color(128, 0, 0))));
		jMenuFontColor.add(new JMenuItem(new StyledEditorKit.ForegroundAction(
				"Navy", new Color(0, 0, 128))));
		jMenuFontColor.add(new JMenuItem(new StyledEditorKit.ForegroundAction(
				"Olive", new Color(128, 128, 0))));
		jMenuFontColor.add(new JMenuItem(new StyledEditorKit.ForegroundAction(
				"Purple", new Color(128, 0, 128))));
		jMenuFontColor.add(new JMenuItem(new StyledEditorKit.ForegroundAction(
				"Red", new Color(255, 0, 0))));
		jMenuFontColor.add(new JMenuItem(new StyledEditorKit.ForegroundAction(
				"Silver", new Color(192, 192, 192))));
		jMenuFontColor.add(new JMenuItem(new StyledEditorKit.ForegroundAction(
				"Teal", new Color(0, 128, 128))));
		jMenuFontColor.add(new JMenuItem(new StyledEditorKit.ForegroundAction(
				"White", new Color(255, 255, 255))));
		jMenuFontColor.add(new JMenuItem(new StyledEditorKit.ForegroundAction(
				"Yellow", new Color(255, 255, 0))));
		jMenuFont.add(jMenuFontColor);

		/* FORMAT Menu */
		jMenuFormat = new JMenu("Format");
		htMenus.put(KEY_MENU_FORMAT, jMenuFormat);
		JMenu jMenuFormatAlign = new JMenu("Align");
		JMenuItem jmiAlignLeft = new JMenuItem(actionAlignLeft);
		if (showMenuIcons) {
			jmiAlignLeft.setIcon(getEkitIcon("AlignLeft"));
		}
		;
		jMenuFormatAlign.add(jmiAlignLeft);
		JMenuItem jmiAlignCenter = new JMenuItem(actionAlignCenter);
		if (showMenuIcons) {
			jmiAlignCenter.setIcon(getEkitIcon("AlignCenter"));
		}
		;
		jMenuFormatAlign.add(jmiAlignCenter);
		JMenuItem jmiAlignRight = new JMenuItem(actionAlignRight);
		if (showMenuIcons) {
			jmiAlignRight.setIcon(getEkitIcon("AlignRight"));
		}
		;
		jMenuFormatAlign.add(jmiAlignRight);
		JMenuItem jmiAlignJustified = new JMenuItem(actionAlignJustified);
		if (showMenuIcons) {
			jmiAlignJustified.setIcon(getEkitIcon("AlignJustified"));
		}
		;
		jMenuFormatAlign.add(jmiAlignJustified);
		jMenuFormat.add(jMenuFormatAlign);
		jMenuFormat.addSeparator();
		JMenuItem jmiUList = new JMenuItem(actionListUnordered);
		if (showMenuIcons) {
			jmiUList.setIcon(getEkitIcon("UList"));
		}
		jMenuFormat.add(jmiUList);
		JMenuItem jmiOList = new JMenuItem(actionListOrdered);
		if (showMenuIcons) {
			jmiOList.setIcon(getEkitIcon("OList"));
		}
		jMenuFormat.add(jmiOList);
		jMenuFormat.add(new JMenuItem(new FormatAction(this, "List Item",
				HTML.Tag.LI)));

		/* INSERT Menu */
		jMenuInsert = new JMenu("Insert");
		htMenus.put(KEY_MENU_INSERT, jMenuInsert);
		JMenuItem jmiInsertAnchor = new JMenuItem(actionInsertAnchor);
		if (showMenuIcons) {
			jmiInsertAnchor.setIcon(getEkitIcon("Anchor"));
		}
		;
		jMenuInsert.add(jmiInsertAnchor);
		JMenuItem jmiBreak = new JMenuItem("Break");
		jmiBreak.setActionCommand("insertbreak");
		jmiBreak.addActionListener(this);
		jmiBreak.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,
				java.awt.Event.SHIFT_MASK, false));
		jMenuInsert.add(jmiBreak);
		JMenuItem jmiNBSP = new JMenuItem("Nonbreaking Space");
		jmiNBSP.setActionCommand("insertnbsp");
		jmiNBSP.addActionListener(this);
		jMenuInsert.add(jmiNBSP);
		JMenu jMenuUnicode = new JMenu("Unicode Characters");
		if (showMenuIcons) {
			jMenuUnicode.setIcon(getEkitIcon("Unicode"));
		}
		;
		JMenuItem jmiUnicodeAll = new JMenuItem("All Characters" + menuDialog);
		if (showMenuIcons) {
			jmiUnicodeAll.setIcon(getEkitIcon("Unicode"));
		}
		;
		jmiUnicodeAll.setActionCommand("insertunicode");
		jmiUnicodeAll.addActionListener(this);
		jMenuUnicode.add(jmiUnicodeAll);
		JMenuItem jmiUnicodeMath = new JMenuItem("Math Symbols" + menuDialog);
		if (showMenuIcons) {
			jmiUnicodeMath.setIcon(getEkitIcon("Math"));
		}
		;
		jmiUnicodeMath.setActionCommand("insertunicodemath");
		jmiUnicodeMath.addActionListener(this);
		jMenuUnicode.add(jmiUnicodeMath);
		JMenuItem jmiUnicodeDraw = new JMenuItem("Drawing Symbols" + menuDialog);
		if (showMenuIcons) {
			jmiUnicodeDraw.setIcon(getEkitIcon("Draw"));
		}
		;
		jmiUnicodeDraw.setActionCommand("insertunicodedraw");
		jmiUnicodeDraw.addActionListener(this);
		jMenuUnicode.add(jmiUnicodeDraw);
		JMenuItem jmiUnicodeDing = new JMenuItem("Dingbats" + menuDialog);
		jmiUnicodeDing.setActionCommand("insertunicodeding");
		jmiUnicodeDing.addActionListener(this);
		jMenuUnicode.add(jmiUnicodeDing);
		JMenuItem jmiUnicodeSigs = new JMenuItem("Signifiers" + menuDialog);
		jmiUnicodeSigs.setActionCommand("insertunicodesigs");
		jmiUnicodeSigs.addActionListener(this);
		jMenuUnicode.add(jmiUnicodeSigs);
		JMenuItem jmiUnicodeSpec = new JMenuItem("Specials" + menuDialog);
		jmiUnicodeSpec.setActionCommand("insertunicodespec");
		jmiUnicodeSpec.addActionListener(this);
		jMenuUnicode.add(jmiUnicodeSpec);
		jMenuInsert.add(jMenuUnicode);
		JMenuItem jmiHRule = new JMenuItem((Action) actions.get("InsertHR"));
		jmiHRule.setText("Insert Horizontal Rule");
		jMenuInsert.add(jmiHRule);
		jMenuInsert.addSeparator();
		JMenuItem jmiImageLocal = new JMenuItem("Image from File" + menuDialog);
		jmiImageLocal.setActionCommand("insertlocalimage");
		jmiImageLocal.addActionListener(this);
		jMenuInsert.add(jmiImageLocal);

		/* TABLE Menu */
		jMenuTable = new JMenu("Table");
		htMenus.put(KEY_MENU_TABLE, jMenuTable);
		JMenuItem jmiTable = new JMenuItem("Create Table" + menuDialog);
		if (showMenuIcons) {
			jmiTable.setIcon(getEkitIcon("TableCreate"));
		}
		;
		jmiTable.setActionCommand("inserttable");
		jmiTable.addActionListener(this);
		jMenuTable.add(jmiTable);
		jMenuTable.addSeparator();
		JMenuItem jmiTableRow = new JMenuItem("Insert Row");
		if (showMenuIcons) {
			jmiTableRow.setIcon(getEkitIcon("InsertRow"));
		}
		;
		jmiTableRow.setActionCommand("inserttablerow");
		jmiTableRow.addActionListener(this);
		jMenuTable.add(jmiTableRow);
		JMenuItem jmiTableCol = new JMenuItem("Insert Column");
		if (showMenuIcons) {
			jmiTableCol.setIcon(getEkitIcon("InsertColumn"));
		}
		;
		jmiTableCol.setActionCommand("inserttablecolumn");
		jmiTableCol.addActionListener(this);
		jMenuTable.add(jmiTableCol);
		jMenuTable.addSeparator();
		JMenuItem jmiTableRowDel = new JMenuItem("Delete Row");
		if (showMenuIcons) {
			jmiTableRowDel.setIcon(getEkitIcon("DeleteRow"));
		}
		;
		jmiTableRowDel.setActionCommand("deletetablerow");
		jmiTableRowDel.addActionListener(this);
		jMenuTable.add(jmiTableRowDel);
		JMenuItem jmiTableColDel = new JMenuItem("Delete Column");
		if (showMenuIcons) {
			jmiTableColDel.setIcon(getEkitIcon("DeleteColumn"));
		}
		;
		jmiTableColDel.setActionCommand("deletetablecolumn");
		jmiTableColDel.addActionListener(this);
		jMenuTable.add(jmiTableColDel);

		/* SEARCH Menu */
		jMenuSearch = new JMenu("Search");
		htMenus.put(KEY_MENU_SEARCH, jMenuSearch);
		JMenuItem jmiFind = new JMenuItem("Find...");
		if (showMenuIcons) {
			jmiFind.setIcon(getEkitIcon("Find"));
		}
		;
		jmiFind.setActionCommand("find");
		jmiFind.addActionListener(this);
		jmiFind.setAccelerator(KeyStroke.getKeyStroke('F',
				java.awt.Event.CTRL_MASK, false));
		jMenuSearch.add(jmiFind);
		JMenuItem jmiFindAgain = new JMenuItem("Find Again...");
		if (showMenuIcons) {
			jmiFindAgain.setIcon(getEkitIcon("FindAgain"));
		}
		;
		jmiFindAgain.setActionCommand("findagain");
		jmiFindAgain.addActionListener(this);
		jmiFindAgain.setAccelerator(KeyStroke.getKeyStroke('G',
				java.awt.Event.CTRL_MASK, false));
		jMenuSearch.add(jmiFindAgain);
		JMenuItem jmiReplace = new JMenuItem("Replace...");
		if (showMenuIcons) {
			jmiReplace.setIcon(getEkitIcon("Replace"));
		}
		;
		jmiReplace.setActionCommand("replace");
		jmiReplace.addActionListener(this);
		jmiReplace.setAccelerator(KeyStroke.getKeyStroke('R',
				java.awt.Event.CTRL_MASK, false));
		jMenuSearch.add(jmiReplace);

		/* HELP Menu */
		jMenuHelp = new JMenu("Help");
		htMenus.put(KEY_MENU_HELP, jMenuHelp);
		JMenuItem jmiShowHelp = new JMenuItem("TerpWord Help...");
		jmiShowHelp.setActionCommand("showhelp");
		jmiShowHelp.addActionListener(this);
		jMenuHelp.add(jmiShowHelp);
		JMenuItem jmiAbout = new JMenuItem("About...");
		jmiAbout.setActionCommand("helpabout");
		jmiAbout.addActionListener(this);
		jMenuHelp.add(jmiAbout);

		/* Create menubar and add menus */
		jMenuBar = new JMenuBar();
		jMenuBar.add(jMenuFile);
		jMenuBar.add(jMenuEdit);
		jMenuBar.add(jMenuView);
		jMenuBar.add(jMenuFont);
		jMenuBar.add(jMenuFormat);
		jMenuBar.add(jMenuSearch);
		jMenuBar.add(jMenuInsert);
		jMenuBar.add(jMenuTable);
		if (jMenuTools != null) {
			jMenuBar.add(jMenuTools);
		}
		jMenuBar.add(jMenuHelp);
		if (debugMode) {
			jMenuBar.add(jMenuDebug);
		}

		/* Create toolbar tool objects */
		jbtnNewHTML = new JButtonNoFocus(getEkitIcon("New"));
		jbtnNewHTML.setToolTipText("New Document");
		jbtnNewHTML.setActionCommand("newdoc");
		jbtnNewHTML.addActionListener(this);
		htTools.put(KEY_TOOL_NEW, jbtnNewHTML);
		jbtnOpenHTML = new JButtonNoFocus(getEkitIcon("Open"));
		jbtnOpenHTML.setToolTipText("Open Document");
		jbtnOpenHTML.setActionCommand("openhtml");
		jbtnOpenHTML.addActionListener(this);
		htTools.put(KEY_TOOL_OPEN, jbtnOpenHTML);
		jbtnSaveHTML = new JButtonNoFocus(getEkitIcon("Save"));
		jbtnSaveHTML.setToolTipText("Save Document");
		jbtnSaveHTML.setActionCommand("saveas");
		jbtnSaveHTML.addActionListener(this);
		htTools.put(KEY_TOOL_SAVE, jbtnSaveHTML);
		jbtnCut = new JButtonNoFocus("");
		jbtnCut.setIcon(getEkitIcon("Cut"));
		jbtnCut.setActionCommand("textcut");
		jbtnCut.addActionListener(this);
		jbtnCut.setToolTipText("Cut");
		htTools.put(KEY_TOOL_CUT, jbtnCut);
		jbtnCopy = new JButtonNoFocus("");
		jbtnCopy.setIcon(getEkitIcon("Copy"));
		jbtnCopy.setToolTipText("Copy");
		jbtnCopy.addActionListener(this);
		jbtnCopy.setActionCommand("textcopy");
		htTools.put(KEY_TOOL_COPY, jbtnCopy);
		jbtnPaste = new JButtonNoFocus("");
		jbtnPaste.setIcon(getEkitIcon("Paste"));
		jbtnPaste.setToolTipText("Paste");
		jbtnPaste.addActionListener(this);
		jbtnPaste.setActionCommand("textpaste");
		htTools.put(KEY_TOOL_PASTE, jbtnPaste);
		jbtnUndo = new JButtonNoFocus(undoAction);
		jbtnUndo.setIcon(getEkitIcon("Undo"));
		jbtnUndo.setText(null);
		jbtnUndo.setToolTipText("Undo");
		htTools.put(KEY_TOOL_UNDO, jbtnUndo);
		jbtnRedo = new JButtonNoFocus(redoAction);
		jbtnRedo.setIcon(getEkitIcon("Redo"));
		jbtnRedo.setText(null);
		jbtnRedo.setToolTipText("Redo");
		htTools.put(KEY_TOOL_REDO, jbtnRedo);
		jbtnBold = new JToggleButtonNoFocus(actionFontBold);
		jbtnBold.setIcon(getEkitIcon("Bold"));
		jbtnBold.setText(null);
		jbtnBold.setToolTipText("Bold");
		htTools.put(KEY_TOOL_BOLD, jbtnBold);
		jbtnItalic = new JToggleButtonNoFocus(actionFontItalic);
		jbtnItalic.setIcon(getEkitIcon("Italic"));
		jbtnItalic.setText(null);
		jbtnItalic.setToolTipText("Italic");
		htTools.put(KEY_TOOL_ITALIC, jbtnItalic);
		jbtnUnderline = new JToggleButtonNoFocus(actionFontUnderline);
		jbtnUnderline.setIcon(getEkitIcon("Underline"));
		jbtnUnderline.setText(null);
		jbtnUnderline.setToolTipText("Underline");
		htTools.put(KEY_TOOL_UNDERLINE, jbtnUnderline);
		jbtnStrike = new JButtonNoFocus(actionFontStrike);
		jbtnStrike.setIcon(getEkitIcon("Strike"));
		jbtnStrike.setText(null);
		jbtnStrike.setToolTipText("Strike-through");
		htTools.put(KEY_TOOL_STRIKE, jbtnStrike);
		jbtnSuperscript = new JButtonNoFocus(actionFontSuperscript);
		jbtnSuperscript.setIcon(getEkitIcon("Super"));
		jbtnSuperscript.setText(null);
		jbtnSuperscript.setToolTipText("Superscript");
		htTools.put(KEY_TOOL_SUPER, jbtnSuperscript);
		jbtnSubscript = new JButtonNoFocus(actionFontSubscript);
		jbtnSubscript.setIcon(getEkitIcon("Sub"));
		jbtnSubscript.setText(null);
		jbtnSubscript.setToolTipText("Subscript");
		htTools.put(KEY_TOOL_SUB, jbtnSubscript);
		jbtnUList = new JButtonNoFocus(actionListUnordered);
		jbtnUList.setIcon(getEkitIcon("UList"));
		jbtnUList.setText(null);
		jbtnUList.setToolTipText("Unordered List");
		htTools.put(KEY_TOOL_ULIST, jbtnUList);
		jbtnOList = new JButtonNoFocus(actionListOrdered);
		jbtnOList.setIcon(getEkitIcon("OList"));
		jbtnOList.setText(null);
		jbtnOList.setToolTipText("Ordered List");
		htTools.put(KEY_TOOL_OLIST, jbtnOList);
		jbtnAlignLeft = new JButtonNoFocus(actionAlignLeft);
		jbtnAlignLeft.setIcon(getEkitIcon("AlignLeft"));
		jbtnAlignLeft.setText(null);
		jbtnAlignLeft.setToolTipText("Align Left");
		htTools.put(KEY_TOOL_ALIGNL, jbtnAlignLeft);
		jbtnAlignCenter = new JButtonNoFocus(actionAlignCenter);
		jbtnAlignCenter.setIcon(getEkitIcon("AlignCenter"));
		jbtnAlignCenter.setText(null);
		jbtnAlignCenter.setToolTipText("Align Center");
		htTools.put(KEY_TOOL_ALIGNC, jbtnAlignCenter);
		jbtnAlignRight = new JButtonNoFocus(actionAlignRight);
		jbtnAlignRight.setIcon(getEkitIcon("AlignRight"));
		jbtnAlignRight.setText(null);
		jbtnAlignRight.setToolTipText("Align Right");
		htTools.put(KEY_TOOL_ALIGNR, jbtnAlignRight);
		jbtnAlignJustified = new JButtonNoFocus(actionAlignJustified);
		jbtnAlignJustified.setIcon(getEkitIcon("AlignJustified"));
		jbtnAlignJustified.setText(null);
		jbtnAlignJustified.setToolTipText("Align Justified");
		htTools.put(KEY_TOOL_ALIGNJ, jbtnAlignJustified);
		jbtnUnicode = new JButtonNoFocus();
		jbtnUnicode.setActionCommand("insertunicode");
		jbtnUnicode.addActionListener(this);
		jbtnUnicode.setIcon(getEkitIcon("Unicode"));
		jbtnUnicode.setText(null);
		jbtnUnicode.setToolTipText("Insert Unicode Characters");
		htTools.put(KEY_TOOL_UNICODE, jbtnUnicode);
		jbtnUnicodeMath = new JButtonNoFocus();
		jbtnUnicodeMath.setActionCommand("insertunicodemath");
		jbtnUnicodeMath.addActionListener(this);
		jbtnUnicodeMath.setIcon(getEkitIcon("Math"));
		jbtnUnicodeMath.setText(null);
		jbtnUnicodeMath.setToolTipText("Insert Math Symbols");
		htTools.put(KEY_TOOL_UNIMATH, jbtnUnicodeMath);
		jbtnFind = new JButtonNoFocus();
		jbtnFind.setActionCommand("find");
		jbtnFind.addActionListener(this);
		jbtnFind.setIcon(getEkitIcon("Find"));
		jbtnFind.setText(null);
		jbtnFind.setToolTipText("Find");
		htTools.put(KEY_TOOL_FIND, jbtnFind);
		jbtnAnchor = new JButtonNoFocus(actionInsertAnchor);
		jbtnAnchor.setIcon(getEkitIcon("Anchor"));
		jbtnAnchor.setText(null);
		jbtnAnchor.setToolTipText("Anchor");
		htTools.put(KEY_TOOL_ANCHOR, jbtnAnchor);
		jtbtnViewSource = new JToggleButtonNoFocus(getEkitIcon("Source"));
		jtbtnViewSource.setText(null);
		jtbtnViewSource.setToolTipText("View Source");
		jtbtnViewSource.setActionCommand("viewsource");
		jtbtnViewSource.addActionListener(this);
		jtbtnViewSource.setPreferredSize(jbtnAnchor.getPreferredSize());
		jtbtnViewSource.setMinimumSize(jbtnAnchor.getMinimumSize());
		jtbtnViewSource.setMaximumSize(jbtnAnchor.getMaximumSize());
		htTools.put(KEY_TOOL_SOURCE, jtbtnViewSource);
		jtbtnSplitPanel = new JToggleButtonNoFocus(getEkitIcon("Split"));
		jtbtnSplitPanel.setText(null);
		jtbtnSplitPanel.setToolTipText("Split Panel");
		jtbtnSplitPanel.setActionCommand("splitpanel");
		jtbtnSplitPanel.addActionListener(this);
		jtbtnSplitPanel.setPreferredSize(jbtnAnchor.getPreferredSize());
		jtbtnSplitPanel.setMinimumSize(jbtnAnchor.getMinimumSize());
		jtbtnSplitPanel.setMaximumSize(jbtnAnchor.getMaximumSize());
		htTools.put(KEY_TOOL_SPLITPANEL, jtbtnSplitPanel);
		String[] fonts = java.awt.GraphicsEnvironment
				.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		Vector vcFontnames = new Vector(fonts.length + 1);
		for (int i = 0; i < fonts.length; i++) {
			vcFontnames.add(fonts[i]);
		}
		Collections.sort(vcFontnames);
		jcmbFontSelector = new JComboBoxNoFocus(vcFontnames);
		timesNewRoman = vcFontnames.indexOf((Object) ("Times New Roman"));
		jcmbFontSelector.setAction(new SetFontFamilyAction(this,
				"[FONTSELECTOR]"));
		jcmbFontSelector.setMaximumSize(new Dimension(200, 50));
		jcmbFontSelector.setSelectedIndex(timesNewRoman);
		htTools.put(KEY_TOOL_FONTS, jcmbFontSelector);
		Vector sizes = new Vector(8);
		sizes.add("10");
		sizes.add("12");
		sizes.add("14");
		sizes.add("16");
		sizes.add("18");
		sizes.add("24");
		sizes.add("36");
		sizes.add("48");
		Collections.sort(sizes);
		jcmbFontSize = new JComboBoxNoFocus(sizes);
		jcmbFontSize.setMaximumRowCount(8);
		jcmbFontSize.setAction(new SetFontSizeAction(this, "[SIZESELECTOR]"));
		jcmbFontSize.setSelectedIndex(2);
		jcmbFontSize.setMaximumSize(new Dimension(50, 50));
		htTools.put(KEY_TOOL_FONTSIZE, jcmbFontSize);

		/* Create the toolbar */

		//top horizontal toolbar
		jToolBarMain = new JToolBar(JToolBar.HORIZONTAL);
		jToolBarMain.setFloatable(false);
		jToolBarMain.add((JButtonNoFocus) (htTools.get(KEY_TOOL_NEW)));
		jToolBarMain.add((JButtonNoFocus) (htTools.get(KEY_TOOL_OPEN)));
		jToolBarMain.add((JButtonNoFocus) (htTools.get(KEY_TOOL_SAVE)));
		jToolBarMain.add(new JToolBar.Separator());
		jToolBarMain.add((JButtonNoFocus) (htTools.get(KEY_TOOL_CUT)));
		jToolBarMain.add((JButtonNoFocus) (htTools.get(KEY_TOOL_COPY)));
		jToolBarMain.add((JButtonNoFocus) (htTools.get(KEY_TOOL_PASTE)));
		jToolBarMain.add(new JToolBar.Separator());
		jToolBarMain.add((JButtonNoFocus) (htTools.get(KEY_TOOL_UNDO)));
		jToolBarMain.add((JButtonNoFocus) (htTools.get(KEY_TOOL_REDO)));
		jToolBarMain.add(new JToolBar.Separator());
		jToolBarMain.add((JButtonNoFocus) (htTools.get(KEY_TOOL_FIND)));
		jToolBarMain.add(new JToolBar.Separator());
		jToolBarMain.add((JButtonNoFocus) (htTools.get(KEY_TOOL_UNICODE)));
		jToolBarMain.add((JButtonNoFocus) (htTools.get(KEY_TOOL_UNIMATH)));
		jToolBarMain.add(new JToolBar.Separator());
		jToolBarMain.add((JButtonNoFocus) (htTools.get(KEY_TOOL_ANCHOR)));
		jToolBarMain.add((JToggleButtonNoFocus) (htTools.get(KEY_TOOL_SOURCE)));
		jToolBarMain.add((JToggleButtonNoFocus) (htTools
				.get(KEY_TOOL_SPLITPANEL)));

		//2nd horizontal toolbar
		jToolBarStyles = new JToolBar(JToolBar.HORIZONTAL);
		jToolBarStyles.setFloatable(false);
		jToolBarStyles.add((JButtonNoFocus) (htTools.get(KEY_TOOL_ALIGNL)));
		jToolBarStyles.add((JButtonNoFocus) (htTools.get(KEY_TOOL_ALIGNC)));
		jToolBarStyles.add((JButtonNoFocus) (htTools.get(KEY_TOOL_ALIGNR)));
		jToolBarStyles.add((JButtonNoFocus) (htTools.get(KEY_TOOL_ALIGNJ)));
		jToolBarStyles.add(new JToolBar.Separator());
		JComboBoxNoFocus fnt = (JComboBoxNoFocus) (htTools.get(KEY_TOOL_FONTS));
		jToolBarStyles.add(fnt);
		JComboBox sz = (JComboBox) (htTools.get(KEY_TOOL_FONTSIZE));
		sz.setSelectedIndex(1);
		jToolBarStyles.add(sz);
		jToolBarStyles.add(new JToolBar.Separator());
		jToolBarStyles.add((JToggleButtonNoFocus) (htTools.get(KEY_TOOL_BOLD)));
		jToolBarStyles
				.add((JToggleButtonNoFocus) (htTools.get(KEY_TOOL_ITALIC)));
		jToolBarStyles.add((JToggleButtonNoFocus) (htTools
				.get(KEY_TOOL_UNDERLINE)));
		jToolBarStyles.add(new JToolBar.Separator());
		jToolBarStyles.add((JButtonNoFocus) (htTools.get(KEY_TOOL_STRIKE)));
		jToolBarStyles.add((JButtonNoFocus) (htTools.get(KEY_TOOL_SUPER)));
		jToolBarStyles.add((JButtonNoFocus) (htTools.get(KEY_TOOL_SUB)));
		jToolBarStyles.add(new JToolBar.Separator());
		jToolBarStyles.add((JButtonNoFocus) (htTools.get(KEY_TOOL_ULIST)));
		jToolBarStyles.add((JButtonNoFocus) (htTools.get(KEY_TOOL_OLIST)));

		// fix the weird size preference of toggle buttons
		jtbtnViewSource.setPreferredSize(jbtnAnchor.getPreferredSize());
		jtbtnViewSource.setMinimumSize(jbtnAnchor.getMinimumSize());
		jtbtnViewSource.setMaximumSize(jbtnAnchor.getMaximumSize());
		jtbtnSplitPanel.setPreferredSize(jbtnAnchor.getPreferredSize());
		jtbtnSplitPanel.setMinimumSize(jbtnAnchor.getMinimumSize());
		jtbtnSplitPanel.setMaximumSize(jbtnAnchor.getMaximumSize());

		/* Create the scroll area for the text pane */
		jspViewport = new JScrollPane(jtpMain);
		jspViewport
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jspViewport.setPreferredSize(new Dimension(400, 400));
		jspViewport.setMinimumSize(new Dimension(32, 32));

		/* Create the scroll area for the source viewer */
		jspSource = new JScrollPane(jtpSource);
		jspSource.setPreferredSize(new Dimension(400, 100));
		jspSource.setMinimumSize(new Dimension(32, 32));

		jspltDisplay = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		jspltDisplay.setTopComponent(jspViewport);
		jspltDisplay.setDividerSize(1);
		if (showViewSource) {
			jspltDisplay.setBottomComponent(jspSource);
		} else {
			jspltDisplay.setBottomComponent(null);
		}

		iSplitPos = jspltDisplay.getDividerLocation();

		/* Add the components to the app */
		this.setLayout(new BorderLayout());
		this.add(jspltDisplay, BorderLayout.CENTER);
	}

	/**
	 * @param sDocument -
	 *            [String] A text or HTML document to load in the editor upon
	 *            startup.
	 * @param sStyleSheet -
	 *            [String] A CSS stylesheet to load in the editor upon startup.
	 * @param sRawDocument -
	 *            [String] A document encoded as a String to load in the editor
	 *            upon startup.
	 * @param urlStyleSheet -
	 *            [URL] A URL reference to the CSS style sheet.
	 * @param includeToolBar -
	 *            [boolean] Specifies whether the app should include the
	 *            toolbar(s).
	 * @param showViewSource -
	 *            [boolean] Specifies whether or not to show the View Source
	 *            window on startup.
	 * @param showMenuIcons -
	 *            [boolean] Specifies whether or not to show icon pictures in
	 *            menus.
	 * @param editModeExclusive -
	 *            [boolean] Specifies whether or not to use exclusive edit mode
	 *            (recommended on).
	 * @param sLanguage -
	 *            [String] The language portion of the Internationalization
	 *            Locale to run Ekit in.
	 * @param sCountry -
	 *            [String] The country portion of the Internationalization
	 *            Locale to run Ekit in.
	 * @param base64 -
	 *            [boolean] Specifies whether the raw document is Base64 encoded
	 *            or not.
	 * @param debugMode -
	 *            [boolean] Specifies whether to show the Debug menu or not.
	 * @param hasSpellChecker -
	 *            [boolean] Specifies whether or not this uses the SpellChecker
	 *            module
	 * @param multiBar -
	 *            [boolean] Specifies whether to use multiple toolbars or one
	 *            big toolbar.
	 */
	public EkitCore(String sDocument, String sStyleSheet, String sRawDocument,
			URL urlStyleSheet, boolean includeToolBar, boolean showViewSource,
			boolean showMenuIcons, boolean editModeExclusive, String sLanguage,
			String sCountry, boolean base64, boolean debugMode,
			boolean hasSpellChecker, boolean multiBar) {
		this(sDocument, sStyleSheet, sRawDocument, (StyledDocument) null,
				urlStyleSheet, includeToolBar, showViewSource, showMenuIcons,
				editModeExclusive, sLanguage, sCountry, base64, debugMode,
				hasSpellChecker, multiBar);
	}

	/**
	 * @param sDocument -
	 *            [String] A text or HTML document to load in the editor upon
	 *            startup.
	 * @param sStyleSheet -
	 *            [String] A CSS stylesheet to load in the editor upon startup.
	 * @param sRawDocument -
	 *            [String] A document encoded as a String to load in the editor
	 *            upon startup.
	 * @param urlStyleSheet -
	 *            [URL] A URL reference to the CSS style sheet.
	 * @param includeToolBar -
	 *            [boolean] Specifies whether the app should include the
	 *            toolbar(s).
	 * @param showViewSource -
	 *            [boolean] Specifies whether or not to show the View Source
	 *            window on startup.
	 * @param showMenuIcons -
	 *            [boolean] Specifies whether or not to show icon pictures in
	 *            menus.
	 * @param editModeExclusive -
	 *            [boolean] Specifies whether or not to use exclusive edit mode
	 *            (recommended on).
	 * @param sLanguage -
	 *            [String] The language portion of the Internationalization
	 *            Locale to run Ekit in.
	 * @param sCountry -
	 *            [String] The country portion of the Internationalization
	 *            Locale to run Ekit in.
	 * @param base64 -
	 *            [boolean] Specifies whether the raw document is Base64 encoded
	 *            or not.
	 * @param debugMode -
	 *            [boolean] Specifies whether to show the Debug menu or not.
	 */
	public EkitCore(String sDocument, String sStyleSheet, String sRawDocument,
			URL urlStyleSheet, boolean includeToolBar, boolean showViewSource,
			boolean showMenuIcons, boolean editModeExclusive, String sLanguage,
			String sCountry, boolean base64, boolean debugMode) {
		this(sDocument, sStyleSheet, sRawDocument, (StyledDocument) null,
				urlStyleSheet, includeToolBar, showViewSource, showMenuIcons,
				editModeExclusive, sLanguage, sCountry, base64, debugMode,
				false, false);
	}

	/**
	 * @param sDocument -
	 *            [String] A text or HTML document to load in the editor upon
	 *            startup.
	 * @param sStyleSheet -
	 *            [String] A CSS stylesheet to load in the editor upon startup.
	 * @param sRawDocument -
	 *            [String] A document encoded as a String to load in the editor
	 *            upon startup.
	 * @param urlStyleSheet -
	 *            [URL] A URL reference to the CSS style sheet.
	 * @param showViewSource -
	 *            [boolean] Specifies whether or not to show the View Source
	 *            window on startup.
	 * @param showMenuIcons -
	 *            [boolean] Specifies whether or not to show icon pictures in
	 *            menus.
	 * @param editModeExclusive -
	 *            [boolean] Specifies whether or not to use exclusive edit mode
	 *            (recommended on).
	 * @param sLanguage -
	 *            [String] The language portion of the Internationalization
	 *            Locale to run Ekit in.
	 * @param sCountry -
	 *            [String] The country portion of the Internationalization
	 *            Locale to run Ekit in.
	 * @param base64 -
	 *            [boolean] Specifies whether the raw document is Base64 encoded
	 *            or not.
	 * @param debugMode -
	 *            [boolean] Specifies whether to show the Debug menu or not.
	 * @param hasSpellChecker -
	 *            [boolean] Specifies whether or not this uses the SpellChecker
	 *            module
	 */
	public EkitCore(String sDocument, String sStyleSheet, String sRawDocument,
			URL urlStyleSheet, boolean showViewSource, boolean showMenuIcons,
			boolean editModeExclusive, String sLanguage, String sCountry,
			boolean base64, boolean debugMode, boolean hasSpellChecker) {
		this(sDocument, sStyleSheet, sRawDocument, (StyledDocument) null,
				urlStyleSheet, true, showViewSource, showMenuIcons,
				editModeExclusive, sLanguage, sCountry, base64, debugMode,
				hasSpellChecker, false);
	}

	/**
	 * @param sDocument -
	 *            [String] A text or HTML document to load in the editor upon
	 *            startup.
	 * @param sStyleSheet -
	 *            [String] A CSS stylesheet to load in the editor upon startup.
	 * @param sRawDocument -
	 *            [String] A document encoded as a String to load in the editor
	 *            upon startup.
	 * @param urlStyleSheet -
	 *            [URL] A URL reference to the CSS style sheet.
	 * @param showViewSource -
	 *            [boolean] Specifies whether or not to show the View Source
	 *            window on startup.
	 * @param showMenuIcons -
	 *            [boolean] Specifies whether or not to show icon pictures in
	 *            menus.
	 * @param editModeExclusive -
	 *            [boolean] Specifies whether or not to use exclusive edit mode
	 *            (recommended on).
	 * @param sLanguage -
	 *            [String] The language portion of the Internationalization
	 *            Locale to run Ekit in.
	 * @param sCountry -
	 *            [String] The country portion of the Internationalization
	 *            Locale to run Ekit in.
	 * @param base64 -
	 *            [boolean] Specifies whether the raw document is Base64 encoded
	 *            or not.
	 * @param debugMode -
	 *            [boolean] Specifies whether to show the Debug menu or not.
	 */
	public EkitCore(String sDocument, String sStyleSheet, String sRawDocument,
			URL urlStyleSheet, boolean showViewSource, boolean showMenuIcons,
			boolean editModeExclusive, String sLanguage, String sCountry,
			boolean base64, boolean debugMode) {
		this(sDocument, sStyleSheet, sRawDocument, (StyledDocument) null,
				urlStyleSheet, true, showViewSource, showMenuIcons,
				editModeExclusive, sLanguage, sCountry, base64, debugMode,
				false, false);
	}

	/**
	 * Common Constructor
	 * 
	 * @param sDocument
	 *            [String] A text or HTML document to load in the editor upon
	 *            startup.
	 * @param sStyleSheet
	 *            [String] A CSS stylesheet to load in the editor upon startup.
	 * @param includeToolBar
	 *            [boolean] Specifies whether the app should include the
	 *            toolbar(s).
	 * @param showViewSource
	 *            [boolean] Specifies whether or not to show the View Source
	 *            window on startup.
	 * @param showMenuIcons
	 *            [boolean] Specifies whether or not to show icon pictures in
	 *            menus.
	 * @param editModeExclusive
	 *            [boolean] Specifies whether or not to use exclusive edit mode
	 *            (recommended on).
	 * @param sLanguage
	 *            [String] The language portion of the Internationalization
	 *            Locale to run Ekit in.
	 * @param sCountry
	 *            [String] The country portion of the Internationalization
	 *            Locale to run Ekit in.
	 */
	public EkitCore(String sDocument, String sStyleSheet,
			boolean includeToolBar, boolean showViewSource,
			boolean showMenuIcons, boolean editModeExclusive, String sLanguage,
			String sCountry, boolean base64) {
		this(sDocument, sStyleSheet, null, null, includeToolBar,
				showViewSource, showMenuIcons, editModeExclusive, sLanguage,
				sCountry, base64, false);
	}

	/**
	 * StyledDocument Constructor With Spellchecker Specifier
	 * 
	 * @param sdocSource
	 *            [StyledDocument] The optional StyledDocument to use as the
	 *            source Document.
	 * @param showViewSource
	 *            [boolean] Specifies whether or not to show the View Source
	 *            window on startup.
	 * @param showMenuIcons
	 *            [boolean] Specifies whether or not to show icon pictures in
	 *            menus.
	 * @param editModeExclusive
	 *            [boolean] Specifies whether or not to use exclusive edit mode
	 *            (recommended on).
	 * @param sLanguage
	 *            [String] The language portion of the Internationalization
	 *            Locale to run Ekit in.
	 * @param sCountry
	 *            [String] The country portion of the Internationalization
	 *            Locale to run Ekit in.
	 * @param debugMode
	 *            [boolean] Specifies whether to show the Debug menu or not.
	 * @param hasSpellChecker
	 *            [boolean] Specifies whether or not this uses the SpellChecker
	 *            module
	 */
	public EkitCore(StyledDocument sdocSource, boolean showViewSource,
			boolean showMenuIcons, boolean editModeExclusive, String sLanguage,
			String sCountry, boolean debugMode, boolean hasSpellChecker) {
		this(null, null, null, sdocSource, null, true, showViewSource,
				showMenuIcons, editModeExclusive, sLanguage, sCountry, false,
				debugMode, hasSpellChecker, false);
	}

	/**
	 * StyledDocument Constructor
	 * 
	 * @param sdocSource
	 *            [StyledDocument] The optional StyledDocument to use as the
	 *            source Document.
	 * @param showViewSource
	 *            [boolean] Specifies whether or not to show the View Source
	 *            window on startup.
	 * @param showMenuIcons
	 *            [boolean] Specifies whether or not to show icon pictures in
	 *            menus.
	 * @param editModeExclusive
	 *            [boolean] Specifies whether or not to use exclusive edit mode
	 *            (recommended on).
	 * @param sLanguage
	 *            [String] The language portion of the Internationalization
	 *            Locale to run Ekit in.
	 * @param sCountry
	 *            [String] The country portion of the Internationalization
	 *            Locale to run Ekit in.
	 * @param debugMode
	 *            [boolean] Specifies whether to show the Debug menu or not.
	 */
	public EkitCore(StyledDocument sdocSource, boolean showViewSource,
			boolean showMenuIcons, boolean editModeExclusive, String sLanguage,
			String sCountry, boolean debugMode) {
		this(null, null, null, sdocSource, null, true, showViewSource,
				showMenuIcons, editModeExclusive, sLanguage, sCountry, false,
				debugMode, false, false);
	}

	/**
	 * Default Language Constructor
	 * 
	 * @param sDocument
	 *            [String] A text or HTML document to load in the editor upon
	 *            startup.
	 * @param sStyleSheet
	 *            [String] A CSS stylesheet to load in the editor upon startup.
	 * @param includeToolBar
	 *            [boolean] Specifies whether the app should include the
	 *            toolbar(s).
	 * @param showViewSource
	 *            [boolean] Specifies whether or not to show the View Source
	 *            window on startup.
	 * @param showMenuIcons
	 *            [boolean] Specifies whether or not to show icon pictures in
	 *            menus.
	 * @param editModeExclusive
	 *            [boolean] Specifies whether or not to use exclusive edit mode
	 *            (recommended on).
	 */
	public EkitCore(String sDocument, String sStyleSheet,
			boolean includeToolBar, boolean showViewSource,
			boolean showMenuIcons, boolean editModeExclusive, boolean base64) {
		this(sDocument, sStyleSheet, null, null, includeToolBar,
				showViewSource, showMenuIcons, editModeExclusive, null, null,
				base64, false);
	}

	/**
	 * Raw/Base64 Document & Style Sheet URL Constructor (Ideal for EkitApplet)
	 * 
	 * @param sRawDocument
	 *            [String] A document encoded as a String to load in the editor
	 *            upon startup.
	 * @param includeToolBar
	 *            [boolean] Specifies whether the app should include the
	 *            toolbar(s).
	 * @param showViewSource
	 *            [boolean] Specifies whether or not to show the View Source
	 *            window on startup.
	 * @param showMenuIcons
	 *            [boolean] Specifies whether or not to show icon pictures in
	 *            menus.
	 * @param editModeExclusive
	 *            [boolean] Specifies whether or not to use exclusive edit mode
	 *            (recommended on).
	 * @param sLanguage
	 *            [String] The language portion of the Internationalization
	 *            Locale to run Ekit in.
	 * @param sCountry
	 *            [String] The country portion of the Internationalization
	 *            Locale to run Ekit in.
	 */
	public EkitCore(String sRawDocument, URL urlStyleSheet,
			boolean includeToolBar, boolean showViewSource,
			boolean showMenuIcons, boolean editModeExclusive, String sLanguage,
			String sCountry, boolean base64, boolean hasSpellChecker,
			boolean multiBar) {
		this(null, null, sRawDocument, urlStyleSheet, includeToolBar,
				showViewSource, showMenuIcons, editModeExclusive, sLanguage,
				sCountry, base64, false, hasSpellChecker, multiBar);
	}

	/**
	 * Document Constructor
	 * 
	 * @param sRawDocument
	 *            [String] A document encoded as a String to load in the editor
	 *            upon startup.
	 * @param includeToolBar
	 *            [boolean] Specifies whether the app should include the
	 *            toolbar(s).
	 * @param showViewSource
	 *            [boolean] Specifies whether or not to show the View Source
	 *            window on startup.
	 * @param showMenuIcons
	 *            [boolean] Specifies whether or not to show icon pictures in
	 *            menus.
	 * @param editModeExclusive
	 *            [boolean] Specifies whether or not to use exclusive edit mode
	 *            (recommended on).
	 * @param sLanguage
	 *            [String] The language portion of the Internationalization
	 *            Locale to run Ekit in.
	 * @param sCountry
	 *            [String] The country portion of the Internationalization
	 *            Locale to run Ekit in.
	 */
	public EkitCore(String sRawDocument, boolean includeToolBar,
			boolean showViewSource, boolean showMenuIcons,
			boolean editModeExclusive, String sLanguage, String sCountry,
			boolean base64) {
		this(null, null, sRawDocument, null, includeToolBar, showViewSource,
				showMenuIcons, editModeExclusive, sLanguage, sCountry, base64,
				false);
	}

	/**
	 * Default Language & Document Constructor
	 * 
	 * @param sRawDocument
	 *            [String] A document encoded as a String to load in the editor
	 *            upon startup.
	 * @param includeToolBar
	 *            [boolean] Specifies whether the app should include the
	 *            toolbar(s).
	 * @param showViewSource
	 *            [boolean] Specifies whether or not to show the View Source
	 *            window on startup.
	 * @param showMenuIcons
	 *            [boolean] Specifies whether or not to show icon pictures in
	 *            menus.
	 * @param editModeExclusive
	 *            [boolean] Specifies whether or not to use exclusive edit mode
	 *            (recommended on).
	 */
	public EkitCore(String sRawDocument, boolean includeToolBar,
			boolean showViewSource, boolean showMenuIcons,
			boolean editModeExclusive, boolean base64) {
		this(null, null, sRawDocument, null, includeToolBar, showViewSource,
				showMenuIcons, editModeExclusive, null, null, base64, false);
	}

	/**
	 * Flags & Language Constructor
	 * 
	 * @param includeToolBar
	 *            [boolean] Specifies whether the app should include the
	 *            toolbar(s).
	 * @param showViewSource
	 *            [boolean] Specifies whether or not to show the View Source
	 *            window on startup.
	 * @param showMenuIcons
	 *            [boolean] Specifies whether or not to show icon pictures in
	 *            menus.
	 * @param editModeExclusive
	 *            [boolean] Specifies whether or not to use exclusive edit mode
	 *            (recommended on).
	 * @param sLanguage
	 *            [String] The language portion of the Internationalization
	 *            Locale to run Ekit in.
	 * @param sCountry
	 *            [String] The country portion of the Internationalization
	 *            Locale to run Ekit in.
	 */
	public EkitCore(boolean includeToolBar, boolean showViewSource,
			boolean showMenuIcons, boolean editModeExclusive, String sLanguage,
			String sCountry) {
		this(null, null, null, null, includeToolBar, showViewSource,
				showMenuIcons, editModeExclusive, sLanguage, sCountry, false,
				false);
	}

	/**
	 * Flags Constructor
	 * 
	 * @param includeToolBar
	 *            [boolean] Specifies whether the app should include the
	 *            toolbar(s).
	 * @param showViewSource
	 *            [boolean] Specifies whether or not to show the View Source
	 *            window on startup.
	 * @param showMenuIcons
	 *            [boolean] Specifies whether or not to show icon pictures in
	 *            menus.
	 * @param editModeExclusive
	 *            [boolean] Specifies whether or not to use exclusive edit mode
	 *            (recommended on).
	 */
	public EkitCore(boolean includeToolBar, boolean showViewSource,
			boolean showMenuIcons, boolean editModeExclusive) {
		this(null, null, null, null, includeToolBar, showViewSource,
				showMenuIcons, editModeExclusive, null, null, false, false);
	}

	/**
	 * Language & Debug Constructor
	 * 
	 * @param sLanguage
	 *            [String] The language portion of the Internationalization
	 *            Locale to run Ekit in.
	 * @param sCountry
	 *            [String] The country portion of the Internationalization
	 *            Locale to run Ekit in.
	 * @param debugMode
	 *            [boolean] Specifies whether to show the Debug menu or not.
	 */
	public EkitCore(String sLanguage, String sCountry, boolean debugMode) {
		this(null, null, null, null, true, false, true, true, sLanguage,
				sCountry, false, debugMode);
	}

	/**
	 * Language Constructor
	 * 
	 * @param sLanguage
	 *            [String] The language portion of the Internationalization
	 *            Locale to run Ekit in.
	 * @param sCountry
	 *            [String] The country portion of the Internationalization
	 *            Locale to run Ekit in.
	 */
	public EkitCore(String sLanguage, String sCountry) {
		this(null, null, null, null, true, false, true, true, sLanguage,
				sCountry, false, false);
	}

	/**
	 * Debug Constructor
	 * 
	 * @param debugMode
	 *            [boolean] Specifies whether to show the Debug menu or not.
	 */
	public EkitCore(boolean debugMode) {
		this(null, null, null, null, true, false, true, true, null, null,
				false, debugMode);
	}

	/**
	 * Empty Constructor
	 */
	public EkitCore() {
		this(null, null, null, null, true, false, true, true, null, null,
				false, false);
	}

	/* ActionListener method */
	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae) {
		try {
			String command = ae.getActionCommand();
			if (command.equals("newdoc")) {

				String message = "Save before creating new blank document?";
				int choice = JOptionPane.showConfirmDialog(jtpMain, message);
				switch (choice) {
				case JOptionPane.YES_OPTION: // if the user wanted to save
					try {
						writeOut((HTMLDocument) (jtpMain.getDocument()), null);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
					//adding file to recently opened files history
					if (currentFile != null) {
						doc_path = currentFile.getAbsolutePath();
						doc_name = currentFile.getName();
						add_to_history_stack(doc_path, doc_name);
						updateHistory();
					}

					//making new document
					if (styleSheet != null) {
						htmlDoc = new ExtendedHTMLDocument(styleSheet);
					} else {
						htmlDoc = (ExtendedHTMLDocument) (htmlKit
								.createDefaultDocument());
					}
					jtpMain.setText("<HTML><BODY></BODY></HTML>");
					jtpSource.setText(jtpMain.getText());
					registerDocument(htmlDoc);
					currentFile = null;
					updateTitle();
					break;

				case JOptionPane.NO_OPTION: // if the user don't like to save
											// make new doc
					if (styleSheet != null) {
						htmlDoc = new ExtendedHTMLDocument(styleSheet);
					} else {
						htmlDoc = (ExtendedHTMLDocument) (htmlKit
								.createDefaultDocument());
					}
					jtpMain.setText("<HTML><BODY></BODY></HTML>");
					jtpSource.setText(jtpMain.getText());
					registerDocument(htmlDoc);
					currentFile = null;
					updateTitle();
					break;
				case JOptionPane.CANCEL_OPTION: // if the user cancelled
					break; // do nothing
				}
			} else if (command.equals("openhtml")) {
				openDocument(null);
				if (currentFile != null) {
					doc_path = currentFile.getAbsolutePath();
					doc_name = currentFile.getName();
					add_to_history_stack(doc_path, doc_name);
					updateHistory();
				}
			} else if ((command.equals("openfile1")
					|| command.equals("openfile2")
					|| command.equals("openfile3")
					|| command.equals("openfile4") || command
					.equals("openfile5"))) {
				String str = new String(command.substring(command.length() - 1));
				int index = Integer.parseInt(str);
				Vector tmp = new Vector();
				tmp = (Vector) history_stack.elementAt(history_stack.size()
						- index);
				if (((String) tmp.get(1)).compareTo(new String("-")) != 0) {
					File opendoc = new File((String) tmp.get(1));
					openDocument(opendoc);
				}
			} else if (command.equals("save")) {

				writeOut((HTMLDocument) (jtpMain.getDocument()), currentFile);
				updateTitle();
				if (currentFile != null) {
					doc_path = currentFile.getAbsolutePath();
					doc_name = currentFile.getName();
					add_to_history_stack(doc_path, doc_name);
					updateHistory();
				}
			} else if (command.equals("saveas")) {
				writeOut((HTMLDocument) (jtpMain.getDocument()), null);
				if (currentFile != null) {
					doc_path = currentFile.getAbsolutePath();
					doc_name = currentFile.getName();
					add_to_history_stack(doc_path, doc_name);
					updateHistory();
				}
			} else if (command.equals("savertf")) {
				File rtfFile = writeOutRTF((StyledDocument) (jtpMain
						.getStyledDocument()));
				if (rtfFile != null) {
					doc_path = rtfFile.getAbsolutePath();
					doc_name = rtfFile.getName();
					add_to_history_stack(doc_path, doc_name);
					updateHistory();
				}
			} else if (command.equals("print")) {
				PrintUtilities print = new PrintUtilities(this.jtpMain);
				print.print();
			} else if (command.equals("textcut")) {
				if (jspSource.isShowing() && jtpSource.hasFocus()) {
					jtpSource.cut();
				} else {
					jtpMain.cut();
				}
			} else if (command.equals("textcopy")) {
				if (jspSource.isShowing() && jtpSource.hasFocus()) {
					jtpSource.copy();
				} else {
					jtpMain.copy();
				}
			} else if (command.equals("textpaste")) {
				if (jspSource.isShowing() && jtpSource.hasFocus()) {
					jtpSource.paste();
				} else {
					ImageGrabber imgGrabber = new ImageGrabber();
					Image img = imgGrabber.getImageFromClipboard();
					//System.out.println(img);
					if (img == null) {
						jtpMain.paste();
					} else {
						BufferedImage buf = (BufferedImage) img;
						File file = new File("temp.jpg");
						ImageIO.write(buf, "jpg", file);
						insertLocalImage(file);
						file.deleteOnExit();
					}
				}
			} else if (command.equals("toggletoolbar")) {
				jToolBar.setVisible(jcbmiViewToolbar.isSelected());
			} else if (command.equals("toggletoolbarmain")) {
				jToolBarMain.setVisible(jcbmiViewToolbarMain.isSelected());
			} else if (command.equals("toggletoolbarformat")) {
				jToolBarFormat.setVisible(jcbmiViewToolbarFormat.isSelected());
			} else if (command.equals("toggletoolbarstyles")) {
				jToolBarStyles.setVisible(jcbmiViewToolbarStyles.isSelected());
			} else if (command.equals("viewsource")) {
				toggleSourceWindow();
			} else if (command.equals("splitpanel")) {
				splitPanel();
			} else if (command.equals("inserttable")) {
				String[] fieldNames = { "rows", "cols", "border",
						"cellspacing", "cellpadding", "width" };
				String[] fieldTypes = { "text", "text", "text", "text", "text",
						"text" };
				insertTable((Hashtable) null, fieldNames, fieldTypes);
			} else if (command.equals("inserttablerow")) {
				insertTableRow();
			} else if (command.equals("inserttablecolumn")) {
				insertTableColumn();
			} else if (command.equals("deletetablerow")) {
				deleteTableRow();
			} else if (command.equals("deletetablecolumn")) {
				deleteTableColumn();
			} else if (command.equals("insertbreak")) {
				insertBreak();
			} else if (command.equals("insertnbsp")) {
				insertNonbreakingSpace();
			} else if (command.equals("insertlocalimage")) {
				insertLocalImage(null);
			} else if (command.equals("insertunicode")) {
				insertUnicode(UnicodeDialog.UNICODE_BASE);
			} else if (command.equals("insertunicodemath")) {
				insertUnicode(UnicodeDialog.UNICODE_MATH);
			} else if (command.equals("insertunicodedraw")) {
				insertUnicode(UnicodeDialog.UNICODE_DRAW);
			} else if (command.equals("insertunicodeding")) {
				insertUnicode(UnicodeDialog.UNICODE_DING);
			} else if (command.equals("insertunicodesigs")) {
				insertUnicode(UnicodeDialog.UNICODE_SIGS);
			} else if (command.equals("insertunicodespec")) {
				insertUnicode(UnicodeDialog.UNICODE_SPEC);
			} else if (command.equals("find")) {
				doSearch((String) null, (String) null, false,
						lastSearchCaseSetting, lastSearchTopSetting);
			} else if (command.equals("findagain")) {
				doSearch(lastSearchFindTerm, (String) null, false,
						lastSearchCaseSetting, false);
			} else if (command.equals("replace")) {
				doSearch((String) null, (String) null, true,
						lastSearchCaseSetting, lastSearchTopSetting);
			} else if (command.equals("wordcount")) {
				processWordCount();
			} else if (command.equals("exit")) {
				this.dispose();
			} else if (command.equals("helpabout")) {
				SimpleInfoDialog sidAbout = new SimpleInfoDialog(
						this.getFrame(),
						"About",
						true,
						"TerpWord Version 4.0 - Derived from Ekit (c)2000-2004 Howard Kistler",
						SimpleInfoDialog.INFO);
			} else if (command.equals("showhelp")) {

				File temp = new File("getLocalDirectory.txt");
				File location = new File(temp.getAbsoluteFile().getParent()); //local
																			  // directory
																			  // path
				String helpfile = new String("file:///" + location
						+ "//helpDoc//terpHelp.html");
				helpfile = helpfile.replace('\\', '/');
				HelpBrowser help = new HelpBrowser(helpfile);
			}
		} catch (IOException ioe) {
			logException("IOException in actionPerformed method", ioe);
			SimpleInfoDialog sidAbout = new SimpleInfoDialog(this.getFrame(),
					"Error", true, "IO Exception occurred.",
					SimpleInfoDialog.ERROR);
		} catch (BadLocationException ble) {
			logException("BadLocationException in actionPerformed method", ble);
			SimpleInfoDialog sidAbout = new SimpleInfoDialog(this.getFrame(),
					"Error", true, "Bad Location Exception occurred.",
					SimpleInfoDialog.ERROR);
		} catch (NumberFormatException nfe) {
			logException("NumberFormatException in actionPerformed method", nfe);
			SimpleInfoDialog sidAbout = new SimpleInfoDialog(this.getFrame(),
					"Error", true, "Number Format Exception occured.",
					SimpleInfoDialog.ERROR);
		} catch (RuntimeException re) {
			logException("RuntimeException in actionPerformed method", re);
			SimpleInfoDialog sidAbout = new SimpleInfoDialog(this.getFrame(),
					"Error", true, "Runtime Exception occured.",
					SimpleInfoDialog.ERROR);
		}
	}

	/* KeyListener methods */
	/**
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	public void keyTyped(KeyEvent ke) {
		Element elem;
		String selectedText;
		int pos = this.getCaretPosition();
		int repos = -1;
		if (ke.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
			try {
				if (pos > 0) {
					if ((selectedText = jtpMain.getSelectedText()) != null) {
						htmlUtilities.delete();
						return;
					} else {
						int sOffset = htmlDoc.getParagraphElement(pos)
								.getStartOffset();
						if (sOffset == jtpMain.getSelectionStart()) {
							boolean content = true;
							if (htmlUtilities.checkParentsTag(HTML.Tag.LI)) {
								elem = htmlUtilities.getListItemParent();
								content = false;
								int so = elem.getStartOffset();
								int eo = elem.getEndOffset();
								if (so + 1 < eo) {
									char[] temp = jtpMain.getText(so, eo - so)
											.toCharArray();
									for (int i = 0; i < temp.length; i++) {
										if (!(new Character(temp[i]))
												.isWhitespace(temp[i])) {
											content = true;
										}
									}
								}
								if (!content) {
									Element listElement = elem
											.getParentElement();
									htmlUtilities.removeTag(elem, true);
									this.setCaretPosition(sOffset - 1);
									return;
								} else {
									jtpMain.setCaretPosition(jtpMain
											.getCaretPosition() - 1);
									jtpMain.moveCaretPosition(jtpMain
											.getCaretPosition() - 2);
									jtpMain.replaceSelection("");
									return;
								}
							} else if (htmlUtilities
									.checkParentsTag(HTML.Tag.TABLE)) {
								jtpMain.setCaretPosition(jtpMain
										.getCaretPosition() - 1);
								ke.consume();
								return;
							}
						}
						jtpMain.replaceSelection("");
						return;
					}
				}
			} catch (BadLocationException ble) {
				logException("BadLocationException in keyTyped method", ble);
				SimpleInfoDialog sidAbout = new SimpleInfoDialog(this
						.getFrame(), "Error", true,
						"Bad Location Exception occurred.",
						SimpleInfoDialog.ERROR);
			} catch (IOException ioe) {
				logException("IOException in keyTyped method", ioe);
				SimpleInfoDialog sidAbout = new SimpleInfoDialog(this
						.getFrame(), "Error", true, "IO Exception occurred.",
						SimpleInfoDialog.ERROR);
			}
		} else if (ke.getKeyChar() == KeyEvent.VK_ENTER) {
			try {
				if (htmlUtilities.checkParentsTag(HTML.Tag.UL) == true
						| htmlUtilities.checkParentsTag(HTML.Tag.OL) == true) {
					elem = htmlUtilities.getListItemParent();
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
						manageListElement(elem);
					} else {
						if (this.getCaretPosition() + 1 == elem.getEndOffset()) {
							insertListStyle(elem);
							this.setCaretPosition(pos - repos);
						} else {
							int caret = this.getCaretPosition();
							String tempString = this.getTextPane().getText(
									caret, eo - caret);
							this.getTextPane().select(caret, eo - 1);
							this.getTextPane().replaceSelection("");
							htmlUtilities.insertListElement(tempString);
							Element newLi = htmlUtilities.getListItemParent();
							this.setCaretPosition(newLi.getEndOffset() - 1);
						}
					}
				}
			} catch (BadLocationException ble) {
				logException("BadLocationException in keyTyped method", ble);
				SimpleInfoDialog sidAbout = new SimpleInfoDialog(this
						.getFrame(), "Error", true,
						"Bad Location Exception occurred.",
						SimpleInfoDialog.ERROR);
			} catch (IOException ioe) {
				logException("IOException in keyTyped method", ioe);
				SimpleInfoDialog sidAbout = new SimpleInfoDialog(this
						.getFrame(), "Error", true, "IO Exception occurred.",
						SimpleInfoDialog.ERROR);
			}
		}
	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}

	/* FocusListener methods */
	public void focusGained(FocusEvent fe) {
		if (fe.getSource() == jtpMain) {
			setFormattersActive(true);
		} else if (fe.getSource() == jtpSource) {
			setFormattersActive(false);
		}
	}

	public void focusLost(FocusEvent fe) {
	}

	/* DocumentListener methods */
	public void changedUpdate(DocumentEvent de) {
		handleDocumentChange(de);
	}

	public void insertUpdate(DocumentEvent de) {
		handleDocumentChange(de);
	}

	public void removeUpdate(DocumentEvent de) {
		handleDocumentChange(de);
	}

	public void handleDocumentChange(DocumentEvent de) {
		if (!exclusiveEdit) {
			if (isSourceWindowActive()) {
				if (de.getDocument() instanceof HTMLDocument
						|| de.getDocument() instanceof ExtendedHTMLDocument) {
					jtpSource.getDocument().removeDocumentListener(this);
					jtpSource.setText(jtpMain.getText());
					jtpSource.getDocument().addDocumentListener(this);
				} else if (de.getDocument() instanceof PlainDocument
						|| de.getDocument() instanceof DefaultStyledDocument) {
					jtpMain.getDocument().removeDocumentListener(this);
					jtpMain.setText(jtpSource.getText());
					jtpMain.getDocument().addDocumentListener(this);
				}
			}
		}
	}

	/**
	 * Method for setting a document as the current document for the text pane
	 * and re-registering the controls and settings for it
	 * 
	 * @param htmlDoc -
	 *            The document to be placed and registered in the text pane.
	 */
	public void registerDocument(ExtendedHTMLDocument htmlDoc) {
		jtpMain.setDocument(htmlDoc);
		jtpMain.getDocument().addUndoableEditListener(
				new CustomUndoableEditListener());
		jtpMain.getDocument().addDocumentListener(this);
		jtpMain.setCaretPosition(0);
		purgeUndos();
	}

	/**
	 * Method for inserting list elements
	 * 
	 * @param element -
	 *            The element to be inserted.
	 * @throws BadLocationException -
	 *             if the element cannot be inserted at the given location.
	 * @throws IOException -
	 *             if an IO error occurs.
	 */
	public void insertListStyle(Element element) throws BadLocationException,
			IOException {
		if (element.getParentElement().getName() == "ol") {
			actionListOrdered.actionPerformed(new ActionEvent(new Object(), 0,
					"newListPoint"));
		} else {
			actionListUnordered.actionPerformed(new ActionEvent(new Object(),
					0, "newListPoint"));
		}
	}

	/**
	 * Method for inserting an HTML Table
	 * 
	 * @param attribs -
	 *            A Hashtable of the attributes.
	 * @param fieldNames -
	 *            The names of the table fields.
	 * @param fieldTypes -
	 *            The types of the table fields.
	 * @throws IOException -
	 *             if an IO error occurs.
	 * @throws BadLocationException -
	 *             if an insertion cannot be done at a given location.
	 * @throws RuntimeException -
	 *             if a runtime error occurs.
	 * @throws NumberFormatException -
	 *             if a number format is invalid.
	 */
	private void insertTable(Hashtable attribs, String[] fieldNames,
			String[] fieldTypes) throws IOException, BadLocationException,
			RuntimeException, NumberFormatException {
		int caretPos = jtpMain.getCaretPosition();
		StringBuffer compositeElement = new StringBuffer("<TABLE");
		if (attribs != null && attribs.size() > 0) {
			Enumeration attribEntries = attribs.keys();
			while (attribEntries.hasMoreElements()) {
				Object entryKey = attribEntries.nextElement();
				Object entryValue = attribs.get(entryKey);
				if (entryValue != null && entryValue != "") {
					compositeElement.append(" " + entryKey + "=" + '"'
							+ entryValue + '"');
				}
			}
		}
		int rows = 0;
		int cols = 0;
		if (fieldNames != null && fieldNames.length > 0) {
			PropertiesDialog propertiesDialog = new PropertiesDialog(this
					.getFrame(), fieldNames, fieldTypes,
					"Properties For Form Element", true);
			propertiesDialog.setVisible(true);
			String decision = propertiesDialog.getDecisionValue();
			if (decision.equals("Cancel")) {
				propertiesDialog.dispose();
				propertiesDialog = null;
				return;
			} else {
				for (int iter = 0; iter < fieldNames.length; iter++) {
					String fieldName = fieldNames[iter];
					String propValue = propertiesDialog
							.getFieldValue(fieldName);
					if (propValue != null && propValue != ""
							&& propValue.length() > 0) {
						if (fieldName.equals("rows")) {
							rows = Integer.parseInt(propValue);
						} else if (fieldName.equals("cols")) {
							cols = Integer.parseInt(propValue);
						} else {
							compositeElement.append(" " + fieldName + "=" + '"'
									+ propValue + '"');
						}
					}
				}
			}
			propertiesDialog.dispose();
			propertiesDialog = null;
		}
		compositeElement.append(">");
		for (int i = 0; i < rows; i++) {
			compositeElement.append("<TR>");
			for (int j = 0; j < cols; j++) {
				compositeElement.append("<TD></TD>");
			}
			compositeElement.append("</TR>");
		}
		compositeElement.append("</TABLE><P>&nbsp;<P>");
		htmlKit.insertHTML(htmlDoc, caretPos, compositeElement.toString(), 0,
				0, HTML.Tag.TABLE);
		jtpMain.setCaretPosition(caretPos + 1);
		refreshOnUpdate();
	}

	/**
	 * Method for inserting a row into an HTML Table
	 *  
	 */
	private void insertTableRow() {
		int caretPos = jtpMain.getCaretPosition();
		Element element = htmlDoc.getCharacterElement(jtpMain
				.getCaretPosition());
		Element elementParent = element.getParentElement();
		int startPoint = -1;
		int columnCount = -1;
		while (elementParent != null && !elementParent.getName().equals("body")) {
			if (elementParent.getName().equals("tr")) {
				startPoint = elementParent.getStartOffset();
				columnCount = elementParent.getElementCount();
				break;
			} else {
				elementParent = elementParent.getParentElement();
			}
		}
		if (startPoint > -1 && columnCount > -1) {
			jtpMain.setCaretPosition(startPoint);
			StringBuffer sRow = new StringBuffer();
			sRow.append("<TR>");
			for (int i = 0; i < columnCount; i++) {
				sRow.append("<TD></TD>");
			}
			sRow.append("</TR>");
			ActionEvent actionEvent = new ActionEvent(jtpMain, 0,
					"insertTableRow");
			new HTMLEditorKit.InsertHTMLTextAction("insertTableRow", sRow
					.toString(), HTML.Tag.TABLE, HTML.Tag.TR)
					.actionPerformed(actionEvent);
			refreshOnUpdate();
			jtpMain.setCaretPosition(caretPos);
		}
	}

	/**
	 * Method for inserting a column into an HTML Table
	 *  
	 */
	private void insertTableColumn() {
		int caretPos = jtpMain.getCaretPosition();
		Element element = htmlDoc.getCharacterElement(jtpMain
				.getCaretPosition());
		Element elementParent = element.getParentElement();
		int startPoint = -1;
		int rowCount = -1;
		int cellOffset = 0;
		while (elementParent != null && !elementParent.getName().equals("body")) {
			if (elementParent.getName().equals("table")) {
				startPoint = elementParent.getStartOffset();
				rowCount = elementParent.getElementCount();
				break;
			} else if (elementParent.getName().equals("tr")) {
				int rowStart = elementParent.getStartOffset();
				int rowCells = elementParent.getElementCount();
				for (int i = 0; i < rowCells; i++) {
					Element currentCell = elementParent.getElement(i);
					if (jtpMain.getCaretPosition() >= currentCell
							.getStartOffset()
							&& jtpMain.getCaretPosition() <= currentCell
									.getEndOffset()) {
						cellOffset = i;
					}
				}
				elementParent = elementParent.getParentElement();
			} else {
				elementParent = elementParent.getParentElement();
			}
		}
		if (startPoint > -1 && rowCount > -1) {
			jtpMain.setCaretPosition(startPoint);
			String sCell = "<TD></TD>";
			ActionEvent actionEvent = new ActionEvent(jtpMain, 0,
					"insertTableCell");
			for (int i = 0; i < rowCount; i++) {
				Element row = elementParent.getElement(i);
				Element whichCell = row.getElement(cellOffset);
				jtpMain.setCaretPosition(whichCell.getStartOffset());
				new HTMLEditorKit.InsertHTMLTextAction("insertTableCell",
						sCell, HTML.Tag.TR, HTML.Tag.TD, HTML.Tag.TH,
						HTML.Tag.TD).actionPerformed(actionEvent);
			}
			refreshOnUpdate();
			jtpMain.setCaretPosition(caretPos);
		}
	}

	/**
	 * Method for inserting a cell into an HTML Table
	 *  
	 */
	private void insertTableCell() {
		String sCell = "<TD></TD>";
		ActionEvent actionEvent = new ActionEvent(jtpMain, 0, "insertTableCell");
		new HTMLEditorKit.InsertHTMLTextAction("insertTableCell", sCell,
				HTML.Tag.TR, HTML.Tag.TD, HTML.Tag.TH, HTML.Tag.TD)
				.actionPerformed(actionEvent);
		refreshOnUpdate();
	}

	/**
	 * Method for deleting a row from an HTML Table
	 * 
	 * @throws BadLocationException -
	 *             if the given table row cannot be deleted.
	 */
	private void deleteTableRow() throws BadLocationException {
		int caretPos = jtpMain.getCaretPosition();
		Element element = htmlDoc.getCharacterElement(jtpMain
				.getCaretPosition());
		Element elementParent = element.getParentElement();
		int startPoint = -1;
		int endPoint = -1;
		while (elementParent != null && !elementParent.getName().equals("body")) {
			if (elementParent.getName().equals("tr")) {
				startPoint = elementParent.getStartOffset();
				endPoint = elementParent.getEndOffset();
				break;
			} else {
				elementParent = elementParent.getParentElement();
			}
		}
		if (startPoint > -1 && endPoint > startPoint) {
			htmlDoc.remove(startPoint, endPoint - startPoint);
			jtpMain.setDocument(htmlDoc);
			registerDocument(htmlDoc);
			refreshOnUpdate();
			if (caretPos >= htmlDoc.getLength()) {
				caretPos = htmlDoc.getLength() - 1;
			}
			jtpMain.setCaretPosition(caretPos);
		}
	}

	/**
	 * Method for deleting a column from an HTML Table
	 * 
	 * @throws BadLocationException -
	 *             if the given table column cannot be deleted.
	 */
	private void deleteTableColumn() throws BadLocationException {
		int caretPos = jtpMain.getCaretPosition();
		Element element = htmlDoc.getCharacterElement(jtpMain
				.getCaretPosition());
		Element elementParent = element.getParentElement();
		Element elementCell = (Element) null;
		Element elementRow = (Element) null;
		Element elementTable = (Element) null;
		// Locate the table, row, and cell location of the cursor
		while (elementParent != null && !elementParent.getName().equals("body")) {
			if (elementParent.getName().equals("td")) {
				elementCell = elementParent;
			} else if (elementParent.getName().equals("tr")) {
				elementRow = elementParent;
			} else if (elementParent.getName().equals("table")) {
				elementTable = elementParent;
			}
			elementParent = elementParent.getParentElement();
		}
		int whichColumn = -1;
		if (elementCell != null && elementRow != null && elementTable != null) {
			// Find the column the cursor is in
			for (int i = 0; i < elementRow.getElementCount(); i++) {
				if (elementCell == elementRow.getElement(i)) {
					whichColumn = i;
				}
			}
			if (whichColumn > -1) {
				// Iterate through the table rows, deleting cells from the
				// indicated column
				for (int i = 0; i < elementTable.getElementCount(); i++) {
					elementRow = elementTable.getElement(i);
					elementCell = (elementRow.getElementCount() > whichColumn ? elementRow
							.getElement(whichColumn)
							: elementRow.getElement(elementRow
									.getElementCount() - 1));
					int columnCellStart = elementCell.getStartOffset();
					int columnCellEnd = elementCell.getEndOffset();
					htmlDoc.remove(columnCellStart, columnCellEnd
							- columnCellStart);
				}
				jtpMain.setDocument(htmlDoc);
				registerDocument(htmlDoc);
				refreshOnUpdate();
				if (caretPos >= htmlDoc.getLength()) {
					caretPos = htmlDoc.getLength() - 1;
				}
				jtpMain.setCaretPosition(caretPos);
			}
		}
	}

	/**
	 * Method for inserting a break (BR) element
	 * 
	 * @throws IOException -
	 *             if an IO error occurs.
	 * @throws BadLocationException -
	 *             if a break cannot be inserted at current location.
	 * @throws RuntimeException -
	 *             if a runtime error occurs.
	 */
	private void insertBreak() throws IOException, BadLocationException,
			RuntimeException {
		int caretPos = jtpMain.getCaretPosition();
		htmlKit.insertHTML(htmlDoc, caretPos, "<BR>", 0, 0, HTML.Tag.BR);
		jtpMain.setCaretPosition(caretPos + 1);
	}

	/**
	 * Method for opening the Unicode dialog
	 * 
	 * @param index -
	 *            starting index used in UnicodeDialog initialization.
	 * @throws IOException -
	 *             if an IO error occurs.
	 * @throws BadLocationException -
	 *             if a BR element cannot be inserted at current location.
	 * @throws RuntimeException -
	 *             if a runtime error occurs.
	 */
	private void insertUnicode(int index) throws IOException,
			BadLocationException, RuntimeException {
		UnicodeDialog unicodeInput = new UnicodeDialog(this,
				"Unicode Character Insert", false, index);
	}

	/**
	 * Method for inserting Unicode characters via the UnicodeDialog class
	 * 
	 * @param sChar -
	 *            character to be inserted.
	 * @throws IOException -
	 *             if an IO error occurs.
	 * @throws BadLocationException -
	 *             if the character cannot be inserted at current location.
	 * @throws RuntimeException -
	 *             if a runtime error occurs.
	 */
	public void insertUnicodeChar(String sChar) throws IOException,
			BadLocationException, RuntimeException {
		int caretPos = jtpMain.getCaretPosition();
		if (sChar != null) {
			htmlDoc.insertString(caretPos, sChar, jtpMain.getInputAttributes());
			jtpMain.setCaretPosition(caretPos + 1);
		}
	}

	/**
	 * Method for inserting a non-breaking space (&nbsp;)
	 * 
	 * @throws IOException -
	 *             if an IO error occurs.
	 * @throws BadLocationException -
	 *             if a non-breaking space cannot be inserted at current
	 *             location.
	 * @throws RuntimeException -
	 *             if a runtime error occurs.
	 */
	private void insertNonbreakingSpace() throws IOException,
			BadLocationException, RuntimeException {
		int caretPos = jtpMain.getCaretPosition();
		htmlDoc.insertString(caretPos, "\240", jtpMain.getInputAttributes());
		jtpMain.setCaretPosition(caretPos + 1);
	}

	/**
	 * Method that handles initial list insertion and deletion
	 * 
	 * @param element -
	 *            the list element to be modified (inserted or removed).
	 */
	public void manageListElement(Element element) {
		Element h = htmlUtilities.getListItemParent();
		Element listElement = h.getParentElement();
		if (h != null) {
			htmlUtilities.removeTag(h, true);
		}
	}

	/**
	 * Method to initiate a find/replace operation
	 * 
	 * @param searchFindTerm -
	 *            the string for which the search will be carried out.
	 * @param searchReplaceTerm -
	 *            the string that will replace found terms.
	 * @param bIsFindReplace -
	 *            [boolean] Flags whether a Find/Replace action is desired.
	 * @param bCaseSensitive -
	 *            [boolean] Flags whether a case sensitive search is desired.
	 * @param bStartAtTop -
	 *            [boolean] Flags whether or not to starts at the top of the
	 *            document.
	 */
	private void doSearch(String searchFindTerm, String searchReplaceTerm,
			boolean bIsFindReplace, boolean bCaseSensitive, boolean bStartAtTop) {
		boolean bReplaceAll = false;
		JTextComponent searchPane = (JTextComponent) jtpMain;
		if (jspSource.isShowing() || jtpSource.hasFocus()) {
			searchPane = (JTextComponent) jtpSource;
		}
		if (searchFindTerm == null
				|| (bIsFindReplace && searchReplaceTerm == null)) {
			SearchDialog sdSearchInput = new SearchDialog(this.getFrame(),
					"Search", true, bIsFindReplace, bCaseSensitive, bStartAtTop);
			searchFindTerm = sdSearchInput.getFindTerm();
			searchReplaceTerm = sdSearchInput.getReplaceTerm();
			bCaseSensitive = sdSearchInput.getCaseSensitive();
			bStartAtTop = sdSearchInput.getStartAtTop();
			bReplaceAll = sdSearchInput.getReplaceAll();
		}
		if (searchFindTerm != null
				&& (!bIsFindReplace || searchReplaceTerm != null)) {
			if (bReplaceAll) {
				int results = findText(searchFindTerm, searchReplaceTerm,
						bCaseSensitive, 0);
				int findOffset = 0;
				if (results > -1) {
					while (results > -1) {
						findOffset = findOffset + searchReplaceTerm.length();
						results = findText(searchFindTerm, searchReplaceTerm,
								bCaseSensitive, findOffset);
					}
				} else {
					SimpleInfoDialog sidWarn = new SimpleInfoDialog(this
							.getFrame(), "", true, "No occurrences found"
							+ ":\n" + searchFindTerm, SimpleInfoDialog.WARNING);
				}
			} else {
				int results = findText(searchFindTerm, searchReplaceTerm,
						bCaseSensitive, (bStartAtTop ? 0 : searchPane
								.getCaretPosition()));
				if (results == -1) {
					SimpleInfoDialog sidWarn = new SimpleInfoDialog(this
							.getFrame(), "", true, "No match found" + ":\n"
							+ searchFindTerm, SimpleInfoDialog.WARNING);
				}
			}
			lastSearchFindTerm = new String(searchFindTerm);
			if (searchReplaceTerm != null) {
				lastSearchReplaceTerm = new String(searchReplaceTerm);
			} else {
				lastSearchReplaceTerm = (String) null;
			}
			lastSearchCaseSetting = bCaseSensitive;
			lastSearchTopSetting = bStartAtTop;
		}
	}

	/**
	 * Method for finding (and optionally replacing) a string in the text
	 * 
	 * @param findTerm -
	 *            The text to be found.
	 * @param replaceTerm -
	 *            The text to be replaced.
	 * @param bCaseSenstive -
	 *            [boolean] Specifies whether or not to perform a case-sensitive
	 *            search.
	 * @param iOffset -
	 *            Initial offset within the document.
	 * @return location within the document where text was found.
	 */
	private int findText(String findTerm, String replaceTerm,
			boolean bCaseSenstive, int iOffset) {
		JTextComponent jtpFindSource;
		if (isSourceWindowActive() || jtpSource.hasFocus()) {
			jtpFindSource = (JTextComponent) jtpSource;
		} else {
			jtpFindSource = (JTextComponent) jtpMain;
		}
		int searchPlace = -1;
		try {
			Document baseDocument = jtpFindSource.getDocument();
			searchPlace = (bCaseSenstive ? baseDocument.getText(0,
					baseDocument.getLength()).indexOf(findTerm, iOffset)
					: baseDocument.getText(0, baseDocument.getLength())
							.toLowerCase().indexOf(findTerm.toLowerCase(),
									iOffset));
			if (searchPlace > -1) {
				if (replaceTerm != null) {
					AttributeSet attribs = null;
					if (baseDocument instanceof HTMLDocument) {
						Element element = ((HTMLDocument) baseDocument)
								.getCharacterElement(searchPlace);
						attribs = element.getAttributes();
					}
					baseDocument.remove(searchPlace, findTerm.length());
					baseDocument
							.insertString(searchPlace, replaceTerm, attribs);
					jtpFindSource.setCaretPosition(searchPlace
							+ replaceTerm.length());
					jtpFindSource.requestFocus();
					jtpFindSource.select(searchPlace, searchPlace
							+ replaceTerm.length());
				} else {
					jtpFindSource.setCaretPosition(searchPlace
							+ findTerm.length());
					jtpFindSource.requestFocus();
					jtpFindSource.select(searchPlace, searchPlace
							+ findTerm.length());
				}
			}
		} catch (BadLocationException ble) {
			logException("BadLocationException in actionPerformed method", ble);
			SimpleInfoDialog sidAbout = new SimpleInfoDialog(this.getFrame(),
					"Error", true, "Bad Location Exception occurred.",
					SimpleInfoDialog.ERROR);
		}
		return searchPlace;
	}

	/**
	 * Method for inserting an image from a file
	 * 
	 * @param whatImage
	 * @throws IOException -
	 *             if an IO error occurs.
	 * @throws BadLocationException -
	 *             if the image cannot be inserted here.
	 * @throws RuntimeException -
	 *             if a runtime error occurs.
	 */
	private void insertLocalImage(File whatImage) throws IOException,
			BadLocationException, RuntimeException {
		if (whatImage == null) {
			whatImage = getImageFromChooser(imageChooserStartDir, extsIMG,
					"Images");
		}
		if (whatImage != null) {
			//imageChooserStartDir = whatImage.getParent().toString();
			int caretPos = jtpMain.getCaretPosition();
			htmlKit.insertHTML(htmlDoc, caretPos, "<IMG SRC=\"" + whatImage
					+ "\">", 0, 0, HTML.Tag.IMG);
			jtpMain.setCaretPosition(caretPos + 1);
			refreshOnUpdate();
		}
	}

	/**
	 * Method for inserting an image
	 * 
	 * @return - the path of the selected image file.
	 */
	public String insertFile() {
		String selectedFile = null;
		if (ServletURL != null) {
			try {
				URL theServlet = new URL(ServletURL + "?GetFiles="
						+ TreePilotSystemID + "&FileExtensions="
						+ TreePilotProperties.getString("ValidFileExtensions"));
				URLConnection conn = theServlet.openConnection();
				ObjectInputStream in = new ObjectInputStream(conn
						.getInputStream());
				String[] fileList = (String[]) in.readObject();
				FileDialog fileDialog = new FileDialog(this, ImageDir
						+ TreePilotSystemID, fileList, "File Chooser", true);
				selectedFile = fileDialog.getSelectedFile();
				fileDialog.dispose();
				in.close();
			} catch (MalformedURLException mue) {
				System.err.println("MalFormedURLException during insertFile "
						+ mue);
			} catch (IOException ie) {
				System.err.println("IOException during insertFile " + ie);
			} catch (ClassNotFoundException cnfe) {
				System.err.println("ClassNotFoundException during insertFile"
						+ cnfe);
			}
		}
		return selectedFile;
	}

	/**
	 * Method for saving text as a complete HTML document
	 * 
	 * @param doc -
	 *            The current HTML document.
	 * @param whatFile -
	 *            The file that will be saved.
	 * @throws IOException -
	 *             if an IO error occurs.
	 * @throws BadLocationException -
	 *             if a location error occurs.
	 */
	private void writeOut(HTMLDocument doc, File whatFile) throws IOException,
			BadLocationException {
		if (whatFile == null) {
			whatFile = getFileFromChooser(".", JFileChooser.SAVE_DIALOG,
					extsHTML, "HTML Files");
		}
		if (whatFile != null) {
			FileWriter fw = new FileWriter(whatFile);
			String str = ".";
			int loc = -1;
			loc = (whatFile.getName()).indexOf(str);

			if (loc == -1) {
				whatFile = new File((whatFile.getPath()).concat(".html"));
				fw = new FileWriter(whatFile);
				htmlKit.write(fw, doc, 0, doc.getLength());
			} else {
				String file = new String();
				String ext = (whatFile.getName()).substring(loc + 1);
				if (ext.compareTo("txt") == 0) {
					//convert html to txt
					TxtConverter t = new TxtConverter();
					try {
						String tmp = new String();

						String[] result = this.jtpMain.getText().split(
								System.getProperty("line.separator"));
						for (int x = 0; x < result.length; x++) {
							tmp = t.convertHtmlToTxt(result[x]);
							file = file.concat(tmp);
							if (tmp.compareTo("") != 0)
								file = file.concat(System
										.getProperty("line.separator"));
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
					fw.write(file);
				} else {
					htmlKit.write(fw, doc, 0, doc.getLength());
				}
			}
			fw.flush();
			fw.close();
			currentFile = whatFile;
			updateTitle();
		}
		refreshOnUpdate();
	}

	/**
	 * Method for saving text as an RTF document
	 * 
	 * @param doc -
	 *            The current document.
	 * @return the File object that is being saved.
	 * @throws IOException -
	 *             if an IO error occurs.
	 * @throws BadLocationException -
	 *             if a location error occurs.
	 */
	private File writeOutRTF(StyledDocument doc) throws IOException,
			BadLocationException {
		File whatFile = getFileFromChooser(".", JFileChooser.SAVE_DIALOG,
				extsRTF, "RTF Files");
		if (whatFile != null) {
			String str = ".";
			int loc = (whatFile.getName()).indexOf(str);
			if (loc == -1) {
				whatFile = new File((whatFile.getPath()).concat(".rtf"));
			}
			FileOutputStream fos = new FileOutputStream(whatFile);
			RTFEditorKit rtfKit = new RTFEditorKit();
			rtfKit.write(fos, doc, 0, doc.getLength());
			fos.flush();
			fos.close();
		}
		refreshOnUpdate();
		return whatFile;
	}

	/**
	 * Method to invoke loading HTML into the app
	 * 
	 * @param whatFile -
	 *            the HTML document that will be loaded.
	 * @throws IOException -
	 *             if an IO error occurs.
	 * @throws BadLocationException -
	 *             if a location error occurs.
	 */
	private void openDocument(File whatFile) throws IOException,
			BadLocationException {
		if (whatFile == null) {
			whatFile = getFileFromChooser(".", JFileChooser.OPEN_DIALOG,
					extsHTML, "HTML Files");
		}
		if (whatFile != null) {
			try {
				loadDocument(whatFile, null);
			} catch (ChangedCharSetException ccse) {
				String charsetType = ccse.getCharSetSpec().toLowerCase();
				int pos = charsetType.indexOf("charset");
				if (pos == -1) {
					throw ccse;
				}
				while (pos < charsetType.length()
						&& charsetType.charAt(pos) != '=') {
					pos++;
				}
				pos++; // Places file cursor past the equals sign (=)
				String whatEncoding = charsetType.substring(pos).trim();
				loadDocument(whatFile, whatEncoding);
			}
		}
		refreshOnUpdate();
	}

	/**
	 * Method for loading HTML document
	 * 
	 * @param whatFile -
	 *            the HTML document that will be loaded.
	 * @throws IOException -
	 *             if an IO error occurs.
	 * @throws BadLocationException -
	 *             if a location error occurs.
	 */
	public void loadDocument(File whatFile) throws IOException,
			BadLocationException {
		try {
			loadDocument(whatFile, null);
		} catch (ChangedCharSetException ccse) {
			String charsetType = ccse.getCharSetSpec().toLowerCase();
			int pos = charsetType.indexOf("charset");
			if (pos == -1) {
				throw ccse;
			}
			while (pos < charsetType.length() && charsetType.charAt(pos) != '=') {
				pos++;
			}
			pos++; // Places file cursor past the equals sign (=)
			String whatEncoding = charsetType.substring(pos).trim();
			loadDocument(whatFile, whatEncoding);
		}
		refreshOnUpdate();
	}

	/**
	 * Method for loading HTML document into the app, including document
	 * encoding setting
	 * 
	 * @param whatFile -
	 *            the HTML document that will be loaded.
	 * @param whatEncoding -
	 *            [string] the encoding of the file to be loaded.
	 * @throws IOException -
	 *             if an IO error occurs.
	 * @throws BadLocationException -
	 *             if a location error occurs.
	 */
	private void loadDocument(File whatFile, String whatEncoding)
			throws IOException, BadLocationException {
		CsdRtfConverter c = new CsdRtfConverter();
		TxtConverter t = new TxtConverter();
		Reader r = null;
		htmlDoc = (ExtendedHTMLDocument) (htmlKit.createDefaultDocument());
		htmlDoc.putProperty("source.ekit.docsource", whatFile.toString());
		try {
			if (whatEncoding == null) {
				r = new InputStreamReader(new FileInputStream(whatFile));
			} else {
				r = new InputStreamReader(new FileInputStream(whatFile),
						whatEncoding);
				htmlDoc
						.putProperty("IgnoreCharsetDirective",
								new Boolean(true));
			}


			String name = whatFile.getName();
			int loc = name.indexOf(".");
			if (loc != -1) {
				String file = new String();
				String ext = name.substring(loc + 1);
				if (ext.compareTo("rtf") == 0) {
					//read rtf and convert to html
					BufferedReader in = new BufferedReader(r);
					String line = new String();
					while ((line = in.readLine()) != null) {
						file = file.concat(line);
						file = file
								.concat(System.getProperty("line.separator"));
					}
					try {
						file = c.convertRtfToHtml(file);
					} catch (Exception e) {
						e.printStackTrace();
					}
					htmlKit.read(new StringReader(file), htmlDoc, 0);
				} else if (ext.compareTo("txt") == 0) {
					//read txt file and convert to html
					BufferedReader in = new BufferedReader(r);
					String line = new String();
					while ((line = in.readLine()) != null) {

						file = file.concat(line);
						file = file
								.concat(System.getProperty("line.separator"));
					}
					try {
						file = t.convertTxtToHtml(file);
					} catch (Exception e) {
						e.printStackTrace();
					}
					htmlKit.read(new StringReader(file), htmlDoc, 0);
				} else {
					htmlKit.read(r, htmlDoc, 0);
				}
			}

			r.close();
			registerDocument(htmlDoc);
			jtpSource.setText(jtpMain.getText());
			currentFile = whatFile;
			updateTitle();
		} finally {
			if (r != null) {
				r.close();
			}
		}
	}

	/**
	 * Method for obtaining a File for input/output using a JFileChooser dialog
	 * 
	 * @param startDir -
	 *            The initial starting directory.
	 * @param dialogType -
	 *            The type of dialog (input or output).
	 * @param exts -
	 *            The extension filter.
	 * @param desc -
	 *            The description of the file type.
	 * @return - The chosen file.
	 */
	private File getFileFromChooser(String startDir, int dialogType,
			String[] exts, String desc) {
		JFileChooser jfileDialog = new JFileChooser(startDir);
		jfileDialog.setDialogType(dialogType);
		jfileDialog.setFileFilter(new MutableFilter(exts, desc));
		int optionSelected = JFileChooser.CANCEL_OPTION;
		if (dialogType == JFileChooser.OPEN_DIALOG) {
			optionSelected = jfileDialog.showOpenDialog(this);
		} else if (dialogType == JFileChooser.SAVE_DIALOG) {
			optionSelected = jfileDialog.showSaveDialog(this);
		} else // default to an OPEN_DIALOG
		{
			optionSelected = jfileDialog.showOpenDialog(this);
		}
		if (optionSelected == JFileChooser.APPROVE_OPTION) {
			return jfileDialog.getSelectedFile();
		}
		return (File) null;
	}

	/**
	 * Method for obtaining an Image for input using a custom JFileChooser
	 * dialog
	 * 
	 * @param startDir -
	 *            The initial starting directory.
	 * @param exts -
	 *            The extension filter.
	 * @param desc -
	 *            The description of the file type.
	 * @return - The chosen image file.
	 */
	private File getImageFromChooser(String startDir, String[] exts, String desc) {
		ImageFileChooser jImageDialog = new ImageFileChooser(startDir);
		jImageDialog.setDialogType(JFileChooser.CUSTOM_DIALOG);
		jImageDialog.setFileFilter(new MutableFilter(exts, desc));
		jImageDialog.setDialogTitle("Select Image To Insert");
		int optionSelected = JFileChooser.CANCEL_OPTION;
		optionSelected = jImageDialog.showDialog(this, "Insert");
		if (optionSelected == JFileChooser.APPROVE_OPTION) {
			return jImageDialog.getSelectedFile();
		}
		return (File) null;
	}

	/**
	 * Method for describing the node hierarchy of the document
	 * 
	 * @param doc -
	 *            the document to be analyzed.
	 */
	private void describeDocument(StyledDocument doc) {
		Element[] elements = doc.getRootElements();
		for (int i = 0; i < elements.length; i++) {
			indent = indentStep;
			for (int j = 0; j < indent; j++) {
				System.out.print(" ");
			}
			System.out.print(elements[i]);
			traverseElement(elements[i]);
			System.out.println("");
		}
	}

	/**
	 * Traverses nodes for the describing method
	 * 
	 * @param element -
	 *            The element to be described.
	 */
	private void traverseElement(Element element) {
		indent += indentStep;
		for (int i = 0; i < element.getElementCount(); i++) {
			for (int j = 0; j < indent; j++) {
				System.out.print(" ");
			}
			System.out.print(element.getElement(i));
			traverseElement(element.getElement(i));
		}
		indent -= indentStep;
	}

	/**
	 * Method to locate a node element by name
	 * 
	 * @param doc -
	 *            The document to search through.
	 * @param elementName -
	 *            The name of the element to locate.
	 * @return - the found element.
	 */
	private Element locateElementInDocument(StyledDocument doc,
			String elementName) {
		Element[] elements = doc.getRootElements();
		for (int i = 0; i < elements.length; i++) {
			if (elements[i].getName().equalsIgnoreCase(elementName)) {
				return elements[i];
			} else {
				Element rtnElement = locateChildElementInDocument(elements[i],
						elementName);
				if (rtnElement != null) {
					return rtnElement;
				}
			}
		}
		return (Element) null;
	}

	/**
	 * Traverses nodes for the locating method
	 * 
	 * @param element -
	 *            The element to search in.
	 * @param elementName -
	 *            The name of the child element to find.
	 * @return - The located child element.
	 */
	private Element locateChildElementInDocument(Element element,
			String elementName) {
		for (int i = 0; i < element.getElementCount(); i++) {
			if (element.getElement(i).getName().equalsIgnoreCase(elementName)) {
				return element.getElement(i);
			}
		}
		return (Element) null;
	}

	/**
	 * Convenience method for obtaining the WYSIWYG JTextPane
	 * 
	 * @return the primary JTextPane.
	 */
	public JTextPane getTextPane() {
		return jtpMain;
	}

	/**
	 * Convenience method for obtaining the Source JTextPane
	 * 
	 * @return the source JTextPane.
	 */
	public JTextPane getSourcePane() {
		return jtpSource;
	}

	/**
	 * Convenience method for obtaining the application as a Frame
	 * 
	 * @return the application Frame.
	 */
	public Frame getFrame() {
		return frameHandler;
	}

	/**
	 * Convenience method for setting the parent Frame
	 * 
	 * @param parentFrame -
	 *            the frame to be set as the parent Frame.
	 */
	public void setFrame(Frame parentFrame) {
		frameHandler = parentFrame;
	}

	/**
	 * Convenience method for obtaining the pre-generated menu bar
	 * 
	 * @return - the primary menubar.
	 */
	public JMenuBar getMenuBar() {
		return jMenuBar;
	}

	/**
	 * Convenience method for obtaining a custom menu bar
	 * 
	 * @param vcMenus -
	 *            a vector of menu elements.
	 * @return the custom menu bar.
	 */
	public JMenuBar getCustomMenuBar(Vector vcMenus) {
		jMenuBar = new JMenuBar();
		for (int i = 0; i < vcMenus.size(); i++) {
			String menuToAdd = ((String) (vcMenus.elementAt(i))).toLowerCase();
			if (htMenus.containsKey(menuToAdd)) {
				jMenuBar.add((JMenu) (htMenus.get(menuToAdd)));
			}
		}
		return jMenuBar;
	}

	/**
	 * Convenience method for obtaining the pre-generated toolbar
	 * 
	 * @param isShowing -
	 *            [boolean] chooses whether to display the toolbar or hide it.
	 * @return the toolbar.
	 */
	public JToolBar getToolBar(boolean isShowing) {
		if (jToolBar != null) {
			jcbmiViewToolbar.setState(isShowing);
			return jToolBar;
		}
		return (JToolBar) null;
	}

	/**
	 * Convenience method for obtaining the pre-generated main toolbar
	 * 
	 * @param isShowing -
	 *            [boolean] chooses whether to display the main toolbar or hide
	 *            it.
	 * @return the toolbar.
	 */
	public JToolBar getToolBarMain(boolean isShowing) {
		if (jToolBarMain != null) {
			jcbmiViewToolbarMain.setState(isShowing);
			return jToolBarMain;
		}
		return (JToolBar) null;
	}

	/**
	 * Convenience method for obtaining the pre-generated main toolbar
	 * 
	 * @param isShowing -
	 *            [boolean] chooses whether to display the main toolbar or hide
	 *            it.
	 * @return the toolbar.
	 */
	public JToolBar getToolBarStyles(boolean isShowing) {
		if (jToolBarStyles != null) {
			jcbmiViewToolbarStyles.setState(isShowing);
			return jToolBarStyles;
		}
		return (JToolBar) null;
	}

	/**
	 * Convenience method for obtaining a custom toolbar
	 * 
	 * @param whichToolBar -
	 *            the index of the desired toolbar.
	 * @param vcTools -
	 *            a vector of toolbar elements.
	 * @param isShowing -
	 *            [boolean] chooses whether to display the main toolbar or hide
	 *            it.
	 * @return the toolbar.
	 */
	public JToolBar customizeToolBar(int whichToolBar, Vector vcTools,
			boolean isShowing) {
		JToolBar jToolBarX = new JToolBar(JToolBar.HORIZONTAL);
		jToolBarX.setFloatable(false);
		for (int i = 0; i < vcTools.size(); i++) {
			String toolToAdd = ((String) (vcTools.elementAt(i))).toLowerCase();
			if (toolToAdd.equals(KEY_TOOL_SEP)) {
				jToolBarX.add(new JToolBar.Separator());
			} else if (htTools.containsKey(toolToAdd)) {
				if (htTools.get(toolToAdd) instanceof JButtonNoFocus) {
					jToolBarX.add((JButtonNoFocus) (htTools.get(toolToAdd)));
				} else if (htTools.get(toolToAdd) instanceof JToggleButtonNoFocus) {
					jToolBarX.add((JToggleButtonNoFocus) (htTools
							.get(toolToAdd)));
				} else if (htTools.get(toolToAdd) instanceof JComboBoxNoFocus) {
					jToolBarX.add((JComboBoxNoFocus) (htTools.get(toolToAdd)));
				} else {
					jToolBarX.add((JComponent) (htTools.get(toolToAdd)));
				}
			}
		}
		if (whichToolBar == TOOLBAR_MAIN) {
			jToolBarMain = jToolBarX;
			jToolBarMain.setVisible(isShowing);
			jcbmiViewToolbarMain.setSelected(isShowing);
			return jToolBarMain;
		} else if (whichToolBar == TOOLBAR_FORMAT) {
			jToolBarFormat = jToolBarX;
			jToolBarFormat.setVisible(isShowing);
			jcbmiViewToolbarFormat.setSelected(isShowing);
			return jToolBarFormat;
		} else if (whichToolBar == TOOLBAR_STYLES) {
			jToolBarStyles = jToolBarX;
			jToolBarStyles.setVisible(isShowing);
			jcbmiViewToolbarStyles.setSelected(isShowing);
			return jToolBarStyles;
		} else {
			jToolBarMain = jToolBarX;
			jToolBarMain.setVisible(isShowing);
			jcbmiViewToolbarMain.setSelected(isShowing);
			return jToolBarMain;
		}
	}

	/**
	 * Convenience method for activating/deactivating formatting commands
	 * depending on the active editing pane
	 * 
	 * @param state -
	 *            the boolean state of a particular formatting option.
	 */
	private void setFormattersActive(boolean state) {
		actionFontBold.setEnabled(state);
		actionFontItalic.setEnabled(state);
		actionFontUnderline.setEnabled(state);
		actionFontStrike.setEnabled(state);
		actionFontSuperscript.setEnabled(state);
		actionFontSubscript.setEnabled(state);
		actionListUnordered.setEnabled(state);
		actionListOrdered.setEnabled(state);
		actionSelectFont.setEnabled(state);
		actionAlignLeft.setEnabled(state);
		actionAlignCenter.setEnabled(state);
		actionAlignRight.setEnabled(state);
		actionAlignJustified.setEnabled(state);
		actionInsertAnchor.setEnabled(state);
		jbtnUnicode.setEnabled(state);
		jbtnUnicodeMath.setEnabled(state);
		jcmbFontSelector.setEnabled(state);
		jcmbFontSize.setEnabled(state);
		jMenuFont.setEnabled(state);
		jMenuFormat.setEnabled(state);
		jMenuInsert.setEnabled(state);
		jMenuTable.setEnabled(state);
	}
	
	/**
	 * @param state -
	 *            the boolean state of a particular formatting option.
	 */
	private void setStrike(boolean state) {
		actionFontStrike.setEnabled(state);
	}
	
	/**
	 * @param state -
	 *            the boolean state of a particular formatting option.
	 */
	private void setSuper(boolean state) {
		actionFontSuperscript.setEnabled(state);
	}
	
	/**
	 * @param state -
	 *            the boolean state of a particular formatting option.
	 */
	private void setSub(boolean state) {
		actionFontSubscript.setEnabled(state);
	}

	/**
	 * Convenience method for obtaining the current file handle
	 * 
	 * @return the current file handle.
	 */
	public File getCurrentFile() {
		return currentFile;
	}

	/**
	 * Convenience method for obtaining the application name
	 * 
	 * @return the application name.
	 */
	public String getAppName() {
		return appName;
	}

	/**
	 * Convenience method for obtaining the document text
	 * 
	 * @return the document text.
	 */
	public String getDocumentText() {
		if (isSourceWindowActive()) {
			return jtpSource.getText();
		} else {
			return jtpMain.getText();
		}
	}

	/**
	 * Convenience method for obtaining the document text contained within a tag
	 * pair
	 * 
	 * @param tagBlock -
	 *            an HTML tag pair.
	 * @return the text within a tag pair.
	 */
	public String getDocumentSubText(String tagBlock) {
		return getSubText(tagBlock);
	}

	/**
	 * Method for extracting the text within a tag
	 * 
	 * @param containingTag -
	 *            the containing tagblock.
	 * @return the text within a tag.
	 */
	private String getSubText(String containingTag) {
		jtpSource.setText(jtpMain.getText());
		String docTextCase = jtpSource.getText().toLowerCase();
		int tagStart = docTextCase.indexOf("<" + containingTag.toLowerCase());
		int tagStartClose = docTextCase.indexOf(">", tagStart) + 1;
		String closeTag = "</" + containingTag.toLowerCase() + ">";
		int tagEndOpen = docTextCase.indexOf(closeTag);
		if (tagStartClose < 0) {
			tagStartClose = 0;
		}
		if (tagEndOpen < 0 || tagEndOpen > docTextCase.length()) {
			tagEndOpen = docTextCase.length();
		}
		return jtpSource.getText().substring(tagStartClose, tagEndOpen);
	}

	/**
	 * Convenience method for obtaining the document text contained within the
	 * BODY tags (a common request)
	 * 
	 * @return the document text within the BODY tags.
	 */
	public String getDocumentBody() {
		return getSubText("body");
	}

	/**
	 * Convenience method for setting the document text
	 * 
	 * @param sText -
	 *            the document input text.
	 */
	public void setDocumentText(String sText) {
		jtpMain.setText(sText);
		((HTMLEditorKit) (jtpMain.getEditorKit())).setDefaultCursor(new Cursor(
				Cursor.TEXT_CURSOR));
		jtpSource.setText(jtpMain.getText());
	}

	/**
	 * Convenience method for setting the source document
	 * 
	 * @param sDoc -
	 *            the document to be set as the source document.
	 */
	public void setSourceDocument(StyledDocument sDoc) {
		jtpSource.getDocument().removeDocumentListener(this);
		jtpSource.setDocument(sDoc);
		jtpSource.getDocument().addDocumentListener(this);
		jtpMain.setText(jtpSource.getText());
		((HTMLEditorKit) (jtpMain.getEditorKit())).setDefaultCursor(new Cursor(
				Cursor.TEXT_CURSOR));
	}

	/**
	 * Convenience method for communicating the current font selection to the
	 * CustomAction class
	 * 
	 * @return - the current font name.
	 */
	public String getFontNameFromSelector() {
		if (jcmbFontSelector == null) {
			return (String) null;
		} else {
			return jcmbFontSelector.getSelectedItem().toString();
		}
	}

	/**
	 * Convenience method for communicating the current fontsize selection to
	 * the CustomAction class
	 * 
	 * @return - the current font size.
	 */
	public int getFontSizeFromSelector() {
		if (jcmbFontSize == null) {
			return 14;
		} else {		
			return (new Integer(jcmbFontSize.getSelectedItem().toString())).intValue();
		}
	}

	/**
	 * Convenience method for obtaining the document text
	 */
	private void updateTitle() {
		frameHandler.setTitle(appName
				+ (currentFile == null ? "" : " - " + currentFile.getName()));
	}

	/**
	 * Convenience method for clearing out the UndoManager
	 */
	public void purgeUndos() {
		if (undoMngr != null) {
			undoMngr.discardAllEdits();
			undoAction.updateUndoState();
			redoAction.updateRedoState();
		}
	}

	/**
	 * Convenience method for refreshing and displaying changes
	 */
	public void refreshOnUpdate() {
		jtpMain.setText(jtpMain.getText());
		jtpSource.setText(jtpMain.getText());
		purgeUndos();
		this.repaint();
	}

	/**
	 * Convenience method for deallocating the app resources
	 */
	public void dispose() {
		//ask for save
		String message = "Save current file?";
		int choice = JOptionPane.showConfirmDialog(jtpMain, message);
		switch (choice) {
		case JOptionPane.YES_OPTION: // if the user wanted to save
			try {
				writeOut((HTMLDocument) (jtpMain.getDocument()), null);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
			if (currentFile != null) {
				doc_path = currentFile.getAbsolutePath();
				doc_name = currentFile.getName();
				add_to_history_stack(doc_path, doc_name);
				updateHistory();
			}
			frameHandler.dispose();
			System.exit(0);
		case JOptionPane.NO_OPTION: // if the user don't like to save
			frameHandler.dispose();
			System.exit(0); // do nothing
		case JOptionPane.CANCEL_OPTION: // if the user cancelled
			break; // do nothing
		}
	}

	/**
	 * Convenience method for fetching icon images from jar file
	 * 
	 * @param iconName -
	 *            the name of the desired icon.
	 * @return the desired icon as an ImageIcon object.
	 */
	private ImageIcon getEkitIcon(String iconName) {
		File file = new File("]");
		String location = file.getAbsolutePath();
		location = location.substring(0, location.length() - 2);
		ImageIcon myIcon = new ImageIcon(location + "/icons/" + iconName
				+ ".gif");
		if (myIcon != null) {
			return myIcon;
		}

		return (ImageIcon) null;
	}

	/**
	 * Convenience method for outputting exceptions
	 * 
	 * @param internalMessage -
	 *            the message to be displayed.
	 * @param e -
	 *            the current exception.
	 */
	private void logException(String internalMessage, Exception e) {
		System.err.println(internalMessage);
		e.printStackTrace(System.err);
	}

	/**
	 * Convenience method for determining if the source window is active
	 * 
	 * @return whether the source window is active or not.
	 */
	private boolean isSourceWindowActive() {
		return (jspSource != null && jspSource == jspltDisplay
				.getRightComponent());
	}

	/**
	 * Method for toggling source window visibility
	 */
	private void toggleSourceWindow() {
		if (!isSource) {
			jtpSource.setText(jtpMain.getText());
			jspltDisplay.setRightComponent(new JScrollPane(jtpSource));
			if (exclusiveEdit) {
				jspltDisplay.setDividerLocation(0);
				jtpSource.requestFocus();
				jcbmiSplitPanel.setEnabled(false);
				jtbtnSplitPanel.setEnabled(false);
			} else {
				jspltDisplay.setDividerLocation(iSplitPos);
				jspltDisplay.setEnabled(true);
			}
		} else {
			jtpMain.setText(jtpSource.getText());
			jspltDisplay.setRightComponent(jtpSource);
			jspltDisplay.setLeftComponent(new JScrollPane(jtpMain));
			jspltDisplay.remove(jtpSource);
			jcbmiSplitPanel.setEnabled(true);
			jtbtnSplitPanel.setEnabled(true);
			jtpMain.requestFocus();
		}
		this.validate();
		isSource = !isSource;
		jcbmiViewSource.setSelected(isSource);
		jtbtnViewSource.setSelected(isSource);
	}

	/**
	 * Splits the window horizontaly.
	 *  
	 */
	private void splitPanel() {

		jtpSource.setText(jtpMain.getText());

		if (!isSplit) {
			//splitting
			jspltDisplay.setLeftComponent(new JScrollPane(jtpMain));
			jspltDisplay.setRightComponent(new JScrollPane(jtpSource));
			jspltDisplay.setDividerLocation(0.5);
			jspltDisplay.setDividerSize(1);
			jcbmiViewSource.setEnabled(false);
			jtbtnViewSource.setEnabled(false);
			jtpMain.requestFocus();

		} else {
			//returning to original
			jtpMain.setText(jtpSource.getText());
			iSplitPos = jspltDisplay.getDividerLocation();
			jspltDisplay.setRightComponent(jtpSource);
			jspltDisplay.setLeftComponent(new JScrollPane(jtpMain));
			jspltDisplay.remove(jtpSource);
			jspltDisplay.setDividerSize(1);
			jcbmiViewSource.setEnabled(true);
			jtbtnViewSource.setEnabled(true);
			jtpMain.requestFocus();
		}

		this.validate();
		isSplit = !isSplit;
		jcbmiSplitPanel.setSelected(isSplit);
		jtbtnSplitPanel.setSelected(isSplit);
	}

	/**
	 * Counts the words in the current document.
	 *  
	 */
	public void processWordCount() {
		String htmltext = jtpMain.getText();
		String nohtml = htmltext.replaceAll("\\<.*?\\>", "");
		nohtml = nohtml.replaceAll("&#160;", " ");
		StringTokenizer s = new StringTokenizer(nohtml);
		Pattern p = Pattern.compile(" ");
		String words = null;
		int count = 0;
		while (s.hasMoreTokens()) {
			words = s.nextToken();
			count += 1;
		}
		JOptionPane.showMessageDialog(null, "Word Count:  " + count);
	}

	/**
	 * Searches the specified element for CLASS attribute setting
	 * 
	 * @param element -
	 *            the CLASS attribute to locate.
	 * @return - the located style.
	 */
	private String findStyle(Element element) {
		AttributeSet as = element.getAttributes();
		if (as == null) {
			return null;
		}
		Object val = as.getAttribute(HTML.Attribute.CLASS);
		if (val != null && (val instanceof String)) {
			return (String) val;
		}
		for (Enumeration e = as.getAttributeNames(); e.hasMoreElements();) {
			Object key = e.nextElement();
			if (key instanceof HTML.Tag) {
				AttributeSet eas = (AttributeSet) (as.getAttribute(key));
				if (eas != null) {
					val = eas.getAttribute(HTML.Attribute.CLASS);
					if (val != null) {
						return (String) val;
					}
				}
			}

		}
		return null;
	}

	/**
	 * Handles caret tracking and related events, such as displaying the current
	 * style of the text under the caret
	 * 
	 * @param ce -
	 *            the CaretEvent corresponding to the new user input.
	 */
	private void handleCaretPositionChange(CaretEvent ce) {
		int caretPos = ce.getDot();
		Element element = htmlDoc.getCharacterElement(caretPos);

		if (jtpMain.hasFocus()) {
			if (element == null) {
				return;
			}

			String style = null;
			Vector vcStyles = new Vector();

			//updating pane for splitpane
			jtpSource.setText(jtpMain.getText());

			//bold button
			boolean isBold = StyleConstants.isBold(element.getAttributes());
			if (isBold) {
				jbtnBold.getModel().setSelected(true);
			} else {
				jbtnBold.getModel().setSelected(false);
			}

			//italic button
			boolean isItalic = StyleConstants.isItalic(element.getAttributes());
			if (isItalic) {
				jbtnItalic.getModel().setSelected(true);
			} else {
				jbtnItalic.getModel().setSelected(false);
			}

			//underline button
			boolean isUnderline = StyleConstants.isUnderline(element
					.getAttributes());
			if (isUnderline) {
				jbtnUnderline.getModel().setSelected(true);
			} else {
				jbtnUnderline.getModel().setSelected(false);
			}
			
			//strikethrough button
			boolean isStrike = StyleConstants.isStrikeThrough(element
					.getAttributes());
			boolean isSuper = StyleConstants.isSuperscript(element
					.getAttributes());
			boolean isSub = StyleConstants.isSubscript(element
					.getAttributes());
			
			if (isStrike) {
				setSuper(false);
				setSub(false);
			} 
			else if (isSuper) {
				setStrike(false);
				setSub(false);
			}
			else if (isSub) {
				setSuper(false);
			} 
			else
			{
				setSuper(true);
				setSub(true);
				setStrike(true);
			}
			
			
			//font size
			if (jcmbFontSize != null && jcmbFontSize.isVisible()) {		
				int size = StyleConstants.getFontSize(element.getAttributes());
				Integer activeFontSize = new Integer(size);

				jcmbFontSize.getAction().setEnabled(false);
				jcmbFontSize.getModel().setSelectedItem(activeFontSize.toString());
				jcmbFontSize.getAction().setEnabled(true);
			}

			while (element != null) {
				if (style == null) {
					style = findStyle(element);
				}
				vcStyles.add(element);
				element = element.getParentElement();
			}
			// see if current font face is set
			if (jcmbFontSelector != null && jcmbFontSelector.isVisible()) {
				AttributeSet mainAttrs = jtpMain.getCharacterAttributes();
				Enumeration e = mainAttrs.getAttributeNames();
				Object activeFontName = (Object) ("Times New Roman");
				while (e.hasMoreElements()) {
					Object nexte = e.nextElement();
					if (nexte.toString().toLowerCase().equals("face")|| nexte.toString().toLowerCase().equals("font-family")) {
						activeFontName = mainAttrs.getAttribute(nexte);
						break;
					}
				}
				jcmbFontSelector.getAction().setEnabled(false);
				jcmbFontSelector.getModel().setSelectedItem(activeFontName);
				jcmbFontSelector.getAction().setEnabled(true);
			}
		}
	}

	/**
	 * Handles caret tracking and related events, such as displaying the current
	 * style of the text under the caret
	 * 
	 * @param ce -
	 *            the CaretEvent corresponding to the new user input.
	 */
	private void handleSourceCaretPositionChange(CaretEvent ce) {
		if (jtpSource.hasFocus()) {
			//updating pane for splitpane
			jtpMain.setText(jtpSource.getText());
		}
	}

	/**
	 * Server-side image handling methods
	 * 
	 * @param url -
	 *            the url to be set.
	 */
	protected void setServletURL(String url) {
		ServletURL = url;
	}

	/**
	 * @param sysDir -
	 *            the path to the Image directory.
	 */
	protected void setImageDir(String sysDir) {
		ImageDir = sysDir;
	}

	/**
	 * @param theSystem -
	 *            an ID used by the TreePilot library.
	 */
	public void setTreePilotSystemID(String theSystem) {
		TreePilotSystemID = theSystem;
	}

	/**
	 * Utility methods
	 * 
	 * @return the Extended HTML document.
	 */
	public ExtendedHTMLDocument getExtendedHtmlDoc() {
		return (ExtendedHTMLDocument) htmlDoc;
	}

	/**
	 * @return the caret position.
	 */
	public int getCaretPosition() {
		return jtpMain.getCaretPosition();
	}

	/**
	 * @param newPositon -
	 *            the new position where the caret will be placed.
	 */
	public void setCaretPosition(int newPositon) {
		boolean end = true;
		do {
			end = true;
			try {
				jtpMain.setCaretPosition(newPositon);
			} catch (IllegalArgumentException iae) {
				end = false;
				newPositon--;
			}
		} while (!end && newPositon >= 0);
	}

	/**
	 * Writes history of opened files to a local file.
	 * 
	 * @param stack -
	 *            the stack of recently opened files.
	 */
	private void writeState(Stack stack) {
		Vector vt = new Vector();
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(
					"recentlyOpened.txt"));
			for (int i = stack.size() - 5; i < stack.size(); i++) {
				vt = (Vector) stack.elementAt(i);
				String str = new String();
				str = str.concat("<name>");
				str = str.concat((String) vt.elementAt(0));
				str = str.concat("</name><location>");
				str = str.concat((String) vt.elementAt(1));
				str = str.concat("</location>");

				out.write(str);
				out.write(System.getProperty("line.separator"));
			}
			out.close();
		} catch (IOException e) {
		}
	}

	/**
	 * Reads the recently opened file list.
	 * 
	 * @return the stack of recent opened files.
	 */
	private Stack readState() {
		Stack stack = history_stack;
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					"recentlyOpened.txt"));
			String str = new String();
			String result = new String();
			int count = 0;
			String startName = "<name>";
			String endName = "</name>";
			String startLoc = "<location>";
			String endLoc = "</location>";
			int start, end = 0;
			while ((str = in.readLine()) != null && count != 5) {
				//reading name of file
				Vector vt = new Vector();
				start = str.indexOf(startName) + startName.length();
				end = str.indexOf(endName);
				result = str.substring(start, end);
				vt.add(result);

				//reading location of file
				start = str.indexOf(startLoc) + startLoc.length();
				end = str.indexOf(endLoc);
				result = str.substring(start, end);
				vt.add(result);
				stack.push(vt);
				count += 1;
			}
			in.close();

		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
		return stack;
	}

	/**
	 * Adds a file to the recently opened file history stack.
	 * 
	 * @param doc_path2 -
	 *            the path of the file to be added to the history list.
	 * @param doc_name2 -
	 *            the name of the new recently-opened file.
	 */
	private void add_to_history_stack(String doc_path2, String doc_name2) {
		String name = new String();
		String loc = new String();
		Vector vt = new Vector();
		vt.add(doc_name2);
		vt.add(doc_path2);
		boolean confusing = false;
		for (int i = 0; i < history_stack.size(); i++) {
			if (vt.equals((Vector) history_stack.elementAt(i))) {
				history_stack.remove(i);
				history_stack.push(vt);
				confusing = true;
				break;
			}
		}

		if (!confusing)
			history_stack.push(vt);

	}

	/**
	 * Updates history of recent documents.
	 *  
	 */
	private void updateHistory() {
		writeState(history_stack);
		Vector tmp = new Vector();
		tmp = (Vector) history_stack.elementAt(history_stack.size() - 1);
		hist1.setText((String) tmp.get(0));
		tmp = (Vector) history_stack.elementAt(history_stack.size() - 2);
		hist2.setText((String) tmp.get(0));
		tmp = (Vector) history_stack.elementAt(history_stack.size() - 3);
		hist3.setText((String) tmp.get(0));
		tmp = (Vector) history_stack.elementAt(history_stack.size() - 4);
		hist4.setText((String) tmp.get(0));
		tmp = (Vector) history_stack.elementAt(history_stack.size() - 5);
		hist5.setText((String) tmp.get(0));
	}

	/* Inner Classes --------------------------------------------- */

	/**
	 * Class for implementing Undo as an autonomous action
	 */
	class UndoAction extends AbstractAction {
		public UndoAction() {
			super("Undo");
			setEnabled(false);
		}

		public void actionPerformed(ActionEvent e) {
			try {
				undoMngr.undo();
			} catch (CannotUndoException ex) {
				ex.printStackTrace();
			}
			updateUndoState();
			redoAction.updateRedoState();
		}

		protected void updateUndoState() {
			setEnabled(undoMngr.canUndo());
		}
	}

	/**
	 * Class for implementing Redo as an autonomous action
	 */
	class RedoAction extends AbstractAction {
		public RedoAction() {
			super("Redo");
			setEnabled(false);
		}

		public void actionPerformed(ActionEvent e) {
			try {
				undoMngr.redo();
			} catch (CannotUndoException ex) {
				ex.printStackTrace();
			}
			updateRedoState();
			undoAction.updateUndoState();
		}

		protected void updateRedoState() {
			setEnabled(undoMngr.canRedo());
		}
	}

	/**
	 * Class for implementing the Undo listener to handle the Undo and Redo
	 * actions
	 */
	class CustomUndoableEditListener implements UndoableEditListener {
		public void undoableEditHappened(UndoableEditEvent uee) {
			undoMngr.addEdit(uee.getEdit());
			undoAction.updateUndoState();
			redoAction.updateRedoState();
		}
	}
}
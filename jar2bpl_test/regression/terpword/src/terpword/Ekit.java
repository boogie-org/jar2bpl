package terpword;
/*
 GNU Lesser General Public License

 Ekit - Java Swing HTML Editor & Viewer
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

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.awt.Color;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import java.io.FileReader;
import java.awt.Font;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Ekit App for editing and saving HTML in a Java text component
 * 
 * REQUIREMENTS Java 2 (JDK 1.3 or 1.4) Swing Library
 * 
 * @author Howard Kistler
 */

public class Ekit extends JFrame {
	private EkitCore ekitCore;

	private File currentFile = (File) null;

	/**
	 * Master Constructor
	 * 
	 * @param sDocument
	 *            [String] A text or HTML document to load in the editor upon
	 *            startup.
	 * @param sStyleSheet
	 *            [String] A CSS stylesheet to load in the editor upon startup.
	 * @param sRawDocument
	 *            [String] A document encoded as a String to load in the editor
	 *            upon startup.
	 * @param urlStyleSheet
	 *            [URL] A URL reference to the CSS style sheet.
	 * @param includeToolBar
	 *            [boolean] Specifies whether the app should include the
	 *            toolbar.
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
	 * @param base64
	 *            [boolean] Specifies whether the raw document is Base64 encoded
	 *            or not.
	 * @param debugMode
	 *            [boolean] Specifies whether to show the Debug menu or not.
	 * @param useSpellChecker
	 *            [boolean] Specifies whether to include the spellchecker or
	 *            not.
	 * @param multiBar
	 *            [boolean] Specifies whether to use multiple toolbars or one
	 *            big toolbar.
	 */
	public Ekit(String sDocument, String sStyleSheet, String sRawDocument,
			URL urlStyleSheet, boolean includeToolBar, boolean showViewSource,
			boolean showMenuIcons, boolean editModeExclusive, String sLanguage,
			String sCountry, boolean base64, boolean debugMode,
			boolean useSpellChecker, boolean multiBar) {
		ekitCore = new EkitCore(sDocument, sStyleSheet, sRawDocument,
				urlStyleSheet, includeToolBar, showViewSource, showMenuIcons,
				editModeExclusive, sLanguage, sCountry, base64, debugMode,
				false, multiBar);
		ekitCore.setFrame(this);

		/* Add the components to the app */
		if (includeToolBar) {
			if (multiBar) {
				this.getContentPane().setLayout(new GridBagLayout());
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.fill = GridBagConstraints.HORIZONTAL;
				gbc.anchor = GridBagConstraints.NORTH;
				gbc.gridheight = 1;
				gbc.gridwidth = 1;
				gbc.weightx = 1.0;
				gbc.weighty = 0.0;
				gbc.gridx = 1;

				gbc.gridy = 1;
				this.getContentPane().add(
						ekitCore.getToolBarMain(includeToolBar), gbc);

				gbc.gridy = 2;
				this.getContentPane().add(
						ekitCore.getToolBarStyles(includeToolBar), gbc);

				gbc.anchor = GridBagConstraints.SOUTH;
				gbc.fill = GridBagConstraints.BOTH;
				gbc.weighty = 1.0;
				gbc.gridy = 4;
				this.getContentPane().add(ekitCore, gbc);
			} else {
				this.getContentPane().setLayout(new BorderLayout());
				this.getContentPane().add(ekitCore, BorderLayout.CENTER);
				this.getContentPane().add(ekitCore.getToolBar(includeToolBar),
						BorderLayout.NORTH);
			}
		} else {
			this.getContentPane().setLayout(new BorderLayout());
			this.getContentPane().add(ekitCore, BorderLayout.CENTER);
		}

		this.setJMenuBar(ekitCore.getMenuBar());

		this.updateTitle();
		this.pack();
		this.setVisible(true);
	}

	/**
	 * Constructor
	 */
	public Ekit() {
		this(null, null, null, null, true, false, true, true, null, null,
				false, false, false, true);
	}

	  /**
	   * catch requests to close the application's main frame to
	   * ensure proper clean up before the application is
	   * actually terminated.
	   */
	  protected void processWindowEvent(WindowEvent e) {
	    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
	    	this.ekitCore.dispose();
	    }
	    else {
	      super.processWindowEvent(e);
	    }
	  }

	/**
	 * Convenience method for updating the application title bar
	 */
	private void updateTitle() {
		this.setTitle(ekitCore.getAppName()
				+ (currentFile == null ? "" : " - " + currentFile.getName()));
	}

	/**
	 * Usage method, describes command line options
	 */
	public static void usage() {
//		System.out
//				.println("usage: source.ekit.Ekit [-s|S] [-x|X] [-fFILE] [-rRAW] [-uURL] [-h|H|?]");
//		System.out
//				.println("       Each option contained in [] brackets is optional,");
//		System.out
//				.println("       and can be one of the values separated be the | pipe.");
//		System.out
//				.println("       Each option must be proceeded by a - hyphen.");
//		System.out.println("       The options are:");
//		System.out
//				.println("         s|S    : -s = show source window on startup, -S hide source window");
//		System.out
//				.println("         x|X    : -x = exclusive document/source windows, -X use split window");
//		System.out
//				.println("         -fFILE : load HTML document on startup (replace FILE with file name)");
//		System.out
//				.println("         -rRAW  : load raw document on startup (replace RAW with file name)");
//		System.out
//				.println("         -uURL  : load document at URL on startup (replace URL with file URL)");
//		System.out.println("         -h|H|? : print out this help information");
//		System.out.println("         ");
//		System.out.println("For further information, read the README file.");
	}

	/**
	 * getIcon method
	 * 
	 * @param args
	 */
public static BufferedImage getIcon(File f) {

		boolean forPrint = false;

		StringBuffer textBuf = new StringBuffer();
		String line;

		int curX = 20;
		int curY = 20;
		int h = 0;
		int w = 0;
		int totalH = 0;
		int totalW = 0;
		for (int i = 1; i < 51; i++)
			totalH += 15;
		for (int j = 1; j < 15; j++)
			totalW += 40;

		BufferedImage img = new BufferedImage(totalW + 1, totalH + 1,
				BufferedImage.TYPE_INT_RGB);

		Graphics2D g = img.createGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, totalW + 1, totalH + 1);
		
		 Color fColor = Color.BLACK; 
		 Color bColor = Color.WHITE; 
		 Font font = new Font("Default", Font.PLAIN, 12); 
		 //g.setColor(bColor); 
		 //g.fillRect(curX, curY,w, h); 
		 g.setColor(fColor); 
		 g.setFont(font); 
		 
		 
			System.out.println("hey this is the fname = "+f.getName());

			try {
				BufferedReader m_in = new BufferedReader(new FileReader(f));
				while ((line = m_in.readLine()) != null) {
					//this is where you should parse the txt
					System.out.println("line = "+line);
					 g.drawString(line, curX,curY);
					 curY+=20;
				}


			} catch (FileNotFoundException noFile) {
				System.out.println(noFile);
			} catch (IOException io) {
				System.out.println(io);
			} catch (Exception e) {
				System.out.println(e);
			}
		 
		
		return img;
		
		
	}	/**
		  * Main method
		  * 
		  * @param args -
		  *            command line inputs to the main function.
		  */
	public static void main(String[] args) {
		String sDocument = null;
		String sStyleSheet = null;
		String sRawDocument = null;
		URL urlStyleSheet = null;
		boolean includeToolBar = true;
		boolean multibar = true;
		boolean includeViewSource = false;
		boolean includeMenuIcons = true;
		boolean modeExclusive = true;
		String sLang = null;
		String sCtry = null;
		boolean base64 = false;
		boolean debugOn = false;
		boolean spellCheck = false;
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-h") || args[i].equals("-H")
					|| args[i].equals("-?")) {
				usage();
			} else if (args[i].equals("-s")) {
				includeViewSource = true;
			} else if (args[i].equals("-S")) {
				includeViewSource = false;
			} else if (args[i].equals("-x")) {
				modeExclusive = true;
			} else if (args[i].equals("-X")) {
				modeExclusive = false;
			} else if (args[i].startsWith("-f")) {
				sDocument = args[i].substring(2, args[i].length());
			} else if (args[i].startsWith("-r")) {
				sRawDocument = args[i].substring(2, args[i].length());
			} else if (args[i].startsWith("-u")) {
				try {
					urlStyleSheet = new URL(args[i].substring(2, args[i]
							.length()));
				} catch (MalformedURLException murle) {
					murle.printStackTrace(System.err);
				}
			} else if (args[i].startsWith("-l")) {
				if (args[i].indexOf('_') == 4 && args[i].length() >= 7) {
					sLang = args[i].substring(2, args[i].indexOf('_'));
					sCtry = args[i].substring(args[i].indexOf('_') + 1, args[i]
							.length());
				}
			}
		}
		Ekit ekit = new Ekit(sDocument, sStyleSheet, sRawDocument,
				urlStyleSheet, includeToolBar, includeViewSource,
				includeMenuIcons, modeExclusive, sLang, sCtry, base64, debugOn,
				spellCheck, multibar);
	}

}
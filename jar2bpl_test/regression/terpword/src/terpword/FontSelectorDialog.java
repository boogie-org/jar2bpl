/*
GNU Lesser General Public License

FontSelectorDialog
Copyright (C) 2003 Howard Kistler

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
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;


/** Class for providing a dialog that lets the user specify values for tag attributes
  */
public class FontSelectorDialog extends JDialog implements ItemListener
{
	private Vector vcFontnames = (Vector)null;
	private final JComboBox jcmbFontlist;
	private String fontName = new String();
	private JOptionPane jOptionPane;
	private final JTextPane jtpFontPreview;
	private String defaultText;

	/**
	 * @param parent - the application Frame.
	 * @param title - the title of the dialog.
	 * @param bModal - whether or not the dialog will be modal.
	 * @param attribName - the attribute name.
	 * @param demoText - the text used as an example during font selection.
	 */
	public FontSelectorDialog(Frame parent, String title, boolean bModal, String attribName, String demoText)
	{
		super(parent, title, bModal);

		if(demoText != null && demoText.length() > 0)
		{
			if(demoText.length() > 24)
			{
				defaultText = demoText.substring(0, 24);
			}
			else
			{
				defaultText = demoText;
			}
		}
		else
		{
			defaultText = "aAbBcCdDeEfFgGhH,.0123";
		}

		/* Obtain available fonts */
		String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		vcFontnames = new Vector(fonts.length - 5);
		for(int i = 0; i < fonts.length; i++)
		{
			if(!fonts[i].equals("Dialog") && !fonts[i].equals("DialogInput") && !fonts[i].equals("Monospaced") && !fonts[i].equals("SansSerif") && !fonts[i].equals("Serif"))
			{
				vcFontnames.add(fonts[i]);
			}
		}
		jcmbFontlist = new JComboBox(vcFontnames);
		jcmbFontlist.addItemListener(this);

		jtpFontPreview = new JTextPane();
		final HTMLEditorKit kitFontPreview = new HTMLEditorKit();
		final HTMLDocument docFontPreview = (HTMLDocument)(kitFontPreview.createDefaultDocument());
		jtpFontPreview.setEditorKit(kitFontPreview);
		jtpFontPreview.setDocument(docFontPreview);
		jtpFontPreview.setMargin(new Insets(4, 4, 4, 4));
		jtpFontPreview.setBounds(0, 0, 120, 18);
		jtpFontPreview.setText(getFontSampleString(defaultText));
		Object[] panelContents = { attribName, jcmbFontlist, "Font Sample", jtpFontPreview };
		final Object[] buttonLabels = { "Accept", "Cancel" };

		jOptionPane = new JOptionPane(panelContents, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, buttonLabels, buttonLabels[0]);
		setContentPane(jOptionPane);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we)
			{
				jOptionPane.setValue(new Integer(JOptionPane.CLOSED_OPTION));
			}
		});

		jOptionPane.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e)
			{
				String prop = e.getPropertyName();
				if(isVisible() 
					&& (e.getSource() == jOptionPane)
					&& (prop.equals(JOptionPane.VALUE_PROPERTY) || prop.equals(JOptionPane.INPUT_VALUE_PROPERTY)))
				{
					Object value = jOptionPane.getValue();
					if(value == JOptionPane.UNINITIALIZED_VALUE)
					{
						return;
					}
					jOptionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
					if(value.equals(buttonLabels[0]))
					{
						fontName = (String)(jcmbFontlist.getSelectedItem());
						setVisible(false);
					}
					else
					{
						fontName = null;
						setVisible(false);
					}
				}
			}
		});
		this.pack();
		this.setVisible(true);
	}

	/* ItemListener method */
	public void itemStateChanged(ItemEvent ie)
	{
		if(ie.getStateChange() == ItemEvent.SELECTED)
		{
			jtpFontPreview.setText(getFontSampleString(defaultText));
		}
	}

	/**
	 * @param parent - the parent Frame.
	 * @param title -  the title of the dialog.
	 * @param bModal - whether or not the dialog is modal.
	 * @param attribName - the attribute name.
	 */
	public FontSelectorDialog(Frame parent, String title, boolean bModal, String attribName)
	{
		this(parent, title, bModal, attribName, "");
	}

	/**
	 * @return the name of the font.
	 */
	public String getFontName()
	{
		return fontName;
	}

	/**
	 * @param demoText - the sample text used as a comparison basis in the display.
	 * @return the sample string.
	 */
	private String getFontSampleString(String demoText)
	{
		return "<HTML><BODY><FONT FACE=" + '"' + jcmbFontlist.getSelectedItem() + '"' + ">" + demoText + "</FONT></BODY></HTML>";
	}

}


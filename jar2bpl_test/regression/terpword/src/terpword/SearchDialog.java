package terpword;
/*
GNU Lesser General Public License

SearchDialog
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



import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/** Class for providing a dialog that lets the user specify arguments for
  * the Search Find/Replace functions
  */
public class SearchDialog extends JDialog
{
	private String inputFindTerm    = (String)null;
	private String inputReplaceTerm = (String)null;
	private boolean bCaseSensitive  = false;
	private boolean bStartAtTop     = false;
	private boolean bReplaceAll     = false;
	private JOptionPane jOptionPane;

	/**
	 * Constructor 
	 * 
	 * @param parent - the parent Frame.
	 * @param title - the title of the Dialog.
	 * @param bModal - whether or not the dialog will be modal.
	 * @param bIsReplace - [boolean] whether or not to perform replacements.
	 * @param bCaseSetting - [boolean] specifies case-sensitivity.
	 * @param bTopSetting - [boolean] whether to begin the search at the top of the document or not.
	 */
	public SearchDialog(Frame parent, String title, boolean bModal, boolean bIsReplace, boolean bCaseSetting, boolean bTopSetting)
	{
		super(parent, title, bModal);
		final boolean isReplaceDialog    = bIsReplace;
		final JTextField jtxfFindTerm    = new JTextField(3);
		final JTextField jtxfReplaceTerm = new JTextField(3);
		final JCheckBox  jchkCase        = new JCheckBox("Case Sensitive", bCaseSetting);
		final JCheckBox  jchkTop         = new JCheckBox(" Start At Top", bTopSetting);
		final JCheckBox  jchkAll         = new JCheckBox("Replace All", false);
		final Object[] buttonLabels      = { "Accept", "Cancel" };
		if(bIsReplace)
		{
			Object[] panelContents = {
				"Find",
				jtxfFindTerm,
				"Replace",
				jtxfReplaceTerm,
				jchkAll,
				jchkCase,
				jchkTop
			};
			jOptionPane = new JOptionPane(panelContents, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, buttonLabels, buttonLabels[0]);
		}
		else
		{
			Object[] panelContents = {
				"Find",
				jtxfFindTerm,
				jchkCase,
				jchkTop
			};
			jOptionPane = new JOptionPane(panelContents, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, buttonLabels, buttonLabels[0]);
		}
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
						inputFindTerm  = jtxfFindTerm.getText();
						bCaseSensitive = jchkCase.isSelected();
						bStartAtTop    = jchkTop.isSelected();
						if(isReplaceDialog)
						{
							inputReplaceTerm = jtxfReplaceTerm.getText();
							bReplaceAll      = jchkAll.isSelected();
						}
						setVisible(false);
					}
					else
					{
						inputFindTerm    = (String)null;
						inputReplaceTerm = (String)null;
						bCaseSensitive   = false;
						bStartAtTop      = false;
						bReplaceAll      = false;
						setVisible(false);
					}
				}
			}
		});
		this.pack();
		this.setVisible(true);
		jtxfFindTerm.requestFocus();
	}

	/**
	 * @return String - text to be found
	 */
	public String  getFindTerm()      { return inputFindTerm; }
	/**
	 * @return String - text to be replaced
	 */
	public String  getReplaceTerm()   { return inputReplaceTerm; }
	/**
	 * @return boolean - returns wheather case sensitive or not
	 */
	public boolean getCaseSensitive() { return bCaseSensitive; }
	/**
	 * @return boolean - returns wheather starting at top of page or not
	 */
	public boolean getStartAtTop()    { return bStartAtTop; }
	/**
	 * @return boolean - returns wheather to replace all or not
	 */
	public boolean getReplaceAll()    { return bReplaceAll; }
}


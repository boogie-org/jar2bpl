package terpword;
/*
GNU Lesser General Public License

UserInputDialog
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/** Class for providing a dialog that lets the user specify values for tag attributes
  */
public class UserInputDialog extends JDialog
{

	private String inputText = new String();
	private JOptionPane jOptionPane;

	/**
	 * Constructor
	 * 
	 * @param parent - the parent Frame.
	 * @param title - the dialog title.
	 * @param bModal - whether or not the dialog will be modal.
	 * @param attribName - the corresponding attribute name. 
	 * @param defaultText - the default text to be displayed in the dialog.
	 */
	public UserInputDialog(Frame parent, String title, boolean bModal, String attribName, String defaultText)
	{
		super(parent, title, bModal);
		final JTextField jtxfInput = new JTextField(32);
		jtxfInput.setText(defaultText);
		Object[] panelContents = { attribName, jtxfInput };
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

		jtxfInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				jOptionPane.setValue(buttonLabels[0]);
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
						inputText = jtxfInput.getText();
						setVisible(false);
					}
					else
					{
						inputText = null;
						setVisible(false);
					}
				}
			}
		});
		this.pack();
		this.setVisible(true);
		jtxfInput.requestFocus();
	}

	/**
	 * Constructor
	 * 
	 * @param parent - the parent Frame.
	 * @param title - the dialog title.
	 * @param bModal - whether or not the dialog will be modal.
	 * @param attribName - the corresponding attribute name.
	 */
	public UserInputDialog(Frame parent, String title, boolean bModal, String attribName)
	{
		this(parent, title, bModal, attribName, "");
	}

	/**
	 * @return String - returns string of text
	 */
	public String getInputText()
	{
		return inputText;
	}
}


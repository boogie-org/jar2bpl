package terpword;
/*
GNU Lesser General Public License

SimpleInfoDialog
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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

/** Class for providing a dialog that lets the user specify values for tag attributes.
  */
public class SimpleInfoDialog extends JDialog
{
	public static final int ERROR    = JOptionPane.ERROR_MESSAGE;
	public static final int INFO     = JOptionPane.INFORMATION_MESSAGE;
	public static final int WARNING  = JOptionPane.WARNING_MESSAGE;
	public static final int QUESTION = JOptionPane.QUESTION_MESSAGE;
	public static final int PLAIN    = JOptionPane.PLAIN_MESSAGE;

	private JOptionPane jOptionPane;
	private Object[] buttonLabels;
	private Integer buttonState = new Integer(JOptionPane.CLOSED_OPTION);

	/**
	 * Constructor 
	 * 
	 * @param parent - the parent Frame.
	 * @param title - the title of the new Dialog.
	 * @param bModal - whether or not the dialog will be modal.
	 * @param message - the message to be displayed.
	 * @param type - the type of info dialog.
	 */
	public SimpleInfoDialog(Frame parent, String title, boolean bModal, String message, int type)
	{
		super(parent, title, bModal);
		if(type == QUESTION)
		{
			buttonLabels = new Object[]{ "Accept", "Cancel" };
			jOptionPane = new JOptionPane(new JLabel(message, SwingConstants.CENTER), JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, buttonLabels, buttonLabels[0]);
		}
		else
		{
			buttonLabels = new Object[]{ "Close" };
			jOptionPane = new JOptionPane(new JLabel(message, SwingConstants.CENTER), JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, buttonLabels, buttonLabels[0]);
		}

		setContentPane(jOptionPane);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		jOptionPane.addPropertyChangeListener(new PropertyChangeListener()
		{
			public void propertyChange(PropertyChangeEvent e)
			{
				String prop = e.getPropertyName();
				if(isVisible() && (e.getSource() == jOptionPane) && (prop.equals(JOptionPane.VALUE_PROPERTY) || prop.equals(JOptionPane.INPUT_VALUE_PROPERTY)))
				{
					setVisible(false);
				}
			}
		});

		this.pack();
		int centerX = (int)(((parent.getSize().getWidth()  / 2) + parent.getLocation().getX()) - (this.getSize().getWidth()  / 2));
		int centerY = (int)(((parent.getSize().getHeight() / 2) + parent.getLocation().getY()) - (this.getSize().getHeight() / 2));
		if(centerX < 0) { centerX = 0; }
		if(centerY < 0) { centerY = 0; }
		this.setLocation(centerX, centerY);
		this.setVisible(true);
	}

	/**
	 * Constructor
	 * 
	 * @param parent - the parent Frame.
	 * @param title - the dialog title.
	 * @param bModal - whether or not the dialog will be modal.
	 * @param message - the dialog message.
	 */
	public SimpleInfoDialog(Frame parent, String title, boolean bModal, String message)
	{
		this(parent, title, bModal, message, WARNING);
	}

	/**
	 * @return String - returns the Decision Value
	 */
	public String getDecisionValue()
	{
		return jOptionPane.getValue().toString();
	}
}

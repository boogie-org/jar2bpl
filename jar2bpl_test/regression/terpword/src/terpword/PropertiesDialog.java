package terpword;
/*
GNU Lesser General Public License

PropertiesDialog
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



import java.awt.Frame;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;


/** Class for providing a dialog that lets the user specify values for tag attributes
  */
public class PropertiesDialog extends JDialog
{
	private JOptionPane jOptionPane;
	private Hashtable htInputFields;

	/**
	 * Constructor
	 * 
	 * @param parent - the parent Frame.
	 * @param fields - the input field titles.
	 * @param types - the types of input fields.
	 * @param values - the input field values.
	 * @param title - the dialog title.
	 * @param bModal - whether or not the dialog is modal.
	 */
	public PropertiesDialog(Frame parent, String[] fields, String[] types, String[] values, String title, boolean bModal)
	{
		super(parent, title, bModal);
		htInputFields = new Hashtable();
		final Object[] buttonLabels = { "Accept", "Cancel" };
		Object[] panelContents = new Object[(fields.length * 2)];
		int objectCount = 0;
		for(int iter = 0; iter < fields.length; iter++)
		{
			String fieldName = fields[iter];
			String fieldType = types[iter];
			Object fieldComponent;
			if(fieldType.equals("text"))
			{
				fieldComponent = new JTextField(3);
				if(values[iter] != null && values[iter].length() > 0)
				{
					((JTextField)(fieldComponent)).setText(values[iter]);
				}
			}
			else if(fieldType.equals("bool"))
			{
				fieldComponent = new JCheckBox();
				if(values[iter] != null)
				{
					((JCheckBox)(fieldComponent)).setSelected(values[iter] == "true");
				}
			}
			else if(fieldType.equals("combo"))
			{
				fieldComponent = new JComboBox();
				if(values[iter] != null)
				{
					StringTokenizer stParse = new StringTokenizer(values[iter], ",", false);
					while(stParse.hasMoreTokens())
					{
						((JComboBox)(fieldComponent)).addItem(stParse.nextToken());
					}
				}
			}
			else
			{
				fieldComponent = new JTextField(3);
			}
			htInputFields.put(fieldName, fieldComponent);
			panelContents[objectCount] = fieldName; // Translatrix.getTranslationString(fieldName);
			panelContents[objectCount + 1] = fieldComponent;
			objectCount += 2;
		}
		jOptionPane = new JOptionPane(panelContents, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, buttonLabels, buttonLabels[0]);

		setContentPane(jOptionPane);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		jOptionPane.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e)
			{
				String prop = e.getPropertyName();
				if(isVisible() && (e.getSource() == jOptionPane) && (prop.equals(JOptionPane.VALUE_PROPERTY) || prop.equals(JOptionPane.INPUT_VALUE_PROPERTY)))
				{
					Object value = jOptionPane.getValue();
					if(value == JOptionPane.UNINITIALIZED_VALUE)
					{
						return;
					}
					if(value.equals(buttonLabels[0]))
					{
						setVisible(false);
					}
					else
					{
						setVisible(false);
					}
				}
			}
		});
		this.pack();
	}

	/**
	 * Constructor
	 * 
	 * @param parent - the parent Frame.
	 * @param fields - the input field titles.
	 * @param types - the types of input fields.
	 * @param title - the dialog title.
	 * @param bModal - whether or not the dialog is modal.
	 */
	public PropertiesDialog(Frame parent, String[] fields, String[] types, String title, boolean bModal)
	{
		this(parent, fields, types, new String[fields.length], title, bModal);
	}

	/**
	 * 
	 * @param fieldName - name of the file
	 * @return String - value of the field
	 */
	public String getFieldValue(String fieldName)
	{
		Object dataField = htInputFields.get(fieldName);
		if(dataField instanceof JTextField)
		{
			return ((JTextField)dataField).getText();
		}
		else if(dataField instanceof JCheckBox)
		{
			if(((JCheckBox)dataField).isSelected())
			{
				return "true";
			}
			else
			{
				return "false";
			}
		}
		else if(dataField instanceof JComboBox)
		{
			return (String)(((JComboBox)dataField).getSelectedItem());
		}
		else
		{
			return (String)null;
		}
	}

	/**
	 * @return String - decision value
	 */
	public String getDecisionValue()
	{
		return jOptionPane.getValue().toString();
	}
}


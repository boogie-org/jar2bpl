package terpword;
/*
GNU Lesser General Public License

SymbolInputDialog
Copyright (C) 2000-2003 Howard Kistler

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




import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.*;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class SymbolDialog extends JDialog implements ActionListener
{
	private final static String SYMBOLS = "Symbols";
	
	private EkitCore parentEkit;
	private ButtonGroup buttonGroup;
	private String returnSymbol;

	/**
	 * @param peKit - main EkitCore object
	 * @param title - name of the frame
	 * @param bModal - whether or not the dialog will be modal.
	 */
	public SymbolDialog(EkitCore peKit, String title, boolean bModal)
	{
		super(peKit.getFrame(), title, bModal);
		parentEkit = peKit;
		init();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals("accept"))
		{
			setVisible(false);
			if(buttonGroup.getSelection() == null)
			{
				returnSymbol = null;
			}
			else
			{
				returnSymbol = buttonGroup.getSelection().getActionCommand();
			}
		}
		else if(e.getActionCommand().equals("cancel"))
		{
			setVisible(false);
			returnSymbol = null;
		}
	}

	/**
	 * Creates and Initializes the dialog box
	 */
	public void init()
	{
		Container contentPane = getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		setBounds(100,100,400,300);
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(0, 8, 0, 0));
		buttonGroup = new ButtonGroup();
		for(int i = 0; i < SYMBOLS.length(); i++)
		{
			String symbol = Character.toString(SYMBOLS.charAt(i));
			JToggleButton b = new JToggleButton(symbol);
			b.getModel().setActionCommand(symbol);
			centerPanel.add(b);
			buttonGroup.add(b);
		}

		JPanel buttonPanel= new JPanel();
		buttonPanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));

		JButton saveButton = new JButton("Accept");
		saveButton.setActionCommand("accept");
		saveButton.addActionListener(this);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("cancel");
		cancelButton.addActionListener(this);

		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);

		contentPane.add(centerPanel);
		contentPane.add(buttonPanel);

		this.pack();
		this.setVisible(true);
	}

	/**
	 * @return String - returns inputted text
	 */
	public String getInputText()
	{
		return returnSymbol;
	}

}


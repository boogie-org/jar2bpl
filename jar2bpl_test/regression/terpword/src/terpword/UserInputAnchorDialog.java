package terpword;
/*
GNU Lesser General Public License

UserInputAnchorDialog
Copyright (C) 2000 Howard Kistler & other contributors

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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;



public class UserInputAnchorDialog extends JDialog implements ActionListener
{
	private EkitCore parentEkit;
	private String inputText = null;
	private final JTextField jtxfInput = new JTextField(32);

	/**
	 * Constructor 
	 * 
	 * @param peKit - parent
	 * @param title - name of frame
	 * @param bModal - whether or not the dialog will be modal.
	 * @param defaultAnchor - default anchor
	 */
	public UserInputAnchorDialog(EkitCore peKit, String title, boolean bModal, String defaultAnchor)
	{		
		super(peKit.getFrame(), title, bModal);
		parentEkit = peKit;
		jtxfInput.setText(defaultAnchor);
		init();
	}

   	/* (non-Javadoc)
   	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   	 */
   	public void actionPerformed(ActionEvent e)
   	{
		if(e.getActionCommand().equals("accept"))
		{
			inputText = jtxfInput.getText();
			setVisible(false);
		}	
	  	if(e.getActionCommand().equals("cancel"))
		{
			inputText = null;
			setVisible(false);
		}
		else if(e.getActionCommand().equals("files"))
		{
			String selectedFile = parentEkit.insertFile();
			if(selectedFile != null)
			{
				jtxfInput.setText(selectedFile);	
			}
		}
	}

	/**
	 *  creates and initializes the dialog box
	 */
	public void init()
	{
	  	Container contentPane = getContentPane();
	  	contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
	  	setBounds(100,100,400,300);
	  	setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

	  	JPanel centerPanel = new JPanel();
       	centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
	  	JLabel anchorLabel = new JLabel("Anchor:", SwingConstants.LEFT);
	  	centerPanel.add(anchorLabel);
	  	centerPanel.add(jtxfInput);

		JPanel buttonPanel= new JPanel();	  	
//	  	buttonPanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));

		JButton saveButton = new JButton("Accept");
	  	saveButton.setActionCommand("accept");
		saveButton.addActionListener(this);

	  	JButton cancelButton = new JButton("Cancel");
	  	cancelButton.setActionCommand("cancel");
		cancelButton.addActionListener(this);

		JButton filesButton = new JButton("Server Files...");
	  	filesButton.setActionCommand("files");
		filesButton.addActionListener(this);

		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);
		buttonPanel.add(filesButton);

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
		return inputText;
	}

	/**
	 * @param anchor - sets the anchor
	 */
	public void setAnchor(String anchor)
	{
		jtxfInput.setText(anchor);
	}
}


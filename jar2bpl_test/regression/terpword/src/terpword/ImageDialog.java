/*
GNU Lesser General Public License

ImageDialog
Copyright (C) 2003 Howard Kistler & other contributors

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
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;



/**
 * A dialog box to open an image.
 */
public class ImageDialog extends JDialog implements ActionListener
{
	/**
	 * List of borders
	 */
	private final String[] Borders = new String[]
	{
		"none",
		"solid",
		"dotted",
		"dashed",
		"double",
		"groove",
		"ridge",
		"inset",
		"outset"
	};
	
	/**
	 * List of border colors
	 */
	private String[] BorderColors = new String[]
	{
		"none",
		"aqua",
		"black",
		"blue",
		"fuschia",
		"gray",
		"green",
		"lime",
		"maroon",
		"navy",
		"olive",
		"purple",
		"red",
		"silver",
		"teal",
		"white",
		"yellow"
	};
	/**
	 * List of border size
	 */
	private String[]BorderSizes = new String[]
	{
		"none",
		"1",
		"2",
		"3",
		"4",
		"5",
		"6",
		"7",
		"8",
		"9",
		"10"
	};
	/**
	 * List of wraps
	 */
	private final String[]Wraps = new String[]
	{
		"none",
		"left",
		"right",
		"top",
		"middle",
		"bottom"
	};

	private EkitCore ParentEkit;
	private ExtendedHTMLEditorKit ImageHtmlKit;
	private HTMLDocument ImageHtmlDoc;
	private JList WrapList;
	private JList BorderList;
	private JList BorderSizeList;
	private JList BorderColorList;
	private JList ImageList;
	private JTextField ImageAltText;
	private JTextField ImageWidth;
	private JTextField ImageHeight;
	private JEditorPane PreviewPane;

	private String   ImageDir;
	private String[] Images;
	private String   PreviewImage;
	private String   SelectedImage;

	/**
	 * COnstructor
	 * 
	 * @param parentEkit - main EkitCore object
	 * @param imageDir - directory of the image
	 * @param imageList - list of images
	 * @param title - title of the frame
	 * @param modal - whether or not the dialog is modal.
	 */
	public ImageDialog(EkitCore parentEkit, String imageDir, String[] imageList, String title, boolean modal)
	{
		super(parentEkit.getFrame(), title, modal);
		ImageDir = imageDir;
		Images = imageList;
		ParentEkit = parentEkit;
		SelectedImage = null;
		init();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(java.awt.event.ActionEvent e)
	{
		if(e.getActionCommand().equals("apply"))
		{
			ListSelectionModel sm = ImageList.getSelectionModel();
			if(sm.isSelectionEmpty())
			{
				SimpleInfoDialog sidAbout = new SimpleInfoDialog(ParentEkit.getFrame(), "Error", true, "No image selected", SimpleInfoDialog.ERROR);
				ImageList.requestFocus();
			}
			else
			{
				if(validateControls())
				{
					previewSelectedImage();
				}
			}
		}	
		if(e.getActionCommand().equals("save"))
		{
			ListSelectionModel sm = ImageList.getSelectionModel();
			if(sm.isSelectionEmpty())
			{
				SimpleInfoDialog sidAbout = new SimpleInfoDialog(ParentEkit.getFrame(), "Error", true, "No image selected", SimpleInfoDialog.ERROR);
				ImageList.requestFocus();
			}
			else
			{
				if(validateControls())
				{
					previewSelectedImage();
					SelectedImage = PreviewImage;
					setVisible(false);
				}
			}
		}
		else if(e.getActionCommand().equals("cancel"))
		{
			setVisible(false);
		}
	}

	/**
	 * Constructs and initializes the dialog box
	 */
	public void init()
	{
		SelectedImage="";
		Container contentPane = getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		//setBounds(100,100,500,300);
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

		ImageList = new JList(Images);
		ImageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ImageList.clearSelection();
		ListSelectionModel lsm = ImageList.getSelectionModel();

		/* Create the editor kit, document, and stylesheet */
		PreviewPane = new JEditorPane();
		PreviewPane.setEditable(false);
		ImageHtmlKit = new ExtendedHTMLEditorKit();
		ImageHtmlDoc = (HTMLDocument)(ImageHtmlKit.createDefaultDocument());
		ImageHtmlKit.setDefaultCursor(new Cursor(Cursor.TEXT_CURSOR));
		PreviewPane.setCaretPosition(0);
		//PreviewPane.getDocument().addDocumentListener(this);
		//StyleSheet styleSheet = ImageHtmlDoc.getStyleSheet();
		//ImageStyleSheet = styleSheet;
		lsm.addListSelectionListener(new ListSelectionListener() 
		{
			public void valueChanged(ListSelectionEvent e)
			{
				if(!e.getValueIsAdjusting() && validateControls())
				{
					previewSelectedImage();
				}
			}
				
		});

		JScrollPane imageScrollPane = new JScrollPane(ImageList);
		imageScrollPane.setPreferredSize(new Dimension(200,250));
		imageScrollPane.setMaximumSize(new Dimension(200,250));
		imageScrollPane.setAlignmentX(LEFT_ALIGNMENT);
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
		centerPanel.add(imageScrollPane);
		centerPanel.setBorder(BorderFactory.createTitledBorder("Server Images"));

		/* Set up the text pane */
		PreviewPane.setEditorKit(ImageHtmlKit);
		PreviewPane.setDocument(ImageHtmlDoc);
		PreviewPane.setMargin(new Insets(4, 4, 4, 4));
		JScrollPane previewViewport = new JScrollPane(PreviewPane);
		previewViewport.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		previewViewport.setPreferredSize(new Dimension(250,250));
		centerPanel.add(previewViewport); 

		JPanel controlsPanel = new JPanel();
		controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));
		JPanel altPanel = new JPanel();
		altPanel.setLayout(new BoxLayout(altPanel, BoxLayout.X_AXIS));
		altPanel.add(Box.createHorizontalStrut(10));
		JLabel imageAltTextLabel = new JLabel("Alternate Text:", SwingConstants.LEFT);	  
		altPanel.add(imageAltTextLabel);

		ImageAltText = new JTextField("");
		ImageAltText.addActionListener(this);
		ImageAltText.setPreferredSize(new Dimension(300,25));
		ImageAltText.setMaximumSize(new Dimension(600,25));
		altPanel.add(ImageAltText);
		altPanel.add(Box.createHorizontalStrut(10));
		controlsPanel.add(altPanel);
		controlsPanel.add(Box.createVerticalStrut(5));

		JPanel dimPanel = new JPanel();
		dimPanel.setLayout(new BoxLayout(dimPanel, BoxLayout.X_AXIS));
		dimPanel.add(Box.createHorizontalStrut(10));
		JLabel imageWidthLabel = new JLabel("Width:", SwingConstants.LEFT);	  
		dimPanel.add(imageWidthLabel);
		ImageWidth = new JTextField("");
		ImageWidth.setPreferredSize(new Dimension(40,25));
		ImageWidth.setMaximumSize(new Dimension(40,25));
		dimPanel.add(ImageWidth);
		JLabel imageWidthPixels = new JLabel("pix", SwingConstants.LEFT);	  
		imageWidthPixels.setPreferredSize(new Dimension(20,10));
		dimPanel.add(imageWidthPixels);
		dimPanel.add(Box.createHorizontalStrut(10));
		JLabel imageHeightLabel = new JLabel("Height:", SwingConstants.LEFT);	  
		dimPanel.add(imageHeightLabel);
		ImageHeight = new JTextField("");
		ImageHeight.setPreferredSize(new Dimension(40,25));
		ImageHeight.setMaximumSize(new Dimension(40,25));
		dimPanel.add(ImageHeight);
		JLabel imageHeightPixels = new JLabel("pix", SwingConstants.LEFT);	  
		imageHeightPixels.setPreferredSize(new Dimension(20,10));
		dimPanel.add(imageHeightPixels);
		dimPanel.add(Box.createHorizontalStrut(10));

		JLabel wrapLabel = new JLabel("Wrap:", SwingConstants.LEFT);
		dimPanel.add(wrapLabel);
		WrapList = new JList(Wraps);
		WrapList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		WrapList.getSelectionModel().setSelectionInterval(0,0);
		JScrollPane wrapScrollPane = new JScrollPane(WrapList);
		wrapScrollPane.setAlignmentX(LEFT_ALIGNMENT);
		wrapScrollPane.setPreferredSize(new Dimension(80,40));
		wrapScrollPane.setMaximumSize(new Dimension(80,100));
		dimPanel.add(wrapScrollPane);
		controlsPanel.add(dimPanel);

		dimPanel.add(Box.createHorizontalStrut(5));
		JLabel borderSizeLabel = new JLabel("Border Size:", SwingConstants.LEFT);
		dimPanel.add(borderSizeLabel);
		BorderSizeList = new JList(BorderSizes);
		BorderSizeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		BorderSizeList.getSelectionModel().setSelectionInterval(0,0);
		JScrollPane borderSizeScrollPane = new JScrollPane(BorderSizeList);
		borderSizeScrollPane.setAlignmentX(LEFT_ALIGNMENT);
		borderSizeScrollPane.setPreferredSize(new Dimension(80,40));
		borderSizeScrollPane.setMaximumSize(new Dimension(80,100));
		dimPanel.add(borderSizeScrollPane);
		dimPanel.add(Box.createHorizontalStrut(10));
		dimPanel.add(Box.createVerticalStrut(10));

		JPanel buttonPanel= new JPanel();
		buttonPanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
		JButton applyButton = new JButton("Apply");
		applyButton.setActionCommand("apply");
		applyButton.addActionListener(this);

		JButton saveButton = new JButton("Accept");
		saveButton.setActionCommand("save");
		saveButton.addActionListener(this);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("cancel");
		cancelButton.addActionListener(this);

		buttonPanel.add(applyButton);
		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);

		contentPane.add(centerPanel);
		contentPane.add(controlsPanel);
		contentPane.add(buttonPanel);
		this.pack();
		setVisible(true);
    }

    /**
     * creates preview for selected image
     */
    private void previewSelectedImage()
    {
		ListSelectionModel sm = ImageList.getSelectionModel();
		if(!sm.isSelectionEmpty())
		{
			String theImage = Images[sm.getMinSelectionIndex()];
			try
			{
				// Clear the preview area
				PreviewPane.setText("");
				StringBuffer attrString = new StringBuffer();
				if(!ImageHeight.getText().equals(""))
				{
					attrString.append("HEIGHT=\"" + ImageHeight.getText() + "\" ");
				}
				if(!ImageWidth.getText().equals(""))
				{
					attrString.append("WIDTH=\"" + ImageWidth.getText() + "\" ");
				}
				if(!ImageAltText.getText().equals(""))
				{
					attrString.append("ALT=\"" + ImageAltText.getText() + "\" ");
				}
				if(!WrapList.getSelectionModel().isSelectionEmpty())
				{
					String theWrap = Wraps[WrapList.getSelectionModel().getMinSelectionIndex()];
					if(!theWrap.equals("none"))
					{
					attrString.append("ALIGN=\"" + theWrap + "\" ");
					}
				}

				String borderSize = null;
				String borderColor = null;
				if(!BorderSizeList.getSelectionModel().isSelectionEmpty())
				{
					borderSize = BorderSizes[BorderSizeList.getSelectionModel().getMinSelectionIndex()];
					if(!borderSize.equals("none"))
					{
						attrString.append("BORDER=" + borderSize);
					}
				}
				else
				{
					borderSize = BorderSizes[0];
				}

				PreviewImage = "<IMG SRC=" + ImageDir + "/" + theImage + " " + attrString.toString() + ">";
				ImageHtmlKit.insertHTML(ImageHtmlDoc, 0, PreviewImage, 0, 0, HTML.Tag.IMG);
				repaint();
			}
			catch(Exception ex)
			{
				System.err.println("Exception previewing image");
			}
		}
	}

	/**
	 * @return boolean - true if image is valid
	 */
	private boolean validateControls()
	{
		boolean result = true;
		if(!ImageWidth.getText().equals(""))
		{
			try
			{
				Integer.parseInt(ImageWidth.getText());
			}
			catch (NumberFormatException e)
			{
				result = false;
				SimpleInfoDialog sidAbout = new SimpleInfoDialog(ParentEkit.getFrame(), "Error", true, "Image Width is not an integer", SimpleInfoDialog.ERROR);
				ImageWidth.requestFocus();
			}
		}
		if( result && !ImageHeight.getText().equals(""))
		{
			try
			{
				Integer.parseInt(ImageHeight.getText());
			}
			catch (NumberFormatException e)
			{
				result = false;
				SimpleInfoDialog sidAbout = new SimpleInfoDialog(ParentEkit.getFrame(), "Error", true, "Image Height is not an integer", SimpleInfoDialog.ERROR);
				ImageHeight.requestFocus();
			}
		}
		return result;
	}

    /**
     * @return String - selected image
     */
    public String getSelectedImage()
    {
	  return SelectedImage;
    }	
}

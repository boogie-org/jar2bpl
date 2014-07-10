/*
GNU Lesser General Public License

FormatAction
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
import java.awt.event.ActionEvent;
import java.util.Enumeration;

import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.html.HTML;




/** Class for implementing HTML format actions
  */
public class FormatAction extends StyledEditorKit.StyledTextAction
{
	protected EkitCore parentEkit;
	HTML.Tag htmlTag;

	/**
	 * @param ekit - The main EkitCore class.
	 * @param actionName -  the name of action to be formatted.
	 * @param inTag - the corresponding HTML tag.
	 */
	public FormatAction(EkitCore ekit, String actionName, HTML.Tag inTag)
	{
		super(actionName);
		parentEkit = ekit;
		htmlTag    = inTag;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae)
	{
		JTextPane parentTextPane = parentEkit.getTextPane();
		String selText = parentTextPane.getSelectedText();
		int textLength = -1;
		if(selText != null)
		{
			textLength = selText.length();
			System.out.println(textLength);
		}
		if(selText == null || textLength < 1)
		{
			SimpleInfoDialog sidWarn = new SimpleInfoDialog(parentEkit.getFrame(), "", true, "No Text Selected", SimpleInfoDialog.ERROR);
		}
		else
		{
			SimpleAttributeSet sasText = new SimpleAttributeSet(parentTextPane.getCharacterAttributes());
			if (sasText.containsAttribute(htmlTag, new SimpleAttributeSet()))
			{
				int caretOffset = parentTextPane.getSelectionStart();		
				parentTextPane.select(caretOffset, caretOffset + textLength);
				if (htmlTag.toString().compareTo("strike")==0)
				{
					String[] str = {"text-decoration","strike"};
					clearFormat(parentEkit, selText, caretOffset, str);
				}
				else if (htmlTag.toString().compareTo("sup")==0)
				{
					String[] str = {"vertical-align","sup"};
					clearFormat(parentEkit, selText, caretOffset, str);
				}
				else if (htmlTag.toString().compareTo("sub")==0)
				{
					String[] str = {"vertical-align","sub"};
					clearFormat(parentEkit, selText, caretOffset, str);
				}
			}
			else{
				sasText.addAttribute(htmlTag, new SimpleAttributeSet());
				int caretOffset = parentTextPane.getSelectionStart();
				parentTextPane.select(caretOffset, caretOffset + textLength);
				parentTextPane.setCharacterAttributes(sasText, false);
				refreshAndSelect(parentTextPane,caretOffset,textLength);
			}
		}
	}
	/**
	 * clears the given list of HTML.Attributes and HTML.Tags from the selected Text.
	 * @param myParentPanel - the parent Panel object.
	 * @param selText - the selected text.
	 * @param caretOffset - the caret offset.
	 * @param attributes - the list of attributes to remove from each character.
	 */
	public void clearFormat(EkitCore myParentPanel,String selText, int caretOffset, String[] attributes) 
	{
		JTextPane parentTextPane = myParentPanel.getTextPane();
		SimpleAttributeSet sasText = null;
		int internalTextLength = selText.length();

		// clear all character attributes in selection
		for(int i = caretOffset; i <= caretOffset + internalTextLength; i++) {
			parentTextPane.setCaretPosition(i);
			sasText = new SimpleAttributeSet(parentTextPane.getCharacterAttributes().copyAttributes());
			Enumeration attribEntries1 = sasText.getAttributeNames();
			while(attribEntries1.hasMoreElements()) {
				Object entryKey   = attribEntries1.nextElement();
				Object entryValue = sasText.getAttribute(entryKey);
				//System.out.println(entryKey + " = " + entryValue);
				for (int j=0; j < attributes.length; j++) {
					if (!entryKey.toString().equals(HTML.Attribute.NAME.toString())
						&& entryKey.toString().equals(attributes[j])) 
					{
						//System.out.println("removing: " + entryKey.toString() + " = " + entryValue.toString());
						sasText.removeAttribute(entryKey);
					}
				}
			}
			try {
				parentTextPane.select(i, i+1);
				parentTextPane.setCharacterAttributes(sasText, true);
			} catch (Exception e) {
				System.out.println("An Error ocurred: " + e.fillInStackTrace());
			}
		}
		refreshAndSelect(parentTextPane, caretOffset, internalTextLength);
		return;
	}
	
	/**
	 * @param parentTextPane - ekitCore parent
	 * @param caretOffset - starting location of selected text
	 * @param internalTextLength - length of selected text
	 */
	private void refreshAndSelect(JTextPane parentTextPane, int caretOffset, int textLength) {
		parentEkit.refreshOnUpdate();
		//parentTextPane.select(caretOffset, caretOffset + textLength);
		//parentTextPane.setCaretPosition(parentTextPane.getSelectionEnd()-1);
	}

}


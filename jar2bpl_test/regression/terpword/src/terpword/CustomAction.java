/*
GNU Lesser General Public License

CustomAction
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
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.JColorChooser;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.html.HTML;


/** 
 * Class for implementing custom HTML insertion actions
*/
public class CustomAction extends StyledEditorKit.StyledTextAction
{
	protected EkitCore parentEkit;
	private   HTML.Tag htmlTag;
	private   Hashtable htmlAttribs;

	/**
	 * @param ekit - The main EkitCore object.
	 * @param actionName - The name of the specific action.
	 * @param inTag - The HTML tag associated with the action.
	 * @param attribs - Attributes for the new action.
	 */
	public CustomAction(EkitCore ekit, String actionName, HTML.Tag inTag, Hashtable attribs)
	{
		super(actionName);
		parentEkit  = ekit;
		htmlTag     = inTag;
		htmlAttribs = attribs;
	}

	/**
	 * @param ekit  - The main EkitCore object.
	 * @param actionName - The name of the specific action.
	 * @param inTag - The HTML tag associated with the action.
	 */
	public CustomAction(EkitCore ekit, String actionName, HTML.Tag inTag)
	{
		this(ekit, actionName, inTag, new Hashtable());
	}

	
	/** 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae)
	{
		if(this.isEnabled())
		{
			Hashtable htmlAttribs2 = new Hashtable();
			JTextPane parentTextPane = parentEkit.getTextPane();
			String selText = parentTextPane.getSelectedText();
			int textLength = -1;
			if(selText != null)
			{
				textLength = selText.length();
			}
			if(selText == null || textLength < 1)
			{
				SimpleInfoDialog sidWarn = new SimpleInfoDialog(parentEkit.getFrame(), "Error", true, "No Text Selected", SimpleInfoDialog.ERROR);
			}
			else
			{
				int caretOffset = parentTextPane.getSelectionStart();
				int internalTextLength = selText.length();
				String currentAnchor = "";
				// Somewhat ham-fisted code to obtain the first HREF in the selected text,
				// which (if found) is passed to the URL HREF request dialog.
				if(htmlTag.toString().equals(HTML.Tag.A.toString()))
				{
					SimpleAttributeSet sasText = null;
					for(int i = caretOffset; i < caretOffset + internalTextLength; i++)
					{
						parentTextPane.select(i, i + 1);
						sasText = new SimpleAttributeSet(parentTextPane.getCharacterAttributes());
						Enumeration attribEntries1 = sasText.getAttributeNames();
						while(attribEntries1.hasMoreElements() && currentAnchor.equals(""))
						{
							Object entryKey   = attribEntries1.nextElement();
							Object entryValue = sasText.getAttribute(entryKey);
							if(entryKey.toString().equals(HTML.Tag.A.toString()))
							{
								if(entryValue instanceof SimpleAttributeSet)
								{
									Enumeration subAttributes = ((SimpleAttributeSet)entryValue).getAttributeNames();
									while(subAttributes.hasMoreElements() && currentAnchor.equals(""))
									{
										Object subKey = subAttributes.nextElement();
										if(subKey.toString().toLowerCase().equals("href"))
										{
											currentAnchor = ((SimpleAttributeSet)entryValue).getAttribute(subKey).toString();
											break;
										}
									}
								}
							}
						}
						if(!currentAnchor.equals("")) { break; }
					}
				}
				parentTextPane.select(caretOffset, caretOffset + internalTextLength);
				SimpleAttributeSet sasTag  = new SimpleAttributeSet();
				SimpleAttributeSet sasAttr = new SimpleAttributeSet();
				if(htmlTag.toString().equals(HTML.Tag.A.toString()))
				{
					if(!htmlAttribs.containsKey("href"))
					{
						UserInputAnchorDialog uidInput = new UserInputAnchorDialog(parentEkit, "Hyperlink Reference", true, currentAnchor);
						String newAnchor = uidInput.getInputText();
						uidInput.dispose();
						if(newAnchor != null)
						{
							htmlAttribs2.put("href", newAnchor);
						}
						else
						{
							parentEkit.repaint();
							return;
						}
					}
				}
				else if(htmlTag.toString().equals(HTML.Tag.FONT.toString()))
				{
					if(htmlAttribs.containsKey("color"))
					{
						Color color = new JColorChooser().showDialog(parentEkit.getFrame(), "Choose Text Color", Color.black);
					    if(color != null)
						{
							StyledEditorKit.ForegroundAction customColorAction = new StyledEditorKit.ForegroundAction("CustomColor", color);
							customColorAction.actionPerformed(ae);
						}
					}
				}
				if(htmlAttribs2.size() > 0)
				{
					Enumeration attribEntries = htmlAttribs2.keys();
					while(attribEntries.hasMoreElements())
					{
						Object entryKey   = attribEntries.nextElement();
						Object entryValue = htmlAttribs2.get(entryKey);
						insertAttribute(sasAttr, entryKey, entryValue);
					}
					SimpleAttributeSet baseAttrs = new SimpleAttributeSet(parentEkit.getTextPane().getCharacterAttributes());
					Enumeration attribEntriesOriginal = baseAttrs.getAttributeNames();
					while(attribEntriesOriginal.hasMoreElements())
					{
						Object entryKey   = attribEntriesOriginal.nextElement();
						Object entryValue = baseAttrs.getAttribute(entryKey);
						insertAttribute(sasAttr, entryKey, entryValue);
					}
					sasTag.addAttribute(htmlTag, sasAttr);
					parentTextPane.setCharacterAttributes(sasTag, false);
					parentEkit.refreshOnUpdate();
				}
				parentTextPane.select(caretOffset, caretOffset + internalTextLength);
				parentTextPane.requestFocus();
			}
		}
	}

	/**
	 * @param attrs - The CSS attribute to be inserted.
	 * @param key - The key used to index the attribute for internal storage.
	 * @param value - The value of the attribute found at the key.
	 */
	private void insertAttribute(SimpleAttributeSet attrs, Object key, Object value)
	{
		if(value instanceof AttributeSet)
		{
			AttributeSet subSet = (AttributeSet)value;
			Enumeration attribEntriesSub = subSet.getAttributeNames();
			while(attribEntriesSub.hasMoreElements())
			{
				Object subKey   = attribEntriesSub.nextElement();
				Object subValue = subSet.getAttribute(subKey);
				insertAttr(attrs, subKey, subValue);
			}
		}
		else
		{
			insertAttr(attrs, key, value);
		}
		// map CSS font-family declarations to FONT tag face declarations
		if(key.toString().toLowerCase().equals("font-family"))
		{
			if(attrs.isDefined("face"))
			{
				insertAttr(attrs, "face", attrs.getAttribute("face"));
				insertAttr(attrs, "font-family", attrs.getAttribute("face"));
			}
			else
			{
				insertAttr(attrs, "face", value);
			}
		}
		// map CSS font-size declarations to FONT tag size declarations
/*
		if(key.toString().toLowerCase().equals("font-size"))
		{
			if(attrs.isDefined("size"))
			{
				insertAttr(attrs, "size", attrs.getAttribute("size"));
				insertAttr(attrs, "font-size", attrs.getAttribute("size"));
			}
			else
			{
				insertAttr(attrs, "size", value);
			}
		}
*/
	}

	/**
	 * @param attrs - The set of attributes in which new attribues will be inserted.
	 * @param key - The key of a particular attribute.
	 * @param value - The value of the new attribute at the specific key.
	 */
	private void insertAttr(SimpleAttributeSet attrs, Object key, Object value)
	{
		while(attrs.isDefined(key))
		{
			attrs.removeAttribute(key);
		}
		attrs.addAttribute(key, value);
	}
}

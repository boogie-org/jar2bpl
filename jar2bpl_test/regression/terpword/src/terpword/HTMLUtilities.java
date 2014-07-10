/*
GNU Lesser General Public License

HTMLUtilities - Special Utility Functions For Ekit
Copyright (C) 2003 Rafael Cieplinski & Howard Kistler

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
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.html.HTML;


public class HTMLUtilities
{
	EkitCore parent;
	Hashtable tags = new Hashtable();

	/**
	 * Constructor
	 * @param newParent - main EkitCore object
	 */
	public HTMLUtilities(EkitCore newParent)
	{
		parent = newParent;
		HTML.Tag[] tagList = HTML.getAllTags();
		for(int i = 0; i < tagList.length; i++)
		{
			tags.put(tagList[i].toString(), tagList[i]);
		}
	}


	/**
	 * @param content - the list element to be inserted.
	 */
	public void insertListElement(String content)
	{
		int pos = parent.getCaretPosition();
		String source = parent.getSourcePane().getText();
		boolean hit = false;
		String idString;
		int counter = 0;
		do
		{
			hit = false;
			idString = "diesisteineidzumsuchenimsource" + counter;
			if(source.indexOf(idString) > -1)
			{
				counter++;
				hit = true;
				if(counter > 10000)
				{
					return;
				}
			}
		} while(hit);
		Element element = getListItemParent();
		if(element == null)
		{
			return;
		}
		SimpleAttributeSet sa = new SimpleAttributeSet(element.getAttributes());
		sa.addAttribute("id", idString);
		parent.getExtendedHtmlDoc().replaceAttributes(element, sa, HTML.Tag.LI);
		parent.refreshOnUpdate();
		source = parent.getSourcePane().getText();
		StringBuffer newHtmlString = new StringBuffer();
		int[] positions = getPositions(element, source, true, idString);
		newHtmlString.append(source.substring(0, positions[3]));
		newHtmlString.append("<li>");
		newHtmlString.append(content);
		newHtmlString.append("</li>");
		newHtmlString.append(source.substring(positions[3] + 1, source.length()));
		parent.getTextPane().setText(newHtmlString.toString());
		parent.refreshOnUpdate();
		parent.setCaretPosition(pos - 1);
		element = getListItemParent();
		sa = new SimpleAttributeSet(element.getAttributes());
		sa = removeAttributeByKey(sa, "id");
		parent.getExtendedHtmlDoc().replaceAttributes(element, sa, HTML.Tag.LI);
	}


	/**
	 * @param element - the element to be removed.
	 * @param closingTag - the corresponding HTML tag.
	 */
	public void removeTag(Element element, boolean closingTag)
	{
		if(element == null)
		{
			return;
		}
		int pos = parent.getCaretPosition();
		HTML.Tag tag = getHTMLTag(element);
		// Versieht den Tag mit einer einmaligen ID
		String source = parent.getSourcePane().getText();
		boolean hit = false;
		String idString;
		int counter = 0;
		do
		{
			hit = false;
			idString = "diesisteineidzumsuchenimsource" + counter;
			if(source.indexOf(idString) > -1)
			{
				counter++;
				hit = true;
				if(counter > 10000)
				{
					return;
				}
			}
		} while(hit);
		SimpleAttributeSet sa = new SimpleAttributeSet(element.getAttributes());
		sa.addAttribute("id", idString);
		parent.getExtendedHtmlDoc().replaceAttributes(element, sa, tag);
		parent.refreshOnUpdate();
		source = parent.getSourcePane().getText();
		StringBuffer newHtmlString = new StringBuffer();
		int[] position = getPositions(element, source, closingTag, idString);
		if(position == null)
		{
			return;
		}
		for(int i = 0; i < position.length; i++)
		{
			if(position[i] < 0)
			{
				return;
			}
		}
		int beginStartTag = position[0];
		int endStartTag = position[1];
		if(closingTag)
		{
			int beginEndTag = position[2];
			int endEndTag = position[3];
			newHtmlString.append(source.substring(0, beginStartTag));
			newHtmlString.append(source.substring(endStartTag, beginEndTag));
			newHtmlString.append(source.substring(endEndTag, source.length()));
		}
		else
		{
			newHtmlString.append(source.substring(0, beginStartTag));
			newHtmlString.append(source.substring(endStartTag, source.length()));
		}
		parent.getTextPane().setText(newHtmlString.toString());
		parent.refreshOnUpdate();
	}

	/**
	 * @param element - the beginning HTML tag.
	 * @param source - the text to search in.
	 * @param closingTag - the HTML closing tag. 
	 * @param idString - the text to search for.
	 * @return the indecies of the text occurences. 
	 */
	private int[] getPositions(Element element, String source, boolean closingTag, String idString)
	{
		HTML.Tag tag = getHTMLTag(element);
		int[] position = new int[4];
		for(int i = 0; i < position.length; i++)
		{
			position[i] = -1;
		}
		String searchString = "<" + tag.toString();
		int caret = -1; // aktuelle Position im sourceString
		if((caret = source.indexOf(idString)) != -1)
		{
			position[0] = source.lastIndexOf("<",caret);
			position[1] = source.indexOf(">",caret)+1;
		}
		if(closingTag)
		{
			String searchEndTagString = "</" + tag.toString() + ">";
			int hitUp = 0;
			int beginEndTag = -1;
			int endEndTag = -1;
			caret = position[1];
			boolean end = false;
			// Position des 1. Treffer auf den End-Tag wird bestimmt
			beginEndTag = source.indexOf(searchEndTagString, caret);
			endEndTag = beginEndTag + searchEndTagString.length();
			// Schleife läuft solange, bis keine neuen StartTags mehr gefunden werden
			int interncaret = position[1];
			do
			{
				int temphitpoint = -1;
				boolean flaghitup = false;
				// Schleife sucht zwischen dem Start- und End-Tag nach neuen Start-Tags
				hitUp = 0;
				do
				{
					flaghitup = false;
					temphitpoint = source.indexOf(searchString, interncaret);
					if(temphitpoint > 0 && temphitpoint < beginEndTag)
					{
						hitUp++;
						flaghitup = true;
						interncaret = temphitpoint + searchString.length();
					}
				} while(flaghitup);
				// hitUp enthält die Anzahl der neuen Start-Tags
				if(hitUp == 0)
				{
					end = true;
				}
				else
				{
					for(int i = 1; i <= hitUp; i++)
					{
						caret = endEndTag;
						beginEndTag = source.indexOf(searchEndTagString, caret);
						endEndTag = beginEndTag + searchEndTagString.length();
					}
					end = false;
				}
			} while(!end);
			if(beginEndTag < 0 | endEndTag < 0)
			{
				return null;
			}
			position[2] = beginEndTag;
			position[3] = endEndTag;
		}
		return position;
	}

	/**
	 * @param tag - the HTML tag to be checked.
	 * @return the boolean validity of the specified tag.
	 */
	public boolean checkParentsTag(HTML.Tag tag)
	{
		Element e = parent.getExtendedHtmlDoc().getParagraphElement(parent.getCaretPosition());
		String tagString = tag.toString();
		if(e.getName().equalsIgnoreCase(tag.toString()))
		{
			return true;
		}
		do
		{
			if((e = e.getParentElement()).getName().equalsIgnoreCase(tagString))
			{
				return true;
			}
		} while(!(e.getName().equalsIgnoreCase("html")));
		return false;
	}

	/**
	 * @return the element corresponding to the parent of a list item.
	 */
	public Element getListItemParent()
	{
		String listItemTag = HTML.Tag.LI.toString();
		Element eleSearch = parent.getExtendedHtmlDoc().getCharacterElement(parent.getCaretPosition());
		do
		{
			if(listItemTag.equals(eleSearch.getName()))
			{
				return eleSearch;
			}
			eleSearch = eleSearch.getParentElement();
		} while(eleSearch.getName() != HTML.Tag.HTML.toString());
		return null;
	}

	/**
	 * @param sourceAS - the set of attributes.
	 * @param removeKey - the key to be removed from the set.
	 * @return the new attribute set.
	 */
	public SimpleAttributeSet removeAttributeByKey(SimpleAttributeSet sourceAS, String removeKey)
	{
		SimpleAttributeSet temp = new SimpleAttributeSet();
		temp.addAttribute(removeKey, "NULL");
		return removeAttribute(sourceAS, temp);
	}

	/**
	 * @param sourceAS - the set of attributes.
	 * @param removeAS - the key to be removed from the set.
	 * @return the new attribute set.
	 */
	public SimpleAttributeSet removeAttribute(SimpleAttributeSet sourceAS, SimpleAttributeSet removeAS)
	{
		try
		{
			String[] sourceKeys = new String[sourceAS.getAttributeCount()];
			String[] sourceValues = new String[sourceAS.getAttributeCount()];
			Enumeration sourceEn = sourceAS.getAttributeNames();
			int i = 0;
			while(sourceEn.hasMoreElements())
			{
				Object temp = new Object();
				temp = sourceEn.nextElement();
				sourceKeys[i] = (String) temp.toString();
				sourceValues[i] = new String();
				sourceValues[i] = (String) sourceAS.getAttribute(temp).toString();
				i++;
			}
			String[] removeKeys = new String[removeAS.getAttributeCount()];
			String[] removeValues = new String[removeAS.getAttributeCount()];
			Enumeration removeEn = removeAS.getAttributeNames();
			int j = 0;
			while(removeEn.hasMoreElements())
			{
				removeKeys[j] = (String) removeEn.nextElement().toString();
				removeValues[j] = (String) removeAS.getAttribute(removeKeys[j]).toString();
				j++;
			}
			SimpleAttributeSet result = new SimpleAttributeSet();
			boolean hit = false;
			for(int countSource = 0; countSource < sourceKeys.length; countSource++)
			{
				hit = false;
				if(sourceKeys[countSource] == "name" | sourceKeys[countSource] == "resolver")
				{
					hit = true;
				}
				else
				{
					for(int countRemove = 0; countRemove < removeKeys.length; countRemove++)
					{
						if(removeKeys[countRemove] != "NULL")
						{
							if(sourceKeys[countSource].toString() == removeKeys[countRemove].toString())
							{
								if(removeValues[countRemove] != "NULL")
								{
									if(sourceValues[countSource].toString() == removeValues[countRemove].toString())
									{
										hit = true;
									}
								}
								else if(removeValues[countRemove] == "NULL")
								{
									hit = true;
								}
							}
						}
						else if(removeKeys[countRemove] == "NULL")
						{
							if(sourceValues[countSource].toString() == removeValues[countRemove].toString())
							{
								hit = true;
							}
						}
					}
				}
				if(!hit)
				{
					result.addAttribute(sourceKeys[countSource].toString(), sourceValues[countSource].toString());
				}
			}
			return result;
		}
		catch (ClassCastException cce)
		{
			return null;
		}
	}

	/**
	 * @param e - the HTML element to be located. 
	 * @return the HTML tag.
	 */
	public HTML.Tag getHTMLTag(Element e)
	{
		if(tags.containsKey(e.getName()))
		{
			return (HTML.Tag)tags.get(e.getName());
		}
		else
		{
			return null;
		}
	}

	/**
	 * @param strings - the number of strings. 
	 * @return - the desired Unicode strings.
	 */
	public String[] getUniString(int strings)
	{
		parent.refreshOnUpdate();
		String[] result = new String[strings];
		String source = parent.getSourcePane().getText();
		for(int i=0; i<strings; i++)
		{
			int start = -1, end = -1;
			boolean hit = false;
			String idString;
			int counter = 0;
			do
			{
				hit = false;
				idString = "diesisteineidzumsuchen" + counter + "#" + i;
				if(source.indexOf(idString) > -1)
				{
					counter++;
					hit = true;
					if(counter > 10000)
					{
						return null;
					}
				}
			} while(hit);
			result[i] = idString;
		}
		return result;
	}

	/**
	 * @throws BadLocationException - if a location error occurs.
	 * @throws IOException - if an IO error occurs.
	 */
	public void delete()
	throws BadLocationException,IOException
	{
		JTextPane jtpMain = parent.getTextPane();
		JTextPane jtpSource = parent.getSourcePane();
		ExtendedHTMLDocument htmlDoc = parent.getExtendedHtmlDoc();
		int selStart = jtpMain.getSelectionStart();
		int selEnd = jtpMain.getSelectionEnd();
		String[] posStrings = getUniString(2);
		if(posStrings == null)
		{
			return;
		}
		htmlDoc.insertString(selStart,posStrings[0],null);
		htmlDoc.insertString(selEnd+posStrings[0].length(),posStrings[1],null);
		parent.refreshOnUpdate();
		int start = jtpSource.getText().indexOf(posStrings[0]);
		int end = jtpSource.getText().indexOf(posStrings[1]);
		if(start == -1 || end == -1)
		{
			return;
		}
		String htmlString = new String();
		htmlString += jtpSource.getText().substring(0,start);
		htmlString += jtpSource.getText().substring(start + posStrings[0].length(), end);
		htmlString += jtpSource.getText().substring(end + posStrings[1].length(), jtpSource.getText().length());
		String source = htmlString;
		end = end - posStrings[0].length();
		htmlString = new String();
		htmlString += source.substring(0,start);
		htmlString += getAllTableTags(source.substring(start, end));
		htmlString += source.substring(end, source.length());
		parent.getTextPane().setText(htmlString);
		parent.refreshOnUpdate();
	}

	/**
	 * @param source - the text to search in.
	 * @return - the located table tags. 
	 * @throws BadLocationException - if a location error occurs.
	 * @throws IOException if an IO error occurs.
	 */
	private String getAllTableTags(String source)
	throws BadLocationException,IOException
	{
		StringBuffer result = new StringBuffer();
		int caret = -1;
		do
		{
			caret++;
			int[] tableCarets = new int[6];
			tableCarets[0] = source.indexOf("<table",caret);
			tableCarets[1] = source.indexOf("<tr",caret);
			tableCarets[2] = source.indexOf("<td",caret);
			tableCarets[3] = source.indexOf("</table",caret);
			tableCarets[4] = source.indexOf("</tr",caret);
			tableCarets[5] = source.indexOf("</td",caret);
			java.util.Arrays.sort(tableCarets);
			caret = -1;
			for(int i=0; i<tableCarets.length; i++)
			{
				if(tableCarets[i] >= 0)
				{
					caret = tableCarets[i];
					break;
				}
			}
			if(caret != -1)
			{
				result.append(source.substring(caret,source.indexOf(">",caret)+1));
			}
		} while(caret != -1);
		return result.toString();
	}

}
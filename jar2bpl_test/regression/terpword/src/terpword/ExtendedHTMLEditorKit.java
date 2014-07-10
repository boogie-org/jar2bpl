/*
GNU Lesser General Public License

ExtendedHTMLEditorKit
Copyright (C) 2001  Frits Jalvingh, Jerry Pommer & Howard Kistler

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
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;


/**
  * This class extends HTMLEditorKit so that it can provide other renderer classes
  * instead of the defaults. Most important is the part which renders relative
  * image paths.
  *
  * @author <a href="mailto:jal@grimor.com">Frits Jalvingh</a>
  * @version 1.0
  */

public class ExtendedHTMLEditorKit extends HTMLEditorKit
{
	/** Constructor
	  */
	public ExtendedHTMLEditorKit()
	{
	}

	/** Method for returning a ViewFactory which handles the image rendering.
	  */
	public ViewFactory getViewFactory()
	{
		return new HTMLFactoryExtended();
	}

	/* (non-Javadoc)
	 * @see javax.swing.text.EditorKit#createDefaultDocument()
	 */
	public Document createDefaultDocument()
	{
		StyleSheet styles = getStyleSheet();
		StyleSheet ss = new StyleSheet();
		ss.addStyleSheet(styles);
		ExtendedHTMLDocument doc = new ExtendedHTMLDocument(ss);
		doc.setParser(getParser());
		doc.setAsynchronousLoadPriority(4);
		doc.setTokenThreshold(100);
		return doc;
	}

/* Inner Classes --------------------------------------------- */

	/** Class that replaces the default ViewFactory and supports
	  * the proper rendering of both URL-based and local images.
	  */
	public static class HTMLFactoryExtended extends HTMLFactory implements ViewFactory
	{
		/** Constructor
		  */
		public HTMLFactoryExtended()
		{
		}

		/** Method to handle IMG tags and
		  * invoke the image loader.
		  */
		public View create(Element elem)
		{
			Object obj = elem.getAttributes().getAttribute(StyleConstants.NameAttribute);
			if(obj instanceof HTML.Tag)
			{
				HTML.Tag tagType = (HTML.Tag)obj;
				if(tagType == HTML.Tag.IMG)
				{
					return new RelativeImageView(elem);
				}
			}
			return super.create(elem);
		}
	}
}

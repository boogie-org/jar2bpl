package terpword;

/**
 * @author Peter Pichler
 *  
 */

import javax.swing.text.*;
import javax.swing.text.rtf.*;

import java.io.*;

/**
 *  Converts RTF text to HTML.
 */
public class CsdRtfConverter
{

	/**
	 * @param txt - The RTF string to be converted to HTML.
	 * @return  - the converted HTML text as a string
	 * @throws - Exception if the RTF cannot be converted.
	 */
	public static final String convertRtfToHtml(final String txt) throws Exception 
	{
		final RTFEditorKit rtf_edit = new RTFEditorKit();
		final StyleContext rtf_context = new StyleContext();
		final DefaultStyledDocument rtf_doc = new DefaultStyledDocument( rtf_context );
		try 
		{
			rtf_edit.read(new StringReader(txt), rtf_doc, 0);
			StringWriter writer = new StringWriter();
			CsdMinimalHtmlWriter html_edit = new CsdMinimalHtmlWriter(writer, rtf_doc, 0, rtf_doc.getLength());
			html_edit.write();
			return writer.toString().replaceAll(" ", "&nbsp;");
		} catch (Exception ex) 
		{
			throw new Exception("Error while converting rtf: " + ex.toString(), ex);
		}
	}
}
package terpword;
/**
 * @author Peter Pichler
 *
 */

import java.io.Writer;

import javax.swing.text.StyledDocument;
import javax.swing.text.html.MinimalHTMLWriter;

/**
 * Provides storage of basic values for use in text output, such as the
 * line separator and indentation amount. 
 */
public class CsdMinimalHtmlWriter extends MinimalHTMLWriter 
{
	/**
	 * @param w - The active Writer.
	 * @param doc - A CSS document to be used in styling.
	 * @param pos - The position within the file.
	 * @param len - The length of the write transaction.
	 */
	public CsdMinimalHtmlWriter(Writer w, StyledDocument doc, int pos, int len) 
	{
		super(w, doc, pos, len);
		initialize();
	}

	/**
	 *  Initializes values for use in text output.
	 */
	private void initialize() 
	{
		setIndentSpace(0);
		setLineSeparator("");
		setLineLength(Integer.MAX_VALUE);
	}

}
package terpword;


/**
 * @author Serban Padencov
 *  Methods to convert text to html and vice-versa.
 */

public class TxtConverter {

	/**
	 * converts text to html 
	 * 
	 * @param txt - plaintext to be converted
	 * @return String - returns converted text (html text)
	 * @throws Exception - failed conversion exception
	 */
	public String convertTxtToHtml(final String txt) throws Exception {
		String file = new String();
		file = file.concat("<html><head></head><body>");
		file = file.concat(txt);
		file = file.concat("</body></html>");
		file = file.replaceAll(System.getProperty("line.separator"), "<br>");
		file = file.replaceAll(" ", "&nbsp;");
		return file;
	}

	/**
	 * converts html to text
	 * 
	 * @param txt - html text to be converted
	 * @return String - returns converted text (plaintext)
	 * @throws Exception - failed conversion exception
	 */
	public String convertHtmlToTxt(final String txt) throws Exception {
		String file = new String(txt);
	 	file = file.replaceAll("\\<.*?\\>","");
	 	file= file.trim();
		return file;
	}
}
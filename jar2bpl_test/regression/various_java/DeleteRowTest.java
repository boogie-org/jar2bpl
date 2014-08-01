

import javax.swing.text.BadLocationException;
import javax.swing.text.html.parser.Element;

public abstract class DeleteRowTest {

	public int len, pos;
	
	Element element;
	protected abstract Element magic(Element element);
	protected abstract Element magic(int idx);
	protected abstract int num(Element e);
	
	public void deleteTableRow() throws BadLocationException {
		int caretPos = pos;
		Element element = magic(pos);
		Element elementParent = magic(element);
		int startPoint = -1;
		int endPoint = -1;
		while (elementParent != null && !elementParent.getName().equals("body")) {
			if (elementParent.getName().equals("tr")) {
				startPoint = num(elementParent);
				endPoint = num(elementParent);
				break;
			} else {
				elementParent = magic(elementParent);
			}
		}
		if (startPoint > -1 && endPoint > startPoint) {
			if (caretPos >= len) {
				caretPos = len;
			}
		}
	}

}

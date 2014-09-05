

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class TruePositive04 {
	
	private static final int UNICODEBLOCKSIZE = 0;
	private Object parentEkit;
	private Font buttonFont;
	private JToggleButton[] buttonArray = new JToggleButton[UNICODEBLOCKSIZE];
	private ButtonGroup buttonGroup;
	private JComboBox jcmbBlockSelector;
	private JComboBox jcmbPageSelector;
	
	/**
	 * creates and initializes the unicode dialog 
	 * @param startIndex - index of selected text
	 */
	public void init(int startIndex)
	{
		String customFont = "";
		if(customFont != null && customFont.length() > 0)
		{
			buttonFont = new Font("", Font.PLAIN, 12);
		}
		else
		{
			buttonFont = new Font("Monospaced", Font.PLAIN, 12); //that should be unreachable
		}
	}

	/** snippet from terpword that is reported but not obviously a true positive.
	 * The infeasible code is 
	 * j >= 0 in  "while (end == -1 && j >= 0)";
	 * The reason why it is infeasible is that the loop can never terminate if
	 * end == -1 because if j would be less than 0, the deref of temp throw an
	 * exception before reachen the check.
	 * @param temp
	 */
	public void foo(char[] temp) {
		int end = -1;
		int j = temp.length;
		do {
			j--;
			if (new Character(temp[j]).isLetterOrDigit(temp[j])) {
				end = j;
			}
		} while (end == -1 && j >= 0);
		j = end;		
	}
}

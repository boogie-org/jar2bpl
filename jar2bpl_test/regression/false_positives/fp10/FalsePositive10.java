

public class FalsePositive10 {

	//two snippets from terpword that were reported but are false positives.
	
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
	
	private String getText() {return "";}
	
	/**
	 * @return boolean - true if image is valid
	 */
	public boolean validateControls()
	{
		boolean result = true;
		if(!getText().equals(""))
		{
			try
			{
				Integer.parseInt(getText());
			}
			catch (NumberFormatException e)
			{
				result = false;
			}
		}
		if( result && !getText().equals(""))
		{
			try
			{
				Integer.parseInt(getText());
			}
			catch (NumberFormatException e)
			{
				result = false;
			}
		}
		return result;
	}	
	
}

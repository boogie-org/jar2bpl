

public class FalsePositive10 {
	
	private String getText() {return "";}
	
	/**
	 * @return boolean - true if image is valid
	 */
	public boolean validateControls() {
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

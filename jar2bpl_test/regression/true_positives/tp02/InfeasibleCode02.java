

/**
 * @author schaef
 *
 */
public class InfeasibleCode02 {

	public void infeasible01()
	{
		String customFont = "";
		if(customFont != null && customFont.length() > 0)
		{
			System.err.println("From Terpword, UnicodeDialog.java, line 287.");
			return;
		}
		else
		{
			return;
		}
	}
	

	public void feasible01(String source) {
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
	}
	
	
	public void feasibe02(Object htmlTag) {
		if (htmlTag.toString().compareTo("strike")==0)
		{
			String[] str = {"text-decoration","strike"};			
		}
		else if (htmlTag.toString().compareTo("sup")==0)
		{
			String[] str = {"vertical-align","sup"};			
		}
		else if (htmlTag.toString().compareTo("sub")==0)
		{
			String[] str = {"vertical-align","sub"};			
		}
	}

	int x;
	public void infeasible02() {
		boolean hit = false;
		int counter = 0;
		do
		{
			hit = false;			
			if(x==counter)
			{
				counter++;
				hit = true;
				if(counter > 10000)
				{
					return;
				}
			}
		} while(hit);		
	}

	public void infeasible03(char[] temp) {
	int repos = -1;
	int end = -1;
	int j = end;
	do {
		j++;
		if (temp[j]=='a') {
			repos = j - end - 1;
		}
	} while (repos == -1 && j < temp.length);
	if (repos == -1) {
		repos=0; //unreachable
	}
}
	
}




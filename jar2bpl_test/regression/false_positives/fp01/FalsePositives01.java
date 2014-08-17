import java.util.Enumeration;


public class FalsePositives01 {

	private String[] sourceKeys;
	String[] sourceValues;

	String[] removeKeys;
	String[] removeValues;
	
	private int countSource;
	

//	public int foo() {return 1;}
//	public Enumeration bar() {return null;}
//	
//	public void removeAttribute()
//	{
//		try
//		{
//			
//			boolean hit = false;
//			for(int countSource = 0; countSource < sourceKeys.length; countSource++)
//			{
//				hit = false;
//				if(sourceKeys[countSource] == "name" )
//				{
//					hit = true;
//				}
//
//							
//				if(!hit)
//				{
//					//THIS IS THE IMPORTANT LINE!
//					sourceKeys[countSource].toString(); 
//				}
//			}
//		}
//		catch (ClassCastException cce)
//		{
//			
//		}
//	}
//	
	
//	public void FalsePositive01() {
//		boolean hit = false;		
//		int counter = 0;
//		do
//		{
//			hit = false;
//			if(countSource  > counter)
//			{
//				counter++;
//				hit = true;
//				if(counter > 10000)
//				{
//					return;
//				}
//			}
//		} while(hit);		
//		countSource = 3; //do something just to have code here.
//	}
//
	public void FalsePositive02(Object htmlTag) {
		if (htmlTag.toString().compareTo("strike")==0)
		{
			countSource=1;
		}
		else if (htmlTag.toString().compareTo("sup")==0)
		{
			countSource=2;
		}
		else if (htmlTag.toString().compareTo("sub")==0)
		{
			countSource=3;
		}		
	}
	
}

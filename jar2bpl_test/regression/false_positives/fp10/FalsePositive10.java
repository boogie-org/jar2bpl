

public class FalsePositive10 {

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

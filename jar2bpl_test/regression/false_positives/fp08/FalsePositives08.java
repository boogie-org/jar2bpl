

public class FalsePositives08 {
	Object constraintsLock;
	private String[] constraints;
	private String constraint;
	public void fp01() {
        synchronized (constraintsLock) {
            String results[] =
                new String[constraints.length + 1];
            for (int i = 0; i < constraints.length; i++);           
			results[constraints.length] = constraint;
        }
	}
}


public class ExpSolverMain {
	
	public static void main(String[] args) {
		ExpSolver expSolver = null;
		try {
			expSolver = new ExpSolver();
			expSolver.start();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (expSolver != null) {
				expSolver.stop();
			}
		}
		
	}
	
}

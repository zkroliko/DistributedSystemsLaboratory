
public class ExpSolverMain {
	
	public static void main(String[] args) {
		ExpSolver expSolver = null;
		try {
			if (args.length > 0) {
				String arg = args[0];
				if ("+-*/".contains(arg) && arg.length() == 1)
					expSolver = new ExpSolver(args[0]);
				else {
					System.err.println("Invalid operation as argument");
					System.exit(-1);
				}
			} else {
				expSolver = new ExpSolver();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (expSolver != null) {
				expSolver.stop();
			}
		}
		
	}
	
}

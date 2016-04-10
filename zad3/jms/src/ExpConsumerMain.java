
public class ExpConsumerMain {
	
	public static void main(String[] args) {
		ExpConsumer expConsumer = null;
		try {
			if (args.length > 0) {
                String arg = args[0];
                if ("+-*/".contains(arg) && arg.length() == 1)
                    expConsumer = new ExpConsumer(args[0]);
                else {
                    System.err.println("Invalid operation as argument");
                    System.exit(-1);
                }
            } else {
                expConsumer = new ExpConsumer();
            }
			expConsumer.start();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (expConsumer != null) {
				expConsumer.stop();
			}
		}
	}
}

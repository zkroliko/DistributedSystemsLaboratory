
public class ExpProducerMain {
	
	public static void main(String[] args) {
		ExpProducer expProducer = null;
		try {
			if (args.length > 0) {
				String arg = args[0];
				if ("+-*/".contains(arg) && arg.length() == 1) {
                    expProducer = new ExpProducer(args[0]);
                } else {
					System.err.println("Invalid operation as argument");
					System.exit(-1);
				}
			} else {
				expProducer = new ExpProducer();
			}
            expProducer.start();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (expProducer != null) {
				expProducer.stop();
			}
		}
		
	}
	
}


public class ExpProducerMain {
	
	public static void main(String[] args) {
		ExpProducer expProducer = null;
		try {
			expProducer = new ExpProducer();
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

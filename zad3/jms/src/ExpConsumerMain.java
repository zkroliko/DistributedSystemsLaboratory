
public class ExpConsumerMain {
	
	public static void main(String[] args) {
		ExpConsumer expConsumer = null;
		try {
			expConsumer = new ExpConsumer();
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

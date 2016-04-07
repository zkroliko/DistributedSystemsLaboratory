
public class NoteboardMain {
	
	public static void main(String[] args) {
		Noteboard noteboard = null;
		try {
			noteboard = new Noteboard();
			noteboard.start();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (noteboard != null) {
				noteboard.stop();
			}
		}
	}
}


public class NoteboardWriterMain {
	
	public static void main(String[] args) {
		NoteboardWriter noteboardWriter = null;
		try {
			noteboardWriter = new NoteboardWriter();
			noteboardWriter.start();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (noteboardWriter != null) {
				noteboardWriter.stop();
			}
		}
		
	}
	
}

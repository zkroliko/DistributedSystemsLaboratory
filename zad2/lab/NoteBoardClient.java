import java.rmi.Naming;

public class NoteBoardClient {

	public static void main(String[] args) {
		try {
			INoteBoard nb = (INoteBoard) Naming.lookup( "rmi://127.0.0.1/note" );
			System.err.println( "dodajemy: aqq1, aqq2, aqq3" );
			nb.appendText( "aqq1" );
			nb.appendText( "aqq2" );
			nb.appendText( "aqq3" );
			System.err.println( "Sprawdzamy, co jest w srodku..." );
			System.err.println( nb.getText() );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

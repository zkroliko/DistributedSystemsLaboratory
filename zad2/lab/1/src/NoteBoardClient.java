import java.rmi.Naming;
import java.rmi.RemoteException;

public class NoteBoardClient {

	public static void main(String[] args) {
		try {
			INoteBoard nb = (INoteBoard) Naming.lookup( "rmi://127.0.0.1:1099/note" );

			System.err.println( "Rejestrujemy sie" );
			User user = new UserImpl(args[0]);

			nb.register(user, new INoteBoardListener() {
				@Override
				public void onNewText(String text) throws RemoteException {
					System.out.println("Text changed");
				}
			});

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

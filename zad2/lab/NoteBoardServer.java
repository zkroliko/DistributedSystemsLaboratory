import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;

public class NoteBoardServer {
	static INoteBoard nbi;

	public static void main(String[] args) {
		try {
			nbi = new NoteBoardImpl();
			UnicastRemoteObject.exportObject(nbi, 0);
			Naming.rebind( "aqq", nbi );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

import java.rmi.RemoteException;

public class NoteBoardImpl implements INoteBoard {
	private StringBuffer buf;

	public NoteBoardImpl() {
		buf = new StringBuffer();
	}

	public void appendText(String newNote) throws RemoteException {
		buf.append("\n"+newNote);
	}

	public void clean() throws RemoteException {
		buf = new StringBuffer();
	}
}

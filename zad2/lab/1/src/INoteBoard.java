import java.io.ObjectInputStream;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface INoteBoard extends Remote {
	public String getText() throws RemoteException;
	public void appendText(String newNote) throws RemoteException;
	public void clean() throws RemoteException;
	public void register( User u, INoteBoardListener l )
			throws RemoteException, UserRejectedException;
	public void unregister(User u) throws  RemoteException;

}

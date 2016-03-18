import java.io.IOException;
import java.io.ObjectInputStream;
import java.rmi.RemoteException;
import java.util.*;

public class NoteBoardImpl implements INoteBoard {

	private StringBuffer buf;

	private List<User> users = new ArrayList<User>();

	private List<INoteBoardListener> listeners = new ArrayList<INoteBoardListener>();

    private Map<User,INoteBoardListener> registrations= new HashMap<User,INoteBoardListener>();

    public NoteBoardImpl() {
		buf = new StringBuffer();
	}

	@Override
	public String getText() throws RemoteException {
		return buf.toString();
	}

	public synchronized void appendText(String newNote) throws RemoteException {
		buf.append("\n"+newNote);
        registrations.values().stream().forEach(l -> l.notify());
	}

	public void clean() throws RemoteException {
		buf = new StringBuffer();
	}

	@Override
	public void register(User u, INoteBoardListener l) throws RemoteException, UserRejectedException {
		boolean duplicate = registrations.keySet().stream().map(user -> user.getNick()).anyMatch(s -> s.equals(u.getNick()));
		if (!duplicate) {
			registrations.put(u,l);
		} else {
            throw new UserRejectedException();
        }
	}

	@Override
	public void unregister(User u) {
        registrations.remove(u);
	}

}

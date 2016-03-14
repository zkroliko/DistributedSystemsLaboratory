package pl.edu.agh.rmi.lab;

import java.rmi.RemoteException;

public class NoteBoardImpl implements INoteBoard {

	private StringBuffer buf;

	public NoteBoardImpl() {
		buf = new StringBuffer();
	}

	@Override
	public String getText() throws RemoteException {
		return buf.toString();
	}

	public void appendText(String newNote) throws RemoteException {
		buf.append("\n"+newNote);
	}

	public void clean() throws RemoteException {
		buf = new StringBuffer();
	}
}

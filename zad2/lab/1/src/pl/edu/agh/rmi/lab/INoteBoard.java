package pl.edu.agh.rmi.lab;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface INoteBoard extends Remote {
	public String getText() throws RemoteException;
	public void appendText(String newNote) throws RemoteException;
	public void clean() throws RemoteException;
}

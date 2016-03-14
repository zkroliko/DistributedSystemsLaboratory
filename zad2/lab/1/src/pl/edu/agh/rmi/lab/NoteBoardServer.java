package pl.edu.agh.rmi.lab;

import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;

public class NoteBoardServer {

	static INoteBoard nbi;

	public static void main(String[] args) {
		try {
			nbi = new NoteBoardImpl();
			INoteBoard noteBoard = (INoteBoard) UnicastRemoteObject.exportObject(nbi, 0);
			Naming.rebind( "rmi://localhost:1099/note", noteBoard );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

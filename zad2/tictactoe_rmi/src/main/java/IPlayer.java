import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPlayer extends Remote, Serializable {
    public void onGameStarted() throws RemoteException;
    public void move() throws RemoteException;
    public void reactToVictory() throws RemoteException;
}

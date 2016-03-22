import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IListener extends Remote, Serializable {
    public void onGameStarted() throws RemoteException;
    public void onMove() throws RemoteException;
    public void onVictory() throws RemoteException;
}

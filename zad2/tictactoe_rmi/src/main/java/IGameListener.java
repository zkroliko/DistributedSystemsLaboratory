import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGameListener extends Remote, Serializable {
    public void onGameStarted() throws RemoteException;
    public void onMove() throws RemoteException;
    public void onVictory() throws RemoteException;
}

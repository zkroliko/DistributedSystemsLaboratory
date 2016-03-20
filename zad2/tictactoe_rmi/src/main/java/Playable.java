import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Playable extends Remote {
    public void register(Player player, IGameListener gameListener) throws RemoteException;
    public void makeMove(Player player, Coordinates coordinates) throws RemoteException;
    public Board getBoard() throws RemoteException;
    public Player getCurrentPlayer() throws RemoteException;
    public boolean isRunning() throws RemoteException;
    public void fillWithBots() throws RemoteException;
}

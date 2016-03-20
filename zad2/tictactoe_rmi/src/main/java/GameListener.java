import java.rmi.RemoteException;

public class GameListener implements IGameListener {

    @Override
    public void onGameStarted() throws RemoteException {
        System.out.println("Game started");
    }

    public void onMove() throws RemoteException {
        System.out.println("Move");
    }

    public void onVictory() throws RemoteException {
        System.out.println("Victory");
    }
}

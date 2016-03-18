import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface INoteBoardListener extends Remote, Serializable {
    public void onNewText(String text) throws RemoteException;
}

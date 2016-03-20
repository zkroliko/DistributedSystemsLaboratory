import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClient extends Remote, Serializable {
    public void makeMove() throws RemoteException, NegativeArgumentException;
}

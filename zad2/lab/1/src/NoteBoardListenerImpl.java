import java.io.Serializable;
import java.rmi.RemoteException;


public class NoteBoardListenerImpl implements INoteBoardListener{
    @Override
    public void onNewText(String text) throws RemoteException {
        System.out.println("Text changed");
    }
}

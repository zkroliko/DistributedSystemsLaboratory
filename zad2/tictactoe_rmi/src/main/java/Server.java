import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;

public class Server {

    private static Playable game;

    public static void main(String[] args) {
        try {
            game = new Game();
            Playable playable = (Playable) UnicastRemoteObject.exportObject(game, 0);
            Naming.rebind( "rmi://127.0.0.1:1099/game", game );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

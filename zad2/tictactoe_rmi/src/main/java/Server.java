import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;

public class Server {

    public static final String ADDRESS = "rmi://127.0.0.1:1099/game";

    private static Playable game;

    public static void main(String[] args) {
        try {
            game = new Game();
            Playable playable = (Playable) UnicastRemoteObject.exportObject(game, 0);
            Naming.rebind( ADDRESS, game );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

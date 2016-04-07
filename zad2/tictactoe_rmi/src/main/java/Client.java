import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class Client implements IListener {

    public static final String BOT_OPTION = "-b";

    private Playable game;

    private Player local;

    private Scanner scanner;

    private boolean wantBots;

    private String address;

    private boolean hasMoved;

    public Client(String[] args) {
        local = new Player(args[0],args[1].charAt(0));
        local.setListener(this);

        address = args[2];

        wantBots = args.length > 3 && args[3].equals(BOT_OPTION);

        scanner = new Scanner(System.in);
    }

    public void register() throws RemoteException, MalformedURLException, NotBoundException {
        System.err.println( "Registering with nickname " + local.getNick() );
        game = (Playable) Naming.lookup(address);
        game.register(local);

        if (wantBots) {
            game.fillWithBots();
        }
    }

    public static void main(String[] args) {
        try {
            Client client = new Client(args);
            client.distribute();
            client.register();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void distribute() throws RemoteException {
        IListener stub = (IListener) UnicastRemoteObject.exportObject(this, 0);
//        Registry reg = LocateRegistry.getRegistry();
//        reg.rebind("Client", stub);
    }


    private void makeMove() throws RemoteException {
        if (game.isRunning()) {
            displayBoard();
            Coordinates move = getCoordinates();
            game.makeMove(local, move);
        }
    }

    private Coordinates getCoordinates() throws RemoteException {
        int x = -1, y = -1;
        while ( !validInput(x,y)) {
            x = getCoordinate("x");
            y = getCoordinate("y");
            if (!validInput(x,y)) {
                System.out.println("Podano zle wspolrzedne");
            }
            if (!game.getCurrentPlayer().getNick().equals(local.getNick())) {
                System.out.println("To nie twoj ruch, teraz rusza sie: " + game.getCurrentPlayer().getNick());
            }
        }
        return new Coordinates(x,y);
    }

    private int getCoordinate(String msg) {
        System.out.println(String.format("Podaj skladnik %s ruchu", msg));
        return scanner.nextInt();
    }

    private void displayBoard() throws RemoteException {
        System.out.println("Oto plansza:\n" + game.getBoard());
    }

    private boolean validInput(int x, int y) throws RemoteException {
        if (!(x < 0 || x >= Board.SIZE_X || y < 0 || y >= Board.SIZE_Y)) {
            if (Board.fieldEmpty(x,y,game.getBoard())) {
                return true;
            }
        }
        return false;
    }

    private boolean isCurrentPlayer() throws RemoteException {
        return game.getCurrentPlayer().getNick().equals(local.getNick());
    }

    public void onGameStarted() throws RemoteException {
        System.out.println("Gra sie zaczela");
        if (isCurrentPlayer()) {
            makeMove();
        }  else {
            System.out.println(String.format("Pierwszy rusza sie %s", game.getCurrentPlayer().getNick()));
        }
    }

    public void onMove() throws RemoteException {
        displayBoard();
        if (isCurrentPlayer()) {
            System.out.println("Twoj ruch!");
            makeMove();
        } else {
            System.out.println(String.format("Teraz rusza sie %s", game.getCurrentPlayer().getNick()));
        }
    }

    public void onVictory() throws RemoteException {
        System.out.println("------------Koniec gry!!!------------ \nWygral: " + game.getWinner().getNick());
    }
}

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Client implements IClient {

    public static final String ADDRESS = "rmi://127.0.0.1:1099/game";

    public static final String BOT_OPTION = "-b";

    private Playable game;

    private Player local;

    private Scanner scanner;

    public Client(String[] args) {
        try {
            local = new Player(args[0],args[1].charAt(0));

            System.err.println( "Registering with nickname " + local.getNick() );
            game = (Playable) Naming.lookup(args[2]);

            game.register(local, new GameListener());

            if (args.length > 3 && args[3].equals(BOT_OPTION)) {
                game.fillWithBots();
            }

            scanner = new Scanner(System.in);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() throws RemoteException, NegativeArgumentException {
        while (true) {
            makeMove();
        }
    }

    public static void main(String[] args) {
        try {
            Client client = new Client(args);

            client.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makeMove() throws RemoteException, NegativeArgumentException {
            System.out.println("Oto plansza:\n" + game.getBoard());
            int x = -1, y = -1;
            while ( !validInput(x,y) || !game.getCurrentPlayer().getNick().equals(local.getNick())) {
                if (game.getCurrentPlayer().getNick().equals(local.getNick())) {
                    System.out.println("Twoj ruch!\n");
                }
                System.out.println("Podaj skladnik x ruchu");
                x = scanner.nextInt();
                System.out.println("Podaj skladnik y ruchu");
                y = scanner.nextInt();
                if (!validInput(x,y)) {
                    System.out.println("Podano zle wspolrzedne");
                }
                if (!game.getCurrentPlayer().getNick().equals(local.getNick())) {
                    System.out.println("To nie twoj ruch, teraz rusza sie: " + game.getCurrentPlayer().getNick());
                }
            }
            game.makeMove(local, new Coordinates(x,y));
    }

    private static boolean validInput(int x, int y) {
        return !(x < 0 || x >= Board.SIZE_X || y < 0 || y >= Board.SIZE_Y);
    }

}

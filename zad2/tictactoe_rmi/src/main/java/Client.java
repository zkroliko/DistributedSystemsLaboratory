import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        try {
            Playable game = (Playable) Naming.lookup( "rmi://127.0.0.1:1099/game" );

            System.err.println( "Rejestrujemy sie" );
            Player self = new Player(args[0],args[1].charAt(0));

            game.register(self);

            Scanner scanner = new Scanner(System.in);

            while(true) {
                System.out.println("Oto plansza:\n" + game.getBoard());
                int x = -1, y = -1;
                while (!validInput(x,y)) {
                    System.out.println("Podaj skladnik x ruchu");
                    x = scanner.nextInt();
                    System.out.println("Podaj skladnik y ruchu");
                    y = scanner.nextInt();
                    if (!validInput(x,y)) {
                        System.out.println("Podano zle wspolrzedne");
                    }
                }
                game.makeMove(self, new Coordinates(x,y));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean validInput(int x, int y) {
        return !(x < 0 || x >= Board.SIZE_X || y < 0 || y >= Board.SIZE_Y);
    }


}

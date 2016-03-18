import org.fluttercode.datafactory.impl.DataFactory;

import java.rmi.Naming;
import java.util.Scanner;

public class ClientBot {

    public static DataFactory df = new DataFactory();

    private static final int MAX_X = Board.SIZE_X -1;
    private static final int MAX_Y = Board.SIZE_Y -1;

    public static void main(String[] args) {
        try {
            Playable game = (Playable) Naming.lookup( "rmi://127.0.0.1:1099/game" );

            Player self = new Player(df.getName(),args[1].charAt(0));

            game.register(self);

            Scanner scanner = new Scanner(System.in);

            int x = randomX(), y = randomY();

            Board board = game.getBoard();

            while(validCoordinates(x,y,board)) {
                x = randomX();
                y = randomY();
            }

            game.makeMove(self, new Coordinates(x,y));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean validCoordinates(int x, int y, Board board) {
        try {
            if (board.getField(new Coordinates(x,y)) == ' ') {
                return true;
            }
        } catch (NegativeArgumentException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static int randomX() {
        return df.getNumberBetween(0, MAX_X);
    }

    private static int randomY() {
        return df.getNumberBetween(0, MAX_Y);
    }


}

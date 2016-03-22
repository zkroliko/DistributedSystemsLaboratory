import org.fluttercode.datafactory.impl.DataFactory;

import java.rmi.RemoteException;

public class Bot implements IListener {

    public static final String ADDRESS = "rmi://127.0.0.1:1099/game";

    public static DataFactory df = new DataFactory();

    private static final int MAX_X = Board.SIZE_X -1;
    private static final int MAX_Y = Board.SIZE_Y -1;

    private Playable game;

    private Player local;


    public Bot(Game game, Player local) {
        try {
            this.game = game;
            this.local = local;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Player makePlayer(char symbol) {
        return new Player(df.getName(),symbol);
    }

    public void makeMove() throws RemoteException, NegativeArgumentException {

        int x = randomX(), y = randomY();

        Board board = game.getBoard();

        while(!validCoordinates(x,y,board)) {
            x = randomX();
            y = randomY();
        }

        game.makeMove(local, new Coordinates(x,y));
    }

    private static boolean validCoordinates(int x, int y, Board board) {
        try {
            if (board.getField(new Coordinates(x,y)) == Board.EMPTY) {
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


    @Override
    public void onGameStarted() throws RemoteException {

    }

    @Override
    public void onMove() throws RemoteException {

    }

    @Override
    public void onVictory() throws RemoteException {

    }
}

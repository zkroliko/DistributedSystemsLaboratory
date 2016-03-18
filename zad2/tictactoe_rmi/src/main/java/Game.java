import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.max;
import static java.lang.Integer.min;

public class Game implements Playable {

    public static final int MAX_PLAYERS = 2;

    private Board board;

    private List<Player> players;

    private Player currentPlayer;

    public Game() {
        board = new Board();
        players = new ArrayList<Player>();
    }

    public void register(Player player) throws RemoteException {
        if (players.size() < MAX_PLAYERS) {
            players.add(player);
            if (players.size() == 1) {
                currentPlayer = player;
                System.out.println("Current player: " + currentPlayer.getNick());
            }
        }
    }

    public void makeMove(Player player, Coordinates coordinates) throws RemoteException {
        System.out.println("Moving and current player is: " + currentPlayer.getNick());
        if (player.getNick().equals(currentPlayer.getNick())) {
            if (board.getField(coordinates) == Board.EMPTY) {
                board.setField(coordinates,player.getSymbol());
                chooseNextPlayer();
            }
        }
    }

    private void chooseNextPlayer() {
        int currentIndex = players.indexOf(currentPlayer);
        currentPlayer = players.get((currentIndex+1) % players.size());
        System.out.println("Current player: " + currentPlayer.getNick());
    }

    public Board getBoard() throws RemoteException{
        return board;
    }

    public Player getCurrentPlayer() throws RemoteException {
        return currentPlayer;
    }
}

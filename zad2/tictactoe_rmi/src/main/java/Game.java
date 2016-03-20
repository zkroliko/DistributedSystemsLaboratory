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

    private Player winner;

    private boolean gameRunning = false;

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
        if (players.size() == MAX_PLAYERS) {
            gameRunning = true;
        }
    }

    public void makeMove(Player player, Coordinates coordinates) throws RemoteException {
        System.out.println("Moving and current player is: " + currentPlayer.getNick());
        if (gameRunning && player.getNick().equals(currentPlayer.getNick())) {
            if (board.getField(coordinates) == Board.EMPTY) {
                board.setField(coordinates,player.getSymbol());
                chooseNextPlayer();
            }
        } else {
            System.out.println("Bad move by: " + currentPlayer.getNick());
        }
        try {
            if (VictorySolver.victoryExists(board,players)) {
                winner = VictorySolver.findWinner(board,players);
                System.out.println("Winner is : " + winner.getNick());
            }
        } catch (PlayerNotFoundException e) {
            e.printStackTrace();
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

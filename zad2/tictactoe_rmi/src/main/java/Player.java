import java.rmi.RemoteException;

public class Player implements IPlayer {

    private String nick;

    private char symbol;

    private IListener listener;

    public Player(String nick, char symbol) {
        this.nick = nick;
        this.symbol = symbol;
    }

    public String getNick() {
        return nick;
    }

    public char getSymbol() {
        return symbol;
    }

    public IListener getListener() {
        return listener;
    }

    public void setListener(IListener listener) {
        this.listener = listener;
    }

    @Override
    public void reactToStart() throws RemoteException {
        listener.onGameStarted();
    }

    @Override
    public void move() throws RemoteException {
        listener.onMove();
    }

    @Override
    public void reactToVictory() throws RemoteException {
        listener.onVictory();
    }
}

import java.io.Serializable;

public class Player implements Serializable {

    private String nick;

    private char symbol;

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
}

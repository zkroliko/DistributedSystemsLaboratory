
public class PlayerNotFoundException extends Exception {

    public PlayerNotFoundException() {
    }

    public PlayerNotFoundException(String s) {
        super(s);
    }

    public PlayerNotFoundException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public PlayerNotFoundException(Throwable throwable) {
        super(throwable);
    }

    public PlayerNotFoundException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}

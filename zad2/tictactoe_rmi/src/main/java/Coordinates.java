import java.io.Serializable;

public class Coordinates implements Serializable {
    public int x;
    public int y;

    public Coordinates(int y, int x) throws NegativeArgumentException {
        if (y < 0) {
            throw new NegativeArgumentException("y must be positive");
        }
        if (x < 0) {
            throw new NegativeArgumentException("x must be positive");
        }
        if (y >= Board.SIZE_Y) {
            throw new NegativeArgumentException("y must smaller than " + Board.SIZE_Y);
        }
        if (x >= Board.SIZE_X) {
            throw new NegativeArgumentException("x must be smaller than" + Board.SIZE_X);
        }
        this.y = y;
        this.x = x;
    }
}

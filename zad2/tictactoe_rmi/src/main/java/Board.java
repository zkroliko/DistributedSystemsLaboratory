import java.io.Serializable;

public class Board implements Serializable {

    public static final int SIZE_X = 3;
    public static final int SIZE_Y = 3;

    public static final char EMPTY = ' ';

    private char[][] fields;

    public Board() {
        fields = new char[SIZE_X][SIZE_Y];
        iniBoard();
    }

    private void iniBoard() {
        for (int i = 0; i < SIZE_X; i++){
            for (int j = 0; j < SIZE_Y; j++){
                fields[i][j] = EMPTY;
            }
        }
    }

    public char getField(Coordinates cor) {
        return fields[cor.x][cor.y];
    }

    public void setField(Coordinates cor,char val) {
        fields[cor.x][cor.y] = val;
    }

    public String toString() {
        String result = "";
        for (int i = 0; i < SIZE_X; i++){
            for (int j = 0; j < SIZE_Y; j++){
                result += fields[i][j];
            }
            result += '\n';
        }
        return result;
    }

    public char[][] getFields() {
        return fields;
    }
}

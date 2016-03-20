import java.util.List;
import java.util.Optional;

public class VictorySolver {

    public static final int VICTORY_LENGTH = 3;

    // O(n^2)
    public static Player findWinner(Board board, List<Player> players) throws PlayerNotFoundException {
        Player candidatePlayer;

        char victorySymbol;

        victorySymbol = findHorizontal(board.getFields());
        if (victorySymbol != 0) {
            return matchWinner(victorySymbol,players);
        }
        victorySymbol = findVertical(board.getFields());
        if (victorySymbol != 0) {
            return matchWinner(victorySymbol,players);
        }
        victorySymbol = findDiagonalLToR(board.getFields());
        if (victorySymbol != 0) {
            return matchWinner(victorySymbol,players);
        }
        victorySymbol = findDiagonalRToL(board.getFields());
        if (victorySymbol != 0) {
            return matchWinner(victorySymbol,players);
        }
        return null;
    }

    public static boolean victoryExists(Board board, List<Player> players) throws PlayerNotFoundException {
        return findWinner(board, players) != null;
    }

    private static Player matchWinner(final char symbol, List<Player> players) throws PlayerNotFoundException {
        Optional<Player> found =  players.stream().filter(p -> p.getSymbol() == symbol).findAny();
        if (found.isPresent()) {
            return found.get();
        } else {
            throw new PlayerNotFoundException("No players with symbol: " + symbol);
        }
    }


    private static char findHorizontal(char[][] fields) {
        return find(fields);
    }

    private static char findVertical(char[][] fields) {
        return find(transposeMatrix(fields));
    }

    private static char findDiagonalLToR(char[][] fields) {
        return findDiagonal(fields);
    }

    private static char findDiagonalRToL(char[][] fields) {
        return findDiagonal(rotateMatrixCW(fields));
    }

    private static char find(char[][] fields) {
        char actual = 0;
        int count = 0;

        for (int i = 0; i < Board.SIZE_X; i++) {
            for (int j = 0; j < Board.SIZE_Y; j++) {
                if (fields[i][j] != Board.EMPTY) {
                    if (fields[i][j] == actual) {
                        count++;
                        if (count >= VICTORY_LENGTH) {
                            return actual;
                        }
                    } else {
                        actual = fields[i][j];
                        count = 1;
                    }
                } else {
                    actual = 0;
                    count = 0;
                }

            }
        }

        return 0;
    }

    private static char findDiagonal(char[][] fields) {
        char actual = 0;
        int count = 0;

        for (int i = 0; i < Board.SIZE_X-2; i++) {
            for (int j = 0; j < Board.SIZE_Y; j++) {
                if (fields[j][j] != Board.EMPTY) {
                    if (fields[j][j] == actual) {
                        count++;
                        if (count >= VICTORY_LENGTH) {
                            return actual;
                        }
                    } else {
                        actual = fields[j][j];
                        count = 1;
                    }
                } else {
                    actual = 0;
                    count = 0;
                }

            }
        }

        return 0;
    }

    private static char[][] transposeMatrix(char[][] m){
        char[][] temp = new char[m[0].length][m.length];
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[0].length; j++)
                temp[j][i] = m[i][j];
        return temp;
    }

    private static char[][] rotateMatrixCW(char[][] mat) {
        final int M = mat.length;
        final int N = mat[0].length;
        char[][] ret = new char[N][M];
        for (int r = 0; r < M; r++) {
            for (int c = 0; c < N; c++) {
                ret[c][M-1-r] = mat[r][c];
            }
        }
        return ret;
    }




}

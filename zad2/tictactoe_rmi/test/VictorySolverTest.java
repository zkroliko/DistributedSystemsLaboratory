import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;


public class VictorySolverTest {

    private List<Player> players = new ArrayList<Player>();

    @Test
    public void testVertical() throws Exception {
        char[][] fields = {{'x',' ',' '},{'x',' ',' '},{'x',' ',' '}};
        players.add(new Player("ababa",'x'));
        Board board = new Board();
        board.setFields(fields);
        Assert.assertTrue(VictorySolver.victoryExists(board,players));
        Assert.assertTrue(VictorySolver.findWinner(board,players).getSymbol() =='x');
    }

    @Test
    public void testHorizontal() throws Exception {
        char[][] fields = {{'p','x','o'},{'i','x','c'},{'f','x','w'}};
        players.add(new Player("ababa",'x'));
        Board board = new Board();
        board.setFields(fields);
        Assert.assertTrue(VictorySolver.victoryExists(board,players));
        Assert.assertTrue(VictorySolver.findWinner(board,players).getSymbol() =='x');
    }

    @Test
    public void testDiagonal() throws Exception {
        char[][] fields = {{'x','b','o'},{'i','x','c'},{'f',' ','x'}};
        players.add(new Player("ababa",'x'));
        Board board = new Board();
        board.setFields(fields);
        Assert.assertTrue(VictorySolver.victoryExists(board,players));
        Assert.assertTrue(VictorySolver.findWinner(board,players).getSymbol() =='x');
    }

    @Test
    public void testDiagonal2() throws Exception {
        char[][] fields = {{'f',' ','x'},{'i','x','c'},{'x','b','o'}};
        players.add(new Player("ababa",'x'));
        Board board = new Board();
        board.setFields(fields);
        Assert.assertTrue(VictorySolver.victoryExists(board,players));
        Assert.assertTrue(VictorySolver.findWinner(board,players).getSymbol() =='x');
    }
}
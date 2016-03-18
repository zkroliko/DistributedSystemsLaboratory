import junit.framework.Assert;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;

public class BoardTest {

    @Test
    public void constructorTest() {
        Board board = new Board();
        char[][] fields = board.getFields();

        for (int i = 0; i < Board.SIZE_X; i++){
                for (int j = 0; j < Board.SIZE_Y; j++){
                    assertEquals(fields[i][j],' ');
                }
        }
    }
}
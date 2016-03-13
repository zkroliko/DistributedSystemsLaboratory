package pl.edu.agh.liczeniepi;

import org.junit.Test;

import static org.junit.Assert.*;

public class ByteUtilsTest {

    @Test
    public void testBytesToLong() throws Exception {
        byte[] bytes = {1,10,0,0,0,0,0,0,0};
        long result = ByteUtils.bytesToLong(bytes);
        assertEquals(1,result);
    }
}
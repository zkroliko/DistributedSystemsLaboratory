package pl.edu.agh.liczeniepi;

import org.junit.Test;

import static org.junit.Assert.*;

public class ByteUtilsTest {

    @Test
    public void testBytesToLong1() throws Exception {
        byte[] bytes = {1,10,0,0,0,0,0,0,0};
        long result = ByteUtils.bytesToLong(bytes);
        assertEquals(1,result);

    }

    @Test
    public void testBytesToLong128() throws Exception {
        byte[] bytes = {-128,10,0,0,0,0,0,0,0};
        long result = ByteUtils.bytesToLong(bytes);
        assertEquals(128,result);
    }


    @Test
    public void testBytesDecreasing() throws Exception {
        byte[] bytes = {-24,3,10,0,0,0,0,0,0};
        long result = ByteUtils.bytesToLong(bytes);
        assertEquals(1000,result);

        bytes = new byte[]{-12, 1, 10, 0, 0, 0, 0, 0, 0};
        result = ByteUtils.bytesToLong(bytes);
        assertEquals(500,result);
    }
}
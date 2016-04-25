package sr.ice.pi;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PiBuffer {

    private static String pi = "";

    private static long biggestIndex = 0;

    public static final long MAX_SIZE = 1000000;

    public static final long PADDING = PiDigitsGenerator.PREC_OFFSET;

    public static char get(int index) {
        if (index > biggestIndex) {
            throw new IndexOutOfBoundsException("Current buffer index is " + biggestIndex);
        } else {
            return pi.charAt(index-1);
        }
    }

    public synchronized static void set(String pi) {
        if (pi.length() < MAX_SIZE) {
            PiBuffer.pi = pi;
            biggestIndex = pi.length() - PADDING;
        }
    }

    public static long getBiggestIndex() {
        return biggestIndex;
    }

    public static boolean contains(long index) {
        return biggestIndex > index;
    }
}

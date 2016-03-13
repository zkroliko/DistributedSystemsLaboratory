package pl.edu.agh.liczeniepi;

import java.nio.ByteBuffer;

public class ByteUtils {
    private static ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);

    public static final byte SEPARATOR = 10;

    public static byte[] longToBytes(long x) {
        buffer.putLong(0, x);
        return buffer.array();
    }

    public static long bytesToLong(byte[] bytes) {
        int length = 1;
        while(length < 9 && bytes[length] != SEPARATOR && tailEmpty(bytes,length)) {
            length++;
        }
        byte [] fixedBytes = copyAndFillBytes(bytes,length);
        buffer.put(fixedBytes, 0, fixedBytes.length);
        buffer.flip();//need flip
        return buffer.getLong();
    }

    private static boolean tailEmpty(byte[] bytes, int index) {
        for (int i = index+1; i < bytes.length; i++) {
            if (bytes[i] != 0) {
                return false;
            }
        }
        return true;
    }

    private static byte[] copyAndFillBytes(byte[] bytes, int length) {
        byte[] copyBytes = new byte[8];
        int first = 0, second=bytes.length-1-length;
        // Zipping
        while (first < length) {
            copyBytes[second] = bytes[first];
            first++;
            second++;
        }
        return copyBytes;
    }
}
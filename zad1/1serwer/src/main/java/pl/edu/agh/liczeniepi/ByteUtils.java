package pl.edu.agh.liczeniepi;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.NumberFormat;

public class ByteUtils {
    private static ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);

    public static final byte SEPARATOR = 10;

    public synchronized static long bytesToLong(byte[] bytes) {
        buffer.clear();
        int length = 1;
        while(length < 9 && bytes[length] != SEPARATOR && !tailEmpty(bytes,length)) {
            length++;
        }
        byte [] fixedBytes = removeSeparator(bytes,length);
        buffer.put(fixedBytes, 0, fixedBytes.length);
        buffer.flip();//need flip
        buffer.order(ByteOrder.LITTLE_ENDIAN);
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

    private static byte[] removeSeparator(byte[] bytes, int length) {
        byte[] copyBytes = new byte[8];
        for (int i = 0 ; i < length; i++) {
            copyBytes[i] = bytes[i];
        }
        return copyBytes;
    }
}
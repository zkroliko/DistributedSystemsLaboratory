package pl.edu.agh.liczeniepi;


import org.apfloat.Apfloat;
import org.apfloat.samples.Pi;
import sun.applet.AppletListener;
import sun.security.krb5.internal.ccache.CCacheOutputStream;

import java.io.*;

public class PiDigitsGenerator {

    // We will calculate this many additional digits for precision sake
    public static final long PREC_OFFSET = 20;

    public static final long BUFFER_INITIAL_SIZE = 100000;

    private PrintWriter outWriter;

    private ByteArrayOutputStream out;

    public PiDigitsGenerator() {
        out = new ByteArrayOutputStream();
        outWriter = new PrintWriter(out);
        Pi.setOut(outWriter);
        setUpErr();

        setUpInitialBuffer();
    }

    private void setUpErr() {
        Pi.setErr(new PrintWriter(System.out));
    }

    private void setUpInitialBuffer() {
        try {
            getDecimalDigit(BUFFER_INITIAL_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public char getDecimalDigit(long digitNumber) throws IOException {
        if (digitNumber < 1) {
            throw new IllegalArgumentException("Digit number should be positive");
        } else {
            // Checking the buffer
            if (PiBuffer.contains(digitNumber)) {
                return PiBuffer.get((int) digitNumber);
            } else {
                try {
                    Pi.run(digitNumber,10, new Pi.BorweinPiCalculator(digitNumber+PREC_OFFSET,10));
                    outWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String number = new String(out.toByteArray(),"UTF-8");
                out.reset();
                number = representWithoutDot(number);

                System.out.print(number);

                PiBuffer.set(number);

                return number.charAt((int) (digitNumber-1));
            }
        }
    }

    private static String representWithoutDot(String number) {
        return number.replace(".","");
    }
}

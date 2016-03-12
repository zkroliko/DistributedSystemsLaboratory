package pl.edu.agh.liczeniepi;


import org.apfloat.Apfloat;
import org.apfloat.samples.Pi;
import sun.applet.AppletListener;
import sun.security.krb5.internal.ccache.CCacheOutputStream;

import java.io.*;

public class PiDigitsGenerator {

    // We will calculate this many additional digits for precision sake
    public static final long PREC_OFFSET = 20;

    private PrintWriter outWriter;

    private ByteArrayOutputStream out;

    public PiDigitsGenerator() {
        out = new ByteArrayOutputStream();
        outWriter = new PrintWriter(out);
        Pi.setOut(outWriter);

        setUpErr();
    }

    private void setUpErr() {
        Pi.setErr(new PrintWriter(System.out));
    }

    public char getDecimalDigit(long digitNumber) throws IOException {
        if (digitNumber >= 0) {
            try {
                Pi.run(digitNumber,10, new Pi.BorweinPiCalculator(digitNumber+PREC_OFFSET,10));
                outWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String number = new String(out.toByteArray(),"UTF-8");

            number.trim(); // Contains newlines

//            System.out.print(number);

            char[] characters = representWithoutDot(number);

            return characters[(int) (digitNumber-1)];
        } else {
            throw new IllegalArgumentException("Digit number should be positive");
        }
    }


    private static char[] representWithoutDot(String number) {
        return number.replace(".","").toCharArray();
    }
}

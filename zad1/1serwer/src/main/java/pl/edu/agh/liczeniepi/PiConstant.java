package pl.edu.agh.liczeniepi;


import org.apfloat.Apfloat;
import org.apfloat.samples.Pi;
import sun.applet.AppletListener;

import java.io.PrintWriter;

public class PiConstant {

    // We will calculate this many additional digits for precision sake
    public static final long PREC_OFFSET = 4;

    public static char getDecimalDigit(long digitNumber) {
        if (digitNumber >= 0) {
            Pi.ChudnovskyPiCalculator calculator;
            Pi.setErr(new PrintWriter((System.err)));
            calculator = new Pi.ChudnovskyPiCalculator(digitNumber+PREC_OFFSET,10);
            Apfloat number = calculator.execute();
//            System.out.println(number);
            char[] digits = representWithoutDot(number);
            System.out.println(digits);
            return digits[digits.length-1-(int)PREC_OFFSET];
        } else {
            throw new IllegalArgumentException("Digit number should be postive");
        }

    }

    private static char[] representWithoutDot(Apfloat number) {
        return number.toString().replace(".","").toCharArray();
    }
}

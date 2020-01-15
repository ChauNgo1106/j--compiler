import java.lang.Integer;
import java.lang.System;

public class ArithmeticLeftShift {
    public static void main(String[] args) {
        int a = Integer.parseInt(args[0]);
        int b = Integer.parseInt(args[1]);
        System.out.println(a << b);
    }
}

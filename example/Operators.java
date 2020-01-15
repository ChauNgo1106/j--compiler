import java.lang.System;

public class Operators {
    public static void main(String[] args) {
        System.out.println(!true && false || false);
        System.out.println(true != false);
        int x = 42;
        x -= 2;
        x *= 2;
        x /= 10;
        x %= 3;
        System.out.println(x++);
        System.out.println(--x);
        x >>= 1;
        x >>>= 2;
        x <<= 3;
        x ^= 2;
        x |= 4;
        x &= 8;
        System.out.println(x < 100);
        System.out.println(x >= 50);
    }
}

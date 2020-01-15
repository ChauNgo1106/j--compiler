
import java.lang.Error;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.System;

import java.io.IOException;

/**
 * Major stress test.
 */
public class All {
    public static void main(String[] args) throws Exception1, Exception2 {
        int a = Integer.parseInt(args[0]);
        int i = 0;
        switch(a) {
            case 0:
                for (int j = 0; j < 10; j++) {
                    System.out.println(j);
                }
            case 1:
                for (; i < 2; i++);
                break;
            case 2:
                do {
                    a += 1;
                } while(a < 2);
            case 3:  // no catches
                try {
                    int b = 5;
                } finally {
                    int b = 10;
                    System.out.println(b);
                }
            case 4: // 1 catch
                try {
                    int b = 5;
                } catch (Error e) {
                    int b = 10;
                }
            case 5: // 1+ catches
                try {
                    int b = 5;
                } catch (IOException e) {
                    int b = 10;
                } catch (Error e) {
                    int b = 15;
                }
                a ^= i;
            case 6: // 1 catch and finally
                try {
                    int b = 5;
                } catch (Error e) {
                    int b = 10;
                } finally {
                    int b = 15;
                    System.out.println(b);
                }
                a /= i;
                break;
            case 7:
                long x = 12L;
                double y = 12.2;
            case 8:
                int j = 0;
                switch(j) {
                    case 0: case 1: case 2: case 3:
                    case 4: case 5:
                    default:
                        System.out.println(j);
                }
                break;
            case 9:
                --a;
                a++;
                a += (a >= 1) ? 100 : 0;
            default:
                throw new Exception1("None");
        }
    }
}

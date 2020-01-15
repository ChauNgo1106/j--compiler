import java.lang.Double;
import java.lang.Long;
import java.lang.System;

public class BasicTypes {
    public static void main(String[] args) {
        double a = Double.parseDouble(args[0]);
        long b = Long.parseLong(args[1]);
        System.out.println(3.14159D * a * a);
        System.out.println(1729L * (b + b));
    }
}

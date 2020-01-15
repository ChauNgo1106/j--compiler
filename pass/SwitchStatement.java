import java.lang.Integer;
import java.lang.System;

public class SwitchStatement {
    public static void main(String[] args) {
        switch (Integer.parseInt(args[0])) {
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
            System.out.println("Spring");
            break;
        case 6:
        case 7:
        case 8:
            System.out.println("Summer");
            break;
        case 9:
        case 10:
        case 11:
        case 12:
            System.out.println("Fall");
            break;
        default:
            System.out.println("Error!");
        }
    }
}

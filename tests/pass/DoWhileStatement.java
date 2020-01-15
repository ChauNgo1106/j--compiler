import java.lang.System;

public class DoWhileStatement {
    public static void main(String[] args) {
        int i = 0, sum = 0;
        do {
            sum += i++;
        } while(i <= 10);
        System.out.println(sum);
    }
}

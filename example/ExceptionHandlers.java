import java.lang.Exception;
import java.lang.Integer;
import java.lang.System;

public class ExceptionHandlers {
    private static int f(int x) throws Exception {
        if (x == 42) {
            throw new Exception(x + ": The answer to life, the universe and everything!");
        }
        return x;
    }
    
    public static void main(String[] args) {
        try {
            int x = Integer.parseInt(args[0]);
            int y = f(x);
            System.out.println(y);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        finally {
            System.out.println("Done!");
        }
    }
}

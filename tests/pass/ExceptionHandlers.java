public class ExceptionHandlers {
    private static void f() throws Exception1, Exception2 {
        throw new Exception1();
    }
    
    public static void main(String[] args) {
        try {
            f();
        }
        catch (Exception1 e1) { ; }
        catch (Exception2 e2) { ; }
        finally { ; }
    }
}

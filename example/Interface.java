import java.lang.System;


interface I {
    public int f(int x);
}

public class Interface implements I {
    public int f(int x) {
        return x * x;
    }

    public static void main(String[] args) {
        Interface i = new Interface();
        System.out.println(i.f(5));
    }
}

interface A {
    public int f(int x);
}

public class B implements A {
    public int f(int x) {
        return x * x;
    }
}

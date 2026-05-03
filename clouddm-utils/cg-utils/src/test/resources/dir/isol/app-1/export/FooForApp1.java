package export;
import lib.Foo;

public class FooForApp1 implements Foo {
    @Override
    public String run(int mainArg, String... args) {
        return "FooForApp1 running...";
    }

    @Override
    public String version() {
        return "call version running...";
    }
}
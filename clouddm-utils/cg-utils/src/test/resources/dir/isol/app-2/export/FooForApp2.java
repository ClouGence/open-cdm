package export;
import lib.Foo;

public class FooForApp2 implements Foo {
    @Override
    public String run(String... args) {
        return "FooForApp2 running...";
    }
}
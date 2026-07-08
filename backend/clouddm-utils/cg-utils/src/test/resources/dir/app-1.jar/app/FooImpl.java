package app;
import app.lib.Foo;

public class FooImpl implements Foo {
    @Override
    public String run(String... args) {
        return "FooB running...";
    }
}
package app.onlyjar;
import app.lib.Foo;

public class FooOnlyJar implements Foo {
    @Override
    public String run(String... args) {
        return "FooOnlyJar running...";
    }
}
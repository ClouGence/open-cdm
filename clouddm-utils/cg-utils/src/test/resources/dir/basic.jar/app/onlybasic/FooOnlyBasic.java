package app.onlybasic;
import app.lib.Foo;

public class FooOnlyBasic implements Foo {
    @Override
    public String run(String... args) {
        return "FooOnlyBasic running...";
    }
}
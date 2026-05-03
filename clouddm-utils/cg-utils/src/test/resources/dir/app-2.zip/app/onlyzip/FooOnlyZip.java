package app.onlyzip;
import app.lib.Foo;

public class FooOnlyZip implements Foo {
    @Override
    public String run(String... args) {
        return "FooOnlyZip running...";
    }
}
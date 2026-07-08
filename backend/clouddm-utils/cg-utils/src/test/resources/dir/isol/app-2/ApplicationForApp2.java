import export.FooForApp2;
import lib.Foo;
import java.util.function.Function;

public class ApplicationForApp2 implements Function<String, String> {
    @Override
    public String apply(String msg) {
        Foo foo = new FooForApp2();
        return foo.run(msg);
    }
}
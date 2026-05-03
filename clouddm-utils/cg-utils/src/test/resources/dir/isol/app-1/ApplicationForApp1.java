import export.FooForApp1;
import lib.Foo;
import java.util.function.Function;

public class ApplicationForApp1 implements Function<String, String> {
    @Override
    public String apply(String msg) {
        Foo foo = new FooForApp1();
        return foo.run(123, msg);
    }
}
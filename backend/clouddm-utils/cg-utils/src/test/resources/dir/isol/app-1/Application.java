import lib.Foo;
import export.FooForApp1;

import java.util.function.Function;

public class Application implements Function<String, String> {
    @Override
    public String apply(String msg) {
        Foo foo = new FooForApp1();
        return foo.run(123, msg);
    }
}
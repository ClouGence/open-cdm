package com.clougence.clouddm.dsfamily.mysql.execute.function;

import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;

import com.clougence.schema.umi.special.rdb.RdbFunction;
import com.clougence.schema.umi.special.rdb.RdbParam;
import com.clougence.schema.umi.special.rdb.RdbValue;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.ResourcesUtils;

import static com.clougence.schema.umi.special.rdb.RdbAttributeNames.*;

/**
 * @author Cloud Conceal
 * CODEX MySQL built-in function -- data struct define and store
 */
public class MySqlBuiltInFunction {

    private static volatile MySqlBuiltInFunction INSTANCE;

    private final static String                  MysqlBuiltInFunctionFilePath = "/META-INF/clougence/my-built-in-functions.yaml";
    private List<RdbFunction>                    functions;

    private MySqlBuiltInFunction(){
        readFunctions();
    }

    public static MySqlBuiltInFunction getInstance() {
        if (INSTANCE == null) {
            synchronized (MySqlBuiltInFunction.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MySqlBuiltInFunction();
                }
            }
        }

        return INSTANCE;
    }

    private void readFunctions() {
        try (InputStream inputStream = ResourcesUtils.getResourceAsStream(MySqlBuiltInFunction.class.getClassLoader(), MysqlBuiltInFunctionFilePath)) {
            CustomClassLoaderConstructor constructor = new CustomClassLoaderConstructor(BuiltInFunctionCollector.class,
                MySqlBuiltInFunction.class.getClassLoader(),
                new LoaderOptions());
            // data load from local file and did not involve store, so that this method will not be attacked by rce
            BuiltInFunctionCollector builtInFunctionCollector = new Yaml(constructor).load(inputStream);
            this.functions = builtInFunctionCollector.getFunctions();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Value> convertFunctions2Values() {
        return this.functions.stream().map(function -> {
            RdbValue rdbValue = new RdbValue();
            rdbValue.setValue(function.getName());
            rdbValue.setUmiType(UmiTypes.Function);
            StringBuilder paramBuilder = new StringBuilder();
            List<RdbParam> args = function.getRdbParams();
            if (args != null && !args.isEmpty()) {
                args.stream().sorted(Comparator.comparingInt(RdbParam::getOrdinal)).forEach(arg -> {
                    paramBuilder.append("[").append(arg.getName()).append(" ").append(arg.getType().toUpperCase());
                    paramBuilder.append("], ");
                });
                if (paramBuilder.length() > 2) {
                    paramBuilder.delete(paramBuilder.length() - 2, paramBuilder.length());
                    rdbValue.setAttribute(OBJ_UI_TIPS, paramBuilder.toString());
                }
            }
            rdbValue.setAttribute(COMMENT, function.getComment());
            return rdbValue;
        }).collect(Collectors.toList());
    }

    public List<RdbFunction> getFunctions() { return functions; }

}

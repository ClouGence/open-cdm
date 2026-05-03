package com.clougence.clouddm.ds.template.special.oracle;

import static com.clougence.clouddm.sdk.ui.editor.function.FunctionEditorFields.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.clougence.clouddm.ds.oracle.definition.ui.template.OraCmdTemplateSpi;
import org.junit.Test;

import com.clougence.clouddm.sdk.ui.editor.function.FunctionEditorFields;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateSpi;

public class FunctionTest {

    CmdTemplateSpi spi = new OraCmdTemplateSpi();

    private CmdTemplateOption getOption() {
        CmdTemplateOption option = new CmdTemplateOption();

        option.setSchema("test_schema");
        option.setTargetName("target_name");
        option.setData(new HashMap<>());
        option.setDelimited(true);
        return option;
    }

    @Test
    public void createFunctionNoParamTest() {
        CmdTemplateOption option = getOption();
        option.getData().put("test_name", "test_value");
        option.getData().put("test_schema", "test_schema_value");
        option.getData().put("target_name", "target_name_value");
        option.getData().put("test_sql", "test_sql_value");

        option.getData().put(FUNCTION_NAME, "test_name");
        option.getData().put(FunctionEditorFields.RETURN_TYPE, "int");
        option.getData().put(FunctionEditorFields.SQL, "begin \nreturn 1; end");

        List<String> createFunction = spi.getCreateFunction(option);

        assert createFunction.size() == 1;
        assert createFunction.get(0).equals("create function \"test_schema\".\"test_name\"\n" + "return int\n" + "as\n" + "begin \n" + "return 1; end;");
    }

    @Test
    public void createFunctionWithOneParamTest() {
        CmdTemplateOption option = getOption();
        option.getData().put("test_name", "test_value");
        option.getData().put("test_schema", "test_schema_value");
        option.getData().put("target_name", "target_name_value");
        option.getData().put("test_sql", "test_sql_value");

        option.getData().put(FUNCTION_NAME, "test_name");
        option.getData().put(FunctionEditorFields.RETURN_TYPE, "int");
        option.getData().put(FunctionEditorFields.SQL, "begin \nreturn 1; end");

        List<HashMap<String, Object>> params = new ArrayList<>();

        HashMap<String, Object> param = new HashMap<>();
        param.put(PARAM_NAME, "param1");
        param.put(PARAM_DATATYPE, "int");
        params.add(param);
        option.getData().put(FUNCTION_PARAMS, params);
        List<String> createFunction = spi.getCreateFunction(option);

        assert createFunction.size() == 1;
        assert createFunction.get(0).equals("create function \"test_schema\".\"test_name\"(param1 int)\n" + "return int\n" + "as\n" + "begin \n" + "return 1; end;");
    }

    @Test
    public void createFunctionWithDefaultValueParamTest() {
        CmdTemplateOption option = getOption();
        option.getData().put("test_name", "test_value");
        option.getData().put("test_schema", "test_schema_value");
        option.getData().put("target_name", "target_name_value");
        option.getData().put("test_sql", "test_sql_value");

        option.getData().put(FUNCTION_NAME, "test_name");
        option.getData().put(FunctionEditorFields.RETURN_TYPE, "int");
        option.getData().put(FunctionEditorFields.SQL, "begin \nreturn 1; end");

        List<HashMap<String, Object>> params = new ArrayList<>();

        HashMap<String, Object> param = new HashMap<>();
        param.put(PARAM_NAME, "param1");
        param.put(PARAM_DATATYPE, "int");
        param.put(PARAM_MODE, "in");
        param.put(PARAM_DEFAULT_VALUE, "12");
        params.add(param);
        option.getData().put(FUNCTION_PARAMS, params);
        List<String> createFunction = spi.getCreateFunction(option);

        assert createFunction.size() == 1;
        assert createFunction.get(0)
            .equals("create function \"test_schema\".\"test_name\"(param1 in int default '12')\n" + "return int\n" + "as\n" + "begin \n" + "return 1; end;");
    }

    @Test
    public void createFunctionWithTwoParamTest() {
        CmdTemplateOption option = getOption();
        option.getData().put("test_name", "test_value");
        option.getData().put("test_schema", "test_schema_value");
        option.getData().put("target_name", "target_name_value");
        option.getData().put("test_sql", "test_sql_value");

        option.getData().put(FUNCTION_NAME, "test_name");
        option.getData().put(FunctionEditorFields.RETURN_TYPE, "int");
        option.getData().put(FunctionEditorFields.SQL, "begin \nreturn 1; end");

        List<HashMap<String, Object>> params = new ArrayList<>();

        HashMap<String, Object> param = new HashMap<>();
        param.put(PARAM_NAME, "param1");
        param.put(PARAM_DATATYPE, "int");
        params.add(param);
        option.getData().put(FUNCTION_PARAMS, params);

        HashMap<String, Object> param2 = new HashMap<>();
        param2.put(PARAM_NAME, "param2");
        param2.put(PARAM_DATATYPE, "varchar");
        params.add(param2);
        option.getData().put(FUNCTION_PARAMS, params);
        List<String> createFunction = spi.getCreateFunction(option);

        assert createFunction.size() == 1;
        assert createFunction.get(0).equals("create function \"test_schema\".\"test_name\"(param1 int,param2 varchar)\n" + "return int\n" + "as\n" + "begin \n" + "return 1; end;");
    }

}

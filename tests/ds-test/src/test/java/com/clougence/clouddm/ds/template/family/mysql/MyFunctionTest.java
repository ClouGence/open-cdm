package com.clougence.clouddm.ds.template.family.mysql;

import com.clougence.adapter.mysql.umi.MySQLAttributeNames;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.template.MyCmdTemplateSpi;
import com.clougence.clouddm.sdk.ui.editor.function.FunctionEditorFields;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateSpi;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.clougence.clouddm.sdk.ui.editor.function.FunctionEditorFields.*;

public class MyFunctionTest {

    protected CmdTemplateSpi spi;

    public MyFunctionTest(){
        this.spi = new MyCmdTemplateSpi();
    }

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
        assert createFunction.get(0).equals("create function `test_schema`.`test_name`()\n" + "returns int\n" + "begin \n" + "return 1; end;");
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
        assert createFunction.get(0).equals("create function `test_schema`.`test_name`(`param1` int)\n" + "returns int\n" + "begin \n" + "return 1; end;");
    }

    @Test
    public void createFunctionWithScaleParamTest() {
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
        param.put(PARAM_NUM_PRECISION, "5");
        params.add(param);
        option.getData().put(FUNCTION_PARAMS, params);
        List<String> createFunction = spi.getCreateFunction(option);
        assert createFunction.size() == 1;
        assert createFunction.get(0).equals("create function `test_schema`.`test_name`(`param1` int(5))\n" + "returns int\n" + "begin \n" + "return 1; end;");
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
        assert createFunction.get(0).equals("create function `test_schema`.`test_name`(`param1` int,`param2` text)\n" + "returns int\n" + "begin \n" + "return 1; end;");
    }

    @Test
    public void createFunctionWithOptionTest() {
        CmdTemplateOption option = getOption();
        option.getData().put("test_name", "test_value");
        option.getData().put("test_schema", "test_schema_value");
        option.getData().put("target_name", "target_name_value");
        option.getData().put("test_sql", "test_sql_value");

        option.getData().put(FUNCTION_NAME, "test_name");
        option.getData().put(FunctionEditorFields.RETURN_TYPE, "int");
        option.getData().put(FunctionEditorFields.SQL, "begin \nreturn 1; end");
        option.getData().put(MySQLAttributeNames.DETERMINISTIC.getCodeKey(), "true");
        List<String> createFunction = spi.getCreateFunction(option);
        System.out.println(createFunction);
        assert createFunction.size() == 1;
        assert createFunction.get(0).equals("create function `test_schema`.`test_name`()\n" + "returns int\n" + "deterministic\n" + "begin \n" + "return 1; end;");
    }
}

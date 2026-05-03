package com.clougence.clouddm.ds.template.special.dameng;

import static com.clougence.clouddm.sdk.ui.editor.function.FunctionEditorFields.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.clougence.clouddm.ds.dameng.definition.ui.template.DmCmdTemplateSpi;
import org.junit.Test;

import com.clougence.clouddm.sdk.ui.editor.procedure.ProcedureEditorFields;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateSpi;

public class ProcedureTest {

    CmdTemplateSpi spi = new DmCmdTemplateSpi();

    private CmdTemplateOption getOption() {
        CmdTemplateOption option = new CmdTemplateOption();

        option.setSchema("test_schema");
        option.setTargetName("target_name");
        option.setData(new HashMap<>());
        option.setDelimited(true);
        return option;
    }

    @Test
    public void noParamTest() {
        CmdTemplateOption option = getOption();

        option.getData().put(ProcedureEditorFields.PROCEDURE_NAME, "test_name");
        option.getData().put(ProcedureEditorFields.SQL, "begin \n insert into a values(1,2);\n end");

        List<String> procedure = spi.getCreateProcedure(option);

        assert procedure.size() == 1;
        assert procedure.get(0).equals("create procedure \"test_schema\".\"test_name\"\n" + "as\n" + "begin \n" + " insert into a values(1,2);\n" + " end;");
    }

    @Test
    public void oneParamTest() {

        CmdTemplateOption option = getOption();

        option.getData().put(ProcedureEditorFields.PROCEDURE_NAME, "test_name");
        option.getData().put(ProcedureEditorFields.SQL, "begin \n insert into a values(1,2);\n end");
        List<HashMap<String, Object>> params = new ArrayList<>();

        HashMap<String, Object> param = new HashMap<>();
        param.put(PARAM_NAME, "param1");
        param.put(PARAM_DATATYPE, "int");
        params.add(param);
        option.getData().put(ProcedureEditorFields.PROCEDURE_PARAMS, params);

        List<String> procedure = spi.getCreateProcedure(option);
        assert procedure.size() == 1;
        assert procedure.get(0).equals("create procedure \"test_schema\".\"test_name\"(param1 int)\n" + "as\n" + "begin \n" + " insert into a values(1,2);\n" + " end;");
    }

    @Test
    public void twoParamTest() {

        CmdTemplateOption option = getOption();

        option.getData().put(ProcedureEditorFields.PROCEDURE_NAME, "test_name");
        option.getData().put(ProcedureEditorFields.SQL, "begin \n insert into a values(1,2);\n end");
        List<HashMap<String, Object>> params = new ArrayList<>();

        for (int i = 1; i <= 2; i++) {
            HashMap<String, Object> param = new HashMap<>();
            param.put(PARAM_NAME, "param" + i);
            param.put(PARAM_DATATYPE, "int");
            params.add(param);
        }
        option.getData().put(ProcedureEditorFields.PROCEDURE_PARAMS, params);

        List<String> procedure = spi.getCreateProcedure(option);
        assert procedure.size() == 1;
        assert procedure.get(0).equals("create procedure \"test_schema\".\"test_name\"(param1 int,param2 int)\n" + "as\n" + "begin \n" + " insert into a values(1,2);\n" + " end;");
    }

    @Test
    public void twoParamWithModeTest() {

        CmdTemplateOption option = getOption();

        option.getData().put(ProcedureEditorFields.PROCEDURE_NAME, "test_name");
        option.getData().put(ProcedureEditorFields.SQL, "begin \n insert into a values(1,2);\n end");
        List<HashMap<String, Object>> params = new ArrayList<>();

        for (int i = 1; i <= 2; i++) {
            HashMap<String, Object> param = new HashMap<>();
            param.put(PARAM_NAME, "param" + i);
            param.put(PARAM_DATATYPE, "int");
            param.put(PARAM_MODE, "IN");
            if (i == 1) {
                param.put(PARAM_DEFAULT_VALUE, "21");
            }
            params.add(param);
        }
        option.getData().put(ProcedureEditorFields.PROCEDURE_PARAMS, params);

        List<String> procedure = spi.getCreateProcedure(option);
        System.out.println(procedure);
        assert procedure.size() == 1;
        assert procedure.get(0)
            .equals("create procedure \"test_schema\".\"test_name\"(param1 IN int default '21',param2 IN int)\n" + "as\n" + "begin \n" + " insert into a values(1,2);\n" + " end;");
    }
}

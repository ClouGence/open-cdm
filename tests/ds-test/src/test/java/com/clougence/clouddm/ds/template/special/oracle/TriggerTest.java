package com.clougence.clouddm.ds.template.special.oracle;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.clougence.adapter.oracle.umi.OracleAttributeNames;
import com.clougence.clouddm.ds.oracle.definition.ui.template.OraCmdTemplateSpi;
import org.junit.Test;

import com.clougence.clouddm.sdk.ui.editor.trigger.TriggerEditorFields;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateSpi;

public class TriggerTest {

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
    public void normalTest() {
        CmdTemplateOption option = getOption();
        option.getData().put(TriggerEditorFields.TRIGGER_NAME, "test_trigger");
        option.getData().put(TriggerEditorFields.TRIGGER_TABLE, "table");
        option.getData().put(TriggerEditorFields.TRIGGER_TIME, "before");
        option.getData().put(TriggerEditorFields.TRIGGER_EVENT, Arrays.asList("insert"));
        option.getData().put(TriggerEditorFields.TRIGGER_BODY, "begin insert table tmp values(1,2); end");

        List<String> createTrigger = spi.getCreateTrigger(option);
        System.out.println(createTrigger);
        assert createTrigger.size() == 1;
        assert createTrigger.get(0)
            .equals("create trigger \"test_schema\".\"test_trigger\" \n" + "before insert \n" + "on \"test_schema\".\"table\"\n" + "for each row \n"
                    + "begin insert table tmp values(1,2); end;");

    }

    @Test
    public void newAliasTest() {
        CmdTemplateOption option = getOption();
        option.getData().put(TriggerEditorFields.TRIGGER_NAME, "test_trigger");
        option.getData().put(TriggerEditorFields.TRIGGER_TABLE, "table");
        option.getData().put(TriggerEditorFields.TRIGGER_TIME, "before");
        option.getData().put(TriggerEditorFields.TRIGGER_EVENT, Arrays.asList("insert"));
        option.getData().put(TriggerEditorFields.TRIGGER_BODY, "begin insert table tmp values(1,2); end");
        option.getData().put(OracleAttributeNames.NEW_ALIAS.getCodeKey(), "new_name");

        List<String> createTrigger = spi.getCreateTrigger(option);
        System.out.println(createTrigger);
        assert createTrigger.size() == 1;
        assert createTrigger.get(0)
            .equals("create trigger \"test_schema\".\"test_trigger\" \n" + "before insert \n" + "on \"test_schema\".\"table\"\n" + "referencing new as new_name\n"
                    + "for each row \n" + "begin insert table tmp values(1,2); end;");

    }

    @Test
    public void oldAliasTest() {
        CmdTemplateOption option = getOption();
        option.getData().put(TriggerEditorFields.TRIGGER_NAME, "test_trigger");
        option.getData().put(TriggerEditorFields.TRIGGER_TABLE, "table");
        option.getData().put(TriggerEditorFields.TRIGGER_TIME, "before");
        option.getData().put(TriggerEditorFields.TRIGGER_EVENT, Arrays.asList("insert"));
        option.getData().put(TriggerEditorFields.TRIGGER_BODY, "begin insert table tmp values(1,2); end");
        option.getData().put(OracleAttributeNames.OLD_ALIAS.getCodeKey(), "new_name");

        List<String> createTrigger = spi.getCreateTrigger(option);
        System.out.println(createTrigger);
        assert createTrigger.size() == 1;
        assert createTrigger.get(0)
            .equals("create trigger \"test_schema\".\"test_trigger\" \n" + "before insert \n" + "on \"test_schema\".\"table\"\n" + "referencing old as new_name\n"
                    + "for each row \n" + "begin insert table tmp values(1,2); end;");

    }

    @Test
    public void conditionTest() {
        CmdTemplateOption option = getOption();
        option.getData().put(TriggerEditorFields.TRIGGER_NAME, "test_trigger");
        option.getData().put(TriggerEditorFields.TRIGGER_TABLE, "table");
        option.getData().put(TriggerEditorFields.TRIGGER_TIME, "before");
        option.getData().put(TriggerEditorFields.TRIGGER_EVENT, Arrays.asList("insert"));
        option.getData().put(TriggerEditorFields.TRIGGER_BODY, "begin insert table tmp values(1,2); end");
        option.getData().put(OracleAttributeNames.CONDITION.getCodeKey(), "1=1");

        List<String> createTrigger = spi.getCreateTrigger(option);
        System.out.println(createTrigger);
        assert createTrigger.size() == 1;
        assert createTrigger.get(0)
            .equals("create trigger \"test_schema\".\"test_trigger\" \n" + "before insert \n" + "on \"test_schema\".\"table\"\n" + "for each row \n" + "when(1=1)\n"
                    + "begin insert table tmp values(1,2); end;");

    }

}

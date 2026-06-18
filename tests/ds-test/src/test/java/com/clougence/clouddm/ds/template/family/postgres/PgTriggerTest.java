package com.clougence.clouddm.ds.template.family.postgres;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.clougence.adapter.dm.umi.DmAttributeNames;
import com.clougence.adapter.postgre.umi.PostgreAttributeNames;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.template.PgCmdTemplateSpi;
import org.junit.Test;

import com.clougence.clouddm.sdk.ui.editor.trigger.TriggerEditorFields;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateSpi;

public class PgTriggerTest {

    CmdTemplateSpi spi;

    public PgTriggerTest(){
        this.spi = new PgCmdTemplateSpi();
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
    public void testNormal() {
        CmdTemplateOption option = getOption();
        option.getData().put(TriggerEditorFields.TRIGGER_NAME, "test_trigger");
        option.getData().put(TriggerEditorFields.TRIGGER_TABLE, "table_name");
        option.getData().put(TriggerEditorFields.TRIGGER_TIME, "before");
        option.getData().put(TriggerEditorFields.TRIGGER_EVENT, Arrays.asList("insert"));
        option.getData().put(TriggerEditorFields.TRIGGER_BODY, "trigger_function");

        List<String> createTrigger = spi.getCreateTrigger(option);
        System.out.println(createTrigger);
        assert createTrigger.size() == 1;
        assert createTrigger.get(0).equals("create trigger \"test_trigger\"\nbefore insert \non table_name\nexecute function trigger_function();");
    }

    @Test
    public void newAliasTest() {
        CmdTemplateOption option = getOption();
        option.getData().put(TriggerEditorFields.TRIGGER_NAME, "test_trigger");
        option.getData().put(TriggerEditorFields.TRIGGER_TABLE, "table_name");
        option.getData().put(TriggerEditorFields.TRIGGER_TIME, "before");
        option.getData().put(TriggerEditorFields.TRIGGER_EVENT, Arrays.asList("insert"));
        option.getData().put(TriggerEditorFields.TRIGGER_BODY, "trigger_function");
        option.getData().put(PostgreAttributeNames.NEW_ALIAS.getCodeKey(), "new_name");

        List<String> createTrigger = spi.getCreateTrigger(option);
        System.out.println(createTrigger);
        assert createTrigger.size() == 1;
        assert createTrigger.get(0)
            .equals("create trigger \"test_trigger\"\nbefore insert \non table_name\nreferencing new table as new_name\nexecute function trigger_function();");

    }

    @Test
    public void oldAliasTest() {
        CmdTemplateOption option = getOption();
        option.getData().put(TriggerEditorFields.TRIGGER_NAME, "test_trigger");
        option.getData().put(TriggerEditorFields.TRIGGER_TABLE, "table1");
        option.getData().put(TriggerEditorFields.TRIGGER_TIME, "before");
        option.getData().put(TriggerEditorFields.TRIGGER_EVENT, Arrays.asList("insert"));
        option.getData().put(TriggerEditorFields.TRIGGER_BODY, "test_function");
        option.getData().put(PostgreAttributeNames.OLD_ALIAS.getCodeKey(), "new_name");

        List<String> createTrigger = spi.getCreateTrigger(option);
        System.out.println(createTrigger);
        assert createTrigger.size() == 1;
        assert createTrigger.get(0).equals("create trigger \"test_trigger\"\nbefore insert \non table1\nreferencing old table as new_name\nexecute function test_function();");

    }

    @Test
    public void conditionTest() {
        CmdTemplateOption option = getOption();
        option.getData().put(TriggerEditorFields.TRIGGER_NAME, "test_trigger");
        option.getData().put(TriggerEditorFields.TRIGGER_TABLE, "tabl2e");
        option.getData().put(TriggerEditorFields.TRIGGER_TIME, "before");
        option.getData().put(TriggerEditorFields.TRIGGER_EVENT, Arrays.asList("insert"));
        option.getData().put(TriggerEditorFields.TRIGGER_BODY, "funcaa");
        option.getData().put(PostgreAttributeNames.TRIGGER_CONDITION.getCodeKey(), "1=1");

        List<String> createTrigger = spi.getCreateTrigger(option);
        System.out.println(createTrigger);
        assert createTrigger.size() == 1;
        assert createTrigger.get(0).equals("create trigger \"test_trigger\"\nbefore insert \non tabl2e\nwhen(1=1)\nexecute function funcaa();");
    }

    @Test
    public void updateTwoColumnTest() {
        CmdTemplateOption option = getOption();
        option.getData().put(TriggerEditorFields.TRIGGER_NAME, "test_trigger");
        option.getData().put(TriggerEditorFields.TRIGGER_TABLE, "tabl1e");
        option.getData().put(TriggerEditorFields.TRIGGER_TIME, "before");
        option.getData().put(TriggerEditorFields.TRIGGER_EVENT, Arrays.asList("insert", "update"));
        option.getData().put(TriggerEditorFields.TRIGGER_COLUMNS, Arrays.asList("a", "b"));
        option.getData().put(TriggerEditorFields.TRIGGER_BODY, "test");
        option.getData().put(DmAttributeNames.CONDITION.getCodeKey(), "1=1");
        option.getData().put(DmAttributeNames.TRIGGER_GRANULARITY.getCodeKey(), "row");

        List<String> createTrigger = spi.getCreateTrigger(option);
        System.out.println(createTrigger);
        assert createTrigger.size() == 1;
        assert createTrigger.get(0).equals("create trigger \"test_trigger\"\nbefore insert or update of \"a\",\"b\" \non tabl1e\nexecute function test();");
    }

}

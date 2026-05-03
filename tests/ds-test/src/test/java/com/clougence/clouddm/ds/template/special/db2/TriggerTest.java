package com.clougence.clouddm.ds.template.special.db2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.clougence.adapter.db2.umi.Db2AttributeNames;
import com.clougence.clouddm.dsfamily.db2.definition.ui.template.Db2CmdTemplateSpi;
import org.junit.Test;

import com.clougence.clouddm.sdk.ui.editor.trigger.TriggerEditorFields;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateSpi;

public class TriggerTest {

    CmdTemplateSpi spi = new Db2CmdTemplateSpi();

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
        option.getData().put(Db2AttributeNames.TRIGGER_GRANULARITY.getCodeKey(), "row");

        List<String> createTrigger = spi.getCreateTrigger(option);
        System.out.println(createTrigger);
        assert createTrigger.size() == 1;
        assert createTrigger.get(0)
            .equals("create trigger \"test_schema\".\"test_trigger\"\n" + "before insert on \"test_schema\".\"table\"\n" + "for each row\n"
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
        option.getData().put(Db2AttributeNames.TRIGGER_GRANULARITY.getCodeKey(), "row");
        option.getData().put(Db2AttributeNames.NEW_ALIAS.getCodeKey(), "new_name");

        List<String> createTrigger = spi.getCreateTrigger(option);
        System.out.println(createTrigger);
        assert createTrigger.size() == 1;
        assert createTrigger.get(0)
            .equals("create trigger \"test_schema\".\"test_trigger\"\n" + "before insert on \"test_schema\".\"table\"\n" + "referencing new as \"new_name\" \n" + "for each row\n"
                    + "begin insert table tmp values(1,2); end;");

    }

    @Test
    public void newAndOldAliasTest() {
        CmdTemplateOption option = getOption();
        option.getData().put(TriggerEditorFields.TRIGGER_NAME, "test_trigger");
        option.getData().put(TriggerEditorFields.TRIGGER_TABLE, "table");
        option.getData().put(TriggerEditorFields.TRIGGER_TIME, "before");
        option.getData().put(TriggerEditorFields.TRIGGER_EVENT, Arrays.asList("insert"));
        option.getData().put(TriggerEditorFields.TRIGGER_BODY, "begin insert table tmp values(1,2); end");
        option.getData().put(Db2AttributeNames.TRIGGER_GRANULARITY.getCodeKey(), "row");
        option.getData().put(Db2AttributeNames.NEW_ALIAS.getCodeKey(), "new_name");
        option.getData().put(Db2AttributeNames.OLD_ALIAS.getCodeKey(), "old_name");

        List<String> createTrigger = spi.getCreateTrigger(option);
        System.out.println(createTrigger);
        assert createTrigger.size() == 1;
        assert createTrigger.get(0)
            .equals("create trigger \"test_schema\".\"test_trigger\"\n" + "before insert on \"test_schema\".\"table\"\n" + "referencing new as \"new_name\" old as \"old_name\"\n"
                    + "for each row\n" + "begin insert table tmp values(1,2); end;");

    }

    @Test
    public void oldAliasTest() {
        CmdTemplateOption option = getOption();
        option.getData().put(TriggerEditorFields.TRIGGER_NAME, "test_trigger");
        option.getData().put(TriggerEditorFields.TRIGGER_TABLE, "table");
        option.getData().put(TriggerEditorFields.TRIGGER_TIME, "before");
        option.getData().put(TriggerEditorFields.TRIGGER_EVENT, Arrays.asList("insert"));
        option.getData().put(TriggerEditorFields.TRIGGER_BODY, "begin insert table tmp values(1,2); end");
        option.getData().put(Db2AttributeNames.TRIGGER_GRANULARITY.getCodeKey(), "row");
        option.getData().put(Db2AttributeNames.OLD_ALIAS.getCodeKey(), "new_name");

        List<String> createTrigger = spi.getCreateTrigger(option);
        System.out.println(createTrigger);
        assert createTrigger.size() == 1;
        assert createTrigger.get(0)
            .equals("create trigger \"test_schema\".\"test_trigger\"\n" + "before insert on \"test_schema\".\"table\"\n" + "referencing old as \"new_name\"\n" + "for each row\n"
                    + "begin insert table tmp values(1,2); end;");

    }

    @Test
    public void conditionTest() {
        CmdTemplateOption option = getOption();
        option.getData().put(TriggerEditorFields.TRIGGER_NAME, "test_trigger");
        option.getData().put(TriggerEditorFields.TRIGGER_TABLE, "table");
        option.getData().put(TriggerEditorFields.TRIGGER_TIME, "before");
        option.getData().put(TriggerEditorFields.TRIGGER_EVENT, Arrays.asList("insert"));
        option.getData().put(TriggerEditorFields.TRIGGER_BODY, "begin insert table tmp values(1,2); end");
        option.getData().put(Db2AttributeNames.CONDITION.getCodeKey(), "1=1");
        option.getData().put(Db2AttributeNames.TRIGGER_GRANULARITY.getCodeKey(), "row");

        List<String> createTrigger = spi.getCreateTrigger(option);
        System.out.println(createTrigger);
        assert createTrigger.size() == 1;
        assert createTrigger.get(0)
            .equals("create trigger \"test_schema\".\"test_trigger\"\n" + "before insert on \"test_schema\".\"table\"\n" + "for each row\n" + "when(1=1)\n"
                    + "begin insert table tmp values(1,2); end;");
    }

    @Test
    public void updateColumn() {
        CmdTemplateOption option = getOption();
        option.getData().put(TriggerEditorFields.TRIGGER_NAME, "test_trigger");
        option.getData().put(TriggerEditorFields.TRIGGER_TABLE, "table");
        option.getData().put(TriggerEditorFields.TRIGGER_TIME, "before");
        option.getData().put(TriggerEditorFields.TRIGGER_EVENT, Arrays.asList("update"));
        option.getData().put(TriggerEditorFields.TRIGGER_BODY, "begin insert table tmp values(1,2); end");
        option.getData().put(Db2AttributeNames.CONDITION.getCodeKey(), "1=1");
        option.getData().put(Db2AttributeNames.TRIGGER_GRANULARITY.getCodeKey(), "statement");
        option.getData().put(TriggerEditorFields.TRIGGER_COLUMNS, Arrays.asList("a", "b"));

        List<String> createTrigger = spi.getCreateTrigger(option);
        System.out.println(createTrigger);
        assert createTrigger.size() == 1;
        assert createTrigger.get(0)
            .equals("create trigger \"test_schema\".\"test_trigger\"\n" + "before update of \"a\",\"b\" on \"test_schema\".\"table\"\n" + "for each statement\n"
                    + "begin insert table tmp values(1,2); end;");
    }

}

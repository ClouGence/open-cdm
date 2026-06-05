package com.clougence.clouddm.ds.template.special.dameng;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.clougence.adapter.dm.umi.DmAttributeNames;
import com.clougence.clouddm.ds.dameng.definition.ui.template.DmCmdTemplateSpi;
import org.junit.Test;

import com.clougence.clouddm.sdk.ui.editor.trigger.TriggerEditorFields;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateSpi;

public class TriggerTest {

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
    public void normalTest() {
        CmdTemplateOption option = getOption();
        option.getData().put(TriggerEditorFields.TRIGGER_NAME, "test_trigger");
        option.getData().put(TriggerEditorFields.TRIGGER_TABLE, "table");
        option.getData().put(TriggerEditorFields.TRIGGER_TIME, "before");
        option.getData().put(TriggerEditorFields.TRIGGER_EVENT, Arrays.asList("insert"));
        option.getData().put(TriggerEditorFields.TRIGGER_BODY, "begin insert table tmp values(1,2); end");
        option.getData().put(DmAttributeNames.TRIGGER_GRANULARITY.getCodeKey(), "row");

        List<String> createTrigger = spi.getCreateTrigger(option);
        System.out.println(createTrigger);
        assert createTrigger.size() == 1;
        assert createTrigger.get(0)
            .equals("create trigger \"test_trigger\" \n" + "before insert \n" + "on \"test_schema\".\"table\"\n" + "for each row\n" + "begin insert table tmp values(1,2); end;");

    }

    @Test
    public void newAliasTest() {
        CmdTemplateOption option = getOption();
        option.getData().put(TriggerEditorFields.TRIGGER_NAME, "test_trigger");
        option.getData().put(TriggerEditorFields.TRIGGER_TABLE, "table");
        option.getData().put(TriggerEditorFields.TRIGGER_TIME, "before");
        option.getData().put(TriggerEditorFields.TRIGGER_EVENT, Arrays.asList("insert"));
        option.getData().put(TriggerEditorFields.TRIGGER_BODY, "begin insert table tmp values(1,2); end");
        option.getData().put(DmAttributeNames.NEW_ALIAS.getCodeKey(), "new_name");
        option.getData().put(DmAttributeNames.TRIGGER_GRANULARITY.getCodeKey(), "row");

        List<String> createTrigger = spi.getCreateTrigger(option);
        System.out.println(createTrigger);
        assert createTrigger.size() == 1;
        assert createTrigger.get(0)
            .equals("create trigger \"test_trigger\" \n" + "before insert \n" + "on \"test_schema\".\"table\"\n" + "referencing new row as \"new_name\"\n" + "for each row\n"
                    + "begin insert table tmp values(1,2); end;");

    }

    @Test
    public void oldAliasTest() {
        CmdTemplateOption option = getOption();
        option.getData().put(TriggerEditorFields.TRIGGER_NAME, "test_trigger");
        option.getData().put(TriggerEditorFields.TRIGGER_TABLE, "table");
        option.getData().put(TriggerEditorFields.TRIGGER_TIME, "before");
        option.getData().put(TriggerEditorFields.TRIGGER_EVENT, Arrays.asList("insert"));
        option.getData().put(TriggerEditorFields.TRIGGER_BODY, "begin insert table tmp values(1,2); end");
        option.getData().put(DmAttributeNames.OLD_ALIAS.getCodeKey(), "oldName");
        option.getData().put(DmAttributeNames.TRIGGER_GRANULARITY.getCodeKey(), "row");

        List<String> createTrigger = spi.getCreateTrigger(option);
        System.out.println(createTrigger);
        assert createTrigger.size() == 1;
        assert createTrigger.get(0)
            .equals("create trigger \"test_trigger\" \n" + "before insert \n" + "on \"test_schema\".\"table\"\n" + "referencing old row as \"oldName\"\n" + "for each row\n"
                    + "begin insert table tmp values(1,2); end;");

    }

    @Test
    public void oldAndNewAliasTest() {
        CmdTemplateOption option = getOption();
        option.getData().put(TriggerEditorFields.TRIGGER_NAME, "test_trigger");
        option.getData().put(TriggerEditorFields.TRIGGER_TABLE, "table");
        option.getData().put(TriggerEditorFields.TRIGGER_TIME, "before");
        option.getData().put(TriggerEditorFields.TRIGGER_EVENT, Arrays.asList("insert"));
        option.getData().put(TriggerEditorFields.TRIGGER_BODY, "begin insert table tmp values(1,2); end");
        option.getData().put(DmAttributeNames.OLD_ALIAS.getCodeKey(), "oldName");
        option.getData().put(DmAttributeNames.NEW_ALIAS.getCodeKey(), "newName");
        option.getData().put(DmAttributeNames.TRIGGER_GRANULARITY.getCodeKey(), "row");

        List<String> createTrigger = spi.getCreateTrigger(option);
        System.out.println(createTrigger);
        assert createTrigger.size() == 1;
        assert createTrigger.get(0)
            .equals("create trigger \"test_trigger\" \n" + "before insert \n" + "on \"test_schema\".\"table\"\n" + "referencing old row as \"oldName\" new row as \"newName\"\n"
                    + "for each row\n" + "begin insert table tmp values(1,2); end;");

    }

    @Test
    public void conditionTest() {
        CmdTemplateOption option = getOption();
        option.getData().put(TriggerEditorFields.TRIGGER_NAME, "test_trigger");
        option.getData().put(TriggerEditorFields.TRIGGER_TABLE, "table");
        option.getData().put(TriggerEditorFields.TRIGGER_TIME, "before");
        option.getData().put(TriggerEditorFields.TRIGGER_EVENT, Arrays.asList("insert"));
        option.getData().put(TriggerEditorFields.TRIGGER_BODY, "begin insert table tmp values(1,2); end");
        option.getData().put(DmAttributeNames.CONDITION.getCodeKey(), "1=1");
        option.getData().put(DmAttributeNames.TRIGGER_GRANULARITY.getCodeKey(), "row");

        List<String> createTrigger = spi.getCreateTrigger(option);
        System.out.println(createTrigger);
        assert createTrigger.size() == 1;
        assert createTrigger.get(0)
            .equals("create trigger \"test_trigger\" \n" + "before insert \n" + "on \"test_schema\".\"table\"\n" + "for each row\n" + "when(1=1)\n"
                    + "begin insert table tmp values(1,2); end;");
    }

    @Test
    public void twoTypeTest() {
        CmdTemplateOption option = getOption();
        option.getData().put(TriggerEditorFields.TRIGGER_NAME, "test_trigger");
        option.getData().put(TriggerEditorFields.TRIGGER_TABLE, "table");
        option.getData().put(TriggerEditorFields.TRIGGER_TIME, "before");
        option.getData().put(TriggerEditorFields.TRIGGER_EVENT, Arrays.asList("insert", "update"));
        option.getData().put(TriggerEditorFields.TRIGGER_BODY, "begin insert table tmp values(1,2); end");
        option.getData().put(DmAttributeNames.CONDITION.getCodeKey(), "1=1");
        option.getData().put(DmAttributeNames.TRIGGER_GRANULARITY.getCodeKey(), "row");

        List<String> createTrigger = spi.getCreateTrigger(option);
        System.out.println(createTrigger);
        assert createTrigger.size() == 1;
        assert createTrigger.get(0)
            .equals("create trigger \"test_trigger\" \n" + "before insert or update  \n" + "on \"test_schema\".\"table\"\n" + "for each row\n" + "when(1=1)\n"
                    + "begin insert table tmp values(1,2); end;");
    }

    @Test
    public void updateColumnTest() {
        CmdTemplateOption option = getOption();
        option.getData().put(TriggerEditorFields.TRIGGER_NAME, "test_trigger");
        option.getData().put(TriggerEditorFields.TRIGGER_TABLE, "table");
        option.getData().put(TriggerEditorFields.TRIGGER_TIME, "before");
        option.getData().put(TriggerEditorFields.TRIGGER_EVENT, Arrays.asList("insert", "update"));
        option.getData().put(TriggerEditorFields.TRIGGER_COLUMNS, Arrays.asList("a"));
        option.getData().put(TriggerEditorFields.TRIGGER_BODY, "begin insert table tmp values(1,2); end");
        option.getData().put(DmAttributeNames.CONDITION.getCodeKey(), "1=1");
        option.getData().put(DmAttributeNames.TRIGGER_GRANULARITY.getCodeKey(), "row");

        List<String> createTrigger = spi.getCreateTrigger(option);
        System.out.println(createTrigger);
        assert createTrigger.size() == 1;
        assert createTrigger.get(0)
            .equals("create trigger \"test_trigger\" \n" + "before insert or update of \"a\" \n" + "on \"test_schema\".\"table\"\n" + "for each row\n" + "when(1=1)\n"
                    + "begin insert table tmp values(1,2); end;");
    }

    @Test
    public void updateTwoColumnTest() {
        CmdTemplateOption option = getOption();
        option.getData().put(TriggerEditorFields.TRIGGER_NAME, "test_trigger");
        option.getData().put(TriggerEditorFields.TRIGGER_TABLE, "table");
        option.getData().put(TriggerEditorFields.TRIGGER_TIME, "before");
        option.getData().put(TriggerEditorFields.TRIGGER_EVENT, Arrays.asList("insert", "update"));
        option.getData().put(TriggerEditorFields.TRIGGER_COLUMNS, Arrays.asList("a", "b"));
        option.getData().put(TriggerEditorFields.TRIGGER_BODY, "begin insert table tmp values(1,2); end");
        option.getData().put(DmAttributeNames.CONDITION.getCodeKey(), "1=1");
        option.getData().put(DmAttributeNames.TRIGGER_GRANULARITY.getCodeKey(), "row");

        List<String> createTrigger = spi.getCreateTrigger(option);
        System.out.println(createTrigger);
        assert createTrigger.size() == 1;
        assert createTrigger.get(0)
            .equals("create trigger \"test_trigger\" \n" + "before insert or update of \"a\",\"b\" \n" + "on \"test_schema\".\"table\"\n" + "for each row\n" + "when(1=1)\n"
                    + "begin insert table tmp values(1,2); end;");
    }

    @Test
    public void encryptionTest() {
        CmdTemplateOption option = getOption();
        option.getData().put(TriggerEditorFields.TRIGGER_NAME, "test_trigger");
        option.getData().put(TriggerEditorFields.TRIGGER_TABLE, "table");
        option.getData().put(TriggerEditorFields.TRIGGER_TIME, "before");
        option.getData().put(TriggerEditorFields.TRIGGER_EVENT, Arrays.asList("insert", "update"));
        option.getData().put(TriggerEditorFields.TRIGGER_COLUMNS, Arrays.asList("a", "b"));
        option.getData().put(TriggerEditorFields.TRIGGER_BODY, "begin insert table tmp values(1,2); end");
        option.getData().put(DmAttributeNames.ENCRYPTION.getCodeKey(), "true");

        List<String> createTrigger = spi.getCreateTrigger(option);
        System.out.println(createTrigger);
        assert createTrigger.size() == 1;
        assert createTrigger.get(0)
            .equals("create trigger \"test_trigger\" \n" + "with encryption \n" + "before insert or update of \"a\",\"b\" \n" + "on \"test_schema\".\"table\"\n" + "for each null\n"
                    + "begin insert table tmp values(1,2); end;");
    }

}

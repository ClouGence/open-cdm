package com.clougence.clouddm.ds.template.special.hana;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.clougence.clouddm.ds.hana.definition.ui.template.HanaCmdTemplateSpi;
import org.junit.Test;

import com.clougence.adapter.dm.umi.DmAttributeNames;
import com.clougence.clouddm.sdk.ui.editor.trigger.TriggerEditorFields;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateSpi;

public class TriggerTest {

    CmdTemplateSpi spi = new HanaCmdTemplateSpi();

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
            .equals("CREATE TRIGGER \"test_schema\".\"test_trigger\"\n" + "before INSERT \n" + "ON \"test_schema\".\"table\"\n" + "FOR EACH ROW \n"
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
            .equals("CREATE TRIGGER \"test_schema\".\"test_trigger\"\n" + "before INSERT OR UPDATE \n" + "ON \"test_schema\".\"table\"\n" + "FOR EACH ROW \n"
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
            .equals("CREATE TRIGGER \"test_schema\".\"test_trigger\"\n" + "before INSERT OR UPDATE OF \"a\" \n" + "ON \"test_schema\".\"table\"\n" + "FOR EACH ROW \n"
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
            .equals("CREATE TRIGGER \"test_schema\".\"test_trigger\"\n" + "before INSERT OR UPDATE OF \"a\",\"b\" \n" + "ON \"test_schema\".\"table\"\n" + "FOR EACH ROW \n"
                    + "begin insert table tmp values(1,2); end;");
    }

}

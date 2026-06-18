package com.clougence.clouddm.ds.template.family.mysql;

import com.clougence.clouddm.dsfamily.mysql.definition.ui.template.MyCmdTemplateSpi;
import com.clougence.clouddm.sdk.ui.editor.trigger.TriggerEditorFields;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateSpi;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MyTriggerTest {

    CmdTemplateSpi spi;

    public MyTriggerTest(){
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
    public void test() {
        CmdTemplateOption option = getOption();
        option.getData().put(TriggerEditorFields.TRIGGER_NAME, "test_trigger");
        option.getData().put(TriggerEditorFields.TRIGGER_TABLE, "table");
        option.getData().put(TriggerEditorFields.TRIGGER_TIME, "before");
        option.getData().put(TriggerEditorFields.TRIGGER_EVENT, Arrays.asList("insert"));
        option.getData().put(TriggerEditorFields.TRIGGER_BODY, "begin insert table tmp values(1,2); end");

        List<String> createTrigger = spi.getCreateTrigger(option);
        assert createTrigger.size() == 1;
        assert createTrigger.get(0)
            .equals("create trigger `test_schema`.`test_trigger` before insert \n" + "on `test_schema`.`table`\n" + "for each row \n" + "begin insert table tmp values(1,2); end;");

    }
}

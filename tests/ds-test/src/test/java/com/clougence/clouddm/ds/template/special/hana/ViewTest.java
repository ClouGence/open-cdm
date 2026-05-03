package com.clougence.clouddm.ds.template.special.hana;

import java.util.HashMap;
import java.util.List;

import com.clougence.clouddm.ds.hana.definition.ui.template.HanaCmdTemplateSpi;
import org.junit.Test;

import com.clougence.clouddm.sdk.ui.editor.view.ViewEditorFields;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateSpi;

public class ViewTest {

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
    public void createView() {
        CmdTemplateOption option = getOption();
        option.getData().put(ViewEditorFields.VIEW_NAME, "test_name");
        option.getData().put(ViewEditorFields.SQL, "test_sql");
        option.getData().put(ViewEditorFields.COMMENT, "test_comment");
        List<String> createView = spi.getCreateView(option);
        System.out.println(createView);
        assert createView.size() == 1;
        assert createView.get(0).equals("create view \"test_schema\".\"test_name\"\nas\ntest_sql;");

    }

    @Test
    public void alterView() {
        CmdTemplateOption option = getOption();
        option.getData().put(ViewEditorFields.VIEW_NAME, "test_name");
        option.getData().put(ViewEditorFields.SQL, "test_sql");
        option.getData().put(ViewEditorFields.COMMENT, "test_comment");
        List<String> createView = spi.getAlterView(option);
        System.out.println(createView);
        assert createView.size() == 1;
        assert createView.get(0).equals("create or replace view \"test_schema\".\"test_name\"\nas\ntest_sql;");

    }

}

package com.clougence.clouddm.ds.template.special.dameng;

import java.util.HashMap;
import java.util.List;

import com.clougence.clouddm.ds.dameng.definition.ui.template.DmCmdTemplateSpi;
import org.junit.Test;

import com.clougence.adapter.postgre.umi.PostgreAttributeNames;
import com.clougence.clouddm.sdk.ui.editor.view.ViewEditorFields;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateSpi;

public class ViewTest {

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
    public void createView() {
        CmdTemplateOption option = getOption();
        option.getData().put(ViewEditorFields.VIEW_NAME, "test_name");
        option.getData().put(ViewEditorFields.SQL, "test_sql");
        //        option.getData().put(ViewEditorFields.COMMENT, "test_comment");
        List<String> createView = spi.getCreateView(option);

        assert createView.size() == 1;
        assert createView.get(0).equals("create view \"test_schema\".\"test_name\"\n" + "as\n" + "test_sql;");

    }

    @Test
    public void createViewWithComment() {
        CmdTemplateOption option = getOption();
        option.getData().put(ViewEditorFields.VIEW_NAME, "test_name");
        option.getData().put(ViewEditorFields.SQL, "test_sql");
        option.getData().put(ViewEditorFields.COMMENT, "test_comment");
        List<String> createView = spi.getCreateView(option);

        assert createView.size() == 2;
        assert createView.get(0).equals("create view \"test_schema\".\"test_name\"\n" + "as\n" + "test_sql;");
        assert createView.get(1).equals("comment on view \"test_schema\".\"test_name\" is 'test_comment'");

    }

    @Test
    public void alterView() {
        CmdTemplateOption option = getOption();
        option.getData().put(ViewEditorFields.VIEW_NAME, "test_name");
        option.getData().put(ViewEditorFields.SQL, "test_sql");
        option.getData().put(ViewEditorFields.COMMENT, "test_comment");
        option.getData().put(PostgreAttributeNames.VIEW_CHECK_OPTION.getCodeKey(), "local");
        List<String> createView1 = spi.getAlterView(option);
        System.out.println(createView1);
        assert createView1.size() == 2;
        assert createView1.get(0).equals("create or replace view \"test_schema\".\"test_name\"\n" + "as\n" + "test_sql;");
        assert createView1.get(1).equals("comment on view \"test_schema\".\"test_name\" is 'test_comment'");
    }

}

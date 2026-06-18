package com.clougence.clouddm.ds.template.special.db2;

import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import com.clougence.adapter.db2.umi.Db2AttributeNames;
import com.clougence.clouddm.dsfamily.db2.definition.ui.template.Db2CmdTemplateSpi;
import com.clougence.clouddm.sdk.ui.editor.view.ViewEditorFields;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateSpi;

public class ViewTest {

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
    public void createView() {
        CmdTemplateOption option = getOption();
        option.getData().put(ViewEditorFields.VIEW_NAME, "test_name");
        option.getData().put(ViewEditorFields.SQL, "test_sql");
        option.getData().put(ViewEditorFields.COMMENT, "test_comment");
        List<String> createView = spi.getCreateView(option);

        assert createView.size() == 1;
        assert createView.get(0).equals("create view \"test_schema\".\"test_name\"\n" + "as\n" + "test_sql;");

    }

    @Test
    public void createViewWithLOCAL() {
        CmdTemplateOption option = getOption();
        option.getData().put(ViewEditorFields.VIEW_NAME, "test_name");
        option.getData().put(ViewEditorFields.SQL, "test_sql");
        option.getData().put(ViewEditorFields.COMMENT, "test_comment");
        option.getData().put(Db2AttributeNames.VIEW_CHECK_OPTION.getCodeKey(), "local");
        List<String> createView1 = spi.getCreateView(option);
        assert createView1.size() == 1;
        assert createView1.get(0).equals("create view \"test_schema\".\"test_name\"\nas\ntest_sql\nwith local check option;");
    }

    @Test
    public void createViewWithOption() {
        CmdTemplateOption option = getOption();
        option.getData().put(ViewEditorFields.VIEW_NAME, "test_name");
        option.getData().put(ViewEditorFields.SQL, "test_sql");
        option.getData().put(ViewEditorFields.COMMENT, "test_comment");
        option.getData().put(Db2AttributeNames.VIEW_CHECK_OPTION.getCodeKey(), "cascaded");
        List<String> createView1 = spi.getCreateView(option);
        System.out.println(createView1);
        assert createView1.size() == 1;
        assert createView1.get(0).equals("create view \"test_schema\".\"test_name\"\nas\ntest_sql\nwith cascaded check option;");
    }
}

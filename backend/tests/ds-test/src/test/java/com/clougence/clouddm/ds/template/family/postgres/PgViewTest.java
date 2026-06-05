package com.clougence.clouddm.ds.template.family.postgres;

import java.util.HashMap;
import java.util.List;

import com.clougence.adapter.postgre.umi.PostgreAttributeNames;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.template.PgCmdTemplateSpi;
import org.junit.Test;

import com.clougence.clouddm.sdk.ui.editor.view.ViewEditorFields;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateSpi;

public class PgViewTest {

    CmdTemplateSpi spi;

    public PgViewTest(){
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
        option.getData().put(PostgreAttributeNames.VIEW_CHECK_OPTION.getCodeKey(), "local");
        List<String> createView1 = spi.getCreateView(option);

        assert createView1.size() == 1;
        assert createView1.get(0).equals("create view \"test_schema\".\"test_name\"\n" + "with(check_option = local)\n" + "as\n" + "test_sql;");
    }

    @Test
    public void createViewWithBarrier() {
        CmdTemplateOption option = getOption();
        option.getData().put(ViewEditorFields.VIEW_NAME, "test_name");
        option.getData().put(ViewEditorFields.SQL, "test_sql");
        option.getData().put(ViewEditorFields.COMMENT, "test_comment");
        option.getData().put(PostgreAttributeNames.VIEW_SECURITY_BARRIER.getCodeKey(), "true");
        List<String> createView1 = spi.getCreateView(option);
        assert createView1.size() == 1;
        assert createView1.get(0).equals("create view \"test_schema\".\"test_name\"\n" + "with(security_barrier)\n" + "as\n" + "test_sql;");
    }

    @Test
    public void createViewWithBarrierAndLocal() {
        CmdTemplateOption option = getOption();
        option.getData().put(ViewEditorFields.VIEW_NAME, "test_name");
        option.getData().put(ViewEditorFields.SQL, "test_sql");
        option.getData().put(ViewEditorFields.COMMENT, "test_comment");
        option.getData().put(PostgreAttributeNames.VIEW_SECURITY_BARRIER.getCodeKey(), "true");
        option.getData().put(PostgreAttributeNames.VIEW_CHECK_OPTION.getCodeKey(), "local");
        List<String> createView1 = spi.getCreateView(option);
        assert createView1.size() == 1;
        assert createView1.get(0).equals("create view \"test_schema\".\"test_name\"\n" + "with(security_barrier,check_option = local)\n" + "as\n" + "test_sql;");
    }
}
